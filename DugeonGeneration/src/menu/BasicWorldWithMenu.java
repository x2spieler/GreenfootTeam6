package menu;

import greenfoot.Greenfoot;
import scrollWorld.ScrollWorld;

public abstract class BasicWorldWithMenu extends ScrollWorld implements
		IMenuSubworld {

	private IWorldWithMenu menu;
	public static final String MENU_BUTTON_KEY = "escape";

	public BasicWorldWithMenu(int width, int height, int cellSize,
			int fullWidth, int fullHeight, Menu menu) {
		super(width, height, cellSize, fullWidth, fullHeight);
		this.menu = menu;
	}

	@Override
	public void setMenu(Menu menu) {
		this.menu = menu;
	}

	@Override
	public void act() {
		super.act();
		if (menu != null && Greenfoot.isKeyDown(MENU_BUTTON_KEY)) {
			menu.switchTo();
		}
	}

}
