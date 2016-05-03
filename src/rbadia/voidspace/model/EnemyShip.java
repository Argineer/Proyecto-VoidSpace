package rbadia.voidspace.model;

import java.awt.Rectangle;
import java.util.Random;

import rbadia.voidspace.main.GameScreen;

public class EnemyShip extends Rectangle {
	private static final long serialVersionUID = 1L;
	
	public static final int DEFAULT_SPEED = 2;
	
	private int enemyShipWidth = 32;
	private int enemyShipHeight = 32;
	private int speed = DEFAULT_SPEED;

	

	private Random rand = new Random();


	
	/**
	 * Crates a new asteroid at a random x location at the top of the screen 
	 * @param screen the game screen
	 */
	public EnemyShip(GameScreen screen){
	
		this.setLocation(
        		rand.nextInt(screen.getWidth() - enemyShipWidth),
        		0);
		this.setSize(enemyShipWidth, enemyShipHeight);
	}
	
	public int getEnemyShipWidth() {
		return enemyShipWidth;
	}
	public int getAsteroidHeight() {
		return enemyShipHeight;
	}

	/**
	 * Returns the current enemy ship speed
	 * @return the current enemy ship speed
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
	 * Returns the default enemy ship speed.
	 * @return the default enemy ship speed
	 */
	public int getDefaultSpeed(){
		return DEFAULT_SPEED;
	}
}

