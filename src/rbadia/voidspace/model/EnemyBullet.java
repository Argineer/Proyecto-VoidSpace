package rbadia.voidspace.model;
import java.awt.Rectangle;

/**
 * Represents a bullet fired by a enemy ship.
 */
public class EnemyBullet extends Rectangle {
	private static final long serialVersionUID = 1L;
	
	private int bulletWidth = 8;
	private int bulletHeight = 8;
	private int speed = 10;

	/**
	 * Creates a new bullet below the enemy ship, centered on it
	 * @param enemyShip
	 */
	public EnemyBullet(EnemyShip enemyShip) {
		this.setLocation(enemyShip.x + enemyShip.width/2 - bulletWidth/2,
				enemyShip.y - bulletHeight);
		this.setSize(bulletWidth, bulletHeight);
	}
	
	/**
	 * Return the bullet's speed.
	 * @return the bullet's speed.
	 */
	public int getSpeed() {
		return speed;
	}

	/**
	 * Set the bullet's speed
	 * @param speed the speed to set
	 */
	public void setSpeed(int speed) {
		this.speed = speed;
	}
}