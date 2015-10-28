package menu;

import greenfoot.Actor;
import greenfoot.Greenfoot;
import greenfoot.GreenfootImage;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class Button extends Actor {

	public static final int FONTSIZE = 20;

	private int dimX;
	private int dimY;
	private String text;
	private List<ActionListener> listeners = new ArrayList<ActionListener>();

	public Button(String text) {
		super();
		this.text = text;
		dimX = 0;
		dimY = 0;
		ensureSufficientImagesize();
	}

	public Button(int dimX, int dimY, String text) {
		super();
		this.dimX = dimX;
		this.dimY = dimY;
		this.text = text;
		ensureSufficientImagesize();
	}

	@Override
	public void act() {
		super.act();
		if (Greenfoot.mouseClicked(this)) {
			ActionEvent e = new ActionEvent(this, ActionEvent.ACTION_PERFORMED,
					getText());
			for (ActionListener actionListener : listeners) {
				actionListener.actionPerformed(e);
			}
		}
	}

	private void ensureSufficientImagesize() {
		if (dimX == 0 || dimY == 0) {
			dimX = getText().length() * FONTSIZE / 2 + 2 * FONTSIZE;
			dimY = FONTSIZE * 3;
		}
		GreenfootImage img = new GreenfootImage(dimX, dimY);
		img.setColor(Color.GRAY);
		img.fill();
		img.setColor(Color.BLACK);
		img.setFont(new Font("Arial", Font.BOLD, FONTSIZE));
		img.drawString(getText(), FONTSIZE, (int) (FONTSIZE * 1.8));
		setImage(img);
	}

	public void addActionListener(ActionListener al) {
		listeners.add(al);
	}

	public boolean removeActionListener(ActionListener al) {
		return listeners.remove(al);
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	/**
	 * should only be called by the BasicMenu Implementation that this object
	 * belongs to.
	 */
	void setPosition(int x, int y) {
		super.setLocation(x, y);
	}

	/**
	 * does nothing on this object.
	 */
	@Override
	public void setRotation(int rotation) {
	}

	/**
	 * does nothing on this object.
	 */
	@Override
	public void setLocation(int x, int y) {
	}

	/**
	 * does nothing on this object.
	 */
	@Override
	public void move(int distance) {
	}
}
