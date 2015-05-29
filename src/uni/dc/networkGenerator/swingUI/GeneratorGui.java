package uni.dc.networkGenerator.swingUI;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import uni.dc.model.EgressTopology;
import uni.dc.model.Flow;
import uni.dc.model.PriorityConfiguration;
import uni.dc.model.Traffic;
import uni.dc.networkGenerator.RandomMulticastPathGenerator;
import uni.dc.networkGenerator.RandomTopologyGenerator;
import uni.dc.networkGenerator.swingUI.graphviz.GraphVizPanel;
import uni.dc.util.NetworkParser;

public class GeneratorGui extends JFrame {

	private static final long serialVersionUID = 1L;

	JLabel statusLabel;
	JSpinner topologySeedSpinner;
	JSpinner topologyDepthSpinner;
	JSpinner topologyPortsSpinner;

	JButton generateButton;
	JCheckBox autoGenerateCheckBox;

	GraphVizPanel imagePanel;
	JRadioButton topologyImageRadioButton;
	JRadioButton flowImageRadioButton;

	public GeneratorGui() throws HeadlessException {
		this("");
	}

	public GeneratorGui(String title) throws HeadlessException {
		super(title);
		initComponents();
		// this.updateOnParameterChange();
		String fileName = "./Topologies/Linear.json";
		loadFromFile(fileName);
	}

	private class ParameterChangeListener implements ChangeListener {
		private Object widget;

		public ParameterChangeListener(Object widget) {
			this.widget = widget;
		}

		@Override
		public void stateChanged(ChangeEvent e) {
			if (e.getSource() == widget && autoGenerateCheckBox.isSelected())
				updateOnParameterChange();
		}
	}

	private class GenerateActionListener implements ActionListener {
		private Object widget;

		public GenerateActionListener(Object widget) {
			this.widget = widget;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == widget && autoGenerateCheckBox.isSelected()) {
				updateOnParameterChange();
			}
		}
	}

	private void initComponents() {
		this.getContentPane().setLayout(new BorderLayout());

		JPanel topologyPanel = new JPanel(new BorderLayout());
		imagePanel = new GraphVizPanel();
		topologyPanel.add(imagePanel, BorderLayout.CENTER);

		statusLabel = new JLabel("");
		this.getContentPane().add(topologyPanel, BorderLayout.CENTER);

		this.getContentPane().add(statusLabel, BorderLayout.PAGE_END);

		topologySeedSpinner = new JSpinner(new SpinnerNumberModel(1, 0,
				10000000, 1));
		topologySeedSpinner.addChangeListener(new ParameterChangeListener(
				topologySeedSpinner));
		topologyDepthSpinner = new JSpinner(
				new SpinnerNumberModel(3, 2, 100, 1));
		topologyDepthSpinner.addChangeListener(new ParameterChangeListener(
				topologyDepthSpinner));
		topologyPortsSpinner = new JSpinner(new SpinnerNumberModel(9, 2, 1000,
				1));
		topologyPortsSpinner.addChangeListener(new ParameterChangeListener(
				topologyPortsSpinner));

		JPanel topologyParameterPanel = new JPanel(new FlowLayout());
		topologyParameterPanel.add(new JLabel("Seed:"));
		topologyParameterPanel.add(topologySeedSpinner);
		topologyParameterPanel.add(new JLabel("Depth:"));
		topologyParameterPanel.add(topologyDepthSpinner);
		topologyParameterPanel.add(new JLabel("Ports:"));
		topologyParameterPanel.add(topologyPortsSpinner);

		generateButton = new JButton("Generate");
		generateButton.addActionListener(new GenerateActionListener(
				generateButton));

		topologyParameterPanel.add(generateButton);
		autoGenerateCheckBox = new JCheckBox("Generate automatically");
		autoGenerateCheckBox.addActionListener(new GenerateActionListener(
				autoGenerateCheckBox));

		topologyParameterPanel.add(autoGenerateCheckBox);

		JPanel viewTypeSelectionPanel = new JPanel();

		viewTypeSelectionPanel.setLayout(new BoxLayout(viewTypeSelectionPanel,
				BoxLayout.PAGE_AXIS));

		ButtonGroup viewTypeSelectionGroup = new ButtonGroup();

		viewTypeSelectionGroup.add(topologyImageRadioButton = new JRadioButton(
				"Ports"));
		viewTypeSelectionPanel.add(topologyImageRadioButton);
		topologyImageRadioButton.addActionListener(new GenerateActionListener(
				topologyImageRadioButton));
		topologyImageRadioButton.setSelected(true);
		viewTypeSelectionGroup.add(flowImageRadioButton = new JRadioButton(
				"Flows"));
		viewTypeSelectionPanel.add(flowImageRadioButton);
		flowImageRadioButton.addActionListener(new GenerateActionListener(
				flowImageRadioButton));

		topologyParameterPanel.add(viewTypeSelectionPanel);

		topologyPanel.add(topologyParameterPanel, BorderLayout.PAGE_START);

	}

	private void updateOnParameterChange() {
		long t1, t2, t3;

		setStatusMsg("Generating topology ...");
		try {
			t1 = System.nanoTime();

			RandomTopologyGenerator topologyGen = new RandomTopologyGenerator();
			topologyGen.setRng(new Random((Integer) topologySeedSpinner
					.getValue()));
			topologyGen.setDepth((Integer) topologyDepthSpinner.getValue());
			topologyGen.setPorts((Integer) topologyPortsSpinner.getValue());
			EgressTopology topology = topologyGen.generate();

			RandomMulticastPathGenerator flowPathGen = new RandomMulticastPathGenerator();
			flowPathGen.setTopology(topology);
			flowPathGen.setRng(new Random(0));
			flowPathGen.setMinFlowPerPort(3);
			flowPathGen.setMaxDestPerFlow(1);

			Traffic traffic = flowPathGen.generate();
			for (Flow f : traffic) {
				System.out.printf("Flow %s: %s -> %s\n", f.getName(),
						f.getSrcPort(), f.getDestPortSet());
			}

			System.out.printf("Port -> Set<Flow> map: %s\n",
					traffic.getPortFlowMap());

			PriorityConfiguration cfg = new PriorityConfiguration(traffic);
			System.out.print(cfg);

			t2 = System.nanoTime();

			if (topologyImageRadioButton.isSelected())
				imagePanel.setDot(topology.toDot());
			if (flowImageRadioButton.isSelected())
				imagePanel.setDot(traffic.toDot());

			t3 = System.nanoTime();

			setStatusMsg(
					"Done (generated in %.4f sec., rendered in %.4f sec.)",
					(t2 - t1) / 1.0E9, (t3 - t2) / 1.0E9);
		} catch (Exception e) {
			e.printStackTrace();
			setStatusMsg("Generation failed!");
		}
	}

	private void loadFromFile(String fileName) {
		long t1, t2, t3;

		setStatusMsg("Generating topology ...");
		try {
			t1 = System.nanoTime();
			NetworkParser parser = new NetworkParser(fileName);
			EgressTopology topology = parser.getTopology();
			Traffic traffic = parser.getTraffic();

			PriorityConfiguration cfg;

			t2 = System.nanoTime();

			// if (topologyImageRadioButton.isSelected())
			imagePanel.setDot(topology.toDot());
			// if (flowImageRadioButton.isSelected())
			// imagePanel.setDot(traffic.toDot());

			t3 = System.nanoTime();

			setStatusMsg("Done (loaded in %.4f sec., rendered in %.4f sec.)",
					(t2 - t1) / 1.0E9, (t3 - t2) / 1.0E9);
		} catch (Exception e) {
			e.printStackTrace();
			setStatusMsg("Load from File failed!");
		}
	}

	private void setStatusMsg(String fmt, Object... args) {
		statusLabel.setText(String.format(fmt, args));
		statusLabel.repaint();
		statusLabel.validate();
	}

	public static void main(String[] args) throws Exception {

		try {
			// Set System L&F
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {

		}

		GeneratorGui gui = new GeneratorGui();
		gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		gui.setSize(1024, 768);
		gui.setVisible(true);

	}

}
