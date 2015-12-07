package Animations;

import greenfoot.GreenfootImage;
import scrollWorld.ScrollActor;

public abstract class AnimationActor extends ScrollActor
{
	protected GreenfootImage images[];
	protected int FRAMES_PER_IMG=-1;
	
	private int counter=0;
	
	public void act()
	{
		if(counter%FRAMES_PER_IMG==0)
			setImage(images[counter/FRAMES_PER_IMG]);
		counter++;
		if(counter==FRAMES_PER_IMG*images.length)
			getWorld().removeObject(this);
	}
	
	/**
	 * Load the images for the animation
	 */
	protected abstract void loadImages();
}
