package uni.dc.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import uni.dc.ubsOpti.UbsOptiConfig;
import uni.dc.ubsOpti.delayCalc.UbsV0DelayCalc;
import uni.dc.ubsOpti.delayCalc.UbsV3DelayCalc;

public class SettingsGui extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();

	private JSpinner spDepth;
	private JSpinner spPortCount;
	private JSpinner spMaxLength;
	private JSpinner spMaxPrio;
	private JSpinner spMaxStep;
	private JSpinner spRuns;
	private JRadioButton rdbtnUbsV0;
	private JRadioButton rdbtnUbsV3;

	private UbsOptiConfig config;

	public SettingsGui(UbsOptiConfig config) {
		setTitle("Settings - UBsOpti");
		setBounds(100, 100, 200, 350);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		JLabel lblNetworkDepth = new JLabel("Depth");
		lblNetworkDepth.setBounds(10, 25, 75, 15);
		contentPanel.add(lblNetworkDepth);

		JLabel lblPortCount = new JLabel("Port Count");
		lblPortCount.setBounds(10, 50, 75, 15);
		contentPanel.add(lblPortCount);

		JLabel lblMaxframelength = new JLabel("maxLength");
		lblMaxframelength.setBounds(10, 75, 75, 15);
		contentPanel.add(lblMaxframelength);

		JLabel lblMaxprio = new JLabel("maxPrio");
		lblMaxprio.setBounds(10, 130, 75, 15);
		contentPanel.add(lblMaxprio);

		JLabel lblMaxsteps = new JLabel("maxSteps");
		lblMaxsteps.setBounds(10, 155, 75, 15);
		contentPanel.add(lblMaxsteps);

		JLabel lblRuns = new JLabel("runs");
		lblRuns.setBounds(10, 180, 75, 15);
		contentPanel.add(lblRuns);

		spDepth = new JSpinner();
		spDepth.setModel(new SpinnerNumberModel(new Integer(2), new Integer(2),
				null, new Integer(1)));
		spDepth.setValue(config.getDepth());
		spDepth.setBounds(85, 25, 80, 20);
		contentPanel.add(spDepth);

		spPortCount = new JSpinner();
		spPortCount.setModel(new SpinnerNumberModel(new Integer(2),
				new Integer(2), null, new Integer(1)));
		spPortCount.setValue(config.getPortCount());
		spPortCount.setBounds(85, 50, 80, 20);
		contentPanel.add(spPortCount);

		spMaxLength = new JSpinner();
		spMaxLength.setModel(new SpinnerNumberModel(new Integer(1),
				new Integer(1), null, new Integer(1)));
		spMaxLength.setValue(config.getMaxFrameLength());
		spMaxLength.setBounds(85, 74, 80, 20);
		contentPanel.add(spMaxLength);

		spMaxPrio = new JSpinner();
		spMaxPrio.setModel(new SpinnerNumberModel(new Integer(2),
				new Integer(2), null, new Integer(1)));
		spMaxPrio.setValue(config.getMaxPrio());
		spMaxPrio.setBounds(85, 125, 80, 20);
		contentPanel.add(spMaxPrio);

		spMaxStep = new JSpinner();
		spMaxStep.setModel(new SpinnerNumberModel(new Integer(10000),
				new Integer(10000), null, new Integer(1)));
		spMaxStep.setValue(config.getMaxSteps());
		spMaxStep.setBounds(85, 150, 80, 20);
		contentPanel.add(spMaxStep);

		spRuns = new JSpinner();
		spRuns.setModel(new SpinnerNumberModel(new Integer(1), new Integer(1),
				null, new Integer(1)));
		spRuns.setValue(config.getRuns());
		spRuns.setBounds(85, 175, 80, 20);
		contentPanel.add(spRuns);

		rdbtnUbsV0 = new JRadioButton("UBS V0");
		rdbtnUbsV0.setBounds(10, 210, 75, 20);
		contentPanel.add(rdbtnUbsV0);

		rdbtnUbsV3 = new JRadioButton("UBS V3");
		rdbtnUbsV3.setBounds(85, 210, 80, 20);
		contentPanel.add(rdbtnUbsV3);

		if (config.isUbsV0()) {
			rdbtnUbsV0.setSelected(true);
			rdbtnUbsV3.setSelected(false);
		} else {
			rdbtnUbsV0.setSelected(false);
			rdbtnUbsV3.setSelected(true);
		}

		ButtonGroup ubsRadioGroup = new ButtonGroup();
		ubsRadioGroup.add(rdbtnUbsV0);
		ubsRadioGroup.add(rdbtnUbsV3);

		JLabel lblNewLabel = new JLabel("Generator Config");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNewLabel.setBounds(10, 0, 155, 20);
		contentPanel.add(lblNewLabel);

		JLabel lblOptimizerConfig = new JLabel("Optimizer Config");
		lblOptimizerConfig.setHorizontalAlignment(SwingConstants.CENTER);
		lblOptimizerConfig.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblOptimizerConfig.setBounds(10, 100, 155, 20);
		contentPanel.add(lblOptimizerConfig);

		this.config = config;

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		JButton okButton = new JButton("OK");
		okButton.setHorizontalAlignment(SwingConstants.CENTER);
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				acceptChanges();
				close();
			}
		});
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);

		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				close();
			}
		});
		buttonPane.add(cancelButton);

		Panel Generator = new Panel();
		getContentPane().add(Generator, BorderLayout.NORTH);
		Generator.setLayout(null);

		JPanel Optimizer = new JPanel();
		getContentPane().add(Optimizer, BorderLayout.NORTH);
		Optimizer.setLayout(null);
	}

	private void acceptChanges() {
		config.setDepth((Integer) spDepth.getValue());
		config.setPortCount((Integer) spPortCount.getValue());
		config.setMaxFrameLength((Integer) spMaxLength.getValue());
		config.setMaxPrio((Integer) spMaxPrio.getValue());
		config.setMaxSteps((Integer) spMaxStep.getValue());
		config.setRuns((Integer) spRuns.getValue());

		if (rdbtnUbsV0.isSelected() && !config.isUbsV0()) {
			if (config.getTraffic() != null)
				config.setDelayCalc(new UbsV0DelayCalc(config.getTraffic()));
			config.setUbsV0(true);
		} else if (rdbtnUbsV3.isSelected() && config.isUbsV0()) {
			if (config.getTraffic() != null)
				config.setDelayCalc(new UbsV3DelayCalc(config.getTraffic()));
			config.setUbsV0(false);
		}
	}

	private void close() {
		setVisible(false);
		dispose();
	}
}
