package uni.dc.view;

import java.awt.BorderLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.Map;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import org.goataa.impl.utils.Individual;

import uni.dc.model.PriorityConfiguration;
import uni.dc.ubsOpti.UbsOptiConfig;
import edu.uci.ics.jung.algorithms.layout.DAGLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.DelegateForest;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.control.PickingGraphMousePlugin;

public class AllTracerGui extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JLabel lblPrio;

	private Map<String, DelegateForest<Individual<int[], int[]>, String>> graphs;
	private DelegateForest<Individual<int[], int[]>, String> currentGraph;
	private UbsOptiConfig config;
	private Layout<Individual<int[], int[]>, String> layout;
	private VisualizationViewer<Individual<int[], int[]>, String> vv;

	public AllTracerGui(Map<String, DelegateForest<Individual<int[], int[]>, String>> graphs, UbsOptiConfig config) {
		this.graphs = graphs;
		this.config = config;
		setTitle("AllTracer Display");
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
					currentGraph = graphs.get(item);
					layout = new DAGLayout<Individual<int[],int[]>, String>(currentGraph);//TreeLayout<Individual<int[], int[]>, String>(currentGraph);
					vv.setGraphLayout(layout);
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

		currentGraph = graphs.get(algoNames[0]);
		layout = new DAGLayout<Individual<int[],int[]>, String>(currentGraph);//new TreeLayout<Individual<int[], int[]>, String>(currentGraph);
		vv = new VisualizationViewer<Individual<int[], int[]>, String>(layout);

		DefaultModalGraphMouse<?, ?> gm = new DefaultModalGraphMouse<Object, Object>();

		gm.add(new PickingGraphMousePlugin<Individual<int[], int[]>, String>() {
			public void mouseReleased(MouseEvent e) {
				VisualizationViewer vv = (VisualizationViewer) e.getSource();
				if (e.getModifiers() == this.modifiers) {
					if (this.down != null) {
						Point2D out = e.getPoint();
						if ((this.vertex == null) && (!heyThatsTooClose(this.down, out, 5.0D))) {
							pickContainedVertices(vv, this.down, out, true);
						}
						if (this.vertex != null)
							displayPrio(this.vertex);
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

		allTracerPanel.add(vv, BorderLayout.CENTER);
		this.getContentPane().add(allTracerPanel, BorderLayout.CENTER);

		lblPrio = new JLabel("");
		lblPrio.setBorder(LineBorder.createGrayLineBorder());
		allTracerPanel.add(lblPrio, BorderLayout.EAST);
	}

	public void displayPrio(Individual<int[], int[]> p) {
		PriorityConfiguration prio = (PriorityConfiguration) config.getPriorityConfig().clone();
		prio.fromIntArray(p.x);
		Individual<int[], int[]> parent = currentGraph.getParent(p);
		if (parent == null) {
			lblPrio.setText(prio.toHTMLString());
		} else {
			lblPrio.setText(prio.toDiffHTMLString(parent.x));
		}
	}
}
