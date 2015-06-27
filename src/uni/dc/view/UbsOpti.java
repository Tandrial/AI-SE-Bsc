package uni.dc.view;

import java.awt.EventQueue;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;

import javax.swing.UIManager;

import org.jfree.ui.RefineryUtilities;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import uni.dc.model.PriorityConfiguration;
import uni.dc.ubsOpti.Optimizer;
import uni.dc.ubsOpti.UbsOptiConfig;
import uni.dc.ubsOpti.tracer.EndStepTracer;

public class UbsOpti {

	@Option(name = "-help", usage = "displays this hlep message")
	private boolean help = false;

	@Option(name = "-BT", usage = "BackTrack enabled")
	private boolean BT = false;

	@Option(name = "-HC", usage = "HillClimbing enabled")
	private boolean HC = false;

	@Option(name = "-SA", usage = "Simulated Annealing enabled")
	private boolean SA = false;

	@Option(name = "-sEA", usage = "simple EvolutionAlgo enabled")
	private boolean sEA = false;

	@Option(name = "-minPort", usage = "min amount of Ports")
	private int minPort = 2;

	@Option(name = "-maxPort", usage = "max amount of Ports")
	private int maxPort = 20;

	@Option(name = "-minFlow", usage = "min amount of Flows")
	private int minFlow = 6;

	@Option(name = "-maxFlow", usage = "max amount of Flows")
	private int maxFlow = 6;

	@Option(name = "-minPrio", usage = "min amount of Prio")
	private int minPrio = 2;

	@Option(name = "-maxPrio", usage = "max amount of Prio")
	private int maxPrio = 3;

	@Option(name = "-runs", usage = "runs for each Configuration")
	private int runs = 50;

	@Option(name = "-factor", usage = "factor that makes the optimazation easier")
	private double factor = 1d;

	@Option(name = "-maxStep", usage = "max amount of steps before the algo stops trying")
	private long maxStep = 1 << 22;// -1;

	@Option(name = "-seed", usage = "seed for the random generator")
	private long seed = 0x1337;

	public static void main(String[] args) {
		// args = new String[4];
		// args[0] = "-BT";
		// args[1] = "-HC";
		// args[2] = "-SA";
		// args[3] = "-sEA";

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
			if (help) {
				System.out.println("java UbsOpti [options...] arguments...");
				parser.printUsage(System.out);
				return;

			}
		} catch (CmdLineException e) {
			System.err.println(e.getMessage());
			System.err.println("java UbsOpti [options...] arguments...");
			parser.printUsage(System.err);
			System.err.println();
			return;
		}
		if (minPort > maxPort)
			maxPort = minPort;
		if (minFlow > maxFlow)
			maxFlow = minFlow;
		if (minPrio > maxPrio)
			minPrio = maxPrio;
		run();
	}

	private void run() {
		Optimizer opti = Optimizer.getOptimizer();
		UbsOptiConfig config = new UbsOptiConfig();
		config.setSeed(seed);
		config.setModifier(factor);
		EndStepTracer tracer = config.newEndStepTracer();
		opti.addTracer(tracer);
		long t1, t2;
		t1 = System.nanoTime();
		for (int portCount = minPort; portCount <= maxPort; portCount++) {
			config.setPortCount(portCount);
			for (int depthCount = 2; depthCount <= Math.max(2, portCount - 2); depthCount++) {
				config.setDepth(depthCount);
				config.newTopology();
				System.out.println(String.format("\nNew Topologies : %d ports, %d depth:", portCount, depthCount));
				for (int flowCount = minFlow; flowCount <= Math.min(maxFlow, portCount - 2); flowCount++) {
					config.setFlowCount(flowCount);
					System.out.println(String.format("  flowCount : %d", flowCount));
					for (int prioCount = minPrio; prioCount <= maxPrio; prioCount++) {
						config.setMaxPrio(prioCount);
						System.out.print(String.format("    prioCount : %d\n      ", prioCount));
						for (int run = 1; run <= runs; run++) {
							if (run % 10 == 0)
								System.out.print("..." + run);
							config.newTraffic();
							config.setPriorityConfig(new PriorityConfiguration(config.getTraffic()));
							if (maxStep != -1 && config.getMaxSteps() > maxStep)
								continue;

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
						String fileName = String.format("%dports_%dflows_%dprios_%dmodi", portCount, flowCount,
								prioCount, (int) (config.getModifier() * 100));
						EndStepTracer.saveToFile("./Traces/" + fileName, tracer);
						tracer.clearData();
					}
				}
			}
		}
		t2 = System.nanoTime();
		System.out.println(String.format("Batch done! (took %.4f s)", (t2 - t1) / 1.0e9));
	}

	private void startGui() {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					UbsOptiGui gui = new UbsOptiGui("UBS Optimizer");
					RefineryUtilities.centerFrameOnScreen(gui);

					FileHandler fh = new FileHandler("./UbsOpti.log");
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
