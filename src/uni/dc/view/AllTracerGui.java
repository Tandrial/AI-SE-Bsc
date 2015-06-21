package uni.dc.view;

import java.awt.BorderLayout;
import java.util.Map;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.goataa.impl.utils.Individual;

import uni.dc.ubsOpti.UbsOptiConfig;
import uni.dc.util.UbsMousePlugin;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.TreeLayout;
import edu.uci.ics.jung.graph.DelegateForest;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

public class AllTracerGui extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	private Map<String, DelegateForest<Individual<int[], int[]>, String>> graphs;
	private Layout<Individual<int[], int[]>, String> layout;
	private VisualizationViewer<Individual<int[], int[]>, String> vv;

	public AllTracerGui(Map<String, DelegateForest<Individual<int[], int[]>, String>> graphs, UbsOptiConfig config) {
		this.graphs = graphs;
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
					layout = new TreeLayout<Individual<int[], int[]>, String>(graphs.get(item));
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

		layout = new TreeLayout<Individual<int[], int[]>, String>(graphs.get(algoNames[0]));
		vv = new VisualizationViewer<Individual<int[], int[]>, String>(layout);
		
		DefaultModalGraphMouse<?, ?> gm = new DefaultModalGraphMouse<Object, Object>();
		gm.add(new UbsMousePlugin<Individual<int[], int[]>, String>());
		gm.setMode(ModalGraphMouse.Mode.TRANSFORMING);

		vv.setGraphMouse(gm);

		allTracerPanel.add(vv, BorderLayout.CENTER);
		this.getContentPane().add(allTracerPanel, BorderLayout.CENTER);
	}
}
