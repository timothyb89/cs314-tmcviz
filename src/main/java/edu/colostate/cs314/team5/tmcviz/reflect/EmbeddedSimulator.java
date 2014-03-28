package edu.colostate.cs314.team5.tmcviz.reflect;

import edu.colostate.cs314.team5.tmcviz.TMCFrame;
import edu.colostate.cs314.team5.tmcviz.sim.Station;
import java.io.File;
import java.io.IOException;

/**
 * A simulator instance to be passed to the groovy shell. Mainly wraps
 * ReflectionSimulator, but also updates TMCFrame for relevant actions.
 * @author tim
 */
public class EmbeddedSimulator {
	
	private final TMCFrame frame;
	private final ReflectionSimulator simulator;

	public EmbeddedSimulator(TMCFrame frame, ReflectionSimulator simulator) {
		this.frame = frame;
		this.simulator = simulator;
	}

	public void reset() throws ReflectiveOperationException {
		simulator.reset();
		frame.resetSimulation();
	}

	public void createMap(String file) throws ReflectiveOperationException {
		simulator.createMap(file);
	}

	public void createMapFromText(String text) throws ReflectiveOperationException, IOException {
		simulator.createMapFromText(text);
	}
	
	public void loadCurrentMap() throws ReflectiveOperationException, IOException {
		simulator.createMapFromText(frame.getCurrentMap());
	}

	public void addNewRoute(String routeId, String startStation, String endStation, int segments) throws ReflectiveOperationException {
		Station start = frame.getMap().getOrCreateStation(startStation);
		Station end = frame.getMap().getOrCreateStation(endStation);
		frame.getMap().addRoute(routeId, start, end, segments);
		frame.updateMap();
		
		simulator.addNewRoute(routeId, startStation, endStation, segments);
	}

	public void removeRoute(String routeId) throws ReflectiveOperationException {
		frame.getMap().removeRoute(routeId);
		frame.updateMap();
		
		simulator.removeRoute(routeId);
	}

	public void displayStations() throws ReflectiveOperationException {
		simulator.displayStations();
	}

	public void displayRailmap() throws ReflectiveOperationException {
		simulator.displayRailmap();
	}

	public void checkMapValidity() throws ReflectiveOperationException {
		simulator.checkMapValidity();
	}

	public void saveMap() throws ReflectiveOperationException {
		simulator.saveMap();
	}

	public void getStationStatus(String stationId) throws ReflectiveOperationException {
		simulator.getStationStatus(stationId);
	}

	public void getRouteStatus(String routeId) throws ReflectiveOperationException {
		simulator.getRouteStatus(routeId);
	}

	public void getLightStatus(String routeId, int segmentIndex) throws ReflectiveOperationException {
		simulator.getLightStatus(routeId, segmentIndex);
	}

	public String simulate(String file) throws ReflectiveOperationException {
		SimpleOutputMonitor mon = new SimpleOutputMonitor();
		mon.start();
		simulator.simulate(file);
		mon.restore();
		
		frame.updateSimulation(mon.getOut());
		
		return mon.getOut();
	}

	public void simulateFromText(String text) throws ReflectiveOperationException, IOException {
		simulator.simulateFromText(text);
	}

	public void simulateSingleLoop(String loop) throws ReflectiveOperationException {
		simulator.simulateSingleLoop(loop);
	}

	public boolean singleSimulationSupported() {
		return simulator.singleSimulationSupported();
	}

	public File getJarFile() {
		return simulator.getJarFile();
	}

	public String getClassName() {
		return simulator.getClassName();
	}
	
}
