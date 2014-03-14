package edu.colostate.cs314.team5.tmcviz.sim;

import java.io.Serializable;
import java.util.regex.Pattern;
import lombok.Data;
import lombok.ToString;

/**
 *
 * @author tim
 */
@Data
public class Station implements Serializable, TrainContainer {
	
	public static final Pattern PATTERN = Pattern.compile("\\b([A-Z]{4})\\b");
	
	private final String id;
	
	@Override
	public String toString() {
		return id;
	}
	
}
