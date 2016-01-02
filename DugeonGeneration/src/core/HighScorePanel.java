package core;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

public class HighScorePanel extends BackgroundPanel {

	private static final long serialVersionUID = -6287639398060155139L;
	private JLabel label_1;
	private JLabel label_5;
	private JLabel label_4;
	private JLabel label_6;
	private JLabel label_2;
	private JLabel label;
	private JLabel label_3;

	public HighScorePanel() {
		super(new ImageIcon(HighScorePanel.class.getClassLoader().getResource("images/background/Background.jpg")).getImage());
		setLayout(new BorderLayout(0, 0));

		JScrollPane scrollPane = new JScrollPane();

		JPanel panel = new JPanel();
		scrollPane.setViewportView(panel);

		JLabel lblNewLabel = new JLabel("Highscore");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);

		label = new JLabel("");

		label_1 = new JLabel("");

		label_2 = new JLabel("");

		label_3 = new JLabel("");

		label_4 = new JLabel("");

		label_5 = new JLabel("");

		label_6 = new JLabel("");
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(gl_panel.createParallelGroup(Alignment.LEADING).addGroup(
				gl_panel.createSequentialGroup().addContainerGap().addGroup(
						gl_panel.createParallelGroup(Alignment.LEADING).addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 428, GroupLayout.PREFERRED_SIZE).addGroup(Alignment.TRAILING, gl_panel.createSequentialGroup().addGap(11).addComponent(label, GroupLayout.DEFAULT_SIZE, 417, Short.MAX_VALUE)).addGroup(Alignment.TRAILING, gl_panel.createSequentialGroup().addGap(11).addComponent(label_1, GroupLayout.DEFAULT_SIZE, 417, Short.MAX_VALUE)).addGroup(Alignment.TRAILING, gl_panel.createSequentialGroup().addGap(11).addComponent(label_2, GroupLayout.DEFAULT_SIZE, 417, Short.MAX_VALUE)).addGroup(Alignment.TRAILING, gl_panel.createSequentialGroup().addGap(11).addComponent(label_3, GroupLayout.DEFAULT_SIZE, 417, Short.MAX_VALUE)).addGroup(Alignment.TRAILING, gl_panel.createSequentialGroup().addGap(11).addComponent(label_4, GroupLayout.DEFAULT_SIZE, 417, Short.MAX_VALUE)).addGroup(
								Alignment.TRAILING, gl_panel.createSequentialGroup().addGap(11).addComponent(label_5, GroupLayout.DEFAULT_SIZE, 417, Short.MAX_VALUE)).addGroup(Alignment.TRAILING, gl_panel.createSequentialGroup().addGap(11).addComponent(label_6, GroupLayout.DEFAULT_SIZE, 417, Short.MAX_VALUE))).addContainerGap()));
		gl_panel.setVerticalGroup(gl_panel.createParallelGroup(Alignment.LEADING).addGroup(
				gl_panel.createSequentialGroup().addContainerGap().addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(label, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(label_1, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(label_2, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(label_3, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(label_4, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(label_5, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(
						label_6, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE).addContainerGap(14, Short.MAX_VALUE)));
		panel.setLayout(gl_panel);
		add(scrollPane);

	}

	public JLabel getLabel_1() {
		return label_1;
	}

	public JLabel getLabel_5() {
		return label_5;
	}

	public JLabel getLabel_4() {
		return label_4;
	}

	public JLabel getLabel_6() {
		return label_6;
	}

	public JLabel getLabel_2() {
		return label_2;
	}

	public JLabel getLabel() {
		return label;
	}

	public JLabel getLabel_3() {
		return label_3;
	}
}
