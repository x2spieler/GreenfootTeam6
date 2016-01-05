package core;

import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import database.HighscoreEntry;
import world.DungeonMap;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class HighScorePanel extends BackgroundPanel {

	DungeonMap dMap=null;
	private static final long serialVersionUID = 1L;
	
	private JLabel[] names=null;
	private JLabel[] dashes=null;
	private JLabel[] scores=null;
	
	private int numListEntries=-1;

	public HighScorePanel(DungeonMap dm, int numListEntries, ActionListener backToMainMenuListener) {
		super(new ImageIcon(HighScorePanel.class.getClassLoader().getResource("images/background/highscore/HighScoreBG.jpg")).getImage());
		setBounds(200, 200, 1024, 768);
		this.dMap=dm;
		this.numListEntries=numListEntries;
		buildGui(backToMainMenuListener);
		updateList();
	}

	private void buildGui(ActionListener backToMainMenuListener)
	{
		
		names=new JLabel[numListEntries];
		dashes=new JLabel[numListEntries];
		scores=new JLabel[numListEntries];
		int x1=300;
		int x2=500;
		int x3=500;
		int yStart=200;
		int yBorder=50;
		for(int i=0;i<numListEntries;i++)
		{
			names[i]=new JLabel();
			names[i].setBounds(new Rectangle(x1, yStart+i*yBorder, 200, 30));
			names[i].setHorizontalAlignment(SwingConstants.LEFT);
			dashes[i]=new JLabel();
			dashes[i].setBounds(new Rectangle(x2, yStart+i*yBorder, 200, 30));
			scores[i]=new JLabel();
			scores[i].setBounds(new Rectangle(x3, yStart+i*yBorder, 200, 30));
			scores[i].setHorizontalAlignment(SwingConstants.RIGHT);
			
			add(names[i]);
			add(dashes[i]);
			add(scores[i]);
		}
		
		JButton mmenu = new JButton();
		mmenu.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				mmenu.setIcon(new ImageIcon(MainMenuPanel.class.getResource("/images/background/highscore/back-shine.png")));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				mmenu.setIcon(new ImageIcon(HighScorePanel.class.getResource("/images/background/highscore/back.png")));
			}
		});
		mmenu.setIcon(new ImageIcon(HighScorePanel.class.getResource("/images/background/highscore/back.png")));
		mmenu.setBounds(583, 627, 178, 64);
		mmenu.addActionListener(backToMainMenuListener);
		mmenu.setContentAreaFilled(false);
		mmenu.setBorder(null);
		mmenu.setOpaque(false);
		setLayout(null);
		add(mmenu);
	}
	
	public void updateList()
	{
		ArrayList<HighscoreEntry> entries=dMap.getTopEntries(numListEntries);
		for(int i=0;i<numListEntries;i++)
		{
			if(i<entries.size())
			{
				HighscoreEntry entry=entries.get(i);
				names[i].setText(entry.getName());
				scores[i].setText(""+entry.getScore());
				dashes[i].setText("-");
			}
			else
			{
				names[i].setText("");
				scores[i].setText("");
				dashes[i].setText("");
			}
		}
	}
}
