package uni.dc.view;

import java.awt.EventQueue;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;

import javax.swing.UIManager;

import org.jfree.ui.RefineryUtilities;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import uni.dc.model.PriorityConfiguration;
import uni.dc.ubsOpti.Optimizer;
import uni.dc.ubsOpti.UbsOptiConfig;
import uni.dc.ubsOpti.tracer.EndStepTracer;

public class UbsOpti {

	@Option(name = "-factor", usage = "Factor that makes the optimazation easier")
	private double factor = 1.2d;

	@Option(name = "-minPort", usage = "min amount of Ports")
	private int minPort = 2;

	@Option(name = "-maxPort", usage = "max amount of Ports")
	private int maxPort = 10;

	@Option(name = "-minFlow", usage = "min amount of Flows")
	private int minFlow = 3;

	@Option(name = "-maxFlow", usage = "max amount of Flows")
	private int maxFlow = 6;

	@Option(name = "-minPrio", usage = "min amount of Prio")
	private int minPrio = 2;

	@Option(name = "-maxPrio", usage = "max amount of Prio")
	private int maxPrio = 2;

	@Option(name = "-runs", usage = "runs for each Configuration")
	private int runs = 50;

	@Option(name = "-BT", usage = "BackTrack enabled")
	private boolean BT = false;

	@Option(name = "-HC", usage = "HillClimbing enabled")
	private boolean HC = false;

	@Option(name = "-SA", usage = "Simulated Annealing enabled")
	private boolean SA = false;

	@Option(name = "-sEA", usage = "simple EvolutionAlgo enabled")
	private boolean sEA = false;

	@Argument
	private List<String> arguments = new ArrayList<String>();

	public static void main(String[] args) {
		args = new String[4];
		args[0] = "-BT";
		args[1] = "-HC";
		args[2] = "-SA";
		args[3] = "-sEA";

		if (args.length == 0) {
			new UbsOpti().startGui();
		} else {
			new UbsOpti().startCli(args);
		}
	}

	public void startCli(String[] args) {
		CmdLineParser parser = new CmdLineParser(this);
		try {
			parser.parseArgument(args);
		} catch (CmdLineException e) {
			System.err.println(e.getMessage());
			System.err.println("java UbsOpti [options...] arguments...");
			parser.printUsage(System.err);
			System.err.println();
			return;
		}
		run();
	}

	private void run() {
		Optimizer opti = Optimizer.getOptimizer();
		UbsOptiConfig config = new UbsOptiConfig();
		config.setSeed(minPort << 1 + maxPort << 2 + minFlow << 3 + maxFlow << 4 + minPrio << 5 + maxPrio << 6);
		config.setModifier(factor);
		EndStepTracer tracer = config.newEndStepTracer();
		opti.addTracer(tracer);
		long t1, t2;
		t1 = System.nanoTime();
		for (int portCount = minPort; portCount <= maxPort; portCount++) {
			config.setPortCount(portCount);
			for (int depthCount = 2; depthCount <= portCount - 2; depthCount++) {
				config.setDepth(depthCount);
				config.newTopology();
				System.out.println(String.format("\nNew Topologies : %d ports, %d depth:", portCount, depthCount));
				for (int flowCount = minFlow; flowCount <= Math.min(maxFlow, portCount - 2); flowCount++) {
					config.setFlowCount(flowCount);
					System.out.println(String.format("  flowCount : %d", flowCount));
					for (int prioCount = minPrio; prioCount <= maxPrio; prioCount++) {
						config.setMaxPrio(prioCount);
						System.out.print(String.format("    maxStep : %d    prioCount : %d\n      ",
								config.getMaxSteps(), prioCount));
						int cnt = 0;
						for (int run = 1; run <= runs; run++) {
							String id = String.format("%03d%03d%03d%03d", portCount, depthCount, flowCount, prioCount,
									run);
							tracer.setID(id);
							if (++cnt % 10 == 0)
								System.out.print("..." + cnt);
							config.newTraffic();
							config.setPriorityConfig(new PriorityConfiguration(config.getTraffic()));
							if (BT)
								opti.optimize(config, "BT");
							if (HC)
								opti.optimize(config, "HC");
							if (SA)
								opti.optimize(config, "SA");
							if (sEA)
								opti.optimize(config, "sEA");

						}
						System.out.println("");
					}
				}
			}
		}
		t2 = System.nanoTime();
		String fileName = String.format("%d_%dports_%dStreamConfigCount_%dmaxSteps_%dMaxPrio_%dmodi%s%s%s%s%s.csv",
				minPort, maxPort, runs, config.getMaxSteps(), config.getMaxPrio(), (int) (config.getModifier() * 100),
				BT ? "BT" : "", HC ? "HC" : "", SA ? "SA" : "", sEA ? "sEA" : "");
		EndStepTracer.saveToFile(new File("./Traces/" + fileName), tracer);
		System.out
				.println(String.format("Batch done! (took %.4f s)\n See %s for results!", (t2 - t1) / 1.0e9, fileName));

	}

	private void startGui() {
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
