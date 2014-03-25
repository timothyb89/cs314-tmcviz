package edu.colostate.cs314.team5.tmcviz.sim;

import java.io.Serializable;
import java.util.regex.Pattern;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 *
 * @author tim
 */
@Data
public class Train implements Serializable {
	
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
	
	public String getStyle() {
		// todo: stopped flag
		return "shape=ellipse;fillColor=yellow";
	}
	
	@Override
	public String toString() {
		return id;
	}
	
}
