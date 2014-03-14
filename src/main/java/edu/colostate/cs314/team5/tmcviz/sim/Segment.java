package edu.colostate.cs314.team5.tmcviz.sim;

import java.io.Serializable;
import lombok.Data;
import lombok.ToString;

/**
 *
 * @author tim
 */
@Data
public class Segment implements Serializable {

	private final Route parent;
	private final int index;
	
	@Override
	public String toString() {
		return String.format("%s.%d", parent.getId(), (index + 1));
	}
	
}
