package uni.dc.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
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
	private JSpinner spSpeed;
	private JSpinner spMaxPrio;
	private JSpinner spMaxStep;
	private JSpinner spRuns;
	private JRadioButton rdbtnUbsV0;
	private JRadioButton rdbtnUbsV3;

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

		JLabel lblMaxframelength = new JLabel("maxLength[bit]");
		lblMaxframelength.setBounds(10, 75, 75, 15);
		contentPanel.add(lblMaxframelength);

		JLabel lblMaxspeed = new JLabel("maxSpeed[%]");
		lblMaxspeed.setBounds(10, 100, 75, 15);
		contentPanel.add(lblMaxspeed);

		JLabel lblMaxprio = new JLabel("maxPrio");
		lblMaxprio.setBounds(10, 155, 75, 15);
		contentPanel.add(lblMaxprio);

		JLabel lblMaxsteps = new JLabel("maxSteps");
		lblMaxsteps.setBounds(10, 180, 75, 15);
		contentPanel.add(lblMaxsteps);

		JLabel lblRuns = new JLabel("runs");
		lblRuns.setBounds(10, 205, 75, 15);
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

		spSpeed = new JSpinner();
		spSpeed.setModel(new SpinnerNumberModel(new Integer(1), new Integer(1),
				new Integer(100), new Integer(1)));
		spSpeed.setValue(config.getMaxSpeed());
		spSpeed.setBounds(85, 101, 80, 20);
		contentPanel.add(spSpeed);

		spMaxPrio = new JSpinner();
		spMaxPrio.setModel(new SpinnerNumberModel(new Integer(2),
				new Integer(2), null, new Integer(1)));
		spMaxPrio.setValue(config.getMaxPrio());
		spMaxPrio.setBounds(85, 150, 80, 20);
		contentPanel.add(spMaxPrio);

		spMaxStep = new JSpinner();
		spMaxStep.setModel(new SpinnerNumberModel(new Integer(10000),
				new Integer(10000), null, new Integer(1)));
		spMaxStep.setValue(config.getMaxSteps());
		spMaxStep.setBounds(85, 175, 80, 20);
		contentPanel.add(spMaxStep);

		spRuns = new JSpinner();
		spRuns.setModel(new SpinnerNumberModel(new Integer(1), new Integer(1),
				null, new Integer(1)));
		spRuns.setValue(config.getRuns());
		spRuns.setBounds(85, 200, 80, 20);
		contentPanel.add(spRuns);

		rdbtnUbsV0 = new JRadioButton("UBS V0");
		rdbtnUbsV0.setBounds(10, 235, 75, 20);
		contentPanel.add(rdbtnUbsV0);

		rdbtnUbsV3 = new JRadioButton("UBS V3");
		rdbtnUbsV3.setBounds(85, 235, 80, 20);
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

		JLabel lblGeneratorConfig = new JLabel("Generator Config");
		lblGeneratorConfig.setHorizontalAlignment(SwingConstants.CENTER);
		lblGeneratorConfig.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblGeneratorConfig.setBounds(10, 0, 155, 20);
		contentPanel.add(lblGeneratorConfig);

		JLabel lblOptimizerConfig = new JLabel("Optimizer Config");
		lblOptimizerConfig.setHorizontalAlignment(SwingConstants.CENTER);
		lblOptimizerConfig.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblOptimizerConfig.setBounds(10, 124, 155, 20);
		contentPanel.add(lblOptimizerConfig);

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		JButton okButton = new JButton("OK");
		okButton.setHorizontalAlignment(SwingConstants.CENTER);
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				acceptChanges(config);
				setVisible(false);
				dispose();
			}
		});
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);

		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				dispose();
			}
		});
		buttonPane.add(cancelButton);
	}

	private void acceptChanges(UbsOptiConfig config) {
		config.setDepth((Integer) spDepth.getValue());
		config.setPortCount((Integer) spPortCount.getValue());
		config.setMaxFrameLength((Integer) spMaxLength.getValue());
		config.setMaxSpeed((Integer) spSpeed.getValue());
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
}
