package sim.tetris;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
/**
 * Queue input events.
 */
public class InputMap implements KeyListener
{
	public String START = "start";
	public String MOVE_LEFT = "left";
	public String MOVE_RIGHT = "right";
	public String ROTATE_CW = "rotate CW";
	public String ROTATE_CCW = "rotate CCW";
	
	/** the unput queue */
	private ArrayList<String> inputQueue = new ArrayList<String>();
	
	/**
	 * Adds a keylistener to the specified Canvas, and sets it to focusable.
	 * 
	 * @param canvas - the component used to attach a key listener
	 */
	public InputMap()
	{
//		if (canvas != null) {
//			canvas.addKeyListener(this);
//			canvas.setFocusable(true);	
//		}
	}
	
	/**
	 * Adds an event to the input queue.
	 * 
	 * @param event - the input event
	 */
	public synchronized void addInputEvent(String event) {
		inputQueue.add(event);
	}
	
	/**
	 * Gets the next input event.
	 * 
	 * @return - the event, or null if the queue is empty
	 */
	public synchronized String getInputEvent() {
		if (inputQueue.size() > 0) {
			return inputQueue.remove(0);
		}
		
		return null;
	}
	
	/**
	 * Checks for keypresses.
	 */
	public void keyPressed(KeyEvent e) 
	{				
		if (e.getKeyChar() == KeyEvent.VK_ENTER) inputQueue.add(START);
		if (e.getKeyCode() == KeyEvent.VK_LEFT) inputQueue.add(MOVE_LEFT);
		if (e.getKeyCode() == KeyEvent.VK_RIGHT) inputQueue.add(MOVE_RIGHT); 
		if (e.getKeyCode() == KeyEvent.VK_CONTROL) inputQueue.add(ROTATE_CW);
		if (e.getKeyCode() == KeyEvent.VK_SPACE) inputQueue.add(ROTATE_CCW);
		
//		if (e.getKeyCode() == KeyEvent.VK_SHIFT) Game.ULTRA_MODE = !Game.ULTRA_MODE;
		
		// check the escape button, quit if pressed
		if (e.getKeyChar() == KeyEvent.VK_ESCAPE)
			System.exit(0);	
	}

	/**
	 * Not implemented.
	 */
	public void keyReleased(KeyEvent e) {}

    /**
     * Not implemented.
     */
	public void keyTyped(KeyEvent e) {}
}