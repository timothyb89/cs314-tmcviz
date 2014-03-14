package edu.colostate.cs314.team5.tmcviz.reflect;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author tim
 */
public class SimpleOutputMonitor {
	
	private PrintStream stdoutOriginal;
	private PrintStream stderrOriginal;
	
	private ByteArrayOutputStream stdoutTemp;
	private ByteArrayOutputStream stderrTemp;

	public SimpleOutputMonitor() {
		stdoutTemp = new ByteArrayOutputStream();
		stderrTemp = new ByteArrayOutputStream();
	}
	
	public void start() {
		stdoutOriginal = System.out;
		stderrOriginal = System.err;
		
		stdoutTemp.reset();
		stderrTemp.reset();
		
		System.setOut(new PrintStream(stdoutTemp));
		System.setErr(new PrintStream(stderrTemp));
	}
	
	public void restore() {
		System.setOut(stdoutOriginal);
		System.setErr(stderrOriginal);
	}
	
	public String getOut() {
		return stdoutTemp.toString();
	}
	
	public String[] getOutLines() {
		return StringUtils.split(stdoutTemp.toString(), '\n');
	}
	
	public String getErr() {
		return stderrTemp.toString();
	}
	
	public String[] getErrLines() {
		return StringUtils.split(stderrTemp.toString(), '\n');
	}
	
	public static void main(String[] args) {
		System.out.println("test 1");
		System.err.println("test 2");
		
		SimpleOutputMonitor m = new SimpleOutputMonitor();
		m.start();
		
		System.out.println("test 3");
		System.err.println("test 4");
		
		m.restore();
		
		System.out.println("stdout:");
		for (String s : m.getOutLines()) {
			System.out.println("\t" + s);
		}
		
		System.out.println("stderr:");
		for (String s : m.getErrLines()) {
			System.out.println("\t" + s);
		}
	}
	
}
