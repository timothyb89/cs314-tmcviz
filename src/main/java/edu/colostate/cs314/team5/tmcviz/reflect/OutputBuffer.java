package edu.colostate.cs314.team5.tmcviz.reflect;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author tim
 */
public class OutputBuffer implements OutputListener {
	
	private final List<String> stdout;
	private final List<String> stderr;
	private final List<String> combined;
	
	public OutputBuffer() {
		stdout = Collections.synchronizedList(new LinkedList<String>());
		stderr = Collections.synchronizedList(new LinkedList<String>());
		combined = Collections.synchronizedList(new LinkedList<String>());
	}

	@Override
	public void stdoutMessage(String line) {
		stdout.add(line);
		combined.add(line);
	}

	@Override
	public void stderrMessage(String line) {
		stderr.add(line);
		combined.add(line);
	}

	public List<String> getStdout() {
		return stdout;
	}

	public List<String> getStderr() {
		return stderr;
	}

	public List<String> getCombined() {
		return combined;
	}
	
}
