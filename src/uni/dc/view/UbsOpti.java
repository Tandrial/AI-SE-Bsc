package uni.dc.view;

import java.awt.EventQueue;
import java.io.File;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;

import javax.swing.UIManager;

import org.jfree.ui.RefineryUtilities;

import uni.dc.model.PriorityConfiguration;
import uni.dc.ubsOpti.Optimizer;
import uni.dc.ubsOpti.UbsOptiConfig;
import uni.dc.ubsOpti.tracer.EndStepTracer;

public class UbsOpti {
	public static void main(String[] args) {
		if (args.length == 0) {
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
		} else {
			// Mass test

		}
	}

	private static void run(int minPort, int maxPort, int minFlow, int maxFlow, int minPrio, int maxPrio, int runs,
			boolean BF, boolean BT, boolean HC, boolean SA, boolean sEA) {
		Optimizer opti = Optimizer.getOptimizer();
		UbsOptiConfig config = new UbsOptiConfig();
		config.setSeed(minPort << 1 + maxPort << 2 + minFlow << 3 + maxFlow << 4 + minPrio << 5 + maxPrio << 6);
		EndStepTracer tracer = config.newEndStepTracer();
		opti.addTracer(tracer);
		long t1, t2;
		t1 = System.nanoTime();
		for (int portCount = minPort; portCount <= maxPort; portCount++) {
			config.setPortCount(portCount);
			for (int depthCount = 2; depthCount <= portCount; depthCount++) {
				config.setDepth(depthCount);
				config.newTopology();
				for (int flowCount = minFlow; flowCount <= maxFlow; flowCount++) {
					config.setFlowCount(flowCount);
					for (int prioCount = minPort; prioCount >= maxPrio; prioCount++) {
						config.setMaxPrio(prioCount);
						int cnt = 0;
						for (int run = 1; run <= runs; run++) {
							String id = String.format("%d%03d%03d%03d", portCount, depthCount, flowCount, prioCount,
									run);
							tracer.setID(Integer.valueOf(id));
							if (++cnt % 10 == 0)
								System.out.print("..." + cnt);
							config.newTraffic();
							config.setPriorityConfig(new PriorityConfiguration(config.getTraffic()));
							if (portCount <= 10 && BF)
								opti.optimize(config, "BF");
							if (BT)
								opti.optimize(config, "BT");
							if (HC)
								opti.optimize(config, "HC");
							if (SA)
								opti.optimize(config, "SA");
							if (sEA)
								opti.optimize(config, "sEA");

						}
					}
				}
			}
		}
		t2 = System.nanoTime();
		String fileName = String.format("%d_%dports_%dStreamConfigCount_%dmaxSteps_%dMaxPrio_%dmodi%s%s%s%s%s.csv",
				minPort, maxPort, runs, config.getMaxSteps(), config.getMaxPrio(), (int) (config.getModifier() * 100),
				BF ? "BF" : "", BT ? "BT" : "", HC ? "HC" : "", SA ? "SA" : "", sEA ? "sEA" : "");
		EndStepTracer.saveToFile(new File("./Traces/" + fileName), tracer);
		System.out
				.println(String.format("Batch done! (took %.4f s)\n See %s for results!", (t2 - t1) / 1.0e9, fileName));

	}
}
