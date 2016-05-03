package rbadia.voidspace.main;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Random;

import javax.swing.JLabel;
import javax.swing.JPanel;

import rbadia.voidspace.graphics.GraphicsManager;
import rbadia.voidspace.model.Asteroid;
import rbadia.voidspace.model.Asteroid2;
import rbadia.voidspace.model.Bullet;
import rbadia.voidspace.model.EnemyBullet;
import rbadia.voidspace.model.Ship;
import rbadia.voidspace.model.EnemyShip;
import rbadia.voidspace.sounds.SoundManager;

/**
 * Main game screen. Handles all game graphics updates and some of the game logic.
 */
public class GameScreen extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private BufferedImage backBuffer;
	private Graphics2D g2d;
	
	private static final int NEW_SHIP_DELAY = 500;
	private static final int NEW_ASTEROID_DELAY = 500;
	private static final int NEW_ENEMYSHIP_DELAY = 500;
	
	private long lastShipTime;
	private long lastAsteroidTime;
	private long lastEnemyShipTime;
	private long lastBulletTime;
	
	private Rectangle asteroidExplosion;
	private Rectangle shipExplosion;
	private Rectangle enemyShipExplosion;
	
	private JLabel shipsValueLabel;
	private JLabel asteroidsDestroyedValueLabel;
	private JLabel enemyShipsDestroyedValueLabel;
	private JLabel scoreValueLabel;

	private Random rand;
	
	private Font originalFont;
	private Font bigFont;
	private Font biggestFont;
	
	private GameStatus status;
	private SoundManager soundMan;
	private GraphicsManager graphicsMan;
	private GameLogic gameLogic;

	/**
	 * This method initializes 
	 * 
	 */
	public GameScreen() {
		super();
		// initialize random number generator
		rand = new Random();
		
		initialize();
		
		// init graphics manager
		graphicsMan = new GraphicsManager();
		
		// init back buffer image
		backBuffer = new BufferedImage(600, 500, BufferedImage.TYPE_INT_RGB);
		g2d = backBuffer.createGraphics();
	}

	/**
	 * Initialization method (for VE compatibility).
	 */
	private void initialize() {
		// set panel properties
        this.setSize(new Dimension(600, 500));
        this.setPreferredSize(new Dimension(600, 500));
        this.setBackground(Color.BLACK);
	}

	/**
	 * Update the game screen.
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		// draw current backbuffer to the actual game screen
		g.drawImage(backBuffer, 0, 0, this);
	}
	
	/**
	 * Update the game screen's backbuffer image.
	 */
	public void updateScreen(){
		Ship ship = gameLogic.getShip();
		Asteroid asteroid = gameLogic.getAsteroid();
		Asteroid2 asteroid2 = gameLogic.getAsteroid2();
		List<Bullet> bullets = gameLogic.getBullets();
		EnemyShip enemyShip = gameLogic.getEnemyShip();
		List<EnemyBullet> enemyBullets = gameLogic.getEnemyBullets();
		
		// set original font - for later use
		if(this.originalFont == null){
			this.originalFont = g2d.getFont();
			this.bigFont = originalFont;
		}
		
		// erase screen
		g2d.setPaint(Color.BLACK);
		g2d.fillRect(0, 0, getSize().width, getSize().height);

		// draw 50 random stars
		drawStars(50);
		
		// if the game is starting, draw "Get Ready" message
		if(status.isGameStarting()){
			drawGetReady();
			return;
		}
		
		// if the game is over, draw the "Game Over" message
		if(status.isGameOver()){
			// draw the message
			drawGameOver();
			
			long currentTime = System.currentTimeMillis();
			// draw the explosions until their time passes
			if((currentTime - lastAsteroidTime) < NEW_ASTEROID_DELAY){
				graphicsMan.drawAsteroidExplosion(asteroidExplosion, g2d, this);
			}
			if((currentTime - lastShipTime) < NEW_SHIP_DELAY){
				graphicsMan.drawShipExplosion(shipExplosion, g2d, this);
			}
			if((currentTime - lastEnemyShipTime) < NEW_ENEMYSHIP_DELAY){
				graphicsMan.drawShipExplosion(shipExplosion, g2d, this);
			}
			return;
		}
		
		// the game has not started yet
		if(!status.isGameStarted()){
			// draw game title screen
			initialMessage();
			return;
		}
		
		// the player has passed the 1st level, if they destroyed 5 asteroids
		if (status.isGameOnNextLevel() && (status.getAsteroidsDestroyed()==5)){
			drawNextLevel();
		}
		
		// draw ship
		if(!status.isNewShip()){
			// draw it in its current location
			graphicsMan.drawShip(ship, g2d, this);
		}
		else{
			// draw a new one
			long currentTime = System.currentTimeMillis();
			if((currentTime - lastShipTime) > NEW_SHIP_DELAY){
				lastShipTime = currentTime;
				status.setNewShip(false);
				ship = gameLogic.newShip(this);
			}
			else{
				// draw explosion
				graphicsMan.drawShipExplosion(shipExplosion, g2d, this);
			}
		}
		
		// draw asteroid
		if(!status.isNewAsteroid()){
			// draw the asteroid until it reaches the bottom of the screen
			if(asteroid.getY() + asteroid.getSpeed() < this.getHeight()){
				asteroid.translate(0, asteroid.getSpeed());
				graphicsMan.drawAsteroid(asteroid, g2d, this);
			}	
			else{
				asteroid.setLocation(rand.nextInt(getWidth() - asteroid.width), 0);
				
			}
		}
		else{
			long currentTime = System.currentTimeMillis();
			if((currentTime - lastAsteroidTime) > NEW_ASTEROID_DELAY){
				// draw a new asteroid
				lastAsteroidTime = currentTime;
				status.setNewAsteroid(false);
				asteroid.setLocation(rand.nextInt(getWidth() - asteroid.width), 0);
				asteroid.translate(0, asteroid.getSpeed());
				graphicsMan.drawAsteroid(asteroid, g2d, this);
			}
			else{
				// draw explosion
				graphicsMan.drawAsteroidExplosion(asteroidExplosion, g2d, this);
			}
		}
		
		// draw asteroid2
		if (status.isGameOnNextLevel() && (status.getAsteroidsDestroyed()>=5)){
				if(!status.isNewAsteroid2()){
					// draw the second asteroid until it reaches the bottom of the screen
					if(asteroid2.getY() + asteroid2.getSpeed() < this.getHeight()){
						asteroid2.translate(2,asteroid2.getSpeed());
						graphicsMan.drawAsteroid2(asteroid2, g2d, this);		
					}
					else{
						asteroid2.setLocation(rand.nextInt(getWidth() - asteroid2.width - 100),0);
						
					}
				}
				else{
					long currentTime = System.currentTimeMillis();
					if((currentTime - lastAsteroidTime) > NEW_ASTEROID_DELAY){
						// draw a new second asteroid
						lastAsteroidTime = currentTime;
						status.setNewAsteroid2(false);
						asteroid2.setLocation(rand.nextInt(getWidth() - asteroid2.width - 100),0);
						asteroid2.translate(2,asteroid2.getSpeed());
						graphicsMan.drawAsteroid2(asteroid2, g2d, this);
					}
					else{
						// draw explosion
						graphicsMan.drawAsteroidExplosion(asteroidExplosion, g2d, this);
					}
				}
		}
		
		// draw enemy ships
		if (status.isGameOnNextLevel() && (status.getAsteroidsDestroyed()>=5)){
		if(!status.isNewEnemyShip()){
			// draw the enemy ship until it reaches the bottom of the screen
			if(enemyShip.getY() + enemyShip.getSpeed() < this.getHeight()){
				enemyShip.translate(0, enemyShip.getSpeed());
				graphicsMan.drawEnemyShip(enemyShip, g2d, this);	
				
				
				for(int i=0; i < 10; i++){
					if(i%5==0){
						// fire only up to 5 bullets per second
						long currentTime = System.currentTimeMillis();
						if((currentTime - lastBulletTime) > 1000){
							lastBulletTime = currentTime;
							gameLogic.fireEnemyBullet();
						}}
				}
			}
			
			else{
				enemyShip.setLocation(rand.nextInt(getWidth() - enemyShip.width), 0);
			}
		}
		else{
			long currentTime = System.currentTimeMillis();
			if((currentTime - lastEnemyShipTime) > NEW_ENEMYSHIP_DELAY){
				// draw a new enemy ship
				lastEnemyShipTime = currentTime;
				status.setNewEnemyShip(false);
				enemyShip.setLocation(rand.nextInt(getWidth() - enemyShip.width), 0);
			}
			else{
				// draw explosion
				graphicsMan.drawEnemyShipExplosion(enemyShipExplosion, g2d, this);
			}
		}
		}
		
		// draw bullets
		for(int i=0; i<bullets.size(); i++){
			Bullet bullet = bullets.get(i);
			graphicsMan.drawBullet(bullet, g2d, this);
			
			boolean remove = gameLogic.moveBullet(bullet);
			if(remove){
				bullets.remove(i);
				i--;
			}
		}
		
		// draw enemy bullets
		for(int i=0; i<enemyBullets.size(); i++){
			EnemyBullet enemyBullet = enemyBullets.get(i);
			graphicsMan.drawEnemyBullet(enemyBullet, g2d, this);
			
			boolean remove = gameLogic.moveEnemyBullet(enemyBullet);
			if(remove){
				enemyBullets.remove(i);
				i--;
			}
		}
		
		// check bullet-asteroid collisions
		for(int i=0; i<bullets.size(); i++){
			Bullet bullet = bullets.get(i);
			if(asteroid.intersects(bullet) || asteroid2.intersects(bullet)){
				// increase asteroids destroyed count
				status.setAsteroidsDestroyed(status.getAsteroidsDestroyed() + 1);
				
				// increase score
				status.setScore(status.getScore() + 20);
				
				// "remove" asteroid
				if(asteroid.intersects(bullet)){
		        asteroidExplosion = new Rectangle(
		        		asteroid.x,
		        		asteroid.y,
		        		asteroid.width,
		        		asteroid.height);
				asteroid.setLocation(-asteroid.width, -asteroid.height);
				status.setNewAsteroid(true);
				lastAsteroidTime = System.currentTimeMillis();
				}
				// "remove" asteroid2
				if(asteroid2.intersects(bullet)){
		        asteroidExplosion = new Rectangle(
		        		asteroid2.x,
		        		asteroid2.y,
		        		asteroid2.width,
		        		asteroid2.height);
				asteroid2.setLocation(-asteroid2.width, -asteroid2.height);
				status.setNewAsteroid2(true);
				lastAsteroidTime = System.currentTimeMillis();
				}
				// play asteroid explosion sound
				soundMan.playAsteroidExplosionSound();
				
				// remove bullet
				bullets.remove(i);
				
				//condition to pass to next level
				if (status.getAsteroidsDestroyed() == 5){ 
					status.setGameOnNextLevel(true);
				}
				break;
			}
		}

		
		// check bullet-enemy ship collisions
		for(int i=0; i<bullets.size(); i++){
			Bullet bullet = bullets.get(i);
			if(enemyShip.intersects(bullet)){
				// increase enemy ships destroyed count
				status.setEnemyShipsDestroyed(status.getEnemyShipsDestroyed() + 1);
				
				// increase score
				status.setScore(status.getScore() + 60);
				
				// "remove" enemy ship
		        enemyShipExplosion = new Rectangle(
		        		enemyShip.x,
		        		enemyShip.y,
		        		enemyShip.width,
		        		enemyShip.height);
				enemyShip.setLocation(-enemyShip.width, -enemyShip.height);
				status.setNewEnemyShip(true);
				lastEnemyShipTime = System.currentTimeMillis();
				
				// play enemy ship explosion sound
				soundMan.playShipExplosionSound();
				
				// remove bullet
				bullets.remove(i);
				break;
			}
		}
		
		// check enemy bullet-ship collisions
		for(int i=0; i<enemyBullets.size(); i++){
			EnemyBullet enemyBullet = enemyBullets.get(i);
			if(enemyBullet.intersects(ship)){
				
				// decrease score
				status.setScore(status.getScore() - 60);
				
				//decrease number of ships left
				status.setShipsLeft(status.getShipsLeft() - 1);
			
				// "remove" ship
		        shipExplosion = new Rectangle(
		        		ship.x,
		        		ship.y,
		        		ship.width,
		        		ship.height);
				ship.setLocation(this.getWidth() + ship.width, -ship.height);
				status.setNewShip(true);
				lastShipTime = System.currentTimeMillis();
				
				// play ship explosion sound
				soundMan.playShipExplosionSound();
				
				// remove bullet
				enemyBullets.remove(i);
				break;
			}
		}
		
		
		
		// check ship-asteroid collisions
		if(asteroid.intersects(ship) || asteroid2.intersects(ship)){
			// decrease number of ships left
			status.setShipsLeft(status.getShipsLeft() - 1);
			
			//increase number of asteroids destroyed
			status.setAsteroidsDestroyed(status.getAsteroidsDestroyed() + 1);
			
			//decrease score
			status.setScore(status.getScore() - 20);

			// "remove" asteroid
			if(asteroid.intersects(ship)){
	        asteroidExplosion = new Rectangle(
	        		asteroid.x,
	        		asteroid.y,
	        		asteroid.width,
	        		asteroid.height);
			asteroid.setLocation(-asteroid.width, -asteroid.height);
			status.setNewAsteroid(true);
			lastAsteroidTime = System.currentTimeMillis();
			}
			
			// "remove" asteroid2
			if(asteroid2.intersects(ship)){
	        asteroidExplosion = new Rectangle(
	        		asteroid2.x,
	        		asteroid2.y,
	        		asteroid2.width,
	        		asteroid2.height);
			asteroid2.setLocation(-asteroid2.width, -asteroid2.height);
			status.setNewAsteroid2(true);
			lastAsteroidTime = System.currentTimeMillis();
			}
			
			// "remove" ship
	        shipExplosion = new Rectangle(
	        		ship.x,
	        		ship.y,
	        		ship.width,
	        		ship.height);
			ship.setLocation(this.getWidth() + ship.width, -ship.height);
			status.setNewShip(true);
			lastShipTime = System.currentTimeMillis();
			
			// play ship explosion sound
			soundMan.playShipExplosionSound();
			// play asteroid explosion sound
			soundMan.playAsteroidExplosionSound();
		}
		
	
		// check ship-enemy ship collisions
		if(ship.intersects(enemyShip)){
			// decrease number of ships left
			status.setShipsLeft(status.getShipsLeft() - 1);
			
			//increase number of enemy ships destroyed
			status.setEnemyShipsDestroyed(status.getEnemyShipsDestroyed() + 1);
			
			//decrease score
			status.setScore(status.getScore() - 60);

			// "remove" enemy ship 
	        enemyShipExplosion = new Rectangle(
	        		enemyShip.x,
	        		enemyShip.y,
	        		enemyShip.width,
	        		enemyShip.height);
			enemyShip.setLocation(-enemyShip.width, -enemyShip.height);
			status.setNewEnemyShip(true);
			lastEnemyShipTime = System.currentTimeMillis();
			
			// "remove" ship
	        shipExplosion = new Rectangle(
	        		ship.x,
	        		ship.y,
	        		ship.width,
	        		ship.height);
			ship.setLocation(this.getWidth() + ship.width, -ship.height);
			status.setNewShip(true);
			lastShipTime = System.currentTimeMillis();
			
			// play ship explosion sound
			soundMan.playShipExplosionSound();
			// play asteroid explosion sound
			soundMan.playShipExplosionSound();
		}
		
		// update asteroids destroyed label
		asteroidsDestroyedValueLabel.setText(Long.toString(status.getAsteroidsDestroyed()));
		
		// update enemy ships destroyed label
		enemyShipsDestroyedValueLabel.setText(Long.toString(status.getEnemyShipsDestroyed()));
		
		// update ships left label
		shipsValueLabel.setText(Integer.toString(status.getShipsLeft()));
		
		//update score label
		scoreValueLabel.setText(Long.toString(status.getScore()));
		
	}
	
	

	/**
	 * Draws the "Game Over" message.
	 */
	private void drawGameOver() {
		String gameOverStr = "GAME OVER";
		Font currentFont = biggestFont == null? bigFont : biggestFont;
		float fontSize = currentFont.getSize2D();
		bigFont = currentFont.deriveFont(fontSize + 10).deriveFont(Font.BOLD);
		FontMetrics fm = g2d.getFontMetrics(bigFont);
		int strWidth = fm.stringWidth(gameOverStr);
		if(strWidth > this.getWidth() - 10){
			biggestFont = currentFont;
			bigFont = biggestFont;
			fm = g2d.getFontMetrics(bigFont);
			strWidth = fm.stringWidth(gameOverStr);
		}
		int ascent = fm.getAscent();
		int strX = (this.getWidth() - strWidth)/2;
		int strY = (this.getHeight() + ascent)/2;
		g2d.setFont(bigFont);
		g2d.setPaint(Color.WHITE);
		g2d.drawString(gameOverStr, strX, strY);
	}

	/**
	 * Draws the "Level 1 Passed!" message.
	 */
	private void drawNextLevel() {
		String nextLevelStr = "Level 1 Passed!";
		g2d.setFont(bigFont.deriveFont(bigFont.getSize2D() + 10).deriveFont(Font.BOLD));
		FontMetrics fm = g2d.getFontMetrics(bigFont);
		int strWidth = fm.stringWidth(nextLevelStr);
		int ascent = fm.getAscent();
		int strX = (this.getWidth() - strWidth)/2;
		int strY = (this.getHeight() + ascent)/2;
		g2d.setPaint(Color.WHITE);
		g2d.drawString(nextLevelStr, strX, strY);
	}
	
	/**
	 * Draws the initial "Get Ready!" message.
	 */
	private void drawGetReady() {
		String readyStr = "Get Ready!";
		g2d.setFont(originalFont.deriveFont(originalFont.getSize2D() + 1));
		FontMetrics fm = g2d.getFontMetrics();
		int ascent = fm.getAscent();
		int strWidth = fm.stringWidth(readyStr);
		int strX = (this.getWidth() - strWidth)/2;
		int strY = (this.getHeight() + ascent)/2;
		g2d.setPaint(Color.WHITE);
		g2d.drawString(readyStr, strX, strY);
	}

	/**
	 * Draws the specified number of stars randomly on the game screen.
	 * @param numberOfStars the number of stars to draw
	 */
	private void drawStars(int numberOfStars) {
		g2d.setColor(Color.WHITE);
		for(int i=0; i<numberOfStars; i++){
			int x = (int)(Math.random() * this.getWidth());
			int y = (int)(Math.random() * this.getHeight());
			g2d.drawLine(x, y, x, y);
		}
	}

	/**
	 * Display initial game title screen.
	 */
	private void initialMessage() {
		String gameTitleStr = "Void Space";
		
		Font currentFont = biggestFont == null? bigFont : biggestFont;
		float fontSize = currentFont.getSize2D();
		bigFont = currentFont.deriveFont(fontSize + 1).deriveFont(Font.BOLD).deriveFont(Font.ITALIC);
		FontMetrics fm = g2d.getFontMetrics(bigFont);
		int strWidth = fm.stringWidth(gameTitleStr);
		if(strWidth > this.getWidth() - 10){
			bigFont = currentFont;
			biggestFont = currentFont;
			fm = g2d.getFontMetrics(currentFont);
			strWidth = fm.stringWidth(gameTitleStr);
		}
		g2d.setFont(bigFont);
		int ascent = fm.getAscent();
		int strX = (this.getWidth() - strWidth)/2;
		int strY = (this.getHeight() + ascent)/2 - ascent;
		g2d.setPaint(Color.YELLOW);
		g2d.drawString(gameTitleStr, strX, strY);
		
		g2d.setFont(originalFont);
		fm = g2d.getFontMetrics();
		String newGameStr = "Press <Space> to Start a New Game.";
		strWidth = fm.stringWidth(newGameStr);
		strX = (this.getWidth() - strWidth)/2;
		strY = (this.getHeight() + fm.getAscent())/2 + ascent + 16;
		g2d.setPaint(Color.WHITE);
		g2d.drawString(newGameStr, strX, strY);
		
		fm = g2d.getFontMetrics();
		String exitGameStr = "Press <Esc> to Exit the Game.";
		strWidth = fm.stringWidth(exitGameStr);
		strX = (this.getWidth() - strWidth)/2;
		strY = strY + 16;
		g2d.drawString(exitGameStr, strX, strY);
	}
	
	/**
	 * Prepare screen for game over.
	 */
	public void doGameOver(){
		shipsValueLabel.setForeground(new Color(128, 0, 0));
	}
	
	/**
	 * Prepare screen for a new game.
	 */
	public void doNewGame(){		
		lastAsteroidTime = -NEW_ASTEROID_DELAY;
		lastShipTime = -NEW_SHIP_DELAY;
		lastEnemyShipTime = -NEW_ENEMYSHIP_DELAY;
				
		bigFont = originalFont;
		biggestFont = null;
				
        // set labels' text
		shipsValueLabel.setForeground(Color.BLACK);
		shipsValueLabel.setText(Integer.toString(status.getShipsLeft()));
		asteroidsDestroyedValueLabel.setText(Long.toString(status.getAsteroidsDestroyed()));
		enemyShipsDestroyedValueLabel.setText(Long.toString(status.getEnemyShipsDestroyed()));
		scoreValueLabel.setText(Long.toString(status.getScore()));
	}

	/**
	 * Sets the game graphics manager.
	 * @param graphicsMan the graphics manager
	 */
	public void setGraphicsMan(GraphicsManager graphicsMan) {
		this.graphicsMan = graphicsMan;
	}

	/**
	 * Sets the game logic handler
	 * @param gameLogic the game logic handler
	 */
	public void setGameLogic(GameLogic gameLogic) {
		this.gameLogic = gameLogic;
		this.status = gameLogic.getStatus();
		this.soundMan = gameLogic.getSoundMan();
	}

	/**
	 * Sets the label that displays the value for asteroids destroyed.
	 * @param asteroidsDestroyedValueLabel the label to set
	 */
	public void setAsteroidsDestroyedValueLabel(JLabel asteroidsDestroyedValueLabel) {
		this.asteroidsDestroyedValueLabel = asteroidsDestroyedValueLabel;
	}
	
	/**
	 * Sets the label that displays the value for enemy ships destroyed.
	 * @param enemyShipsDestroyedValueLabel the label to set
	 */
	public void setEnemyShipsDestroyedValueLabel(JLabel enemyShipsDestroyedValueLabel) {
		this.enemyShipsDestroyedValueLabel = enemyShipsDestroyedValueLabel;
	}
	
	/**
	 * Sets the label that displays the value for ship (lives) left
	 * @param shipsValueLabel the label to set
	 */
	public void setShipsValueLabel(JLabel shipsValueLabel) {
		this.shipsValueLabel = shipsValueLabel;
	}
	
	/**
	 * Sets the label that displays the value for the score
	 * @param scoreValueLabel the label to set
	 */
	public void setScoreValueLabel(JLabel scoreValueLabel) {
		this.scoreValueLabel = scoreValueLabel;
	}
}
