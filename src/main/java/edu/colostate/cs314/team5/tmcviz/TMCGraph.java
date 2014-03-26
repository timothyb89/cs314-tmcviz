package edu.colostate.cs314.team5.tmcviz;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.view.mxGraph;
import edu.colostate.cs314.team5.tmcviz.sim.RailMap;
import edu.colostate.cs314.team5.tmcviz.sim.Route;
import edu.colostate.cs314.team5.tmcviz.sim.Segment;
import edu.colostate.cs314.team5.tmcviz.sim.Station;
import edu.colostate.cs314.team5.tmcviz.sim.Train;
import edu.colostate.cs314.team5.tmcviz.sim.TrainContainer;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author tim
 */
public class TMCGraph extends mxGraph {
	
	private RailMap map;
	
	private List<Station> orderedStations;
	private Map<Station, mxCell> stationCells;
	private Map<Station, Double> stationAngles;
	private Map<Segment, mxCell> segmentCells;
	private Map<Train, mxCell> trainCells;
	private List<Route> dirtyAdjacentRoutes;
	private List<Route> dirtyRoutes;
	
	public TMCGraph() {
		orderedStations = new ArrayList<>();
		stationCells = new HashMap<>();
		stationAngles = new HashMap<>();
		segmentCells = new HashMap<>();
		trainCells = new HashMap<>();
		dirtyAdjacentRoutes = new ArrayList<>();
		dirtyRoutes = new ArrayList<>();
		
		setCellsDeletable(true);
		setCellsCloneable(false);
		setCellsEditable(false);
		setCellsResizable(false);
		setCellsDisconnectable(false);
		setCellsBendable(false);
		//setCellsLocked(false);
		//setCellsSelectable(false);
		setAllowDanglingEdges(false);
		setVertexLabelsMovable(false);
		setEdgeLabelsMovable(false);
		
		setCellsMovable(true);
	}
	
	private double interpolate(double min, double max, double ratio) {
		double range = max - min;
		
		return min + ratio * range;
	}
	
	private Point interpolate(mxCell a, mxCell b, double ratio) {
		mxGeometry ag = a.getGeometry();
		mxGeometry bg = b.getGeometry();
		
		return new Point(
				(int) interpolate(ag.getCenterX(), bg.getCenterX(), ratio),
				(int) interpolate(ag.getCenterY(), bg.getCenterY(), ratio));
	}
	
	private boolean isAdjacent(Station a, Station b) {
		if (orderedStations.size() < 2) {
			System.out.println("size too small");
			return false;
		}
		
		Station last = null;
		
		for (Station station : orderedStations) {
			if (last == null) {
				last = station;
				continue;
			}
			
			if ((last == a && station == b) || (station == a && last == b)) {
				return true;
			}
			
			last = station;
		}
		
		Station first = orderedStations.get(0);
		if ((first == a && last == b) || (last == a && first == b)) {
			return true;
		}
		
		return false;
	}
	
	private boolean isDirtyAdjacent(Route r) {
		for (Route dirty : dirtyAdjacentRoutes) {
			if ((dirty.getStart() == r.getStart() && dirty.getEnd() == r.getEnd())
					|| (dirty.getStart() == r.getEnd() && dirty.getEnd() == r.getStart())) {
				return true;
			}
		}
		
		return false;
	}
	
	private boolean isDirty(Route r) {
		for (Route dirty : dirtyRoutes) {
			if ((dirty.getStart() == r.getStart() && dirty.getEnd() == r.getEnd())
					|| (dirty.getStart() == r.getEnd() && dirty.getEnd() == r.getStart())) {
				return true;
			}
		}
		
		return false;
	}
	
	private mxCell getCell(TrainContainer container) {
		if (container instanceof Station) {
			return stationCells.get((Station) container);
		} else if (container instanceof Segment) {
			return segmentCells.get((Segment) container);
		} else {
			return null;
		}
	}
	
	public void setMap(RailMap map) {
		System.out.println("setMap()");
		
		removeCells(stationCells.values().toArray(), true);
		removeCells(segmentCells.values().toArray(), true);
		removeCells(trainCells.values().toArray(), true);
		
		getModel().beginUpdate();
		
		stationCells.clear();
		segmentCells.clear();
		trainCells.clear();
		
		orderedStations.clear();
		dirtyRoutes.clear();
		dirtyAdjacentRoutes.clear();
		
		Object parent = getDefaultParent();
		
		double primaryRadius = 250;
		double thetaDiff = (2 * Math.PI) / map.getStations().size();
		
		// arrange station nodes in a circle
		for (int i = 0; i < map.getStations().size(); i++) {
			Station s = map.getStations().get(i);
			orderedStations.add(s);
			
			double theta = thetaDiff * i;
			double x = primaryRadius * Math.cos(theta) + primaryRadius;
			double y = primaryRadius * Math.sin(theta) + primaryRadius;
			
			mxCell c = (mxCell) insertVertex(parent, null, s, (int) x, (int) y, 50, 50);
			stationCells.put(s, c);
			stationAngles.put(s, theta);
		}
		
		// create routes as chained lines between their connecting stations
		for (Route r : map.getRoutes()) {
			Station sa = r.getStart();
			Station sb = r.getEnd();
			mxCell a = stationCells.get(r.getStart());
			mxCell b = stationCells.get(r.getEnd());
			
			int offsetX = 0;
			int offsetY = 0;
			
			System.out.println("adjacent: " + isAdjacent(sa, sb) + "; dirty: " + isDirtyAdjacent(r));
			if (isAdjacent(sa, sb) && !isDirtyAdjacent(r)) {
				// attempt to align around the circle
				// only adjacent stations will have routes positioned this way
				// i.e. they must be next to each other around the circle
				
				dirtyAdjacentRoutes.add(r);
				
				// get + normalize the positions on the circle of the 2 stations
				double angleStart = stationAngles.get(sa);
				double angleEnd = stationAngles.get(sb);

				//System.out.printf("%s [ %.2f ] -> %s [ %.2f ]\n",
				//		sa.toString(), Math.toDegrees(angleStart),
				//		sb.toString(), Math.toDegrees(angleEnd));
				
				while (angleStart > angleEnd) {
					angleEnd += 2 * Math.PI;
				}
				
				mxCell first = null;
				mxCell prev = null;
				for (int i = 0; i < r.getSegmentCount(); i++) {
					Segment s = r.getSegment(i);
					
					double ratio = (double) (s.getIndex() + 1) / (double) (r.getSegmentCount() + 1);
					double segmentTheta = interpolate(angleStart, angleEnd, ratio);
					
					double segmentX = primaryRadius * Math.cos(segmentTheta) + primaryRadius;
					double segmentY = primaryRadius * Math.sin(segmentTheta) + primaryRadius;
					
					mxCell segmentCell = (mxCell) insertVertex(
							parent, null, s,
							(int) segmentX, (int) segmentY,
							45, 35, s.getStyle());
					segmentCells.put(s, segmentCell);
					
					if (prev != null) {
						insertEdge(parent, null, null, prev, segmentCell);
					}

					if (first == null) {
						first = segmentCell;
					}

					prev = segmentCell;
				}
				
				if (first != null) {
					insertEdge(parent, null, null, a, first);
				}

				if (prev != null) {
					insertEdge(parent, null, null, prev, b);
				}
			} else {
				// fall back to linear alignment if that "branch" is already
				// occupied by another route, or if they aren't adjacent
				// stations
				
				if (isDirty(r)) {
					offsetX += 30;
					offsetY += 25;
				}
				dirtyRoutes.add(r);

				mxCell first = null;
				mxCell prev = null;
				for (int i = 0; i < r.getSegmentCount(); i++) {
					Segment s = r.getSegment(i);

					double ratio = (double) (s.getIndex() + 1) / (double) (r.getSegmentCount() + 1);
					Point p = interpolate(a, b, ratio);

					mxCell segmentCell = (mxCell) insertVertex(
							parent, null, s,
							p.x - 20 + offsetX, p.y - 15 + offsetY,
							45, 35, s.getStyle());
					segmentCells.put(s, segmentCell);

					if (prev != null) {
						insertEdge(parent, null, null, prev, segmentCell);
					}

					if (first == null) {
						first = segmentCell;
					}

					prev = segmentCell;
				}

				if (first != null) {
					insertEdge(parent, null, null, a, first);
				}

				if (prev != null) {
					insertEdge(parent, null, null, prev, b);
				}
			}
		}
		
		// find the trains on each cell
		Map<mxCell, List<Train>> trainsForCell = new HashMap<>();
		for (Train t : map.getTrains()) {
			mxCell cell = getCell(t.getLocation());
			
			List<Train> list = trainsForCell.get(cell);
			if (list == null) {
				list = new LinkedList<>();
				trainsForCell.put(cell, list);
			}
			
			list.add(t);
		}
		
		double trainRadius = 30;
		
		// for each cell with trains, place new train cells (and try to arrange
		// them sanely)
		for (mxCell cell : trainsForCell.keySet()) {
			// mxGraph center points are weird, normalize both the "x/y" and
			// "center x/y"
			mxGeometry geom = cell.getGeometry();
			double cx = (geom.getX() + geom.getCenterX()) / 2;
			double cy = (geom.getY() + geom.getCenterY()) / 2;
			
			List<Train> trains = trainsForCell.get(cell);
			
			thetaDiff = (2 * Math.PI) / trains.size();
			
			for (int i = 0; i < trains.size(); i++) {
				Train t = trains.get(i);
				
				double theta = i * thetaDiff;
				double x = trainRadius * Math.cos(theta) + cx;
				double y = trainRadius * Math.sin(theta) + cy;
				
				mxCell trainCell = (mxCell) insertVertex(
						parent, null, t,
						x, y,
						30, 30,
						t.getStyle());
				trainCells.put(t, trainCell);
			}
		}
		
		getModel().endUpdate();
	}
	
}
