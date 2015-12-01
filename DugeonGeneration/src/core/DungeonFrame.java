package core;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.GridBagLayout;
import javax.swing.JButton;
import java.awt.GridBagConstraints;
import org.eclipse.wb.swing.FocusTraversalOnArray;
import java.awt.Component;
import java.awt.FlowLayout;

import javax.swing.ImageIcon;
import javax.swing.JSlider;
import javax.swing.JScrollBar;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JSpinner;
import java.awt.CardLayout;
import javax.swing.JTextField;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JPopupMenu;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowListener;
import javax.swing.JMenuItem;
import java.awt.Button;
import java.awt.List;
import javax.swing.event.AncestorListener;
import javax.swing.event.AncestorEvent;
import javax.swing.border.BevelBorder;
import javax.swing.BoxLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Image;
import java.awt.Panel;
import java.awt.SystemColor;
import java.awt.Font;
import javax.swing.UIManager;
import java.awt.Color;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;

public class DungeonFrame extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7379852013198002869L;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DungeonFrame frame = new DungeonFrame();
					frame.setVisible(true);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public DungeonFrame() {
		Image image = new ImageIcon("src/images/background/background2.jpg")
				.getImage();
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(200, 200, 1024, 800);
		getContentPane().setLayout(null);
		final BackgroundPanel panel = new BackgroundPanel(image);
		panel.setBounds(0, 0, 1024, 800);

		panel.setForeground(SystemColor.desktop);
		panel.setFont(new Font("Dialog", Font.PLAIN, 5));
		getContentPane().add(panel);

		JButton GameStart = new JButton("");
		GameStart.setIcon(new ImageIcon(DungeonFrame.class.getResource("/images/background/StartGame.png")));
		GameStart.setBackground(UIManager.getColor("Button.highlight"));
		GameStart.setContentAreaFilled(false);
		GameStart.setOpaque(false);

		JButton Help = new JButton("");
		Help.setIcon(new ImageIcon(DungeonFrame.class.getResource("/images/background/Help.png")));
		Help.setContentAreaFilled(false);
		Help.setOpaque(false);
		
		JButton HighScore = new JButton("");
		HighScore.setIcon(new ImageIcon(DungeonFrame.class.getResource("/images/background/HighScore.png")));
		HighScore.setContentAreaFilled(false);
		HighScore.setOpaque(false);
		
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(335)
							.addComponent(GameStart, GroupLayout.PREFERRED_SIZE, 266, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(730)
							.addComponent(HighScore, GroupLayout.PREFERRED_SIZE, 222, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(403)
							.addComponent(Help, GroupLayout.PREFERRED_SIZE, 144, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap(72, Short.MAX_VALUE))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(39)
					.addComponent(HighScore, GroupLayout.PREFERRED_SIZE, 52, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 299, Short.MAX_VALUE)
					.addComponent(GameStart, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
					.addGap(27)
					.addComponent(Help, GroupLayout.PREFERRED_SIZE, 44, GroupLayout.PREFERRED_SIZE)
					.addGap(289))
		);
		panel.setLayout(gl_panel);

	}
}