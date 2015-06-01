package uni.dc.view;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.ButtonGroup;
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
import uni.dc.ubsOpti.DelayCalc.UbsDelayCalc;
import uni.dc.ubsOpti.DelayCalc.UbsV0DelayCalc;
import uni.dc.ubsOpti.DelayCalc.UbsV3DelayCalc;
import uni.dc.util.NetworkParser;

public class OptimizerGUI extends JFrame {
	private static final long serialVersionUID = 1L;
	private GraphVizPanel imagePanel;
	private JLabel statusLabel;
	private NetworkParser parser;
	private Optimizer optimizer;
	private UbsDelayCalc delayCalc;

	private boolean portDisplay = true;

	public OptimizerGUI(String title) {
		super(title);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		setSize(1024, 768);

		optimizer = new Optimizer();

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

		JMenu mnOptimize = new JMenu("Optimize");
		menuBar.add(mnOptimize);

		JMenuItem mntmBruteforce = new JMenuItem("Brute Force");
		mntmBruteforce.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (parser != null) {
					optimizer.optimize(parser, delayCalc, "Brute Force");
				}
			}
		});
		mnOptimize.add(mntmBruteforce);

		JMenuItem mntmHillclimbing = new JMenuItem("Hillclimbing");
		mntmHillclimbing.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (parser != null) {
					optimizer.optimize(parser, delayCalc, "Hillclimbing");
				}
			}
		});
		mnOptimize.add(mntmHillclimbing);

		JMenuItem mntmSimulatedAnnealing = new JMenuItem("Simulated Annealing");
		mntmSimulatedAnnealing.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (parser != null) {
					optimizer
							.optimize(parser, delayCalc, "Simulated Annealing");
				}
			}
		});
		mnOptimize.add(mntmSimulatedAnnealing);

		JMenuItem mntmGeneticevolutionaryAlgorithm = new JMenuItem(
				"SimpleGenerational Evolutionary Algorithm");
		mntmGeneticevolutionaryAlgorithm
				.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if (parser != null) {
							optimizer.optimize(parser, delayCalc,
									"SimpleGenerationalEA");
						}
					}
				});
		mnOptimize.add(mntmGeneticevolutionaryAlgorithm);

		JMenuItem mntmRandomWalks = new JMenuItem("Random Walks");
		mntmRandomWalks.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (parser != null) {
					optimizer.optimize(parser, delayCalc, "Random Walks");
				}
			}
		});
		mnOptimize.add(mntmRandomWalks);

		mnOptimize.add(new JSeparator());

		JMenuItem mntmAllnoBruteforce = new JMenuItem("All (no BruteForce)");
		mntmAllnoBruteforce.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (parser != null) {
					optimizer.optimize(parser, delayCalc, "Hillclimbing");
					optimizer
							.optimize(parser, delayCalc, "Simulated Annealing");
					optimizer.optimize(parser, delayCalc,
							"SimpleGenerationalEA");
					optimizer.optimize(parser, delayCalc, "Random Walks");
				}
			}
		});
		mnOptimize.add(mntmAllnoBruteforce);

		JMenu mnSettings = new JMenu("Settings");
		menuBar.add(mnSettings);

		JMenu mnTrafficModel = new JMenu("Traffic Model");
		mnSettings.add(mnTrafficModel);

		ButtonGroup trafficTypeSelectionGroup = new ButtonGroup();

		JRadioButtonMenuItem rdbtnmntmUbsV0 = new JRadioButtonMenuItem(
				"UBS V0", true);
		rdbtnmntmUbsV0.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				delayCalc = new UbsV0DelayCalc(parser.getTraffic());
			}
		});
		mnTrafficModel.add(rdbtnmntmUbsV0);
		trafficTypeSelectionGroup.add(rdbtnmntmUbsV0);

		JRadioButtonMenuItem rdbtnmntmUbsV3 = new JRadioButtonMenuItem("UBS V3");
		rdbtnmntmUbsV3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				delayCalc = new UbsV3DelayCalc(parser.getTraffic());
			}
		});
		mnTrafficModel.add(rdbtnmntmUbsV3);
		trafficTypeSelectionGroup.add(rdbtnmntmUbsV3);

		JMenu mnDisplaytype = new JMenu("Displaytype");
		mnSettings.add(mnDisplaytype);

		ButtonGroup viewTypeSelectionGroup = new ButtonGroup();

		JRadioButtonMenuItem rdbtnmntmPorts = new JRadioButtonMenuItem("Ports",
				true);
		mnDisplaytype.add(rdbtnmntmPorts);
		rdbtnmntmPorts.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				portDisplay = true;
				updateDisplay(parser.getTopology().toDot());
			}
		});
		viewTypeSelectionGroup.add(rdbtnmntmPorts);

		JRadioButtonMenuItem rdbtnmntmFlows = new JRadioButtonMenuItem("Flows");
		mnDisplaytype.add(rdbtnmntmFlows);
		rdbtnmntmFlows.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				portDisplay = false;
				updateDisplay(parser.getTraffic().toDot());
			}
		});
		viewTypeSelectionGroup.add(rdbtnmntmFlows);

		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		statusLabel = new JLabel("");
		contentPane.add(statusLabel, BorderLayout.PAGE_END);

		JPanel topologyPanel = new JPanel(new BorderLayout());
		imagePanel = new GraphVizPanel();
		topologyPanel.add(imagePanel, BorderLayout.CENTER);

		contentPane.add(topologyPanel, BorderLayout.CENTER);
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
