package uni.dc.view;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Paint;
import java.awt.Stroke;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import org.apache.commons.collections15.Transformer;
import org.goataa.impl.utils.Individual;

import uni.dc.model.PriorityConfiguration;
import uni.dc.ubsOpti.UbsOptiConfig;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.TreeLayout;
import edu.uci.ics.jung.algorithms.shortestpath.BFSDistanceLabeler;
import edu.uci.ics.jung.graph.DelegateForest;
import edu.uci.ics.jung.graph.util.Pair;
import edu.uci.ics.jung.visualization.Layer;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.control.PickingGraphMousePlugin;
import edu.uci.ics.jung.visualization.renderers.Renderer;

public class AllTracerGui extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JLabel lblPrio;

	private DelegateForest<Individual<int[], int[]>, String> currentGraph;
	private UbsOptiConfig config;
	private Layout<Individual<int[], int[]>, String> layout;
	private VisualizationViewer<Individual<int[], int[]>, String> vv;

	private Individual<int[], int[]> mFrom = null;
	private Individual<int[], int[]> mTo = null;
	private Individual<int[], int[]> selected = null;
	private Set<Individual<int[], int[]>> mPred = null;;

	public AllTracerGui(String title, Map<String, DelegateForest<Individual<int[], int[]>, String>> graphs,
			UbsOptiConfig config) {
		this.config = config;
		setTitle(title);
		setBounds(100, 100, 450, 300);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JPanel allTracerPanel = new JPanel(new BorderLayout());

		JPanel allTracerParameterPanel = new JPanel();
		allTracerParameterPanel.add(new JLabel("Trace to display"));
		allTracerPanel.add(allTracerParameterPanel, BorderLayout.PAGE_START);

		JComboBox<String> comboBox = new JComboBox<String>();
		comboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					String item = (String) e.getItem();
					if (!item.equals("sEA")) {
						currentGraph = graphs.get(item);
						mFrom = config.getAllTracer().getStartPoint(item);
						mTo = config.getAllTracer().getEndPoint(item);
						layout = new TreeLayout<Individual<int[], int[]>, String>(currentGraph);
						vv.setGraphLayout(layout);
					} else {
						JOptionPane.showMessageDialog(null,
								"This Graph view is not correctly implemented for the Algo sEA!",
								"Implementation missing!", JOptionPane.WARNING_MESSAGE);
					}
				}
			}
		});

		String[] algoNames = new String[graphs.keySet().size()];
		int pos = 0;
		for (String name : graphs.keySet()) {
			algoNames[pos++] = name;
		}

		comboBox.setModel(new DefaultComboBoxModel<String>(algoNames));
		allTracerParameterPanel.add(comboBox);

		mFrom = config.getAllTracer().getStartPoint(algoNames[0]);
		mTo = config.getAllTracer().getEndPoint(algoNames[0]);

		currentGraph = graphs.get(algoNames[0]);
		layout = new TreeLayout<Individual<int[], int[]>, String>(currentGraph);
		vv = new VisualizationViewer<Individual<int[], int[]>, String>(layout);
		vv.setBackground(Color.WHITE);

		vv.getRenderContext().setVertexDrawPaintTransformer(new Transformer<Individual<int[], int[]>, Paint>() {
			@Override
			public Paint transform(Individual<int[], int[]> v) {
				if (selected == v) {
					return Color.red;
				}
				return Color.black;
			}
		});
		vv.getRenderContext().setVertexFillPaintTransformer(new Transformer<Individual<int[], int[]>, Paint>() {
			@Override
			public Paint transform(Individual<int[], int[]> v) {
				if (v == mFrom) {
					return Color.RED;
				}
				if (v == mTo) {
					return Color.GREEN;
				}
				if (mPred == null) {
					return Color.LIGHT_GRAY;
				} else {
					if (mPred.contains(v)) {
						return Color.YELLOW;
					} else {
						return Color.LIGHT_GRAY;
					}
				}
			}
		});
		vv.getRenderContext().setVertexStrokeTransformer(new Transformer<Individual<int[], int[]>, Stroke>() {
			@Override
			public Stroke transform(Individual<int[], int[]> v) {
				if (v == selected)
					return new BasicStroke(4);
				else
					return new BasicStroke(1);
			}

		});
		vv.getRenderContext().setEdgeDrawPaintTransformer(new Transformer<String, Paint>() {
			@Override
			public Paint transform(String e) {
				if (mPred == null || mPred.size() == 0)
					return Color.BLACK;
				if (isBlessed(e)) {
					return new Color(0.0f, 0.0f, 1.0f, 0.5f);// Color.BLUE;
				} else {
					return Color.LIGHT_GRAY;
				}
			}
		});
		vv.getRenderContext().setEdgeStrokeTransformer(new Transformer<String, Stroke>() {
			protected final Stroke THIN = new BasicStroke(1);
			protected final Stroke THICK = new BasicStroke(2);

			@Override
			public Stroke transform(String e) {
				if (mPred == null || mPred.size() == 0)
					return THIN;
				if (isBlessed(e)) {
					return THICK;
				} else
					return THIN;
			}
		});

		vv.addPostRenderPaintable(new VisualizationViewer.Paintable() {
			public boolean useTransform() {
				return true;
			}

			public void paint(Graphics g) {
				if (mPred == null)
					return;

				// for all edges, paint edges that are in shortest path
				for (String e : layout.getGraph().getEdges()) {

					if (isBlessed(e)) {
						Individual<int[], int[]> v1 = currentGraph.getEndpoints(e).getFirst();
						Individual<int[], int[]> v2 = currentGraph.getEndpoints(e).getSecond();
						Point2D p1 = layout.transform(v1);
						Point2D p2 = layout.transform(v2);
						p1 = vv.getRenderContext().getMultiLayerTransformer().transform(Layer.LAYOUT, p1);
						p2 = vv.getRenderContext().getMultiLayerTransformer().transform(Layer.LAYOUT, p2);
						Renderer<Individual<int[], int[]>, String> renderer = vv.getRenderer();
						renderer.renderEdge(vv.getRenderContext(), layout, e);
					}
				}
			}
		});

		DefaultModalGraphMouse<?, ?> gm = new DefaultModalGraphMouse<Object, Object>();

		gm.add(new PickingGraphMousePlugin<Individual<int[], int[]>, String>() {
			@SuppressWarnings("unchecked")
			public void mouseReleased(MouseEvent e) {
				VisualizationViewer<Individual<int[], int[]>, String> vv = (VisualizationViewer<Individual<int[], int[]>, String>) e
						.getSource();
				if (e.getModifiers() == this.modifiers) {
					if (this.down != null) {
						Point2D out = e.getPoint();
						if ((this.vertex == null) && (!heyThatsTooClose(this.down, out, 5.0D))) {
							pickContainedVertices(vv, this.down, out, true);
						}
						if (this.vertex != null) {
							selected = this.vertex;
							displayPrio();
						}
					}
				} else if ((e.getModifiers() == this.addToSelectionModifiers) && (this.down != null)) {
					Point2D out = e.getPoint();
					if ((this.vertex == null) && (!heyThatsTooClose(this.down, out, 5.0D))) {
						pickContainedVertices(vv, this.down, out, false);
					}
				}
				this.down = null;
				this.vertex = null;
				this.edge = null;
				this.rect.setFrame(0.0D, 0.0D, 0.0D, 0.0D);
				vv.removePostRenderPaintable(this.lensPaintable);
				vv.repaint();
			}

			private boolean heyThatsTooClose(Point2D p, Point2D q, double min) {
				return (Math.abs(p.getX() - q.getX()) < min) && (Math.abs(p.getY() - q.getY()) < min);
			}
		});

		gm.setMode(ModalGraphMouse.Mode.TRANSFORMING);
		vv.setGraphMouse(gm);
		if (!comboBox.getSelectedItem().equals("sEA")) {
			drawShortest();
			vv.repaint();
		} else {
			JOptionPane.showMessageDialog(null, "This Graph view is not correctly implemented for the Algo sEA!",
					"Implementation missing!", JOptionPane.WARNING_MESSAGE);
		}

		allTracerPanel.add(vv, BorderLayout.CENTER);
		this.getContentPane().add(allTracerPanel, BorderLayout.CENTER);

		lblPrio = new JLabel("");
		lblPrio.setBorder(LineBorder.createGrayLineBorder());
		allTracerPanel.add(lblPrio, BorderLayout.EAST);
	}

	public void displayPrio() {
		PriorityConfiguration prio = (PriorityConfiguration) config.getPriorityConfig().clone();
		prio.fromIntArray(selected.x);
		Individual<int[], int[]> parent = currentGraph.getParent(selected);
		if (parent == null) {
			lblPrio.setText(prio.toHTMLString());
		} else {
			lblPrio.setText(prio.toDiffHTMLString(parent.x));
		}
	}

	boolean isBlessed(String e) {
		Pair<Individual<int[], int[]>> endpoints = currentGraph.getEndpoints(e);
		Individual<int[], int[]> v1 = endpoints.getFirst();
		Individual<int[], int[]> v2 = endpoints.getSecond();
		return v1.equals(v2) == false && mPred.contains(v1) && mPred.contains(v2);
	}

	protected void drawShortest() {
		if (mFrom == null || mTo == null) {
			return;
		}
		BFSDistanceLabeler<Individual<int[], int[]>, String> bdl = new BFSDistanceLabeler<Individual<int[], int[]>, String>();
		bdl.labelDistances(currentGraph, mFrom);
		mPred = new HashSet<Individual<int[], int[]>>();

		// grab a predecessor
		Individual<int[], int[]> v = mTo;
		Set<Individual<int[], int[]>> prd = bdl.getPredecessors(v);
		mPred.add(mTo);
		while (prd != null && prd.size() > 0) {
			v = prd.iterator().next();
			mPred.add(v);
			if (v == mFrom)
				return;
			prd = bdl.getPredecessors(v);
		}
	}
}
