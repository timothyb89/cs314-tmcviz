package edu.colostate.cs314.team5.tmcviz;

import com.mxgraph.swing.mxGraphComponent;
import edu.colostate.cs314.team5.tmcviz.reflect.ReflectionSimulator;
import edu.colostate.cs314.team5.tmcviz.reflect.SimpleOutputMonitor;
import edu.colostate.cs314.team5.tmcviz.sim.LoopParser;
import edu.colostate.cs314.team5.tmcviz.sim.RailMap;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.LayoutStyle;
import javax.swing.ScrollPaneConstants;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author tim
 */
@Slf4j
public class TMCFrame extends javax.swing.JFrame {

	private mxGraphComponent graphView;
	private TMCGraph graph;
	
	private String currentMap;
	private RailMap map;
	private LoopParser loopParser;
	
	@Getter @Setter
	private ReflectionSimulator simulator;
	
	public static File lastDirectory;
	
	/**
	 * Creates new form TMCFrame
	 */
	public TMCFrame() {
		initComponents();
		
		graph = new TMCGraph();
		graphView = new mxGraphComponent(graph);
		graphContainer.add(graphView, BorderLayout.CENTER);
		
		mapEditField.setText(
				"FoCoSys["
						+ "{0000:FTCL;LGMT:4}"
						+ "{0001:LGMT;FTCL:3}"
						+ "{0002:LGMT;BOLD:2}"
						+ "{0005:BOLD;FTCL:5}"
						+ "]");
		mapEditButtonActionPerformed(null);
		
		setVisible(true);
		
		TMCPropertiesDialog dialog = new TMCPropertiesDialog(this);
		dialog.setVisible(true);
	}

	private void error(String text) {
		JOptionPane.showMessageDialog(
				this, text, "Error", JOptionPane.ERROR_MESSAGE);
	}
	
	private void error(String text, Throwable t) {
		log.error(text, t);
		
		error(text + t.getMessage());
	}
	
	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        vertSplit = new JSplitPane();
        metaContainer = new JPanel();
        buttonContainer = new JPanel();
        mapEditButton = new JButton();
        mapEditField = new JTextField();
        mapResetButton = new JButton();
        graphContainer = new JPanel();
        loopSlider = new JSlider();
        ioContainer = new JPanel();
        outputPaneScroll = new JScrollPane();
        outputPane = new JEditorPane();
        execSingleField = new JTextField();
        execFileButton = new JButton();
        execSingleButton = new JButton();
        menuBar = new JMenuBar();
        fileMenu = new JMenu();
        quitItem = new JMenuItem();
        editMenu = new JMenu();
        propertiesItem = new JMenuItem();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("TMCViz");

        vertSplit.setOrientation(JSplitPane.VERTICAL_SPLIT);
        vertSplit.setResizeWeight(1.0);

        metaContainer.setBorder(BorderFactory.createTitledBorder("Current State"));
        metaContainer.setLayout(new BorderLayout());

        buttonContainer.setMaximumSize(new Dimension(32767, 200));

        mapEditButton.setText("Set Map");
        mapEditButton.setEnabled(false);
        mapEditButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                mapEditButtonActionPerformed(evt);
            }
        });

        mapEditField.setEnabled(false);
        mapEditField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent evt) {
                mapEditFieldKeyReleased(evt);
            }
        });

        mapResetButton.setText("Reset");
        mapResetButton.setEnabled(false);

        GroupLayout buttonContainerLayout = new GroupLayout(buttonContainer);
        buttonContainer.setLayout(buttonContainerLayout);
        buttonContainerLayout.setHorizontalGroup(
            buttonContainerLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(GroupLayout.Alignment.TRAILING, buttonContainerLayout.createSequentialGroup()
                .addComponent(mapEditField, GroupLayout.DEFAULT_SIZE, 572, Short.MAX_VALUE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(mapEditButton)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(mapResetButton))
        );
        buttonContainerLayout.setVerticalGroup(
            buttonContainerLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(GroupLayout.Alignment.TRAILING, buttonContainerLayout.createSequentialGroup()
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(buttonContainerLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(mapEditButton)
                    .addComponent(mapEditField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(mapResetButton))
                .addContainerGap())
        );

        metaContainer.add(buttonContainer, BorderLayout.SOUTH);

        graphContainer.setBorder(null);
        graphContainer.setLayout(new BorderLayout());
        metaContainer.add(graphContainer, BorderLayout.CENTER);

        loopSlider.setMajorTickSpacing(2);
        loopSlider.setMaximum(10);
        loopSlider.setMinorTickSpacing(1);
        loopSlider.setOrientation(JSlider.VERTICAL);
        loopSlider.setPaintLabels(true);
        loopSlider.setPaintTicks(true);
        loopSlider.setSnapToTicks(true);
        loopSlider.setToolTipText("Shown simulation loop");
        loopSlider.setValue(0);
        loopSlider.setEnabled(false);
        loopSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent evt) {
                loopSliderStateChanged(evt);
            }
        });
        metaContainer.add(loopSlider, BorderLayout.EAST);

        vertSplit.setTopComponent(metaContainer);

        ioContainer.setBorder(BorderFactory.createTitledBorder("Output"));
        ioContainer.setMinimumSize(new Dimension(0, 150));

        outputPaneScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        outputPane.setEditable(false);
        outputPane.setEnabled(false);
        outputPane.setMinimumSize(new Dimension(112, 100));
        outputPaneScroll.setViewportView(outputPane);

        execSingleField.setToolTipText("Requires simulateSingleLoop(String) defined in TMCSimulator");
        execSingleField.setEnabled(false);

        execFileButton.setText("From File...");
        execFileButton.setEnabled(false);
        execFileButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                execFileButtonActionPerformed(evt);
            }
        });

        execSingleButton.setText("Execute");
        execSingleButton.setToolTipText("Requires simulateSingleLoop(String) defined in TMCSimulator");
        execSingleButton.setEnabled(false);

        GroupLayout ioContainerLayout = new GroupLayout(ioContainer);
        ioContainer.setLayout(ioContainerLayout);
        ioContainerLayout.setHorizontalGroup(
            ioContainerLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(outputPaneScroll, GroupLayout.DEFAULT_SIZE, 717, Short.MAX_VALUE)
            .addGroup(ioContainerLayout.createSequentialGroup()
                .addComponent(execSingleField)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(execSingleButton)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(execFileButton)
                .addContainerGap())
        );
        ioContainerLayout.setVerticalGroup(
            ioContainerLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(GroupLayout.Alignment.TRAILING, ioContainerLayout.createSequentialGroup()
                .addGroup(ioContainerLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(execSingleField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(execFileButton)
                    .addComponent(execSingleButton))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(outputPaneScroll, GroupLayout.DEFAULT_SIZE, 136, Short.MAX_VALUE))
        );

        vertSplit.setBottomComponent(ioContainer);

        fileMenu.setText("File");

        quitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_MASK));
        quitItem.setText("Quit");
        fileMenu.add(quitItem);

        menuBar.add(fileMenu);

        editMenu.setText("Edit");

        propertiesItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_MASK));
        propertiesItem.setText("Properties...");
        propertiesItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                propertiesItemActionPerformed(evt);
            }
        });
        editMenu.add(propertiesItem);

        menuBar.add(editMenu);

        setJMenuBar(menuBar);

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(vertSplit)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(vertSplit, GroupLayout.DEFAULT_SIZE, 669, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void mapEditButtonActionPerformed(ActionEvent evt) {//GEN-FIRST:event_mapEditButtonActionPerformed
        try {
			map = RailMap.parse(mapEditField.getText());
			loopParser = new LoopParser(map);
			graph.setMap(map);
			currentMap = mapEditField.getText();
		} catch (Exception ex) {
			error("Invalid railmap - check your syntax.\n" + ex.getMessage());
		}
    }//GEN-LAST:event_mapEditButtonActionPerformed

    private void mapEditFieldKeyReleased(KeyEvent evt) {//GEN-FIRST:event_mapEditFieldKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
			mapEditButtonActionPerformed(null);
		}
    }//GEN-LAST:event_mapEditFieldKeyReleased

    private void propertiesItemActionPerformed(ActionEvent evt) {//GEN-FIRST:event_propertiesItemActionPerformed
        TMCPropertiesDialog dialog = new TMCPropertiesDialog(this);
		dialog.setVisible(true);
    }//GEN-LAST:event_propertiesItemActionPerformed

    private void execFileButtonActionPerformed(ActionEvent evt) {//GEN-FIRST:event_execFileButtonActionPerformed
        if (simulator == null) {
			error("You must load a simulator class first.");
			return;
		}
		
		JFileChooser chooser = new JFileChooser(lastDirectory);
		if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			try {
				File f = chooser.getSelectedFile();
				
				log.info("Executing simulator with input file: " + f);
				
				simulator.reset();
				simulator.createMapFromText(currentMap);
				
				SimpleOutputMonitor mon = new SimpleOutputMonitor();
				mon.start();
				simulator.simulate(f.getPath());
				mon.restore();
				
				outputPane.setText(mon.getOut());
				
				lastDirectory = f.getParentFile();
				
				loopParser.parseLoop(mon.getOut());
				loopSlider.setEnabled(true);
				loopSlider.setValue(loopParser.getMinIndex());
				loopSlider.setMinimum(loopParser.getMinIndex());
				loopSlider.setMaximum(loopParser.getMaxIndex());
			} catch (ReflectiveOperationException ex) {
				error("Error executing simulator class", ex);
			} catch (IOException ex) {
				error("Error creating simulator file", ex);
			}
		}
    }//GEN-LAST:event_execFileButtonActionPerformed

    private void loopSliderStateChanged(ChangeEvent evt) {//GEN-FIRST:event_loopSliderStateChanged
        RailMap temp = loopParser.getLoop(loopSlider.getValue());
		graph.setMap(temp);
    }//GEN-LAST:event_loopSliderStateChanged

	public void configured() {
		mapEditField.setEnabled(true);
		mapEditButton.setEnabled(true);
		
		execFileButton.setEnabled(true);
		outputPane.setEnabled(true);
		
		if (simulator.singleSimulationSupported()) {
			execSingleField.setEnabled(true);
			execSingleButton.setEnabled(true);
		}
	}
	
	public void resetSimulation() {
		loopSlider.setEnabled(false);
		graph.setMap(map);
	}
	
	/**
	 * @param args the command line arguments
	 */
	public static void main(String args[]) {
		/* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
		 * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
		 */
		try {
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException ex) {
			java.util.logging.Logger.getLogger(TMCFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (InstantiationException ex) {
			java.util.logging.Logger.getLogger(TMCFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(TMCFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(TMCFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		}
        //</editor-fold>

		/* Create and display the form */
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new TMCFrame();
			}
		});
	}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JPanel buttonContainer;
    private JMenu editMenu;
    private JButton execFileButton;
    private JButton execSingleButton;
    private JTextField execSingleField;
    private JMenu fileMenu;
    private JPanel graphContainer;
    private JPanel ioContainer;
    private JSlider loopSlider;
    private JButton mapEditButton;
    private JTextField mapEditField;
    private JButton mapResetButton;
    private JMenuBar menuBar;
    private JPanel metaContainer;
    private JEditorPane outputPane;
    private JScrollPane outputPaneScroll;
    private JMenuItem propertiesItem;
    private JMenuItem quitItem;
    private JSplitPane vertSplit;
    // End of variables declaration//GEN-END:variables
}
