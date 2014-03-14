package edu.colostate.cs314.team5.tmcviz.sim;

import java.util.regex.Pattern;

/**
 *
 * @author tim
 */
public interface TrainContainer {
	
	public static final Pattern COMBINED_PATTERN = Pattern.compile(
			"\\b([A-Z]{4}|\\d{4}\\.\\d+|\\d{3})\\b"
	);
	
}
