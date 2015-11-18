package menu;

import greenfoot.GreenfootImage;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import world.DungeonMap;

@SuppressWarnings("serial")
public class ShopEntry extends JPanel 
{
	private JLabel descriptionLabel=null;
	private JLabel costLabel=null;
	private JLabel amountLabel=null;
	private JButton buyButton=null;
	private BufferedImage icon=null;
	private DungeonMap world;
	
	private BuyItem buyItem;
	private int price;
	private int amount;
	
	public ShopEntry(BuyItem type, GreenfootImage icon, int price, int amount, DungeonMap world)
	{
		this.world=world;
		this.icon=icon.getAwtImage();
		this.price=price;
		this.amount=amount;
		this.buyItem=type;
		
		buildGUI();
	}
	
	private void buildGUI()
	{
		setLayout(null);
		setPreferredSize(new Dimension(700,50));
		
		int height=40;
		int y=5;
		int x=0;
		int width=250;
		descriptionLabel=new JLabel(new ImageIcon(icon));
		descriptionLabel.setText(buyItem.getValue());
		descriptionLabel.setHorizontalTextPosition(JLabel.RIGHT);
		descriptionLabel.setVerticalTextPosition(JLabel.CENTER);
		descriptionLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		descriptionLabel.setBounds(x, y, width, height);
		add(descriptionLabel);
		
		x+=width+150;
		width=100;
		amountLabel=new JLabel();
		amountLabel.setText("Amount: "+amount);
		amountLabel.setBounds(x, y, width, height);
		add(amountLabel);
		
		x+=width+50;
		width=100;
		costLabel=new JLabel();
		costLabel.setText("Price: "+price);
		costLabel.setBounds(x, y, width, height);
		add(costLabel);
		
		x+=width+100;
		width=150;
		buyButton=new JButton();
		buyButton.setText("Buy");		//Surprise surprise!
		buyButton.setSize(new Dimension(width,30));
		buyButton.setLocation(x, 10);
		buyButton.addActionListener((ActionEvent e)->{
			boolean success=world.playerTriesToBuy(buyItem, price, amount);
			if(success)
				world.updateFeedbackLabel(true, "Bought "+amount+" "+buyItem.getValue()+" for "+price+" Coins.");
			else
				world.updateFeedbackLabel(false, "Failed to buy "+amount+" "+buyItem.getValue()+" for "+price+" Coins.");
		});
		add(buyButton);
	}
}
