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
import javax.swing.JTextPane;

public class DungeonFrame extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7379852013198002869L;
	private JTextField textField;

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
		Image image = new ImageIcon("src/images/background/background.png")
				.getImage();
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(200, 200, 1331, 750);
		getContentPane().setLayout(null);
		final BackgroundPanel panel = new BackgroundPanel(image);
		panel.setBounds(0, 0, 1387, 750);

		panel.setForeground(SystemColor.desktop);
		panel.setFont(new Font("Dialog", Font.PLAIN, 5));
		getContentPane().add(panel);

		JButton GameStart = new JButton();
		GameStart.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				GameStart.setIcon(new ImageIcon(DungeonFrame.class.getResource("/images/background/StartGame_white.png")));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				GameStart.setIcon(new ImageIcon(DungeonFrame.class.getResource("/images/background/Startgame.png")));
			}
		});
		GameStart.setIcon(new ImageIcon(DungeonFrame.class.getResource("/images/background/Startgame.png")));
		GameStart.setBackground(UIManager.getColor("Button.highlight"));
		GameStart.setContentAreaFilled(false);
		GameStart.setOpaque(false);
		GameStart.setBorder(null);

		JButton eingeben = new JButton("");
		eingeben.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				eingeben.setIcon(new ImageIcon(DungeonFrame.class.getResource("/images/background/eingeben_white.png")));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				eingeben.setIcon(new ImageIcon(DungeonFrame.class.getResource("/images/background/Eingeben.png")));
			}
		});
		eingeben.setIcon(new ImageIcon(DungeonFrame.class.getResource("/images/background/Eingeben.png")));
		eingeben.setContentAreaFilled(false);
		eingeben.setOpaque(false);
		eingeben.setBorder(null);
		
		JButton HighScore = new JButton("");
		HighScore.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				HighScore.setIcon(new ImageIcon(DungeonFrame.class.getResource("/images/background/Highscore_white.png")));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				HighScore.setIcon(new ImageIcon(DungeonFrame.class.getResource("/images/background/Highscore.png")));
			}
		});
		HighScore.setIcon(new ImageIcon(DungeonFrame.class.getResource("/images/background/Highscore.png")));
		HighScore.setContentAreaFilled(false);
		HighScore.setOpaque(false);
		HighScore.setBorder(null);
		
		textField = new JTextField();
		textField.setColumns(10);
		textField.setOpaque(false);
		textField.setBorder(null);
		
		
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.TRAILING)
				.addGroup(Alignment.LEADING, gl_panel.createSequentialGroup()
					.addGap(427)
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
							.addGroup(gl_panel.createSequentialGroup()
								.addGap(86)
								.addComponent(HighScore, GroupLayout.PREFERRED_SIZE, 348, GroupLayout.PREFERRED_SIZE))
							.addGroup(gl_panel.createSequentialGroup()
								.addGap(217)
								.addComponent(eingeben, GroupLayout.PREFERRED_SIZE, 215, GroupLayout.PREFERRED_SIZE))
							.addComponent(GameStart, GroupLayout.PREFERRED_SIZE, 423, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(205)
							.addComponent(textField, GroupLayout.PREFERRED_SIZE, 282, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap(473, Short.MAX_VALUE))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap(263, Short.MAX_VALUE)
					.addComponent(GameStart, GroupLayout.PREFERRED_SIZE, 89, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(HighScore, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(textField, GroupLayout.PREFERRED_SIZE, 48, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(eingeben)
					.addGap(194))
		);
		panel.setLayout(gl_panel);

	}
}