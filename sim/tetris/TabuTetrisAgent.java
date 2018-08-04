package sim.tetris;

import java.util.ArrayList;
/**
 * Random tetris agent.
 */
public class TabuTetrisAgent implements TetrisAgent {
	
	public String START = "start";
	public String MOVE_LEFT = "left";
	public String MOVE_RIGHT = "right";
	public String ROTATE_CW = "rotate CW";
	public String ROTATE_CCW = "rotate CCW";
	
	InputMap inputMap;
	
	public double HEIGHT_FACTOR = 1.0 + Math.random()*0.1;
	public double BALANCE_FACTOR = 1.0 + Math.random()*0.1;
	public double HOLE_FACTOR = 12.0 + Math.random()*0.1;
	public double BLOCK_FACTOR = 1.0 + Math.random()*0.1;
	public double LINE_FACTOR = 1.0 + Math.random()*0.1;
	
	/**
	 *
	 */
	public TabuTetrisAgent(InputMap inputMap) {
		this.inputMap = inputMap;
	}

	/**
	 * Called when a new begins falling.
	 */
	public void newBlock(Block block, Block next, Level level) {

		// Move manager generates moves
		ArrayList<Move> moves = createAllMoves(level, block, next);

		// Object Function evaluates moves
		evaluateMoves(moves);
		
		// Best non-tabu move is picked
		Move move = getBestMove(moves);
		
		// Move operates on the current solution
		applyMove(move);
	}
	
	/**
	 * Generates all moves consisting of rotating a block then moving it. This
	 * method does not create moves that translate the block once it has begun 
	 * dropping.
	 * 
	 * @param l - the Tetris level status
	 * @param b - the block being dropped
	 * @return - a list of all possible moves
	 */
	public ArrayList<Move> createAllMoves(Level l, Block b, Block n) {		
        ArrayList<Move> moves = new ArrayList<Move>();
		
		// save the initial positions
		Level level = l.copy();
		Block block = b.copy();
		int sx = b.getX();
		int sy = b.getY();
		
		// generate every possible move
		for (int i=-5; i<=5; i++) {
			for (int r=0; r<4; r++) {				
				ArrayList<String> moveList = new ArrayList<String>();
				block.setX(sx);
				block.setY(sy);
								
				// rotate the block
				if (r!=0 || i!=-5) {
					block.rotateCW();
				}								
				for (int rotate=0; rotate<r; rotate++) {					
					moveList.add(ROTATE_CW);
				}
				
				// position the block
				for (int move=0; move<Math.abs(i); move++) {
					if (i < 0) {
						level.moveBlockLeft(block);
						moveList.add(MOVE_LEFT);
					}
					else {
						level.moveBlockRight(block);
						moveList.add(MOVE_RIGHT);
					}
				}
								
				// move the block down until it collides with the level
				while (level.collision(block) == false) {
					block.drop();
				}
		
				// create the move
				Level newLevel = level.copy();
				if (newLevel.addBlockToLevel(block, true) == false) {
					continue;
				}
				moves.add(new Move(newLevel, moveList));
			}
		}		
		
		return moves;
	}

	/**
	 * Evaluates the object function of the given moves.
	 * 
	 * @param moves - the moves to evaluate
	 */
	public void evaluateMoves(ArrayList<Move> moves) {
		for (Move move : moves) {
			move.getEvaluation(HEIGHT_FACTOR, BALANCE_FACTOR, HOLE_FACTOR, BLOCK_FACTOR, LINE_FACTOR);
		}
	}
	
	/**
	 * Returns the best non-tabu move.  If no move is possible, then an empty
	 * move is returned.
	 * 
	 * @param moves - the possible moves
	 * @return - the best non-tabu move
	 */
	public Move getBestMove(ArrayList<Move> moves) {
		Move best = null;
		double fitness = -1 * Double.MAX_VALUE;
				
		// find the best non tabu move
		for (Move move : moves) {
			double evaluation = move.getEvaluation(HEIGHT_FACTOR, BALANCE_FACTOR, HOLE_FACTOR, BLOCK_FACTOR, LINE_FACTOR);			
			if (evaluation >= fitness) {
				best = move;
				fitness = evaluation;
			}
		}		
		
		// if no move is possible, create an empty move
		if (best == null) {
			best = new Move(null, new ArrayList<String>());
		}
		
		return best;
	}
	
	/**
	 * Applies the specified move to the solution.
	 * 
	 * @param move - the move to apply to the solution
	 */
	public void applyMove(Move move) {
		
		// apply the best move
		for (String input : move.getMoves()) {
			inputMap.addInputEvent(input);			
//			Game.input.addInputEvent(input);			
		}
	}
	
	/**
	 * Called when a block drops.
	 */
	public void blockDropped(Block block) {
	}
}
