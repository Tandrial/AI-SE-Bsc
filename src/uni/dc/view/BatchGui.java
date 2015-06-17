package uni.dc.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;

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

		JLabel lblCountTopologies = new JLabel("Count Topologies");
		lblCountTopologies.setBounds(10, 10, 110, 15);
		contentPanel.add(lblCountTopologies);

		JLabel lblCountStreamlayout = new JLabel("Count StreamLayouts");
		lblCountStreamlayout.setBounds(10, 35, 110, 15);
		contentPanel.add(lblCountStreamlayout);

		JSpinner spTopoCount = new JSpinner();
		spTopoCount.setModel(new SpinnerNumberModel(new Integer(10), new Integer(1), null, new Integer(1)));
		spTopoCount.setBounds(130, 8, 45, 20);
		contentPanel.add(spTopoCount);

		JSpinner spStreamCount = new JSpinner();
		spStreamCount.setModel(new SpinnerNumberModel(new Integer(10), new Integer(1), null, new Integer(1)));
		spStreamCount.setBounds(130, 35, 45, 20);
		contentPanel.add(spStreamCount);

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
				setVisible(false);
				dispose();
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
}
