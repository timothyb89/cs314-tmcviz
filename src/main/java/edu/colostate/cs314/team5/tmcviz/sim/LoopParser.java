package edu.colostate.cs314.team5.tmcviz.sim;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author tim
 */
public class LoopParser {
	
	private RailMap map;
	
	private RailMap working;
	
	private String currentLine;
	private List<String> result;
	
	private Map<Integer, RailMap> loops;
	
	public LoopParser(RailMap map) {
		this.map = map;
		
		loops = new HashMap<>();
	}
	
	public RailMap parse(String[] lines) {
		working = map.copy();
		
		for (String line : lines) {
			currentLine = line;
			
			if (findAll(pi("loop"), pi("\\d+"))) {
				working = working.copy();
				working.setLoop(Integer.parseInt(result.get(1)));
				loops.put(working.getLoop(), working);
			} else if (findAll(pi("light"), Segment.PATTERN, pi("(red|green)"))) {
				working.setLight(result.get(1), result.get(2));
			} else if (findAll(
					pi("train"), Train.PATTERN, pi("moved"),
					TrainContainer.COMBINED_PATTERN, pi("to"),
					TrainContainer.COMBINED_PATTERN)) {
				// train 123 ... moved ... 1234 ... to ... 1234
				System.out.printf("train %s moved from %s to %s\n",
						result.get(1), result.get(3), result.get(5));
			}
		}
		
		return working;
	}
	
	public RailMap parseLoop(String text) {
		return parse(StringUtils.split(text, "\n"));
	}
	
	private List<String> findAll(String text, Pattern... patterns) {
		List<String> ret = new ArrayList<>();
		
		//System.out.println("text:    " + text);
		
		Matcher m = null;
		for (Pattern p : patterns) {
			//System.out.print("\tpattern: " + p.pattern());
			
			// enforce ordering: subsequent find calls won't have their position
			// reset
			if (m == null) {
				m = p.matcher(text);
			} else {
				m.usePattern(p);
			}
			
			if (m.find()) {
				//System.out.println(" - match: " + m.group());
				ret.add(m.group());
			} else {
				//System.out.println(" - no match");
				return null;
			}
		}
		
		return ret;
	}
	
	private boolean findAll(Pattern... patterns) {
		result = findAll(currentLine, patterns);
		
		return result != null;
	}
	
	private Pattern p(String text) {
		return Pattern.compile(text);
	}
	
	private Pattern pi(String text) {
		return Pattern.compile(text, Pattern.CASE_INSENSITIVE);
	}
	
	public static void main(String[] args) {
		RailMap map = RailMap.parse(
				"FoCoSys["
						+ "{0000:FTCL;LGMT:4}"
						+ "{0001:LGMT;FTCL:3}"
						+ "{0002:LGMT;BOLD:2}"
						+ "{0005:BOLD;FTCL:5}"
						+ "]");
		LoopParser p = new LoopParser(map);
		
		RailMap newMap = p.parse(new String[] {
			"The light in 0000.3 has changed to red",
			"Train 300 was moved from 0002.2 to BOLD",
			"Train 200 was moved from LGMT to 0002.1",
			"Train 211 was moved from 0005.1 to 0005.2",
		});
		
		System.out.println(map.getSegment("0000.3").getStatus());
		System.out.println(newMap.getSegment("0000.3").getStatus());
	}
	
}
