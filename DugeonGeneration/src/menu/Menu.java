package menu;

import greenfoot.Greenfoot;
import greenfoot.World;

public class Menu extends World implements IWorldWithMenu {

	/*private List<IWorldWithMenu> worlds = new ArrayList<IWorldWithMenu>();

	public Menu() throws IOException {
		this(DungeonMap.VIEWPORT_WIDTH, DungeonMap.VIEWPORT_HEIGHT);
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

	

	*/
	@Override
	public void switchTo() {
		Greenfoot.setWorld(this);
	}
	
	public Menu(int menuWidth, int menuHeight) {
		super(menuWidth, menuHeight, 1, true);
	}
}
