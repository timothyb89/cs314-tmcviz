package edu.colostate.cs314.team5.tmcviz;

import com.mxgraph.swing.mxGraphComponent;
import java.awt.BorderLayout;
import javax.swing.JComponent;

/**
 *
 * @author tim
 */
public class TMCGraphComponent extends JComponent {

	private mxGraphComponent graphView;
	private TMCGraph graph;
	
	public TMCGraphComponent() {
		setLayout(new BorderLayout());
		
		
	}

	public TMCGraph getGraph() {
		return graph;
	}
	
}
