package sim.tetris;

import java.util.ArrayList;

public class Move {
		
	private ArrayList<String> moves;
	private Level level;
	private double evaluation = 0;
	private boolean evaluated = false;
	
    // 25000 1 1 20 1 1

//	public double HEIGHT_FACTOR = 1.0;
//	public double BALANCE_FACTOR = 1.0;
//	public double HOLE_FACTOR = 10.0;
//	public double BLOCK_FACTOR = 1.0;
//	public double LINE_FACTOR = 1.0;

	
	public Move(Level level, ArrayList<String> moves) {
		this.level = level;
		this.moves = moves;
	}
	
	public Level getLevel() {
		return level;
	}
	
	public double getEvaluation( double HEIGHT_FACTOR,
			 double BALANCE_FACTOR,
			 double HOLE_FACTOR,
			 double BLOCK_FACTOR,
			 double LINE_FACTOR ) {
		
		if (evaluated == false) {
			calculateEvaluation(HEIGHT_FACTOR, BALANCE_FACTOR, HOLE_FACTOR, BLOCK_FACTOR, LINE_FACTOR);
			evaluated = true;
		}
		
		return evaluation;
	}
	
	public ArrayList<String> getMoves() {
		return moves;
	}
	
	private void calculateEvaluation( double HEIGHT_FACTOR,
			 double BALANCE_FACTOR,
			 double HOLE_FACTOR,
			 double BLOCK_FACTOR,
			 double LINE_FACTOR ) {

		// clear the lines
		evaluation += level.getClearedLines().size()*LINE_FACTOR;
		level.clearLines(level.getClearedLines());
		int[][] data = level.getLevelData();	
		
		// maximum height
		boolean done = false;
		for (int y=0; y<data.length; y++) {
			for (int x=0; x<data[y].length; x++) {
				if (data[y][x] > 0) {
					evaluation += y/20.0*HEIGHT_FACTOR;
					done = true;
					break;
				}
			}
			if (done) break;
		}
		
		// balance
		for (int x=0; x<9; x++) {
			int left = 20;			
			for (int y=0; y<20; y++) {
				if (data[y][x] > 0) {
					left = y;
					break;
				}
			}
			int right = 20;			
			for (int y=0; y<20; y++) {
				if (data[y][x+1] > 0) {
					right = y;
					break;
				}
			}
			
			double diff = BALANCE_FACTOR*Math.abs(left - right);
			diff = diff*diff;
			if (x == 0) {
				if (left > right) 
					evaluation -= 2.5*diff;
				else 
					evaluation -= 0.4*diff;					
			}
			else if (x ==8) {
				if (left < right) 
					evaluation -= 2.5*diff;
				else 
					evaluation -= 0.4*diff;					
			}
			else {
				evaluation -= diff;
			}
		}
		
		// holes and blocks
		for (int x=0; x<10; x++) {
			int top = 20;
			
			for (int y=0; y<20; y++) {
				if (data[y][x] > 0) {
					top = y;
					break;
				}
			}
			
			boolean block = false;
			for (int y=(top + 1); y<20; y++) {
				if (data[y][x] == 0) {
					if (block == false) {
						for (int u=(y-1); u >= 0; u--) {
							if (data[u][x] > 0) {
								if (data[u][x] == 9) {
									evaluation -= BLOCK_FACTOR;
								}
							}
							else {
								break;
							}
						}						
						block = true;
					}

					evaluation -= HOLE_FACTOR;					
				}
			}
		}			
	}	
}
