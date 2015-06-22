package uni.dc.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;

import uni.dc.model.PriorityConfiguration;
import uni.dc.ubsOpti.Optimizer;
import uni.dc.ubsOpti.UbsOptiConfig;
import uni.dc.ubsOpti.tracer.EndStepTracer;
import javax.swing.JCheckBox;

public class BatchGui extends JDialog {
	private static final long serialVersionUID = 1L;
	static final Logger logger = Logger.getLogger(BatchGui.class.getName());

	private final JPanel contentPanel = new JPanel();

	public BatchGui() {
		setTitle("BatchMode");
		setBounds(100, 100, 200, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		JLabel lblCountPorts = new JLabel("max Ports");
		lblCountPorts.setBounds(10, 39, 110, 15);
		contentPanel.add(lblCountPorts);

		JLabel lblCountDepth = new JLabel("runs each config");
		lblCountDepth.setBounds(10, 126, 110, 15);
		contentPanel.add(lblCountDepth);

		JLabel lblMinPorts = new JLabel("min Ports");
		lblMinPorts.setBounds(10, 13, 110, 15);
		contentPanel.add(lblMinPorts);

		JLabel lblMaxPrio = new JLabel("max Prio");
		lblMaxPrio.setBounds(10, 97, 110, 15);
		contentPanel.add(lblMaxPrio);

		JLabel lblRunsMaxSteps = new JLabel("runs max Steps");
		lblRunsMaxSteps.setBounds(10, 155, 110, 15);
		contentPanel.add(lblRunsMaxSteps);

		JSpinner spMinPort = new JSpinner();
		spMinPort.setModel(new SpinnerNumberModel(new Integer(2), new Integer(2), null, new Integer(1)));
		spMinPort.setBounds(130, 11, 45, 20);
		contentPanel.add(spMinPort);

		JSpinner spMaxPort = new JSpinner();
		spMaxPort.setModel(new SpinnerNumberModel(new Integer(10), new Integer(2), null, new Integer(1)));
		spMaxPort.setBounds(130, 36, 45, 20);
		contentPanel.add(spMaxPort);

		JSpinner spMaxPrio = new JSpinner();
		spMaxPrio.setModel(new SpinnerNumberModel(new Integer(2), new Integer(2), null, new Integer(1)));
		spMaxPrio.setBounds(130, 94, 45, 20);
		contentPanel.add(spMaxPrio);

		JSpinner spRuns = new JSpinner();
		spRuns.setModel(new SpinnerNumberModel(new Integer(50), new Integer(2), null, new Integer(1)));
		spRuns.setBounds(130, 123, 45, 20);
		contentPanel.add(spRuns);

		JSpinner spMaxSteps = new JSpinner();
		spMaxSteps.setModel(new SpinnerNumberModel(new Integer(500000), new Integer(50000), null, new Integer(1)));
		spMaxSteps.setBounds(94, 152, 81, 20);
		contentPanel.add(spMaxSteps);

		JLabel lblMaxFlows = new JLabel("max Flows");
		lblMaxFlows.setBounds(10, 68, 110, 15);
		contentPanel.add(lblMaxFlows);

		JSpinner spMaxFlow = new JSpinner();
		spMaxFlow.setModel(new SpinnerNumberModel(new Integer(6), new Integer(3), null, new Integer(1)));
		spMaxFlow.setBounds(130, 65, 45, 20);
		contentPanel.add(spMaxFlow);

		JCheckBox cbBF = new JCheckBox("BF");
		cbBF.setBounds(6, 177, 45, 23);
		contentPanel.add(cbBF);

		JCheckBox cbBT = new JCheckBox("BT");
		cbBT.setSelected(true);
		cbBT.setBounds(58, 177, 45, 23);
		contentPanel.add(cbBT);

		JCheckBox cbHC = new JCheckBox("HC");
		cbHC.setSelected(true);
		cbHC.setBounds(104, 177, 45, 23);
		contentPanel.add(cbHC);

		JCheckBox cbSA = new JCheckBox("SA");
		cbSA.setSelected(true);
		cbSA.setBounds(6, 199, 45, 23);
		contentPanel.add(cbSA);

		JCheckBox cbSEA = new JCheckBox("sEA");
		cbSEA.setSelected(true);
		cbSEA.setBounds(58, 199, 45, 23);
		contentPanel.add(cbSEA);

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int minPort = (Integer) spMinPort.getValue();
				int maxPort = (Integer) spMaxPort.getValue();
				int maxFlow = (Integer) spMaxFlow.getValue();
				int maxPrio = (Integer) spMaxPrio.getValue();
				int runs = (Integer) spRuns.getValue();
				int maxSteps = (Integer) spMaxSteps.getValue();

				run(minPort, maxPort, maxPrio, runs, maxSteps, maxFlow, cbBF.isSelected(), cbBT.isSelected(),
						cbHC.isSelected(), cbSA.isSelected(), cbSEA.isSelected());
			}
		});
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);

		JButton cancelButton = new JButton("Close");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				dispose();
			}
		});
		buttonPane.add(cancelButton);
	}

	private static void run(int minPort, int maxPort, int maxPrio, int runs, int maxSteps, int maxFlowCount,
			boolean BF, boolean BT, boolean HC, boolean SA, boolean sEA) {
		Optimizer opti = Optimizer.getOptimizer();
		UbsOptiConfig config = new UbsOptiConfig();
		EndStepTracer tracer = config.newEndStepTracer();

		opti.addTracer(tracer);

		config.setSeed(minPort << 8 + maxPort);
		config.setMaxSteps(maxSteps);
		config.setMaxFlowCount(maxFlowCount);
		config.setModifier(1.2d);
		config.setMaxPrio(maxPrio);
		long t1, t2;
		System.out.println("Starting run: maxPort = " + maxPort);
		t1 = System.nanoTime();
		for (int portCount = minPort; portCount <= maxPort; portCount++) {
			config.setPortCount(portCount);
			System.out.println("PortCount = " + portCount);
			for (int depthCount = 2; depthCount <= portCount; depthCount++) {
				config.setDepth(depthCount);
				config.newTopology();
				int cnt = 0;
				System.out.print("Stream config (max " + runs + ") 0");
				for (int streamCount = 1; streamCount <= runs; streamCount++) {
					String id = String.format("%d%03d%04d", portCount, depthCount, streamCount);
					tracer.setRoundNumber(Integer.valueOf(id));
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
				System.out.println();
			}
		}
		String fileName = String.format("%d_%dports_%dStreamConfigCount_%dmaxSteps_%dMaxPrio_%dmodi%s%s%s%s%s.csv",
				minPort, maxPort, runs, config.getMaxSteps(), config.getMaxPrio(), (int) (config.getModifier() * 100),
				BF ? "BF" : "", BT ? "BT" : "", HC ? "HC" : "", SA ? "SA" : "", sEA ? "sEA" : "");
		t2 = System.nanoTime();
		EndStepTracer.saveToFile(new File(fileName), tracer);
		JOptionPane.showMessageDialog(null,
				String.format("Batch done! (took %.4f s)\n See %s for results!", (t2 - t1) / 1.0e9, fileName),
				"Batch mode ", JOptionPane.PLAIN_MESSAGE);
	}
}
