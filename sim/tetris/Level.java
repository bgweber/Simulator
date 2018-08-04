package sim.tetris;

import java.util.ArrayList;
/**
 * A level is a 10 wide by 20 tall grid which is composed of tiles.  A level
 * starts empty, but is filled up by blocks placed in it.
 */
public class Level
{
    /** the level height */
	public final int LEVEL_HEIGHT = 20;

	/** the level width */
	public final int LEVEL_WIDTH = 10;

    /** the left bounds of the level, inclusive */
	public final int LEFT_BOUNDS = 0;

	/** the right bounds of the level, inclusive */
	public final int RIGHT_BOUNDS = LEVEL_WIDTH-1;

	/** the y starting position of a block */
	public final int START_Y = -2;	
	
	/** the tiles that make up the level */
	private int[][] level;	
	
	/**
	 * Creates an empty level.
	 */
	public Level()
	{
		level = new int[LEVEL_HEIGHT][LEVEL_WIDTH];
		clearLevel();
	}
	
	/**
	 * Gets the level data.
	 * 
	 * @return - the level data about the tiles
	 */
	public int[][] getLevelData()
	{
		return level;
	}
	
	/**
	 * Clears the level, setting all of the tiles to be blank.
	 */
	public void clearLevel()
	{
		for (int i=0; i<LEVEL_HEIGHT; i++) 
		{
			for (int j=0; j<LEVEL_WIDTH; j++) 
			{
				level[i][j] = 0;
			}
		}		
	}
	
	/**
	 * Moves the block one space to the left.
	 * 
	 * @param block - the block to move
	 */
	public void moveBlockLeft(Block block)
	{	
		block.moveLeft();
	}
	
	/**
	 * Moves the block one space to the right.
	 * 
	 * @param block - the block to move
	 */
	public void moveBlockRight(Block block)
	{	
		block.moveRight();
	}
	
	/**
	 * Moves the block down one space.
	 */
	public void dropBlock(Block block)
	{
		block.drop();		
	}
	
	/**
	 * Determines if a block is colliding with this level.
	 * 
	 * @param block - the block to check for collision
	 * @return - true if there is a collision, otherwise false
	 */
	public boolean collision(Block block) 
	{
		// get the position of the block
		int x = block.getX();
		int y = block.getY();
		int size = block.getBlockSize();
	
		for (int i=0; i<size; i++) 
		{
			for (int j=0; j<size; j++) 
			{
				// get the tile location relative to the level
				int cx = x + j;
				int cy = y + i;
				
				// check the bounds of the tile
				// check for a collision if this tile exist.
				if (block.getBlockData()[i][j] == 1)
				{
 					if (cx < LEVEL_WIDTH && cy < LEVEL_HEIGHT)				
						if (cx >= 0 && cy >= 0 && level[cy][cx] > 0) 
							return true;
							
					// check if the block is at the bottom of the level
					if (cy >= LEVEL_HEIGHT)
						return true;
				}	
			}
		}

		// no collision
		return false;		
	}
	
	/**
	 * Adds the block to the level.  It assumes the block was just dropped, and
	 * then collided with the level.
	 * 
	 * @param block - the block to add
	 * @return - true if placing this block causes the player to die, otherwise
	 *           false
	 */
	public boolean addBlockToLevel(Block block, boolean color)
	{
		// get the position of the block
		int x = block.getX();
		int y = block.getY() - 1;
		int size = block.getBlockSize();
	
		for (int i=0; i<size; i++) 
		{
			for (int j=0; j<size; j++) 
			{
				if (block.getBlockData()[i][j] == 1)
				{
					// get the tile location relative to the level
					int cx = x + j;
					int cy = y + i;
					
					if (cy >= 0)
					{
						if (color == false) {
							level[cy][cx] = block.getType() + 1;
						}
						else {
							level[cy][cx] = 9;							
						}
					}
					else 
						return false;
				}
			}
		}
		
		// block added to level
		return true;
	}
	
	/**
	 * Finds the lines that are completed and need to be cleared.
	 * 
	 * @return - an arraylist containing intergers of the lines to be cleared
	 */
	public ArrayList getClearedLines()
	{
		ArrayList<Integer> lines = new ArrayList<Integer>();
		
		for (int i=0; i<LEVEL_HEIGHT; i++)
		{
			int count = 0;
			
			for (int j=0; j<LEVEL_WIDTH; j++)
			{
				if (level[i][j] > 0)
					count++;
			}
			
			// check if the level should be added to the clear list
			if (count== LEVEL_WIDTH)
			{
				lines.add(new Integer(i));
			}
		}
		
		return lines;
	}
	
	/**
	 * Clears the specified lines from the level.
	 * 
	 * @param lines - an arraylist of intergers containing the Integers of the
	 *                line numbers to clear
	 */
	public void clearLines(ArrayList lines)
	{
		// iterate through the lines that need to be cleared
		for (int i=0; i<lines.size(); i++)
		{
			int line = ((Integer)lines.get(i)).intValue();

			for (int j=line; j>0; j--)
			{
				level[j] = level[j-1];			
			}
			
			// clear the top line
			level[0] = new int[10];			
		}		
	}
	
	public Level copy() {
		Level l = new Level();
		for (int y=0; y<level.length; y++) {
			for (int x=0; x<level[y].length; x++) {				
				l.level[y][x] = level[y][x];
			}			
		}
		return l;
	}
	
	public void print() {
		for (int y=0; y<20; y++) {
			for (int x=0; x<10; x++) {
				System.out.print(level[y][x] + " ");
			}
			System.out.println();
		}
		System.out.println();		

	}
}