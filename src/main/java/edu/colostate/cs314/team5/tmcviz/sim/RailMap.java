package edu.colostate.cs314.team5.tmcviz.sim;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.ToString;

/**
 *
 * @author tim
 */
@ToString
public class RailMap implements Serializable {
	
	private String id;
	private List<Station> stations;
	private List<Route> routes;
	
	public RailMap(String id) {
		this.id = id;
		
		stations = new ArrayList<>();
		routes = new ArrayList<>();
	}

	public List<Station> getStations() {
		return stations;
	}
	
	public Station getStation(String id) {
		for (Station s : stations) {
			if (s.getId().equalsIgnoreCase(id)) {
				return s;
			}
		}
		
		return null;
	}
	
	public Station getOrCreateStation(String id) {
		for (Station s : stations) {
			if (s.getId().equalsIgnoreCase(id)) {
				return s;
			}
		}
		
		Station ret = new Station(id);
		stations.add(ret);
		return ret;
	}

	public List<Route> getRoutes() {
		return routes;
	}
	
	public void addRoute(Route route) {
		routes.add(route);
	}
	
	public void addRoute(String id, Station start, Station end, int segments) {
		routes.add(new Route(id, start, end, segments));
	}
	
	public static RailMap parse(String map) {
		map = map.trim();
		
		Pattern p = Pattern.compile("^(\\w+)\\[(.*)\\]$");
		Matcher m = p.matcher(map);
		
		if (!m.matches()) {
			throw new IllegalArgumentException("Invalid map: " + map);
		}
		
		RailMap ret = new RailMap(m.group(1));
		
		String routes = m.group(2).trim();
		
		p = Pattern.compile("\\{"
				+ "\\s*(\\d{4})\\s*\\:"
				+ "\\s*(\\w{4})\\s*\\;"
				+ "\\s*(\\w{4})\\s*\\:"
				+ "\\s*(\\d+)\\s*\\}");
		m = p.matcher(routes);
		
		while (m.find()) {
			String id = m.group(1);
			Station start = ret.getOrCreateStation(m.group(2));
			Station end = ret.getOrCreateStation(m.group(3));
			int segments = Integer.parseInt(m.group(4));
			
			ret.addRoute(id, start, end, segments);
		}
		
		return ret;
	}
	
}
