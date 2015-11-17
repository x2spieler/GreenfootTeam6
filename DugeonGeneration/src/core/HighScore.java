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
import javax.swing.JTextPane;
import javax.swing.JEditorPane;

public class HighScore extends JFrame {

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					HighScore frame = new HighScore();
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
	public HighScore() {
		Image image = new ImageIcon("src/images/background/highscore.jpg")
		.getImage();
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 692, 550);
		getContentPane().setLayout(new BorderLayout(0, 0));
		BackgroundPanel panel = new BackgroundPanel(image);
		panel.setBackground(SystemColor.text);
		
		panel.setForeground(SystemColor.desktop);
		panel.setFont(new Font("Dialog", Font.PLAIN, 5));
		getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
	}
	private static void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});
	}
}
