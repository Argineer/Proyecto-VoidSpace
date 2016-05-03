package rbadia.voidspace.model;

import java.awt.Rectangle;
import java.util.Random;

import rbadia.voidspace.main.GameScreen;

public class Asteroid2 extends Rectangle {
	private static final long serialVersionUID = 1L;
	
	public static final int DEFAULT_SPEED = 6;
	
	private int asteroid2Width = 32;
	private int asteroid2Height = 32;
	private int speed = DEFAULT_SPEED;

	private Random rand = new Random();
	
	/**
	 * Crates a new second asteroid at a random x location at the top of the screen 
	 * @param screen the game screen
	 */
	public Asteroid2(GameScreen screen){
		this.setLocation(
        		rand.nextInt(screen.getWidth() - asteroid2Width),
        		0);
		this.setSize(asteroid2Width, asteroid2Height);
	}
	
	public int getAsteroid2Width() {
		return asteroid2Width;
	}
	public int getAsteroid2Height() {
		return asteroid2Height;
	}

	/**
	 * Returns the current asteroid speed
	 * @return the current asteroid speed
	 */
	public int getSpeed() {
		return speed;
	}
	
	/**
	 * Set the current asteroid speed
	 * @param speed the speed to set
	 */
	public void setSpeed(int speed) {
		this.speed = speed;
	}
	
	/**
	 * Returns the default asteroid speed.
	 * @return the default asteroid speed
	 */
	public int getDefaultSpeed(){
		return DEFAULT_SPEED;
	}
}
