package edu.colostate.cs314.team5.tmcviz.sim;

import java.util.regex.Pattern;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 *
 * @author tim
 */
@Data
@ToString
public class Train {
	
	public static final Pattern PATTERN = Pattern.compile("\\b\\d{3}\\b");
	
	private final String id;
	private TrainContainer location;

	public Train(String id) {
		this.id = id;
	}
	
	public Train(String id, TrainContainer location) {
		this.id = id;
		this.location = location;
	}
	
}
