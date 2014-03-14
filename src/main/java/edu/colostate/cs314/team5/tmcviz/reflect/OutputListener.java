package edu.colostate.cs314.team5.tmcviz.reflect;

/**
 *
 * @author tim
 */
public interface OutputListener {
	
	public void stdoutMessage(String line);
	public void stderrMessage(String line);
	
}
