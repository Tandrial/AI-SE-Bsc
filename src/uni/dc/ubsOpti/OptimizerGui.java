package uni.dc.ubsOpti;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;

import uni.dc.model.EgressTopology;
import uni.dc.model.Traffic;
import uni.dc.networkGenerator.swingUI.graphviz.GraphVizPanel;
import uni.dc.util.NetworkParser;

public class OptimizerGui extends JFrame {

	private static final long serialVersionUID = 1L;

	JLabel statusLabel;
	JButton loadButton;

	GraphVizPanel imagePanel;
	JRadioButton topologyImageRadioButton;
	JRadioButton flowImageRadioButton;

	NetworkParser parser;

	public OptimizerGui(String title) throws HeadlessException {
		super(title);
		initComponents();
	}

	private class UpdateActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			updateDisplay();
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

		JPanel topologyParameterPanel = new JPanel(new FlowLayout());

		loadButton = new JButton("Load from file");
		loadButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser c = new JFileChooser();
				c.setCurrentDirectory(new File("./Topologies/"));
				c.setFileFilter(new FileNameExtensionFilter(
						"UBS Optimizer file", "json"));
				int rVal = c.showOpenDialog(OptimizerGui.this);
				if (rVal == JFileChooser.APPROVE_OPTION)
					loadFromFile(c.getSelectedFile());
			}
		});

		topologyParameterPanel.add(loadButton);

		JPanel viewTypeSelectionPanel = new JPanel();

		viewTypeSelectionPanel.setLayout(new BoxLayout(viewTypeSelectionPanel,
				BoxLayout.PAGE_AXIS));

		ButtonGroup viewTypeSelectionGroup = new ButtonGroup();

		viewTypeSelectionGroup.add(topologyImageRadioButton = new JRadioButton(
				"Ports"));
		viewTypeSelectionPanel.add(topologyImageRadioButton);
		topologyImageRadioButton.addActionListener(new UpdateActionListener());
		topologyImageRadioButton.setSelected(true);
		viewTypeSelectionGroup.add(flowImageRadioButton = new JRadioButton(
				"Flows"));
		viewTypeSelectionPanel.add(flowImageRadioButton);
		flowImageRadioButton.addActionListener(new UpdateActionListener());
		topologyParameterPanel.add(viewTypeSelectionPanel);

		topologyPanel.add(topologyParameterPanel, BorderLayout.PAGE_START);
	}

	public void updateDisplay() {
		long t1, t2;
		t1 = System.nanoTime();

		if (topologyImageRadioButton.isSelected())
			imagePanel.setDot(parser.getTopology().toDot());

		if (flowImageRadioButton.isSelected())
			imagePanel.setDot(parser.getTraffic().toDot());

		t2 = System.nanoTime();

		setStatusMsg("Done (rendered in %.4f sec.)", (t2 - t1) / 1.0E9);
	}

	private void loadFromFile(File file) {
		long t1, t2, t3;

		setStatusMsg("Generating topology ...");
		try {
			t1 = System.nanoTime();
			parser = new NetworkParser(file);
			EgressTopology topology = parser.getTopology();
			Traffic traffic = parser.getTraffic();
			t2 = System.nanoTime();

			if (topologyImageRadioButton.isSelected())
				imagePanel.setDot(topology.toDot());
			if (flowImageRadioButton.isSelected())
				imagePanel.setDot(traffic.toDot());

			t3 = System.nanoTime();

			setStatusMsg(
					"Done (loaded %s in %.4f sec., rendered in %.4f sec.)",
					file.toString(), (t2 - t1) / 1.0E9, (t3 - t2) / 1.0E9);
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
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		OptimizerGui gui = new OptimizerGui("UBS Optimizer");
		gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gui.setSize(1024, 768);
		gui.setVisible(true);
	}
}
