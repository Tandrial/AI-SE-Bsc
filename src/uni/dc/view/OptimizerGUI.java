package uni.dc.view;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Random;
import java.util.Set;

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
import uni.dc.model.Flow;
import uni.dc.model.PriorityConfiguration;
import uni.dc.model.Traffic;
import uni.dc.networkGenerator.RandomMulticastPathGenerator;
import uni.dc.networkGenerator.RandomTopologyGenerator;
import uni.dc.ubsOpti.Optimizer;
import uni.dc.ubsOpti.DelayCalc.UbsDelayCalc;
import uni.dc.ubsOpti.DelayCalc.UbsV0DelayCalc;
import uni.dc.ubsOpti.DelayCalc.UbsV3DelayCalc;
import uni.dc.util.NetworkParser;
import uni.dc.util.OptimizerConfig;

public class OptimizerGUI extends JFrame {
	private static final long serialVersionUID = 1L;
	private GraphVizPanel imagePanel;
	private JLabel statusLabel;
	private NetworkParser parser;
	private Optimizer optimizer;
	private OptimizerConfig optiConfig = new OptimizerConfig();
	private UbsDelayCalc delayCalc;

	private EgressTopology rndTopology;
	private Traffic rndTraffic;
	private PriorityConfiguration rndPrio;

	private boolean portDisplay = true;
	private boolean ubsV0 = true;
	private boolean randomNetwork = false;

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
						"UBS Optimizer Network file", "ubsNetwork"));
				int rVal = c.showOpenDialog(OptimizerGUI.this);
				if (rVal == JFileChooser.APPROVE_OPTION) {
					loadFromFile(c.getSelectedFile());
					randomNetwork = false;
				}
			}
		});

		JMenuItem mntmRandomNetwork = new JMenuItem("Random Network");
		mntmRandomNetwork.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				randomNetwork = true;
				generateRandom();
			}
		});
		mnFile.add(mntmRandomNetwork);
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

		JMenuItem mntmBF = new JMenuItem("BruteForce");
		mntmBF.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				optimize("BruteForce");
			}
		});
		mnOptimize.add(mntmBF);

		JMenuItem mntmHC = new JMenuItem("Hillclimbing");
		mntmHC.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				optimize("HillClimbing");
			}
		});
		mnOptimize.add(mntmHC);

		JMenuItem mntmSA = new JMenuItem("SimulatedAnnealing");
		mntmSA.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				optimize("SimulatedAnnealing");
			}
		});
		mnOptimize.add(mntmSA);

		JMenuItem mntmGA = new JMenuItem(
				"SimpleGenerational Evolutionary Algorithm");
		mntmGA.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				optimize("SimpleGenerationalEA");
			}
		});
		mnOptimize.add(mntmGA);

		JMenuItem mntmRW = new JMenuItem("RandomWalks");
		mntmRW.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				optimize("RandomWalks");
			}
		});
		mnOptimize.add(mntmRW);

		mnOptimize.add(new JSeparator());

		JMenuItem mntmAllnoBruteforce = new JMenuItem("All (no BruteForce)");
		mntmAllnoBruteforce.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (parser != null) {
					String[] algos = new String[] { "Hillclimbing",
							"SimulatedAnnealing", "SimpleGenerationalEA",
							"RandomWalks" };
					for (int i = 0; i < algos.length; i++) {
						optimize(algos[i]);
					}
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
				if (!ubsV0) {
					ubsV0 = true;
					delayCalc = new UbsV0DelayCalc(parser.getTraffic());
					delayCalc.setInitialDelays(parser.getPriorityConfig());
				}
			}
		});
		mnTrafficModel.add(rdbtnmntmUbsV0);
		trafficTypeSelectionGroup.add(rdbtnmntmUbsV0);

		JRadioButtonMenuItem rdbtnmntmUbsV3 = new JRadioButtonMenuItem("UBS V3");
		rdbtnmntmUbsV3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (ubsV0) {
					ubsV0 = false;
					delayCalc = new UbsV3DelayCalc(parser.getTraffic());
					delayCalc.setInitialDelays(parser.getPriorityConfig());
				}
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
				if (parser != null && !portDisplay) {
					updateDisplay(parser.getTopology().toDot());
				} else if (randomNetwork && !portDisplay) {
					updateDisplay(rndTopology.toDot());
				}
				portDisplay = true;
			}
		});
		viewTypeSelectionGroup.add(rdbtnmntmPorts);

		JRadioButtonMenuItem rdbtnmntmFlows = new JRadioButtonMenuItem("Flows");
		mnDisplaytype.add(rdbtnmntmFlows);
		rdbtnmntmFlows.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (parser != null && portDisplay) {
					updateDisplay(parser.getTraffic().toDot());
				} else if (randomNetwork && !portDisplay) {
					updateDisplay(rndTraffic.toDot());
				}
				portDisplay = false;
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

	private void optimize(String algo) {
		if (parser == null)
			return;
		SpeedUpGui s = new SpeedUpGui(parser.getTraffic());
		s.setModal(true);
		s.setVisible(true);

		Set<Flow> speedUp = s.getflowsToSpeedUp();
		if (speedUp == null)
			return;
		setStatusMsg("Optimizing Priorities! This might take a while ...");

		long t1, t2;
		t1 = System.nanoTime();
		for (Flow f : parser.getTraffic()) {
			f.resetSpeed();
		}
		delayCalc.setInitialDelays(parser.getPriorityConfig());

		optiConfig.setFlows(speedUp);
		optiConfig.setDelayCalc(delayCalc);
		optiConfig.setParser(parser);

		optimizer.optimize(optiConfig, algo);

		t2 = System.nanoTime();

		setStatusMsg("Done (optimized in %.4f sec.)", (t2 - t1) / 1.0E9);
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
			delayCalc = ubsV0 ? new UbsV0DelayCalc(parser.getTraffic())
					: new UbsV3DelayCalc(parser.getTraffic());
			delayCalc.setInitialDelays(parser.getPriorityConfig());

			t2 = System.nanoTime();
			imagePanel.setDot(portDisplay ? parser.getTopology().toDot()
					: parser.getTraffic().toDot());
			t3 = System.nanoTime();

			setStatusMsg("Done (loaded in %.4f sec., rendered in %.4f sec.)",
					(t2 - t1) / 1.0E9, (t3 - t2) / 1.0E9);
			setTitle("UBS Optimizer - " + parser.getFileName());
			randomNetwork = false;
		} catch (Exception e) {
			e.printStackTrace();
			setStatusMsg("Load from File failed!");
		}
	}

	private void generateRandom() {
		long t1, t2, t3;

		setStatusMsg("Generating topology ...");
		try {
			t1 = System.nanoTime();

			RandomTopologyGenerator topologyGen = new RandomTopologyGenerator();
			topologyGen.setRng(new Random(0x1337));
			topologyGen.setDepth(5);
			topologyGen.setPorts(12);
			rndTopology = topologyGen.generate();

			RandomMulticastPathGenerator flowPathGen = new RandomMulticastPathGenerator();
			flowPathGen.setTopology(rndTopology);
			flowPathGen.setRng(new Random(0));
			flowPathGen.setMinFlowPerPort(3);
			flowPathGen.setMaxDestPerFlow(2);

			rndTraffic = flowPathGen.generate();

			rndPrio = new PriorityConfiguration(rndTraffic);

			t2 = System.nanoTime();
			imagePanel.setDot(portDisplay ? rndTopology.toDot() : rndTraffic
					.toDot());
			t3 = System.nanoTime();
			randomNetwork = true;

			setStatusMsg(
					"Done (generated in %.4f sec., rendered in %.4f sec.)",
					(t2 - t1) / 1.0E9, (t3 - t2) / 1.0E9);
		} catch (Exception e) {
			e.printStackTrace();
			setStatusMsg("Generation failed!");
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
