package menu;

import greenfoot.Greenfoot;
import greenfoot.World;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import world.DungeonMap;

public class Menu extends World implements IWorldWithMenu {

	private List<IWorldWithMenu> worlds = new ArrayList<IWorldWithMenu>();

	public Menu() {
		this(800, 600);
		final DungeonMap dungeon = new DungeonMap(this);
		Button button = new Button("Start");
		addObject(button, getWidth() / 2, getHeight() / 2);
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dungeon.switchTo();
				Greenfoot.playSound("button_clicked.wav");
			}
		});
	}

	public Menu(int menuWidth, int menuHeight) {
		super(menuWidth, menuHeight, 1, true);
	}

	@Override
	public void switchTo() {
		Greenfoot.setWorld(this);
	}

}
