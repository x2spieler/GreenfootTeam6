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
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JMenuItem;
import java.awt.Button;
import java.awt.List;
import java.awt.MediaTracker;

import javax.swing.event.AncestorListener;
import javax.swing.event.AncestorEvent;
import javax.swing.border.BevelBorder;
import javax.imageio.ImageIO;
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
import javax.swing.SwingUtilities;
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
		Image image = new ImageIcon("src/images/background/Background.jpg")
				.getImage();
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(200, 200, 1024, 768);
		getContentPane().setLayout(null);
		final BackgroundPanel panel = new BackgroundPanel(image);
		panel.setBounds(0, 0, 1024, 768);

		panel.setForeground(SystemColor.desktop);
		panel.setFont(new Font("Dialog", Font.PLAIN, 5));
		getContentPane().add(panel);

		JButton GameStart = new JButton();
		GameStart.setBounds(293, 328, 423, 89);
		GameStart.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				GameStart.setIcon(new ImageIcon(DungeonFrame.class.getResource("/images/background/Game-Start-white.png")));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				GameStart.setIcon(new ImageIcon(DungeonFrame.class.getResource("/images/background/Game-Start.png")));
			}
		});
		GameStart.setIcon(new ImageIcon(DungeonFrame.class.getResource("/images/background/Game-Start.png")));
		GameStart.setBackground(UIManager.getColor("Button.highlight"));
		GameStart.setContentAreaFilled(false);
		GameStart.setOpaque(false);
		GameStart.setBorder(null);

		JButton eingeben = new JButton("");
		eingeben.setBounds(422, 484, 215, 53);
		eingeben.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				eingeben.setIcon(new ImageIcon(DungeonFrame.class.getResource("/images/background/new-seed-white.png")));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				eingeben.setIcon(new ImageIcon(DungeonFrame.class.getResource("/images/background/new-seed.png")));
			}
		});
		eingeben.setIcon(new ImageIcon(DungeonFrame.class.getResource("/images/background/new-seed.png")));
		eingeben.setContentAreaFilled(false);
		eingeben.setOpaque(false);
		eingeben.setBorder(null);
		
		JButton HighScore = new JButton("");
		HighScore.setBounds(342, 414, 348, 70);
		HighScore.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		HighScore.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				HighScore.setIcon(new ImageIcon(DungeonFrame.class.getResource("/images/background/High-Score-white.png")));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				HighScore.setIcon(new ImageIcon(DungeonFrame.class.getResource("/images/background/High-Score.png")));
			}
		});
		HighScore.setIcon(new ImageIcon(DungeonFrame.class.getResource("/images/background/High-Score.png")));
		HighScore.setContentAreaFilled(false);
		HighScore.setOpaque(false);
		HighScore.setBorder(null);
		
		textField = new JTextField();
		textField.setBounds(476, 543, 282, 48);
		textField.setColumns(10);
		textField.setOpaque(false);
		textField.setBorder(null);
		panel.setLayout(null);
		panel.add(HighScore);
		panel.add(eingeben);
		panel.add(GameStart);
		panel.add(textField);

	}
}