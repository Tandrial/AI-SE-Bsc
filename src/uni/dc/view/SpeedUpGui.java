package uni.dc.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import uni.dc.model.Flow;
import uni.dc.util.DeterministicHashSet;

public class SpeedUpGui extends JDialog {
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();

	private List<JCheckBox> speedUpBoxes = new ArrayList<JCheckBox>();
	private List<Flow> allFlows = new ArrayList<Flow>();
	private Set<Flow> flowsToSpeedUp = new DeterministicHashSet<Flow>();

	public SpeedUpGui(Set<Flow> flows) {
		super();
		setBounds(100, 100, 250, 200);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		for (Flow f : flows) {
			allFlows.add(f);
			JCheckBox checkBox = new JCheckBox(f.getName() + " Path: "
					+ f.toString());
			checkBox.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (!flowsToSpeedUp.contains(f)) {
						flowsToSpeedUp.add(f);
					} else {
						flowsToSpeedUp.remove(f);
					}
				}
			});
			speedUpBoxes.add(checkBox);
			contentPanel.add(checkBox);
		}
		
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		JButton okButton = new JButton("OK");
		okButton.setActionCommand("OK");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (flowsToSpeedUp.size() == 0
						|| flowsToSpeedUp.size() == flows.size()) {
					JOptionPane.showMessageDialog(null,
							"Please select between 1 and " + (flows.size() - 1)
									+ " flows!");
				} else {
					setVisible(false);
				}
			}
		});
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);

		JButton cancelButton = new JButton("Cancel");
		cancelButton.setActionCommand("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				flowsToSpeedUp = null;
				setVisible(false);
			}
		});
		buttonPane.add(cancelButton);
	}

	public Set<Flow> getflowsToSpeedUp() {
		return flowsToSpeedUp;
	}
}
