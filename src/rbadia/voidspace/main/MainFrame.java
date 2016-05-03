package rbadia.voidspace.main;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;


/**
 * The game's main frame. Contains all the game's labels, file menu, and game screen.
 */
public class MainFrame extends JFrame {
	private static final long serialVersionUID = 1L;

	private JPanel jContentPane = null;

	private GameScreen gameScreen = null;
	
	private JLabel asteroidsDestroyedLabel;
	private JLabel asteroidsDestroyedValueLabel;
	
	private JLabel enemyShipsDestroyedLabel;
	private JLabel enemyShipsDestroyedValueLabel;

	private JLabel shipsLabel;
	private JLabel shipsValueLabel;
	
	private JLabel scoreLabel;
	private JLabel scoreValueLabel;
	
	/**
	 * This is the default constructor
	 */
	public MainFrame() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(602, 540);
		this.setContentPane(getJContentPane());
		this.setTitle("Void Space");
//		this.setResizable(false);
		
		Dimension dim = this.getToolkit().getScreenSize();
		Rectangle bounds = this.getBounds();
		this.setLocation(
			(dim.width - bounds.width) / 2,
			(dim.height - bounds.height) / 2);
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
		
			GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
			gridBagConstraints8.insets = new Insets(0, 0, 0, 0);
			gridBagConstraints8.gridy = 1;
			gridBagConstraints8.anchor = GridBagConstraints.EAST;
			gridBagConstraints8.weightx = 0;
			gridBagConstraints8.gridx = 3;
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.insets = new Insets(0, 0, 0, 0);
			gridBagConstraints7.gridy = 1;
			gridBagConstraints7.anchor = GridBagConstraints.CENTER;
			gridBagConstraints7.weightx = 0;
			gridBagConstraints7.gridx = 3;
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.insets = new Insets(0, 0, 0, 0);
			gridBagConstraints6.gridy = 1;
			gridBagConstraints6.anchor = GridBagConstraints.WEST;
			gridBagConstraints6.weightx = 1.0D;
			gridBagConstraints6.gridx = 3;
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.insets = new Insets(0, 0, 0, 0);
			gridBagConstraints5.gridy = 1;
			gridBagConstraints5.anchor = GridBagConstraints.EAST;
			gridBagConstraints5.weightx = 1.0D;
			gridBagConstraints5.gridx = 2;
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.insets = new Insets(0, 0, 0, 0);
			gridBagConstraints4.gridy = 1;
			gridBagConstraints4.anchor = GridBagConstraints.WEST;
			gridBagConstraints4.weightx = 1.0D;
			gridBagConstraints4.gridx = 2;
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.insets = new Insets(0, 0, 0, 0);
			gridBagConstraints3.gridy = 1;
			gridBagConstraints3.anchor = GridBagConstraints.EAST;
			gridBagConstraints3.weightx = 1.0D;
			gridBagConstraints3.gridx = 1;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.insets = new Insets(0, 0, 0, 0);
			gridBagConstraints2.gridy = 1;
			gridBagConstraints2.anchor = GridBagConstraints.WEST;
			gridBagConstraints2.weightx = 0;
			gridBagConstraints2.gridx = 1;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.insets = new Insets(0, 0, 0, 0);
			gridBagConstraints1.gridy = 1;
			gridBagConstraints1.anchor = GridBagConstraints.EAST;
			gridBagConstraints1.weightx = 0;
			gridBagConstraints1.gridx = 0;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.insets = new Insets(0, 0, 0, 0);
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.fill = GridBagConstraints.NONE;
			gridBagConstraints.gridwidth = 4;
			shipsLabel = new JLabel("Ships Left: ");
			shipsValueLabel = new JLabel("3");
			asteroidsDestroyedLabel = new JLabel("Asteroids Destroyed: ");
			asteroidsDestroyedValueLabel = new JLabel("0");
			enemyShipsDestroyedLabel = new JLabel("   Enemy Ships Destroyed:");
			enemyShipsDestroyedValueLabel = new JLabel("0");
			scoreLabel = new JLabel("Score: ");
			scoreValueLabel = new JLabel("0");
			jContentPane = new JPanel();
			jContentPane.setLayout(new GridBagLayout());
			jContentPane.add(getGameScreen(), gridBagConstraints);
			jContentPane.add(shipsLabel, gridBagConstraints1);
			jContentPane.add(shipsValueLabel, gridBagConstraints2);
			jContentPane.add(scoreLabel, gridBagConstraints3);
			jContentPane.add(scoreValueLabel, gridBagConstraints4);
			jContentPane.add(asteroidsDestroyedLabel, gridBagConstraints5);
			jContentPane.add(asteroidsDestroyedValueLabel, gridBagConstraints6);
			jContentPane.add(enemyShipsDestroyedLabel, gridBagConstraints7);
			jContentPane.add(enemyShipsDestroyedValueLabel, gridBagConstraints8);
		}
		return jContentPane;
	}

	/**
	 * This method initializes gameScreen	
	 * 	
	 * @return GameScreen
	 */
	public GameScreen getGameScreen() {
		if (gameScreen == null) {
			gameScreen = new GameScreen();
			gameScreen.setShipsValueLabel(shipsValueLabel);
			gameScreen.setAsteroidsDestroyedValueLabel(asteroidsDestroyedValueLabel);
			gameScreen.setEnemyShipsDestroyedValueLabel(enemyShipsDestroyedValueLabel);
			gameScreen.setScoreValueLabel(scoreValueLabel);
		}
		return gameScreen;
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
