package Animations;

import greenfoot.GreenfootImage;

public class PickupAnimation extends AnimationActor {

	public PickupAnimation() {
		FRAMES_PER_IMG = 10;
		loadImages();
	}

	@Override
	protected void loadImages() {
		int numFrames = 6;
		images = new GreenfootImage[numFrames];
		for (int i = 0; i < numFrames; i++)
			images[i] = new GreenfootImage("animations/pickup/" + i + ".png");
	}

}
