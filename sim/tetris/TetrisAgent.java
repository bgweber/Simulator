package sim.tetris;

/**
 * Interface for informing an agent of block events. 
 */
public interface TetrisAgent {	

	/**
	 * New Tetris block.
	 * 
	 * @param block - the new block
	 * @param next - the next block
	 * @param level - the current level
	 */
	public void newBlock(Block block, Block next, Level level);	

	/**
	 * Tetris block dropped.
	 * 
	 * @param block - dropped block
	 */	
	public void blockDropped(Block block);
}
