package sim.tetris; 

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;
/**
 * This Scene contains is the actual "game" scene.  It displays a level, the
 * current block being placed, the next block to place, the score, number of
 * lines cleared, and current level of the player.  Once the player dies, 
 * this scene switches to the Title screen.
 */
public class GameScene
{	
	public String START = "start";
	public String MOVE_LEFT = "left";
	public String MOVE_RIGHT = "right";
	public String ROTATE_CW = "rotate CW";
	public String ROTATE_CCW = "rotate CCW";
	
	public final int delay = 25;

	
	private boolean CLEARING_ANIMATION = true;
	
	/** the level associated with this game */
	private Level level;
	
	Random random;
	
	/** the block being placed */
	private Block block = null;	

	/** the next block to place */
	private Block next = null;
		
	/** tells if lines are being cleared */
	private boolean clearing = false;

	/** the lines to be removed */
	private ArrayList lines = new ArrayList();

	/** the size in pixels, of a block */
	public final int BLOCK_SIZE = 24;

	/** timer for animation */
	private double timer = 0;
	
	/** timer for falling blocks */
	private double levelTimer = 0;
	
	/** tells if a block should move down a space */
	private boolean dropping = false;

    /** the player playing this game */
	private Player player;

	/** tells if the player is dead */
	private boolean dead = false;
	
	/** the number of lines of blocks that have been filled with death blocks
	    once the player has died */
	private int deathCount = 0;
	
	/** tells if the game is paused */
	private boolean pause = false;
	
	/** tells if the game has started */
	private boolean started = false;

	TetrisAgent agent; 
	
	public boolean isDead() {
		return dead;
	}
	
	/**
	 * Creates a Tetris level, loads the images, and begins dropping blocks.
	 * 
	 * @param p - the player object containing the level to start on
	 */	
	public GameScene(int seed, Player p, TetrisAgent agent)
	{
		this.agent = agent;
        random = new Random(seed);
				
		// begin placing the first block
		player = p;		
		level = new Level();
	}
				
	/**
	 * Updates the scene.
	 * 
	 * <p>This first checks if the player is dead.  If the player is dead, it 
	 * updates the death animation and exits.  If the player is alive, the
	 * first thing it checks is if the player wants to pause.
	 * 
	 * <p>If lines are not currently being cleared, then we check to see if the
	 * block should move down a space, and also see if the player wants to drop
	 * the block.  If the block is not dropping, then we check for keypresses
	 * to move and rotate the block.  Next we check if the block is dropping
	 * and if lines are being cleared, so that we can udpate the status of
	 * these animations.
	 * 
	 * @param input - the input map that contains a list of buttons pressed
	 */
	public void update(InputMap input)
	{
		String event = input.getInputEvent();	
		
		//check if started
		if (started == false) 
		{
			//if (InputMap.START.equals(event)) 
			//{
				started = true;
				newBlock();
			//}
			
			return;
		}
		
		// check if dead
		if (dead)
		{				
			// game over
			if (deathCount == 20)
			{
				// check if the stats should be exported
//				if (Tetris.filename != null) {
//					Properties properties = new Properties();
//					properties.put("lines", "" + player.getLines());
//					properties.put("score", "" + player.getScore());
//					try {
//						properties.store(new FileOutputStream(Tetris.filename), "properties");
//					}
//					catch (Exception e) {
//					}
//					System.exit(0);
//				}
				// check if the player wants to restart
				if (START.equals(event)) 
				{
					CLEARING_ANIMATION = true;
					level = new Level();
					newBlock();
					clearing = false;
					lines = new ArrayList();
					timer = 0;
					levelTimer = 0;
					dropping = false;
					player = new Player();
					dead = false;
					deathCount = 0;
					pause = false;
					started = true;

				}
			}
			else
			{
				// update the death animation
				timer += delay/1000.0;
			
				if (timer > 0.1 )
					deathCount++;				
			}
			return;
		}
		
		// check if paused
		if (pause == true)
		{
			if (START.equals(event)) 
			{
				pause = false;	
			}
			else			
				return;
		}
		else
		{
			// check pause button
			if (START.equals(event))
			{
				pause = true;
				return;
			}
		}
			
		// check if drop time
		if (!clearing) 
		{
			checkDropTimer();
		}
			
		while (event != null) {
		   checkInput(event);
		   event = input.getInputEvent();
		}	
	
		
		if (dropping) {
			dropBlock();
			agent.blockDropped(block);
//			Tetris.agentThread.blockDropped(block);
		}
		
		// lines in the level are being cleared
		if (clearing)
			clearLines();
		
		return;
	}
	
	/**
	 * Checks if it's time to drop the block.
	 */
	public void checkDropTimer()
	{
		// check if something should drop
		levelTimer += delay/1000.0;
		if (levelTimer > 0.1/(player.getLevel()+1.0))
		{
			dropping = true;
		}
	}
	
	/**
	 * Drops the current block down one line.
	 */
	public void dropBlock()
	{
		// drop the block
		level.dropBlock(block);
		dropping = false;
		levelTimer = 0;			
				
		// check if this cause a collisoin
		if (level.collision(block))
		{
			// add the block to the level
			if (level.addBlockToLevel(block, false)) 
			{
				lines = level.getClearedLines();
						
				if (lines.size() > 0)
				{
					timer = 0;
					clearing = true;
				}
				else
					newBlock();
			}
			// if block couldn't be added, player is dead
			else
				dead = true;
		}
	}
	
	/**
	 * Updates the line clearing animation.
	 */
	public void clearLines()
	{
		// increment the timer
		timer += delay/1000.0;

		// check the timer			
		if (timer > 1 || !CLEARING_ANIMATION)  
		{
			level.clearLines(lines);
			clearing = false;
			newBlock();
				
			// calculate the points to add
			// scoring function = (level + 1) * 50 * lines!
			int size = lines.size();
			int points = (player.getLevel() + 1) * 50;				
			for (int i=2; i <= size; i++)
			{
				points = points*i;
			}
				
			// update the player
			player.addLines(size);
			player.addScore(points);
		}		
	}
	
	/**
	 * Checks for player input.  Checks if the block should be moved or 
	 * rotated.
	 * 
	 * @param input - the player input
	 */
	public void checkInput(String event)
	{
		if (event == null) {
			return;
		}
		
		// check for a clockwise rotation
		if (event.equals(ROTATE_CCW))
		{
			block.rotateCCW();		
		
			// if the rotation causes a collision, undo it
			if (level.collision(block)) {
				block.rotateCW();
			}
			//check if the rotation causes a collision with the wall
			else
			{
				// fixing the rotation may cause a collision, need to fix
				int x = block.getX();
				block.fixRotationWallCollisions();
	
				if (level.collision(block))
				{
					block.rotateCW();
					block.setX(x);
				}
			}
		}
		
		// check for a counter-clockwise rotation
		else if (event.equals(ROTATE_CW))
		{
			block.rotateCW();		
		
			// if the rotation causes a collision, undo it
			if (level.collision(block)) {
				block.rotateCCW();
			}
			//check if the rotation causes a collision with the wall
			else
			{
				// fixing the rotation may cause a collision, need to fix
				int x = block.getX();
				block.fixRotationWallCollisions();
	
				if (level.collision(block))
				{
					System.out.println("Collision!");
					block.rotateCCW();
					block.setX(x);
				}
			}
		}
	
		// check for a left move
		else if (event.equals(MOVE_LEFT))
		{
			block.moveLeft();
	
			if (level.collision(block)) {
				block.moveRight();
			}
		}
			
		// check for a right move
		else if (event.equals(MOVE_RIGHT))
		{
			block.moveRight();
	
			if (level.collision(block)) {
				block.moveLeft();
			}
		}		
	}
	
	/**
	 * Converts an int into a string that is center aligned.
	 * 
	 * @param value - the int value
	 * @return - the string representation of the int
	 */
	public String format(int value)
	{
		String text = "";
		int size = ("" + value).length();
		
		for (int i=0; i<(6-size); i++)
		{
			text += " ";
		}
		
		return text + value;
	}
	
	/**
	 * Determines where a block should be placed on the screen.
	 * 
	 * @param x - the x location of the block, relative to the level
	 * @return - the screen location of the block
	 */
	public int screenX(int x)
	{
		return 48 + BLOCK_SIZE*x;
	}
	
	/**
	 * Determines where a block should be placed on the screen.
	 * 
	 * @param y - the y location of the block, relative to the level
	 * @return - the screen location of the block
	 */
	public int screenY(int y)
	{
		return 10 + BLOCK_SIZE*y;
	}
	
	int NUM_BLOCKS = 7;
	
	/**
	 * Generates a new block, and sets the current block to the block that
	 * was previously being displayed. If this is the first time it has ran, 
	 * then the current block and the next block are both generated.
	 */
	public void newBlock()
	{
		if (next == null)
		{
			// get a block
			next = new Block((int)((random.nextDouble()*1000.0)%(7)));
			block = new Block((int)((random.nextDouble()*1000.0)%(7)));		
		}
		else
		{
			block = next;
			next = new Block((int)((random.nextDouble()*1000.0)%7));
		}
		
		agent.newBlock(block, next, level);
	}	
	
}