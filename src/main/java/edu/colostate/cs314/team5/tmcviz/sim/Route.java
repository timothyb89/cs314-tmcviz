package edu.colostate.cs314.team5.tmcviz.sim;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import lombok.Data;
import lombok.ToString;

/**
 *
 * @author tim
 */
@Data
@ToString
public class Route implements Serializable {
	
	public static final Pattern PATTERN = Pattern.compile("\\b\\d{4}\\b");
	
	private final String id;
	private final Station start;
	private final Station end;
	private final int segmentCount;
	
	private List<Segment> segments;
	
	public Route(String id, Station start, Station end, int segmentCount) {
		this.id = id;
		this.start = start;
		this.end = end;
		this.segmentCount = segmentCount;
		
		segments = new ArrayList<>();
		for (int i = 0; i < segmentCount; i++) {
			segments.add(new Segment(this, i));
		}
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
