package edu.colostate.cs314.team5.tmcviz.sim;

import java.io.Serializable;
import lombok.Data;
import lombok.ToString;

/**
 *
 * @author tim
 */
@Data
public class Station implements Serializable {
	
	private final String id;
	
	@Override
	public String toString() {
		return id;
	}
	
}
