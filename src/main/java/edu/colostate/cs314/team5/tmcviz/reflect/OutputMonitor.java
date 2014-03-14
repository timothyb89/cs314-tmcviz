package edu.colostate.cs314.team5.tmcviz.reflect;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

/**
 * this was apparently a bad idea
 * @author tim
 */
@Slf4j
public class OutputMonitor {
	
	private final List<OutputListener> listeners;
	
	private PrintStream stdoutOriginal;
	private PrintStream stderrOriginal;
	
	private PipedOutputStream stdoutPipe;
	private PipedInputStream stdoutReader;
	private PrintStream stdoutPipeWriter;
	
	private PipedOutputStream stderrPipe;
	private PipedInputStream stderrReader;
	private PrintStream stderrPipeWriter;
	
	public OutputMonitor() {
		listeners = Collections.synchronizedList(new LinkedList<OutputListener>());
	}
	
	public void start() {
		stdoutOriginal = System.out;
		stderrOriginal = System.err;
		
		try {
			stdoutReader = new PipedInputStream();
			stdoutPipe = new PipedOutputStream(stdoutReader);
			stdoutPipeWriter = new PrintStream(stdoutPipe);
			System.setOut(stdoutPipeWriter);
		} catch (IOException ex) {
			log.debug("wat", ex);
		}
		
		new Thread(stdoutMonitor).start();
		
		try {
			stderrReader = new PipedInputStream();
			stderrPipe = new PipedOutputStream(stderrReader);
			stderrPipeWriter = new PrintStream(stderrPipe);
			System.setErr(stderrPipeWriter);
		} catch (IOException ex) {
			log.debug("wat", ex);
		}
		
		new Thread(stderrMonitor).start();
	}
	
	public <T extends OutputListener> T addListener(T l) {
		listeners.add(l);
		return l;
	}
	
	public OutputBuffer createBuffer() {
		return addListener(new OutputBuffer());
	}
	
	public <T extends OutputListener> T removeListener(T l) {
		listeners.remove(l);
		return l;
	}
	
	public void restore() {
		System.setOut(stdoutOriginal);
		System.setErr(stderrOriginal);
		
		try {
			stdoutReader.close();
			stderrReader.close();
		} catch (Exception ex) {
			log.debug("wat", ex);
		}
	}
	
	private final Runnable stdoutMonitor = new Runnable() {

		@Override
		public void run() {
			InputStreamReader isr = new InputStreamReader(stdoutReader);
			
			try (BufferedReader br = new BufferedReader(isr)) {
				String line;
				while ((line = br.readLine()) != null) {
					//stdoutOriginal.println("out: " + line);
					synchronized (listeners) {
						for (OutputListener l : listeners) {
							l.stdoutMessage(line);
						}
					}
				}
				stdoutOriginal.println("stdout done");
			} catch (IOException ex) {
				log.debug("wat", ex);
			}
		}
		
	};
	
	private final Runnable stderrMonitor = new Runnable() {

		@Override
		public void run() {
			InputStreamReader isr = new InputStreamReader(stderrReader);
			
			try (BufferedReader br = new BufferedReader(isr)) {
				String line;
				while ((line = br.readLine()) != null) {
					stderrOriginal.println("err: " + line);
					synchronized (listeners) {
						for (OutputListener l : listeners) {
							l.stderrMessage(line);
						}
					}
				}
				stdoutOriginal.println("stderr done");
			} catch (IOException ex) {
				log.debug("wat", ex);
			}
		}
		
	};
	
	public static void main(String[] args) {
		OutputMonitor monitor = new OutputMonitor();
		
		System.out.println("test 1");
		
		monitor.start();
		
		System.out.println("test 2");
		
		OutputBuffer b = monitor.addListener(new OutputBuffer());
		
		System.out.println("test 3");
		System.out.println("test 4");
		System.err.println("test 5");
		
		monitor.removeListener(b);
		
		System.out.println("test 6");
		
		monitor.restore();
		
		System.out.println("test 7");
		
		System.out.println("stdout:");
		for (String s : b.getStdout()) {
			System.out.println("\t" + s);
		}
		
		System.out.println("stderr:");
		for (String s : b.getStderr()) {
			System.out.println("\t" + s);
		}
		
		System.out.println("combined:");
		for (String s : b.getCombined()) {
			System.out.println("\t" + s);
		}
		
	}
	
}
