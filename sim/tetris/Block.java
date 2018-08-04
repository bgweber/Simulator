package sim.tetris;

/**
 * A block represents one of the seven different types of blocks from tetris.
 * There are two types of groups of blocks.  Some blocks can only be rotated
 * between two different states, while the rest can be rotated between 4 
 * different states.  A block is simply a 2D array where a 0 represents empty
 * space, and a 1 represents a tile.  Blocks can be moved and rotated, but the
 * collision between a level and a block should be checked before changing the
 * block.
 */
public class Block
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
	
    /** constant of the line shape */
	public final int SHAPE_LINE = 0;
    public final int[][] LINE = {{0,0,0,0},
    	                                {0,0,0,0},
                                        {1,1,1,1},
                                        {0,0,0,0}};

	/** constant of the S shape */
	public final int SHAPE_S = 1;
	public final int[][] S = {{0,1,1}, 
									 {1,1,0},
									 {0,0,0}};

	/** constant of the Z shape */
	public final int SHAPE_Z = 2;
	public final int[][] Z = {{1,1,0}, 
									 {0,1,1},
									 {0,0,0}};
									 
    /** constant of the square shape */
	public final int SHAPE_SQUARE = 3;
	public final int[][] SQUARE = {{1,1}, 
										  {1,1}};

	/** constant of the L shape */
	public final int SHAPE_L = 4;
	public final int[][] L = {{0,0,1}, 
									 {1,1,1},
									 {0,0,0}};

	/** constant of the J shape */
	public final int SHAPE_J = 5;
	public final int[][] J = {{1,0,0}, 
									 {1,1,1},
									 {0,0,0}};


	/** constant of the T shape */
	public final int SHAPE_T = 6;
	public final int[][] T = {{0,1,0}, 
								     {1,1,1},
									 {0,0,0}};

    /** the different shapes */									 
	public Object[] SHAPES = {LINE, S, Z, SQUARE, L, J, T};
	
	/** the x starting position of the blocks */
	private final int[] START_OFFSET = {3, 4, 3, 4, 3, 4, 3 };
	
	/** the number of different blocks */
	public final int NUM_BLOCKS = SHAPES.length;
	
	/** the x location of the block */
	private int x;

	/** the y location of the block */
	private int y;
	
	/** the type of block */
	private int type;

	/** the size of this block, a block must have the same width and hieght */
	private int blockSize;	
	
	/** keeps track if the peice has been rotated, only important for the
	 *  first three types of pieces, because they alternate between 2 states,
	 *  the rest of the pieces have four different rotation possibiliies.
	 */
	private boolean rotated;

    /** 
     * The tiles that make up this block.
     * 
     *  <p>The block is made up of tiles that are specified by the 2D array.
     *  If the value of an element in the array is 1, then that tile exist,
     *  and is part of this block. 
     */
	private int[][] block;
	
	/**
	 * Creates a new block at the top of the level.
	 * 
	 * @param blockType - the type of block to create, specified by one of the
	 *                    shape constants
	 */
	public Block(int blockType)
	{
		// get the initial position of the block
		x = START_OFFSET[blockType];
		y = START_Y;	 
        rotated = false;

        // set the block type
		type = blockType;
        block = (int[][])SHAPES[type];
        blockSize = block[0].length;
	}	
	
	/**
	 * Gets the tile data from this block.  If the value is 0, then the tile
	 * is empty.  If the value is 1, then that tile exists.
	 * 
	 * @return - the data describing the location of the tiles that form this
	 *           block
	 */
	public int[][] getBlockData()
	{
		return block;
	}
	
	/**
	 * Sets the position of the block.
	 * 
	 * @param newX - the x position of the block
	 */
	public void setX(int newX)
	{
		x = newX;
	}

	/**
	 * Sets the position of the block.
	 * 
	 * @param newY - the y position of the block
	 */
	public void setY(int newY)
	{
		y = newY;
	}	
	
	/**
	 * Gets the x position of the block.
	 * 
	 * @return - x position
	 */
	public int getX()
	{
		return x;	
	}

	/**
	 * Gets the y position of the block.
	 * 
	 * @return - y position
	 */
	public int getY()
	{
		return y;	
	}
	
	/**
	 * Gets the type of block.
	 * 
	 * @return - the block type
	 */
	public int getType()
	{
		return type;
	}
	
	/**
	 * Gets the size of this block.
	 * 
	 * @return - the dimensions of the box
	 */
	public int getBlockSize()
	{
		return blockSize;
	}
	
	/**
	 * Moves the block down by one space.
	 */
	public void drop() 
	{
		y++;
	}
	
	/**
	 * Moves the block one space left, if the piece is still within the 
	 * level bounds.
	 */
	public void moveLeft() 
	{
	   if ((x + leftBounds()) > LEFT_BOUNDS)
	   		x -= 1;	
	}
	
	/**
	 * Moves the block one space right, if the piece is still within the 
	 * level bounds.
	 */
	public void moveRight()
	{
		if ((x + rightBounds()) < RIGHT_BOUNDS)
			 x += 1;			
	}
	
	/**
	 * Rotates the block 90 degrees counter-clockwise.
	 */
	public void rotateCW() 
	{
		// check if this is a two state rotation
		if (rotated == true && type < SHAPE_SQUARE) 
		{
		   rotateCCW();
		   return;
		}
		
		// stores the rotated block
		int[][] rotatedBlock = new int[blockSize][blockSize];
		
		// translate each part of the block to its new location
		for (int i=0; i<blockSize; i++)
		{
			int tx = (blockSize - 1) - i;			
			for (int j=0; j<blockSize; j++)
			{
				int ty = j;
				rotatedBlock[ty][tx] = block[i][j];
			}
		}			
		
		// store the result
		rotated = true;
		block = rotatedBlock;
	}
	
	/**
	 * Rotates the block 90 degrees clockwise
	 */
	public void rotateCCW() 
	{
		// check if this is a two state rotation
		if (rotated == false && type < SHAPE_SQUARE)
		{
		   rotateCW();
		   return;
		}
			   
		// stores the rotated block
		int[][] rotatedBlock = new int[blockSize][blockSize];

		for (int i=0; i<blockSize; i++)
		{
			int tx = i;			
			for (int j=0; j<blockSize; j++)
			{
				int ty = (blockSize - 1) - j;
				rotatedBlock[ty][tx] = block[i][j];
			}
		}			
			
		// store the result
		rotated = false;
		block = rotatedBlock;	
	}

    /**
     * Fixes the placement of the block, which may have become incorrect after
     * rotating the block.  The block might now be colliding with the wall, and
     * should be adjusted.
     */
	public void fixRotationWallCollisions() 
	{
		// the bounds
		int left = leftBounds();
		int right = rightBounds();
		
		// move the block to the right if necessary, rotation could cause a
		// maximum offset of two spaces
		if ((x + left) < LEFT_BOUNDS)
		  	x++;
		if ((x + left) < LEFT_BOUNDS)
			x++;

        // move the block to the left if necessary	
		if ((x + right) > RIGHT_BOUNDS) 
		 	x--;				   
		if ((x + right) > RIGHT_BOUNDS) 
			x--;				   
	}

    /**
     * Finds the left bounds of the block.
     * 
     * @return - the leftmost point of the block
     */	
	private int leftBounds() 
	{
		for (int i=0; i<blockSize; i++) 
		{
			for (int j=0; j<blockSize; j++) 
			{
				if (block[j][i] == 1) {
					return i;
				}
			}			
		}		

        // no point was found		
		return 0;
	}
	
	/**
	 * Finds the right bounds of the block.
	 * 
	 * @return - the righttmost point of the block
	 */	
	private int rightBounds()
	{
 		for (int i=blockSize-1; i>=0; i--) 
		{
			for (int j=0; j<blockSize; j++) 
			{
				if (block[j][i] == 1) {
					return i;
				}
			}			
		}
		
		// no point was found		
		return 0;
	}
	
	public Block copy() {
		Block b = new Block(type);
		b.x = x;
		b.y = y;
		b.rotated = rotated;
		for (int y=0; y<block.length; y++) {
			for (int x=0; x<block[y].length; x++) {				
				b.block[y][x] = block[y][x];
			}			
		}
		return b;
	}
}
