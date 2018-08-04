package sim.tetris;

/**
 * A game object sets up a timer to refresh the game status 40 times a second.
 * When the game is updated, the key presses are updated and the current scene
 * is updated.  If the scene needs to be chaned, then the update function will
 * switch to the new scene.
 */
public class Game 
{
	 boolean ULTRA_MODE = false;
	
	/** the current scene of this game */
	private GameScene scene;
	
	/** the player playing this game */
	private Player player;

	/** the drawing surface */
//	private Canvas canvas;
	
	/** a map of the key presses */
	public InputMap input;	

//	/** The thread that sets a frame rate for the game */
//	private Thread thread;

	/** the speed of the game, the frame rate = 1000/delay */
	public final int delay = 25;
		
	/** the properties file that stores the high score */
	public String file = "scores";	
		
	TetrisAgent agent;
	
	/**
	 * Creates the splash screen, and begins running the game.
	 * 
	 * @param drawingCanvas - the canvas used to draw the graphics
	 */
	public Game(int seed)
	{
		// set up the input map
		input = new InputMap();		 
		
		agent = new TabuTetrisAgent(input); 
		
		// set up a new player
		player = new Player();
		
		// set the initial scene
//		canvas = drawingCanvas;
		scene = new GameScene(seed, player, agent);


		// set up the thread
//		thread = new Thread(this);
//		thread.start();
	}
	
	public InputMap getInputMap() {
		return input;
	}

	public double getHeightFactor() {
		return ((TabuTetrisAgent)agent).HEIGHT_FACTOR;
	}
	public double getBalanceFactor() {
		return ((TabuTetrisAgent)agent).BALANCE_FACTOR;
	}
	
	public double getHoleFactor() {
		return ((TabuTetrisAgent)agent).HOLE_FACTOR;
	}
	
	public double getBlockFactor() {
		return ((TabuTetrisAgent)agent).BLOCK_FACTOR;
	}
	
	public double getLineFactor() {
		return ((TabuTetrisAgent)agent).LINE_FACTOR;
	}
	
	/**
	 * Calls update after a certain amount of time has elapsed, and loops until
	 * the program exits.
	 */
	public int runSimulation()
	{
		// loop forever
		while (true)
		{	
			scene.update(input);
			
			if (scene.isDead()) {
				return player.getLines();
			}
		}
	}		
	
	/**
	 * Updates the game.
	 * 
	 * <p>The follow things occur during an update 
	 * <br> 1. update keypresses
	 * <br> 2. update the current scene
	 * <br> 3. redraw the graphics
	 */
	public void update()
	{		
		// update the scene
		scene.update(input);
	
		// redraw the graphics
//		canvas.draw(scene);
	}
}