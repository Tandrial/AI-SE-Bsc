package uni.dc.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;

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

import uni.dc.model.PriorityConfiguration;
import uni.dc.ubsOpti.UbsOptiConfig;
import uni.dc.ubsOpti.delayCalc.UbsV0DelayCalc;
import uni.dc.ubsOpti.delayCalc.UbsV3DelayCalc;

public class SettingsGui extends JDialog {

	private static final long serialVersionUID = 1L;
	public static final Logger logger = Logger.getLogger(SettingsGui.class.getName());

	private JSpinner spDepth;
	private JSpinner spPortCount;
	private JSpinner spMaxLength;
	private JSpinner spRndSeed;
	private JSpinner spMaxPrio;
	private JSpinner spFlowCount;
	private JSpinner spRuns;
	private JRadioButton rdbtnUbsV0;
	private JRadioButton rdbtnUbsV3;

	public SettingsGui(UbsOptiConfig config) {
		setResizable(false);
		setTitle("Settings - UBSOpti");
		setBounds(100, 100, 230, 330);
		getContentPane().setLayout(new BorderLayout());
		JPanel contentPanel = new JPanel();
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		JLabel lblNetworkDepth = new JLabel("Depth");
		lblNetworkDepth.setBounds(10, 25, 100, 15);
		contentPanel.add(lblNetworkDepth);

		JLabel lblPortCount = new JLabel("Port Count");
		lblPortCount.setBounds(10, 50, 100, 15);
		contentPanel.add(lblPortCount);

		JLabel lblMaxframelength = new JLabel("maxLength");
		lblMaxframelength.setBounds(10, 75, 100, 15);
		contentPanel.add(lblMaxframelength);

		JLabel lblMaxspeed = new JLabel("random Seed");
		lblMaxspeed.setBounds(10, 105, 100, 15);
		contentPanel.add(lblMaxspeed);

		JLabel lblMaxprio = new JLabel("maxPrio");
		lblMaxprio.setBounds(10, 130, 100, 15);
		contentPanel.add(lblMaxprio);

		JLabel lblFlowCount = new JLabel("Flow Count");
		lblFlowCount.setBounds(10, 157, 100, 15);
		contentPanel.add(lblFlowCount);

		JLabel lblRuns = new JLabel("runs");
		lblRuns.setBounds(10, 215, 100, 15);
		contentPanel.add(lblRuns);

		spDepth = new JSpinner();
		spDepth.setModel(new SpinnerNumberModel(new Integer(2), new Integer(2), null, new Integer(1)));
		spDepth.setValue(config.getDepth());
		spDepth.setBounds(125, 25, 80, 20);
		contentPanel.add(spDepth);

		spPortCount = new JSpinner();
		spPortCount.setModel(new SpinnerNumberModel(new Integer(2), new Integer(2), null, new Integer(1)));
		spPortCount.setValue(config.getPortCount());
		spPortCount.setBounds(125, 50, 80, 20);
		contentPanel.add(spPortCount);

		spMaxLength = new JSpinner();
		spMaxLength.setModel(new SpinnerNumberModel(new Integer(1), new Integer(1), null, new Integer(1)));
		spMaxLength.setValue(config.getMaxFrameLength());
		spMaxLength.setBounds(125, 75, 80, 20);
		contentPanel.add(spMaxLength);

		spRndSeed = new JSpinner();
		spRndSeed.setModel(new SpinnerNumberModel(new Long(1), new Long(0), null, new Long(1)));
		spRndSeed.setValue(config.getSeed());
		spRndSeed.setBounds(125, 100, 80, 20);
		contentPanel.add(spRndSeed);

		spMaxPrio = new JSpinner();
		spMaxPrio.setModel(new SpinnerNumberModel(new Integer(2), new Integer(2), null, new Integer(1)));
		spMaxPrio.setValue(config.getMaxPrio());
		spMaxPrio.setBounds(125, 125, 80, 20);
		contentPanel.add(spMaxPrio);

		spFlowCount = new JSpinner();
		spFlowCount.setModel(new SpinnerNumberModel(new Integer(3), new Integer(1), null, new Integer(1)));
		spFlowCount.setValue(config.getMaxFlowCount());
		spFlowCount.setBounds(125, 152, 80, 20);
		contentPanel.add(spFlowCount);

		spRuns = new JSpinner();
		spRuns.setModel(new SpinnerNumberModel(new Integer(1), new Integer(1), null, new Integer(1)));
		spRuns.setValue(config.getRuns());
		spRuns.setBounds(125, 210, 80, 20);
		contentPanel.add(spRuns);

		rdbtnUbsV0 = new JRadioButton("UBS V0");
		rdbtnUbsV0.setBounds(10, 245, 75, 20);
		contentPanel.add(rdbtnUbsV0);

		rdbtnUbsV3 = new JRadioButton("UBS V3");
		rdbtnUbsV3.setBounds(105, 245, 100, 20);
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
		lblGeneratorConfig.setBounds(10, 0, 195, 20);
		contentPanel.add(lblGeneratorConfig);

		JLabel lblOptimizerConfig = new JLabel("Optimizer Config");
		lblOptimizerConfig.setHorizontalAlignment(SwingConstants.CENTER);
		lblOptimizerConfig.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblOptimizerConfig.setBounds(10, 183, 195, 20);
		contentPanel.add(lblOptimizerConfig);

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		JButton okButton = new JButton("OK");
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
		config.setFlowCount((Integer) spFlowCount.getValue());
		config.setMaxFrameLength((Integer) spMaxLength.getValue());
		config.setSeed((Long) spRndSeed.getValue());
		config.setMaxPrio((Integer) spMaxPrio.getValue());
		config.setRuns((Integer) spRuns.getValue());

		if (rdbtnUbsV0.isSelected() && !config.isUbsV0()) {
			if (config.getTraffic() != null) {
				config.setDelayCalc(new UbsV0DelayCalc(config.getTraffic()));
				config.getDelayCalc().setPrio(config.getPriorityConfig());
				config.getDelayCalc().calculateDelays();
				config.setPriorityConfig(new PriorityConfiguration(config.getTraffic()));
			}
			config.setUbsV0(true);
		} else if (rdbtnUbsV3.isSelected() && config.isUbsV0()) {
			if (config.getTraffic() != null) {
				config.setDelayCalc(new UbsV3DelayCalc(config.getTraffic()));
				config.getDelayCalc().setPrio(config.getPriorityConfig());
				config.getDelayCalc().calculateDelays();
				config.setPriorityConfig(new PriorityConfiguration(config.getTraffic()));
			}
			config.setUbsV0(false);
		}
		logger.log(Level.INFO, "Config changed:\n" + config);
	}
}
