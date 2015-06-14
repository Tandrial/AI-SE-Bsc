package uni.dc.view;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

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

import org.jfree.ui.RefineryUtilities;

import uni.dc.model.EgressTopology;
import uni.dc.model.PriorityConfiguration;
import uni.dc.model.Traffic;
import uni.dc.networkGenerator.GeneratorAPI;
import uni.dc.ubsOpti.NetworkParser;
import uni.dc.ubsOpti.Optimizer;
import uni.dc.ubsOpti.UbsOptiConfig;
import uni.dc.ubsOpti.delayCalc.UbsDelayCalc;
import uni.dc.ubsOpti.delayCalc.UbsV0DelayCalc;
import uni.dc.ubsOpti.delayCalc.UbsV3DelayCalc;
import uni.dc.ubsOpti.tracer.TraceCollection;

public class OptimizerGUI extends JFrame {
	private static final long serialVersionUID = 1L;

	private static final Logger logger = Logger.getLogger(OptimizerGUI.class
			.getName());

	private GraphVizPanel imagePanel;
	private JLabel statusLabel;
	private NetworkParser parser;
	private Optimizer optimizer;
	private UbsDelayCalc delayCalc;

	private EgressTopology topology = null;
	private Traffic traffic = null;
	private PriorityConfiguration prio = null;
	private TraceCollection traces = null;

	private boolean portDisplay = true;
	private boolean ubsV0 = true;

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

		JMenuItem mntmRandomNetwork = new JMenuItem("New Random Network");
		mntmRandomNetwork.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				generateRandom();
			}
		});
		mnFile.add(mntmRandomNetwork);

		JMenuItem mntmLoad = new JMenuItem("Load Network");
		mntmLoad.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser c = new JFileChooser();
				c.setCurrentDirectory(new File("./Topologies/"));
				c.setFileFilter(new FileNameExtensionFilter(
						"UBS Optimizer Network file", "ubsNetwork", "ser"));
				int rVal = c.showOpenDialog(OptimizerGUI.this);
				if (rVal == JFileChooser.APPROVE_OPTION) {
					loadFromFile(c.getSelectedFile());
				}
			}
		});
		mnFile.add(mntmLoad);

		JMenuItem mntmExportPng = new JMenuItem("Export Picture");
		mntmExportPng.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (topology == null)
					return;
				String fileName;
				if (parser != null) {
					fileName = parser.getFileName();
					fileName = fileName.substring(0, fileName.lastIndexOf("."));
				} else
					fileName = "" + topology;

				imagePanel.saveToFile(topology.toDot(), new File(
						"./Topologies/" + fileName + "_port.png"));
				imagePanel.saveToFile(traffic.toDot(prio), new File(
						"./Topologies/" + fileName + "_flow.png"));
			}
		});
		mnFile.add(mntmExportPng);

		JMenuItem mntmSaveNetwork = new JMenuItem("Save Network");
		mntmSaveNetwork.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (topology == null)
					return;
				String fileName = "" + topology;
				NetworkParser.saveToFile(new File("./Topologies/" + fileName
						+ ".ser"), new UbsOptiConfig(topology, traffic, prio,
						delayCalc, traces));
			}
		});
		mnFile.add(mntmSaveNetwork);
		mnFile.add(new JSeparator());

		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(new ActionListener() {
			@Override
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
			@Override
			public void actionPerformed(ActionEvent arg0) {
				optimize("BruteForce");
			}
		});
		mnOptimize.add(mntmBF);

		JMenuItem mntmHC = new JMenuItem("Hillclimbing");
		mntmHC.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				optimize("HillClimbing");
			}
		});
		mnOptimize.add(mntmHC);

		JMenuItem mntmSA = new JMenuItem("SimulatedAnnealing");
		mntmSA.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				optimize("SimulatedAnnealing");
			}
		});
		mnOptimize.add(mntmSA);

		JMenuItem mntmGA = new JMenuItem(
				"SimpleGenerational Evolutionary Algorithm");
		mntmGA.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				optimize("SimpleGenerationalEA");
			}
		});

		JMenuItem mntmRW = new JMenuItem("RandomWalk Algorithm");
		mntmRW.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				optimize("RandomWalk");
			}
		});
		mnOptimize.add(mntmRW);

		mnOptimize.add(new JSeparator());

		JMenuItem mntmRunAllexcept = new JMenuItem(
				"Run All (except BruteForce)");
		mntmRunAllexcept.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				optimize("HillClimbing");
				optimize("SimulatedAnnealing");
				optimize("SimpleGenerationalEA");
				optimize("RandomWalk");
			}
		});
		mnOptimize.add(mntmRunAllexcept);

		JMenuItem mntmDisplayGraph = new JMenuItem("Display Graph");
		mntmDisplayGraph.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (traces == null)
					return;
				TraceDisplay traceDisplay = new TraceDisplay("Trace Display",
						traces);
				traceDisplay.pack();
				RefineryUtilities.centerFrameOnScreen(traceDisplay);
				traceDisplay.setVisible(true);
			}
		});
		mnOptimize.add(mntmDisplayGraph);

		JMenu mnSettings = new JMenu("Settings");
		menuBar.add(mnSettings);

		JMenu mnTrafficModel = new JMenu("Traffic Model");
		mnSettings.add(mnTrafficModel);

		ButtonGroup trafficTypeSelectionGroup = new ButtonGroup();

		JRadioButtonMenuItem rdbtnmntmUbsV0 = new JRadioButtonMenuItem(
				"UBS V0", true);
		rdbtnmntmUbsV0.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (topology != null && !ubsV0) {
					delayCalc = new UbsV0DelayCalc(traffic);
				}
				ubsV0 = true;
			}
		});
		mnTrafficModel.add(rdbtnmntmUbsV0);
		trafficTypeSelectionGroup.add(rdbtnmntmUbsV0);

		JRadioButtonMenuItem rdbtnmntmUbsV3 = new JRadioButtonMenuItem("UBS V3");
		rdbtnmntmUbsV3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (topology != null && ubsV0) {
					delayCalc = new UbsV3DelayCalc(traffic);
				}
				ubsV0 = false;
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
			@Override
			public void actionPerformed(ActionEvent e) {
				if (topology != null && !portDisplay) {
					updateDisplay(topology.toDot());
				}
				portDisplay = true;
			}
		});
		viewTypeSelectionGroup.add(rdbtnmntmPorts);

		JRadioButtonMenuItem rdbtnmntmFlows = new JRadioButtonMenuItem("Flows");
		mnDisplaytype.add(rdbtnmntmFlows);
		rdbtnmntmFlows.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (traffic != null && portDisplay) {
					updateDisplay(traffic.toDot(prio));
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
		if (topology == null)
			return;
		logger.entering(getClass().getName(), "optimize");
		setStatusMsg("Optimizing Priorities! This might take a while ...");
		prio = new PriorityConfiguration(traffic);
		long t1, t2, t3;
		t1 = System.nanoTime();
		UbsOptiConfig optiConfig = new UbsOptiConfig(topology, traffic, prio,
				delayCalc, traces);
		logger.log(Level.INFO, "Optimazation started with " + algo);
		boolean result = optimizer.optimize(optiConfig, algo);
		prio = traces.getBestConfig();

		t2 = System.nanoTime();
		if (result) {
			logger.log(Level.INFO, String
					.format("Optimazation successful (in %.4f sec)!",
							(t2 - t1) / 1.0e9));
		} else {
			logger.log(Level.INFO, String.format(
					"Optimazation failed (in %.4f sec)!", (t2 - t1) / 1.0e9));
		}
		logger.log(Level.INFO, String.format(
				"Best Prio is: \n%s\nDelays are \n%s", prio, delayCalc));

		imagePanel.setDot(portDisplay ? topology.toDot() : traffic.toDot(prio));
		t3 = System.nanoTime();
		setStatusMsg("Done (optimized in %.4f sec., rendered in %.4f sec.)",
				(t2 - t1) / 1.0E9, (t3 - t2) / 1.0E9);
		logger.exiting(getClass().getName(), "optimize");
	}

	private void updateDisplay(StringBuilder content) {
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
			logger.entering(getClass().getName(), "loadFromFile");
			t1 = System.nanoTime();
			parser = new NetworkParser(file);
			topology = parser.getTopology();
			traffic = parser.getTraffic();
			prio = parser.getPriorityConfig();
			delayCalc = ubsV0 ? new UbsV0DelayCalc(traffic)
					: new UbsV3DelayCalc(traffic);
			delayCalc.calculateDelays(prio);
			traces = new TraceCollection();

			logger.log(
					Level.INFO,
					String.format(
							"Loaded network \"%s\" with these streams:\n%s\nPriorites:\n%s",
							file.getName(), delayCalc, prio));
			t2 = System.nanoTime();
			imagePanel.setDot(portDisplay ? topology.toDot() : traffic
					.toDot(prio));
			t3 = System.nanoTime();

			setStatusMsg("Done (loaded in %.4f sec., rendered in %.4f sec.)",
					(t2 - t1) / 1.0E9, (t3 - t2) / 1.0E9);
			setTitle("UBS Optimizer - " + file.getName());
		} catch (Exception e) {
			e.printStackTrace();
			setStatusMsg("Load from File failed!");
			logger.log(Level.SEVERE, "Load from File failed!", e);
		}
		logger.exiting(getClass().getName(), "loadFromFile");
	}

	private void generateRandom() {
		long t1, t2, t3;

		setStatusMsg("Generating topology ...");
		try {
			logger.entering(getClass().getName(), "generateRandomNetwork");
			t1 = System.nanoTime();

			// GeneratorAPI.generateNetwork(5, 12, 2);
			GeneratorAPI.generateNetwork(6, 9, 4);
			// GeneratorAPI.generateNetwork(2, 4, 2);
			topology = GeneratorAPI.getTopology();
			traffic = GeneratorAPI.getTraffic();
			prio = GeneratorAPI.getPriorityConfiguration();
			traces = new TraceCollection();

			delayCalc = ubsV0 ? new UbsV0DelayCalc(traffic)
					: new UbsV3DelayCalc(traffic);
			delayCalc.setInitialDelays(prio);

			logger.log(
					Level.INFO,
					String.format(
							"Generated new network with these streams:\n%s\nStarting priorites:\n%s",
							delayCalc, prio));

			t2 = System.nanoTime();
			imagePanel.setDot(portDisplay ? topology.toDot() : traffic
					.toDot(prio));
			t3 = System.nanoTime();

			setStatusMsg(
					"Done (generated in %.4f sec., rendered in %.4f sec.)",
					(t2 - t1) / 1.0E9, (t3 - t2) / 1.0E9);
		} catch (Exception e) {
			e.printStackTrace();
			setStatusMsg("Generation failed!");
			logger.log(Level.SEVERE, "Generation failed!", e);
		}
		logger.exiting(getClass().getName(), "generateRandomNetwork");
	}

	private void setStatusMsg(String fmt, Object... args) {
		statusLabel.setText(String.format(fmt, args));
		statusLabel.repaint();
		statusLabel.validate();
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager
							.getSystemLookAndFeelClassName());
					OptimizerGUI gui = new OptimizerGUI("UBS Optimizer");
					RefineryUtilities.centerFrameOnScreen(gui);

					FileHandler fh = new FileHandler("./ubsOpti.log");
					OptimizerGUI.logger.addHandler(fh);
					fh.setFormatter(new SimpleFormatter());
					OptimizerGUI.logger.info("********************");

					gui.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
