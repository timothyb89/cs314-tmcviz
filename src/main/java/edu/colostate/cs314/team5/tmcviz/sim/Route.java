package edu.colostate.cs314.team5.tmcviz.sim;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author tim
 */
@ToString
public class Route implements Serializable {
	
	public static final Pattern PATTERN = Pattern.compile("\\b\\d{4}\\b");
	
	@Getter private final String id;
	@Getter private final Station start;
	@Getter private final Station end;
	@Getter private final int segmentCount;
	
	@Getter private final List<Segment> segments;
	
	@Getter @Setter private boolean closed;
	
	public Route(String id, Station start, Station end, int segmentCount) {
		this.id = id;
		this.start = start;
		this.end = end;
		this.segmentCount = segmentCount;
		
		segments = new ArrayList<>();
		for (int i = 0; i < segmentCount; i++) {
			segments.add(new Segment(this, i));
		}
		
		closed = false;
	}
	
	public Segment getSegment(int index) {
		for (Segment s : segments) {
			if (s.getIndex() == index) {
				return s;
			}
		}
		
		return null;
	}
	
	public Segment getFirstSegment() {
		return getSegment(0);
	}
	
	public Segment getLastSegment() {
		return getSegment(segmentCount - 1);
	}
	
}
