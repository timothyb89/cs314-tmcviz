package edu.colostate.cs314.team5.tmcviz.reflect;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * Wraps an ideal TMCSimulator class via reflection.
 * @author tim
 */
@Slf4j
public class ReflectionSimulator {
	
	@Getter
	private File jarFile;
	
	@Getter
	private String className;
	
	private Class clazz;
	private Object instance;
	
	private Method createMap;
	private Method addNewRoute;
	private Method removeRoute;
	private Method displayStations;
	private Method displayRailmap;
	private Method checkMapValidity;
	private Method saveMap;
	private Method getStationStatus;
	private Method getRouteStatus;
	private Method getLightStatus;
	private Method simulate;
	
	private Method simulateSingleLoop;
	
	public ReflectionSimulator(File jarFile, String className)
			throws MalformedURLException, ReflectiveOperationException {
		this.jarFile = jarFile;
		this.className = className;
		
		URLClassLoader loader = new URLClassLoader(new URL[] {
			jarFile.toURI().toURL()
		});
		
		clazz = loader.loadClass(className);
		
		Constructor cons = clazz.getConstructor();
		instance = cons.newInstance();
		
		createMap = clazz.getMethod("createMap", String.class);
		addNewRoute = clazz.getMethod("addNewRoute",
				String.class, String.class, String.class, Integer.TYPE);
		removeRoute = clazz.getMethod("removeRoute", String.class);
		displayStations = clazz.getMethod("displayStations");
		displayRailmap = clazz.getMethod("displayRailmap");
		checkMapValidity = clazz.getMethod("checkMapValidity");
		saveMap = clazz.getMethod("saveMap");
		getStationStatus = clazz.getMethod("getStationStatus", String.class);
		getRouteStatus = clazz.getMethod("getRouteStatus", String.class);
		getLightStatus = clazz.getMethod("getLightStatus",
				String.class, Integer.TYPE);
		simulate = clazz.getMethod("simulate", String.class);
		
		try {
			simulateSingleLoop = clazz.getMethod(
					"simulateSingleLoop", String.class);
		} catch (NoSuchMethodException ex) {
			log.warn("simulateSingleLoop(String) not found in " + className
					+ ", execute functionality disabled.");
		}
	}
	
	/**
	 * Reloads the instance of the class. Assuming the client class has no
	 * static state, this should reset it.
	 * @throws java.lang.ReflectiveOperationException
	 */
	public void reset() throws ReflectiveOperationException {
		Constructor c = clazz.getConstructor();
		instance = c.newInstance();
	}
	
	public void createMap(String file) throws ReflectiveOperationException {
		createMap.invoke(instance, file);
	}
	
	public void createMapFromText(String text)
			throws ReflectiveOperationException, IOException {
		Path p = Files.createTempFile("tmcviz-map", ".txt");

		Files.write(p,
				Arrays.asList(StringUtils.split(text, '\n')),
				Charset.defaultCharset());
		
		createMap(p.toString());
	}
	
	public void addNewRoute(
			String routeId, String startStation, String endStation, int segments)
			throws ReflectiveOperationException {
		addNewRoute.invoke(instance, routeId, startStation, endStation, segments);
	}
	
	public void removeRoute(String routeId)
			throws ReflectiveOperationException {
		removeRoute.invoke(instance, routeId);
	}
	
	public void displayStations() throws ReflectiveOperationException {
		displayStations.invoke(instance);
	}
	
	public void displayRailmap() throws ReflectiveOperationException {
		displayRailmap.invoke(instance);
	}
	
	public void checkMapValidity() throws ReflectiveOperationException {
		checkMapValidity.invoke(instance);
	}
	
	public void saveMap() throws ReflectiveOperationException {
		saveMap.invoke(instance);
	}
	
	public void getStationStatus(String stationId)
			throws ReflectiveOperationException {
		getStationStatus.invoke(instance, stationId);
	}
	
	public void getRouteStatus(String routeId)
			throws ReflectiveOperationException {
		getRouteStatus.invoke(instance, routeId);
	}
	
	public void getLightStatus(String routeId, int segmentIndex)
			throws ReflectiveOperationException {
		getLightStatus.invoke(instance, routeId, segmentIndex);
	}
	
	public void simulate(String file) throws ReflectiveOperationException {
		simulate.invoke(instance, file);
	}
	
	public void simulateFromText(String text)
			throws ReflectiveOperationException, IOException {
		Path p = Files.createTempFile("tmcviz-sim-", ".txt");
		
		Files.write(p,
				Arrays.asList(StringUtils.split(text, '\n')),
				Charset.defaultCharset());
		
		simulate(p.toString());
	}
	
	public void simulateSingleLoop(String loop)
			throws ReflectiveOperationException {
		simulateSingleLoop.invoke(instance, loop);
	}
	
	public boolean singleSimulationSupported() {
		return simulateSingleLoop != null;
	}
	
}
