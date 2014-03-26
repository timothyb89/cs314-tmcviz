package edu.colostate.cs314.team5.tmcviz.sim;

import java.io.Serializable;
import java.util.regex.Pattern;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
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

	@Getter @Setter private boolean stopped;
	
	public Train(String id) {
		this.id = id;
	}
	
	public Train(String id, TrainContainer location) {
		this.id = id;
		this.location = location;
	}

	public Train(String id, TrainContainer location, boolean stopped) {
		this.id = id;
		this.location = location;
		this.stopped = stopped;
	}
	
	public String getStyle() {
		String shape = "ellipse";
		String color = stopped ? "red" : "yellow";
		
		return String.format("shape=%s;fillColor=%s", shape, color);
	}
	
	@Override
	public String toString() {
		return id;
	}
	
}
