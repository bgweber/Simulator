package sim.tetris;

/**
 * Starts a game of tetris.  This class creates a JFrame and a drawing canvas.
 * A game object is created and the frame is displayed.
 */
public class Tetris
{	
	/**
	 * Starts a game of Tetris.
	 */
	public static void main(String[] args)
	{
		Game game = new Game(1);
		int levels = game.runSimulation();
		
		double heightFactor = game.getHeightFactor();
		double balanceFactor = game.getBalanceFactor();
		double holeFactor = game.getHoleFactor();
		double blockFactor = game.getBlockFactor();
		double lineFactor = game.getLineFactor();
		
		System.out.println(levels);
		System.out.println(heightFactor + " " + balanceFactor + " " + holeFactor + " " + blockFactor + " " + lineFactor);

	}
}