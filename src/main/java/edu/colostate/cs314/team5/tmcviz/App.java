package edu.colostate.cs314.team5.tmcviz;

import edu.colostate.cs314.team5.tmcviz.sim.RailMap;

public class App {

	public static void main(String[] args) {
		RailMap map = RailMap.parse(
				"FoCoSys["
						+ "{0000:FTCL;LGMT:4}"
						+ "{0001:LGMT;FTCL:3}"
						+ "{0002:LGMT;BOLD:2}"
						+ "{0005:BOLD;FTCL:5}"
						+ "]");
		
		System.out.println(map);
	}
}
