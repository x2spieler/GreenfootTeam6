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

public class DungeonFrame extends JFrame {
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
		Image image = new ImageIcon("src/images/background/background2.jpg")
				.getImage();
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 692, 550);
		getContentPane().setLayout(new BorderLayout(0, 0));
		final BackgroundPanel panel = new BackgroundPanel(image);
		final BackgroundPanel panel2 = new BackgroundPanel(image);
		panel.setBackground(SystemColor.text);

		panel.setForeground(SystemColor.desktop);
		panel.setFont(new Font("Dialog", Font.PLAIN, 5));
		getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(null);

		Button GameStart = new Button("Game    Start");
		GameStart.setBackground(UIManager.getColor("Button.highlight"));
		GameStart.setBounds(199, 171, 300, 50);
		GameStart.setFont(new Font("Dialog", Font.PLAIN, 30));
		panel.add(GameStart);

		Button Highscore = new Button("High    Score");
		Highscore.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});
		Highscore.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				panel.setVisible(false);

			}
		});
		Highscore.setActionCommand("");
		Highscore.setFont(new Font("Dialog", Font.PLAIN, 30));
		Highscore.setBackground(UIManager.getColor("Button.highlight"));
		Highscore.setBounds(199, 280, 300, 50);
		panel.add(Highscore);

		textField = new JTextField();
		textField.setBounds(199, 124, 206, 22);
		panel.add(textField);
		textField.setColumns(10);

		Button eingeben = new Button("eingeben");
		eingeben.setFont(new Font("Dialog", Font.BOLD, 16));
		eingeben.setBackground(UIManager.getColor("Button.focus"));
		eingeben.setBounds(410, 123, 121, 22);
		panel.add(eingeben);

		JPanel MenuBarPane = new JPanel();
		MenuBarPane.setForeground(Color.WHITE);
		getContentPane().add(MenuBarPane, BorderLayout.NORTH);
		MenuBarPane.setLayout(new BorderLayout(0, 0));

		JMenuBar menuBar = new JMenuBar();
		menuBar.setMargin(new Insets(2, 0, 0, 0));
		MenuBarPane.add(menuBar);
		menuBar.addAncestorListener(new AncestorListener() {
			public void ancestorAdded(AncestorEvent event) {
			}

			public void ancestorMoved(AncestorEvent event) {
			}

			public void ancestorRemoved(AncestorEvent event) {
			}
		});
		menuBar.setToolTipText("");

		JMenu mnFile = new JMenu("file");
		menuBar.add(mnFile);

		JMenuItem mntmSave = new JMenuItem("save");
		mnFile.add(mntmSave);

		JMenu mnOpen = new JMenu("open");
		mnFile.add(mnOpen);

		JMenuItem mntmRestart = new JMenuItem("restart");
		mnFile.add(mntmRestart);

		JMenu mnEditor = new JMenu("edit");
		menuBar.add(mnEditor);

		JMenu mnAbout = new JMenu("about");
		menuBar.add(mnAbout);

		JMenuItem mntmHowToPlay = new JMenuItem("how to play");
		mnAbout.add(mntmHowToPlay);
		getContentPane().setFocusTraversalPolicy(
				new FocusTraversalOnArray(new Component[] { menuBar, mnFile,
						mntmSave, mnOpen, mntmRestart, mnEditor, mnAbout,
						mntmHowToPlay }));
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
