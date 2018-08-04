package sim.tetris;

/**
 * A player represents somebody playing the game.  They have a score, are on 
 * a certain level, and have a certain number of lines cleared.
 */
public class Player
{
	/** the player's score */
	private int score;
	
	/** the players level */
	private int level;
	
	/** the player's number of lines cleared */
	private int lines;		

    /**
     * Creates a player with level = 0, lines = 0, and score = 0.
     */		
	public Player()
	{
		score = 0;
		level = 0;
		lines = 0;
	}	
	
    /**
     * Sets the level of the player.
     * 
     * @param l - the level to set
     */
	public void setLevel(int l) 
	{
		level = l;
	}

	/**
	 * Gets the level of a player.
	 * 
	 * @return - the player's level
	 */
	public int getLevel()
	{
		return level;
	}

	/**
	 * Gets the lines cleared of a player.
	 * 
	 * @return - the number of lines cleared
	 */
	public int getLines()
	{
		return lines;
	}

	/**
	 * Gets the score of a player.
	 * 
	 * @return - the player's score
	 */
	public int getScore()
	{
		return score;
	}	

    /**
     * Increments the level of a player.
     */
	public void levelUp() 
	{
		level++;
	}
   
    /**
     * Adds more lines to a player's cleared lines, and checks to see if the 
     * player should be sent to the next level.
     * 
     * @param l - the number of lines to add
     */	
	public void addLines(int l)
	{
		lines += l;
		
		// check if a level up is necessary
		if ((lines/10) > level) 
		{
			levelUp();
		}
	}
	
	/**
	 * Adds points to the player's score.
	 * 
	 * @param s - the score to add to the player's score
	 */
	public void addScore(int s) 
	{
		score += s;
	}
}