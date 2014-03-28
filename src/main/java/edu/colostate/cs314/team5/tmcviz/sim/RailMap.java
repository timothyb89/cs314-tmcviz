package edu.colostate.cs314.team5.tmcviz.sim;

import edu.colostate.cs314.team5.tmcviz.sim.Segment.LightStatus;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author tim
 */
@Slf4j
@ToString
public class RailMap implements Serializable {
	
	private String id;
	
	@Getter @Setter
	private int loop;
	
	private List<Station> stations;
	private List<Route> routes;
	private List<Train> trains;
	
	public RailMap(String id) {
		this.id = id;
		
		stations = new ArrayList<>();
		routes = new ArrayList<>();
		trains = new ArrayList<>();
	}
	
	public RailMap(String id, int loop) {
		this(id);
		
		this.loop = loop;
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
	
	public Route getRoute(String id) {
		for (Route r : routes) {
			if (r.getId().equalsIgnoreCase(id)) {
				return r;
			}
		}
		
		return null;
	}
	
	public Segment getSegment(String routeId, int index) {
		Route r = getRoute(routeId);
		if (r == null) {
			return null;
		}
		
		return r.getSegment(index);
	}
	
	public Segment getSegment(String segmentId) {
		Matcher m = Segment.PATTERN.matcher(segmentId);
		if (m.matches()) {
			return getSegment(m.group(1), Integer.parseInt(m.group(2)) - 1);
		} else {
			log.warn("Invalid segment id: " + segmentId);
			return null;
		}
	}
	
	public void addRoute(Route route) {
		routes.add(route);
	}
	
	public void addRoute(String id, Station start, Station end, int segments) {
		routes.add(new Route(id, start, end, segments));
	}
	
	public void removeRoute(Route route) {
		routes.remove(route);
	}
	
	public void removeRoute(String routeId) {
		Route r = getRoute(routeId);
		if (r == null) {
			log.warn("Unable to remove route; not found: {}", routeId);
			return;
		}
		
		removeRoute(r);
	}

	public List<Train> getTrains() {
		return trains;
	}
	
	public Train getTrain(String id) {
		for (Train train : trains) {
			if (train.getId().equalsIgnoreCase(id)) {
				return train;
			}
		}
		
		return null;
	}
	
	public Train getOrCreateTrain(String id) {
		Train t = getTrain(id);
		
		if (t == null) {
			t = new Train(id);
			addTrain(t);
		}
		
		return t;
	}
	
	public void addTrain(Train train) {
		trains.add(train);
	}
	
	public void addTrain(String trainId) {
		addTrain(new Train(trainId));
	}
	
	public void addTrain(String trainId, TrainContainer location) {
		addTrain(new Train(trainId, location));
	}
	
	public void addTrain(String trainId, TrainContainer location, boolean stopped) {
		addTrain(new Train(trainId, location, stopped));
	}
	
	public void moveTrain(String trainId, String containerId) {
		Train train = getOrCreateTrain(trainId);
		TrainContainer c = getContainer(containerId);
		
		if (train == null || c == null) {
			log.warn("Unknown train or container: "
					+ trainId + " / " + containerId);
		} else {
			train.setLocation(c);
		}
	}
	
	public void stopTrain(String trainId) {
		Train train = getTrain(trainId);
		if (train == null) {
			log.warn("Unable to stop train; unknown id: {}", trainId);
			return;
		}
		
		train.setStopped(true);
	}
	
	public void restartTrain(String trainId) {
		Train train = getTrain(trainId);
		if (train == null) {
			log.warn("Unable to restart train; unknown id: {}", trainId);
			return;
		}
		
		train.setStopped(false);
	}
	
	public void setLight(Segment segment, String status) {
		LightStatus st;
		if (status.equalsIgnoreCase("green")) {
			st = LightStatus.GREEN;
		} else if (status.equalsIgnoreCase("red")) {
			st = LightStatus.RED;
		} else {
			log.warn("Unknown status: " + status);
			return;
		}
		
		segment.setStatus(st);
	}
	
	public void setLight(String segmentId, String status) {
		Segment s = getSegment(segmentId);
		if (s == null) {
			log.warn("Invalid segment: " + segmentId);
			return;
		}
		
		setLight(s, status);
	}
	
	public TrainContainer getContainer(String id) {
		Matcher m = Station.PATTERN.matcher(id);
		if (m.matches()) {
			return getStation(m.group(1));
		}
		
		//m = Route.ROUTE_PATTERN.matcher(id);
		//if (m.matches()) {
		//	return getRoute(m.group(1));
		//}
		
		m = Segment.PATTERN.matcher(id);
		if (m.matches()) {
			return getSegment(m.group(1), Integer.parseInt(m.group(2)) - 1);
		}
		
		return null;
	}
	
	public RailMap copy() {
		RailMap copy = new RailMap(id);
		for (Route r : getRoutes()) {
			copy.addRoute(
					r.getId(),
					copy.getOrCreateStation(r.getStart().getId()),
					copy.getOrCreateStation(r.getEnd().getId()),
					r.getSegments().size());
		}
		
		for (Train t : getTrains()) {
			copy.addTrain(
					t.getId(),
					copy.getContainer(t.getLocation().getId()),
					t.isStopped());
		}
		
		return copy;
	}
	
	public String serialize() {
		StringBuilder sb = new StringBuilder();
		for (Route route : routes) {
			sb.append(String.format("{%s:%s;%s:%d}",
					route.getId(),
					route.getStart().getId(), route.getEnd().getId(),
					route.getSegmentCount()));
		}
		
		return String.format("%s[%s]", id, sb);
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
