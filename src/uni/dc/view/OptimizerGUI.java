package uni.dc.view;

import java.awt.BorderLayout;
import java.awt.Choice;
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
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSeparator;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import uni.dc.model.EgressTopology;
import uni.dc.ubsOpti.Optimizer;
import uni.dc.util.NetworkParser;

public class OptimizerGUI extends JFrame {
	private static final long serialVersionUID = 1L;
	private GraphVizPanel imagePanel;
	private Choice choice;
	private JLabel statusLabel;
	private NetworkParser parser;
	private Optimizer optimizer;

	private boolean portDisplay = true;

	public OptimizerGUI(String title) {
		super(title);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		setSize(1024, 768);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);

		JMenuItem mntmLoad = new JMenuItem("Load");
		mntmLoad.addActionListener(new ActionListener() {
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
		mnFile.add(mntmLoad);

		JMenuItem mntmSaveJpg = new JMenuItem("Save picture");
		mntmSaveJpg.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (parser == null)
					return;
				String fileName = parser.getFileName();
				imagePanel.saveToFile(new File("./Topologies/"
						+ fileName.substring(0, fileName.lastIndexOf("."))
						+ (portDisplay ? "_port" : "_flow") + ".png"));
			}
		});
		mnFile.add(mntmSaveJpg);
		mnFile.add(new JSeparator());

		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				dispose();
				System.exit(0);
			}
		});
		mnFile.add(mntmExit);

		JMenu mnDisplay = new JMenu("Display");
		menuBar.add(mnDisplay);

		JRadioButtonMenuItem rdbtnmntmPorts = new JRadioButtonMenuItem("Ports");
		rdbtnmntmPorts.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				portDisplay = true;
				updateDisplay(parser.getTopology().toDot());
			}
		});
		rdbtnmntmPorts.setSelected(true);
		mnDisplay.add(rdbtnmntmPorts);

		JRadioButtonMenuItem rdbtnmntmFlows = new JRadioButtonMenuItem("Flows");
		rdbtnmntmFlows.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				portDisplay = false;
				updateDisplay(parser.getTraffic().toDot());
			}
		});
		mnDisplay.add(rdbtnmntmFlows);

		ButtonGroup viewTypeSelectionGroup = new ButtonGroup();
		viewTypeSelectionGroup.add(rdbtnmntmPorts);
		viewTypeSelectionGroup.add(rdbtnmntmFlows);

		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JPanel topologyPanel = new JPanel(new BorderLayout());

		statusLabel = new JLabel("");
		contentPane.add(topologyPanel, BorderLayout.CENTER);

		contentPane.add(statusLabel, BorderLayout.PAGE_END);
		JPanel topologyParameterPanel = new JPanel(new FlowLayout());
		JPanel viewTypeSelectionPanel = new JPanel(new FlowLayout());

		viewTypeSelectionPanel.setLayout(new BoxLayout(viewTypeSelectionPanel,
				BoxLayout.PAGE_AXIS));

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

	private void updateDisplay(StringBuilder content) {
		if (parser == null)
			return;
		long t1, t2;
		t1 = System.nanoTime();
		imagePanel.setDot(content);
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

			t2 = System.nanoTime();
			imagePanel.setDot(topology.toDot());
			t3 = System.nanoTime();

			setStatusMsg("Done (loaded in %.4f sec., rendered in %.4f sec.)",
					(t2 - t1) / 1.0E9, (t3 - t2) / 1.0E9);
			setTitle("UBS Optimizer - " + parser.getFileName());
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
