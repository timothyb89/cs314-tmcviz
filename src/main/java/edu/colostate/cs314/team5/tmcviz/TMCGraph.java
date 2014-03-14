package edu.colostate.cs314.team5.tmcviz;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.view.mxGraph;
import edu.colostate.cs314.team5.tmcviz.sim.RailMap;
import edu.colostate.cs314.team5.tmcviz.sim.Route;
import edu.colostate.cs314.team5.tmcviz.sim.Segment;
import edu.colostate.cs314.team5.tmcviz.sim.Station;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author tim
 */
public class TMCGraph extends mxGraph {
	
	private RailMap map;
	
	private Map<Station, mxCell> stationCells;
	private List<mxCell> segmentCells;
	private List<Route> dirtyRoutes;
	
	public TMCGraph() {
		stationCells = new HashMap<>();
		segmentCells = new ArrayList<>();
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
	
	private boolean isDirty(Route r) {
		for (Route dirty : dirtyRoutes) {
			if ((dirty.getStart() == r.getStart() && dirty.getEnd() == r.getEnd())
					|| (dirty.getStart() == r.getEnd() && dirty.getEnd() == r.getStart())) {
				return true;
			}
		}
		
		return false;
	}
	
	public void setMap(RailMap map) {
		removeCells(stationCells.values().toArray(), true);
		removeCells(segmentCells.toArray(), true);
		
		//removeCellsFromParent();
		//removeCells();
		getModel().beginUpdate();
		
		
		stationCells.clear();
		
		//
		//for (mxCell c : segmentCells) {
		//	c.removeFromParent();
		//}
		segmentCells.clear();
		
		dirtyRoutes.clear();
		
		//getModel().endUpdate();
		
		// will this actually remove edges?
		
		Object parent = getDefaultParent();
		
		double radius = 200;
		double thetaDiff = (2 * Math.PI) / map.getStations().size();
		
		// arrange station nodes in a circle
		for (int i = 0; i < map.getStations().size(); i++) {
			Station s = map.getStations().get(i);
			
			double theta = thetaDiff * i;
			double x = radius * Math.cos(theta) + radius;
			double y = radius * Math.sin(theta) + radius;
			
			mxCell c = (mxCell) insertVertex(parent, null, s, (int) x, (int) y, 50, 50);
			stationCells.put(s, c);
		}
		
		// create routes as chained lines between their connecting stations
		for (Route r : map.getRoutes()) {
			mxCell a = stationCells.get(r.getStart());
			mxCell b = stationCells.get(r.getEnd());
			
			int offsetX = 0;
			int offsetY = 0;
			
			if (isDirty(r)) {
				offsetX += 25;
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
						40, 30);
				segmentCells.add(segmentCell);
				
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
		
		getModel().endUpdate();
	}
	
}
