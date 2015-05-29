package uni.dc.ubsOpti;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
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
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import uni.dc.model.EgressTopology;
import uni.dc.model.Traffic;
import uni.dc.networkGenerator.swingUI.graphviz.GraphVizPanel;
import uni.dc.util.NetworkParser;

import javax.swing.JList;
import javax.swing.AbstractListModel;

import java.awt.Choice;

public class OptimizerGUI extends JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JRadioButton topologyImageRadioButton;
	private JRadioButton flowImageRadioButton;
	private GraphVizPanel imagePanel;
	private JLabel statusLabel;
	private Choice choice;

	private NetworkParser parser;
	private Optimizer optimizer = new Optimizer();

	private class UpdateActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			updateDisplay();
		}
	}

	public OptimizerGUI(String title) {
		super(title);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		setSize(1024, 768);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JPanel topologyPanel = new JPanel(new BorderLayout());

		statusLabel = new JLabel("");
		contentPane.add(topologyPanel, BorderLayout.CENTER);

		contentPane.add(statusLabel, BorderLayout.PAGE_END);
		JPanel topologyParameterPanel = new JPanel(new FlowLayout());

		JButton loadButton = new JButton("Load from file");
		loadButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser c = new JFileChooser();
				c.setCurrentDirectory(new File("./Topologies/"));
				c.setFileFilter(new FileNameExtensionFilter(
						"UBS Optimizer file", "json"));
				int rVal = c.showOpenDialog(OptimizerGUI.this);
				if (rVal == JFileChooser.APPROVE_OPTION)
					loadFromFile(c.getSelectedFile());
			}
		});
		topologyParameterPanel.add(loadButton);
		JPanel viewTypeSelectionPanel = new JPanel(new FlowLayout());

		viewTypeSelectionPanel.setLayout(new BoxLayout(viewTypeSelectionPanel,
				BoxLayout.PAGE_AXIS));

		topologyImageRadioButton = new JRadioButton("Ports");
		topologyImageRadioButton.addActionListener(new UpdateActionListener());
		topologyImageRadioButton.setSelected(true);
		flowImageRadioButton = new JRadioButton("Flows");
		flowImageRadioButton.addActionListener(new UpdateActionListener());

		ButtonGroup viewTypeSelectionGroup = new ButtonGroup();
		viewTypeSelectionGroup.add(topologyImageRadioButton);
		viewTypeSelectionGroup.add(flowImageRadioButton);

		viewTypeSelectionPanel.add(topologyImageRadioButton);
		viewTypeSelectionPanel.add(flowImageRadioButton);
		topologyParameterPanel.add(viewTypeSelectionPanel);

		topologyPanel.add(topologyParameterPanel, BorderLayout.PAGE_START);

		choice = new Choice();
		choice.addItem("Brute Force");
		choice.addItem("Hill Climbing");
		choice.addItem("Simulated Annealing");
		choice.addItem("Generational Genetic/Evolutionary Algorithm");
		choice.addItem("Random Walks");

		topologyParameterPanel.add(choice);

		JButton btnOptimize = new JButton("Optimize");
		btnOptimize.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				long t1, t2;
				t1 = System.nanoTime();
				optimizer.optimize(parser, choice.getSelectedItem());

				t2 = System.nanoTime();

				setStatusMsg("Done (optimized with %s in %.4f sec.)",
						choice.getSelectedItem(), (t2 - t1) / 1.0E9);
			}
		});

		topologyParameterPanel.add(btnOptimize);

		imagePanel = new GraphVizPanel();
		topologyPanel.add(imagePanel, BorderLayout.CENTER);

	}

	public void updateDisplay() {
		if (parser == null)
			return;
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

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager
							.getSystemLookAndFeelClassName());
					OptimizerGUI gui = new OptimizerGUI("UBS Optimizer");
					gui.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
