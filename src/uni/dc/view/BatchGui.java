package uni.dc.view;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import org.jfree.ui.RefineryUtilities;

import uni.dc.model.PriorityConfiguration;
import uni.dc.ubsOpti.Optimizer;
import uni.dc.ubsOpti.UbsOptiConfig;
import uni.dc.ubsOpti.tracer.EndStepTracer;

public class BatchGui extends JDialog {
	private static final long serialVersionUID = 1L;

	private final JPanel contentPanel = new JPanel();
	private JLabel lblProgress;

	public BatchGui() {
		setTitle("BatchMode");
		setBounds(100, 100, 200, 200);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		JLabel lblCountPorts = new JLabel("max Ports");
		lblCountPorts.setBounds(10, 10, 110, 15);
		contentPanel.add(lblCountPorts);

		JLabel lblCountDepth = new JLabel("Count Traffic");
		lblCountDepth.setBounds(10, 35, 110, 15);
		contentPanel.add(lblCountDepth);

		JSpinner spPortCount = new JSpinner();
		spPortCount.setModel(new SpinnerNumberModel(new Integer(10), new Integer(2), null, new Integer(1)));
		spPortCount.setBounds(130, 8, 45, 20);
		contentPanel.add(spPortCount);

		JSpinner spTrafficCount = new JSpinner();
		spTrafficCount.setModel(new SpinnerNumberModel(new Integer(50), new Integer(2), null, new Integer(1)));
		spTrafficCount.setBounds(130, 35, 45, 20);
		contentPanel.add(spTrafficCount);

		JProgressBar progressBar = new JProgressBar();
		progressBar.setBounds(10, 100, 165, 15);
		contentPanel.add(progressBar);

		lblProgress = new JLabel();
		lblProgress.setBounds(10, 75, 45, 15);
		contentPanel.add(lblProgress);

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int port = (Integer) spPortCount.getValue();
				int traffic = (Integer) spTrafficCount.getValue();
				run(port, 2, traffic);
				// setVisible(false);
				// dispose();
			}
		});
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);

		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				dispose();
			}
		});
		buttonPane.add(cancelButton);
	}

	private static void run(int maxPortCount, int minPortCount, int maxStreamConfig) {
		if (maxPortCount < 2)
			return;

		Optimizer opti = Optimizer.getOptimizer();
		UbsOptiConfig config = new UbsOptiConfig();
		EndStepTracer tracer = config.newEndStepTracer();

		opti.addTracer(tracer);
		config.setSeed(maxPortCount << 8 + maxPortCount);
		config.setMaxSteps(200000);
		config.setModifier(1.1d);
		int cnt = 0;
		int max = (maxPortCount - 1) * maxPortCount / 2 * maxStreamConfig;
		long t1, t2;
		t1 = System.nanoTime();
		for (int portCount = minPortCount; portCount <= maxPortCount; portCount++) {
			config.setPortCount(portCount);
			for (int depthCount = 2; depthCount <= portCount; depthCount++) {
				config.setDepth(depthCount);
				config.newTopology();
				for (int streamCount = 1; streamCount <= maxStreamConfig; streamCount++) {
					tracer.setRoundNumber(cnt);
					if (++cnt % 10 == 0)
						System.out.println(cnt + "/" + max);
					config.newTraffic();
					config.setPriorityConfig(new PriorityConfiguration(config.getTraffic()));

					opti.optimize(config, "BF");

					opti.optimize(config, "BT");

					opti.optimize(config, "HC");

					opti.optimize(config, "SA");

					opti.optimize(config, "sEA");
				}
			}
		}
		String fileName = String.format("%d_%dports_%dStreamConfigCount_%dmaxSteps_%dMaxPrio_%dmodi.csv", minPortCount,
				maxPortCount, maxStreamConfig, config.getMaxSteps(), config.getMaxPrio(),
				(int) (config.getModifier() * 100));
		t2 = System.nanoTime();
		EndStepTracer.saveToFile(new File(fileName), tracer);
		System.out.println("done, took " + (t2 - t1) / 1.0e9);
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					BatchGui gui = new BatchGui();
					RefineryUtilities.centerFrameOnScreen(gui);

					gui.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}
