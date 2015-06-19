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

import uni.dc.model.PriorityConfiguration;
import uni.dc.ubsOpti.NetworkParser;
import uni.dc.ubsOpti.Optimizer;
import uni.dc.ubsOpti.UbsOptiConfig;

public class UbsOptiGui extends JFrame {
	private static final long serialVersionUID = 1L;

	static final Logger logger = Logger.getLogger(UbsOptiGui.class.getName());

	private GraphVizPanel imagePanel;
	private JLabel statusLabel;
	private UbsOptiConfig config = new UbsOptiConfig();

	private boolean portDisplay = true;

	public UbsOptiGui(String title) {
		super(title);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		setSize(1024, 768);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		{
			JMenu mnFile = new JMenu("File");
			menuBar.add(mnFile);
			{
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
						c.setFileFilter(new FileNameExtensionFilter("UBS Optimizer Network file", "ubsNetwork", "ser"));
						int rVal = c.showOpenDialog(UbsOptiGui.this);
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
						if (config.getTopology() == null) {
							setStatusMsg("Please generate/load a network first");
							return;
						}
						String fileName;
						if (NetworkParser.getParser().getFile() != null) {
							fileName = NetworkParser.getParser().getFile().getName();
							fileName = fileName.substring(0, fileName.lastIndexOf("."));
						} else
							fileName = "" + config.getTopology();

						imagePanel.saveToFile(config.getTopology().toDot(), new File("./Topologies/" + fileName
								+ "_port.png"));
						imagePanel.saveToFile(config.getTraffic().toDot(config.getPriorityConfig()), new File(
								"./Topologies/" + fileName + "_flow.png"));
					}
				});
				mnFile.add(mntmExportPng);

				JMenuItem mntmSaveNetwork = new JMenuItem("Save Network");
				mntmSaveNetwork.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						if (config.getTopology() == null) {
							setStatusMsg("Error: No network loaded!");
							return;
						}
						String fileName = "" + config.getTopology();
						NetworkParser.saveToFile(new File("./Topologies/" + fileName + ".ser"), config);
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
			}

			JMenu mnOptimize = new JMenu("Optimize");
			menuBar.add(mnOptimize);
			{
				JMenuItem mntmBF = new JMenuItem("BruteForce");
				mntmBF.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						optimize("BruteForce");
					}
				});
				mnOptimize.add(mntmBF);

				JMenuItem mntmBT = new JMenuItem("BackTrack");
				mntmBT.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						optimize("BackTrack");
					}
				});
				mnOptimize.add(mntmBT);

				mnOptimize.add(new JSeparator());

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

				JMenuItem mntmGA = new JMenuItem("SimpleGenerational Evolutionary Algorithm");
				mntmGA.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						optimize("SimpleGenerationalEA");
					}
				});
				mnOptimize.add(mntmGA);

				mnOptimize.add(new JSeparator());

				JMenuItem mntmRunAllexcept = new JMenuItem("Run All (except BruteForce/BackTrack)");
				mntmRunAllexcept.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						optimize("HillClimbing");
						optimize("SimulatedAnnealing");
						optimize("SimpleGenerationalEA");
					}
				});
				mnOptimize.add(mntmRunAllexcept);

				JMenuItem mntmDisplayGraph = new JMenuItem("Display Best only Linechart");
				mntmDisplayGraph.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						if (config.getTraces() == null) {
							setStatusMsg("Error: Need to run atleast one optimazation!");
							return;
						}
						BestOnlyTraceDisplay traceDisplay = new BestOnlyTraceDisplay("Best only Linechart", config
								.getTraces());
						traceDisplay.pack();
						RefineryUtilities.centerFrameOnScreen(traceDisplay);
						traceDisplay.setVisible(true);
					}
				});
				mnOptimize.add(mntmDisplayGraph);
			}

			JMenu mnSettings = new JMenu("Settings");
			menuBar.add(mnSettings);
			{
				ButtonGroup viewTypeSelectionGroup = new ButtonGroup();

				JRadioButtonMenuItem rdbtnmntmPorts = new JRadioButtonMenuItem("Display Ports", true);
				mnSettings.add(rdbtnmntmPorts);
				rdbtnmntmPorts.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						if (config.getTopology() != null && !portDisplay) {
							updateDisplay(config.getTopology().toDot());
						}
						portDisplay = true;
					}
				});
				viewTypeSelectionGroup.add(rdbtnmntmPorts);

				JRadioButtonMenuItem rdbtnmntmFlows = new JRadioButtonMenuItem("Display Flows");
				mnSettings.add(rdbtnmntmFlows);
				rdbtnmntmFlows.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						if (config.getTopology() != null && portDisplay) {
							updateDisplay(config.getTraffic().toDot(config.getPriorityConfig()));
						}
						portDisplay = false;
					}
				});
				viewTypeSelectionGroup.add(rdbtnmntmFlows);

				mnSettings.add(new JSeparator());

				JMenuItem mntmDisplaySettings = new JMenuItem("Settings");
				mntmDisplaySettings.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						SettingsGui settings = new SettingsGui(config);
						RefineryUtilities.centerFrameOnScreen(settings);
						settings.setVisible(true);
					}
				});
				mnSettings.add(mntmDisplaySettings);
			}
		}

		JPanel contentPane = new JPanel();
		{
			contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
			contentPane.setLayout(new BorderLayout(0, 0));
			setContentPane(contentPane);

			statusLabel = new JLabel("");
			contentPane.add(statusLabel, BorderLayout.PAGE_END);

			imagePanel = new GraphVizPanel();
			contentPane.add(imagePanel, BorderLayout.CENTER);
		}
	}

	private void optimize(String algo) {
		if (config.getTopology() == null) {
			setStatusMsg("Error: No network loaded!");
			return;
		}
		logger.entering(getClass().getName(), "optimize");
		setStatusMsg("Optimizing Priorities! This might take a while ...");
		config.setPriorityConfig(new PriorityConfiguration(config.getTraffic()));
		long t1, t2, t3;
		t1 = System.nanoTime();

		logger.log(Level.INFO,
				String.format("Optimazation for %s started with %s", config.isUbsV0() ? "UBS-V0" : "UBS-V3", algo));

		boolean result = Optimizer.getOptimizer().optimize(config, algo);
		config.setBestConfig();

		t2 = System.nanoTime();
		if (result) {
			logger.log(Level.INFO, String.format("Optimazation successful (in %.4f sec)!", (t2 - t1) / 1.0e9));
		} else {
			logger.log(Level.INFO, String.format("Optimazation failed (in %.4f sec)!", (t2 - t1) / 1.0e9));
		}
		logger.log(Level.INFO,
				String.format("Best Prio is: \n%s\nDelays are \n%s", config.getPriorityConfig(), config.getDelayCalc()));

		imagePanel.setDot(portDisplay ? config.getTopology().toDot() : config.getTraffic().toDot(
				config.getPriorityConfig()));
		t3 = System.nanoTime();
		setStatusMsg("Done (optimized in %.4f sec., rendered in %.4f sec.)", (t2 - t1) / 1.0E9, (t3 - t2) / 1.0E9);
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

			NetworkParser parser = NetworkParser.getParser();
			parser.setFileName(file);
			config.fromParser(parser);

			logger.log(Level.INFO, String.format("Loaded network \"%s\":\n%s", file.getName(), config));
			t2 = System.nanoTime();
			imagePanel.setDot(portDisplay ? config.getTopology().toDot() : config.getTraffic().toDot(
					config.getPriorityConfig()));
			t3 = System.nanoTime();

			setStatusMsg("Done (loaded in %.4f sec., rendered in %.4f sec.)", (t2 - t1) / 1.0E9, (t3 - t2) / 1.0E9);
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

			config.fromGenerator();

			logger.log(Level.INFO, String.format("Generated new network: \n%s", config));

			t2 = System.nanoTime();
			imagePanel.setDot(portDisplay ? config.getTopology().toDot() : config.getTraffic().toDot(
					config.getPriorityConfig()));
			t3 = System.nanoTime();

			setStatusMsg("Done (generated in %.4f sec., rendered in %.4f sec.)", (t2 - t1) / 1.0E9, (t3 - t2) / 1.0E9);
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
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					UbsOptiGui gui = new UbsOptiGui("UBS Optimizer");
					RefineryUtilities.centerFrameOnScreen(gui);

					FileHandler fh = new FileHandler("./ubsOpti.log");
					fh.setFormatter(new SimpleFormatter());

					UbsOptiGui.logger.addHandler(fh);
					SettingsGui.logger.addHandler(fh);
					gui.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
