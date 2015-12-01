package camera;

import java.awt.AWTException;
import java.awt.Cursor;
import java.awt.HeadlessException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import javax.imageio.ImageIO;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import world.DungeonMap;

public class MouseController {
	private static final int BORDER_WIDTH = 8;
	private static final int MAX_ANCHOR_DISTANCE = 900;
	private int viewPortDimX;
	private int viewPortDimY;
	private Robot robot;
	private ExecutorService thread = Executors.newSingleThreadExecutor();
	private FutureTask<Void> mController;
	private DungeonMap world;
	private JScrollPane viewPortPane;
	private Cursor rangedCursor;
	private Cursor meleeCursor;

	private boolean active;

	public MouseController(DungeonMap world)
			throws AWTException, HeadlessException, IndexOutOfBoundsException, IOException {
		this.world = world;
		Toolkit tk = Toolkit.getDefaultToolkit();
		this.rangedCursor = tk.createCustomCursor(
				ImageIO.read(getClass().getClassLoader().getResource("images" + File.separator + "default_cursor.png")),
				new Point(32, 32), "Ranged Cursor");
		robot = new Robot();
	}

	public void setViewPorPane(JScrollPane viewPortPane) {
		if (viewPortPane == null)
			throw new IllegalArgumentException();
		this.viewPortPane = viewPortPane;
		viewPortDimX = viewPortPane.getWidth();
		viewPortDimY = viewPortPane.getHeight();
		viewPortDimX -= BORDER_WIDTH;
		viewPortDimY -= BORDER_WIDTH;
		SwingUtilities.getRoot(viewPortPane).setCursor(rangedCursor);
	}

	public void start() {
		if (viewPortPane == null)
			return;
		mController = new MController();
		thread.submit(mController);
		System.out.println("mouseController started");
	}

	public void stop() {
		if (mController == null)
			return;
		mController.cancel(true);
		System.out.println("mouseController stopped");
	}

	public void checkMouse() {
	}

	private void setMousePosition(int x, int y) {
		if (viewPortPane == null)
			return;
		Point target = new Point(x, y);
		SwingUtilities.convertPointToScreen(target, viewPortPane);
		robot.mouseMove(target.x, target.y);
	}

	private void setMouseHidden(boolean hidden) {
		if (viewPortPane == null)
			return;
		if (hidden) {
			viewPortPane.setCursor(Toolkit.getDefaultToolkit().createCustomCursor(
					new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB), new Point(0, 0), "empty_cursor"));
		} else {
			viewPortPane.setCursor(Cursor.getDefaultCursor());
		}
	}

	//	@Override
	//	protected void addedToWorld(World world) {
	//		this.viewPortPane = ((DungeonMap) world).getViewPort();
	//
	//		Dimension viewPortSize = viewPortPane.getSize();
	//		restingXinPane = viewPortSize.width / 2;
	//		restingYinPane = viewPortSize.height / 2;
	//		setMousePosition(restingXinPane, restingYinPane);
	//	}

	//	@Override
	//	public void setLocation(int x, int y) {
	//		if (anchor == null)
	//			return;
	//		int anchorX = anchor.getX();
	//		int anchorY = anchor.getY();
	//		int dX = x - anchorX;
	//		int dY = y - anchorY;
	//		double delta = Math.sqrt(x * x + y * y);
	//		if ((delta > MAX_ANCHOR_DISTANCE)) {
	//			double factor = MAX_ANCHOR_DISTANCE / delta;
	//			dX = (int) Math.sqrt(dX * dX * factor);
	//			dY = (int) Math.sqrt(dY * dY * factor);
	//			super.setLocation(anchorX + cursorCenter, anchorY + cursorCenter);
	//		} else {
	//			super.setLocation(x, y);
	//		}
	//	}

	private boolean isInViewport(int x, int y) {
		return x >= 0 && y >= 0 && x < DungeonMap.VIEWPORT_WIDTH && y < DungeonMap.VIEWPORT_HEIGHT;
	}

	private class MController extends FutureTask<Void> {

		public MController() {
			super(new Callable<Void>() {
				@Override
				public Void call() throws Exception {

					Point mousePos;
					Point componentPos;
					boolean changed;
					while (true) {
						mousePos = MouseInfo.getPointerInfo().getLocation();
						componentPos = viewPortPane.getLocationOnScreen();
						int mouseInComponentX = mousePos.x - componentPos.x;
						int mouseInComponentY = mousePos.y - componentPos.y;
						changed = false;
						if (mouseInComponentX > viewPortDimX) {
							mouseInComponentX = viewPortDimX;
							changed = true;
						} else if (mouseInComponentX < BORDER_WIDTH) {
							mouseInComponentX = BORDER_WIDTH;
							changed = true;
						}
						if (mouseInComponentY > viewPortDimY) {
							mouseInComponentY = viewPortDimY;
							changed = true;
						} else if (mouseInComponentY < BORDER_WIDTH) {
							mouseInComponentY = BORDER_WIDTH;
							changed = true;
						}
						if (changed) {
							robot.mouseMove(componentPos.x + mouseInComponentX, componentPos.y + mouseInComponentY);
						}
						Thread.sleep(1);
					}
				}
			});
		}

	}
}
