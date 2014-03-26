package edu.colostate.cs314.team5.tmcviz.sim;

import java.io.Serializable;
import java.util.regex.Pattern;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author tim
 */
@Data
public class Segment implements Serializable, TrainContainer {

	public static final Pattern PATTERN = Pattern.compile("\\b(\\d{4})\\.(\\d)\\b");
	
	private final Route parent;
	private final int index;
	
	@Getter @Setter
	private LightStatus status = LightStatus.GREEN;
	
	@Override
	public String getId() {
		return toString();
	}
	
	@Override
	public String toString() {
		return String.format("%s.%d", parent.getId(), (index + 1));
	}
	
	public String getStyle() {
		String shape = parent.isClosed() ? "square" : "ellipse";
		
		String fillColor = "lightgreen";
		if (status == LightStatus.RED) {
			fillColor = "red";
		}
		
		return String.format("shape=%s;fillColor=%s", shape, fillColor);
	}
	
	public enum LightStatus {
		RED, GREEN;
	}
	
}
