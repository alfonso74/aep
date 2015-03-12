package rcp.manticora.services;

import java.util.Calendar;

import org.eclipse.nebula.widgets.ganttchart.GanttChart;
import org.eclipse.nebula.widgets.ganttchart.GanttEvent;
import org.eclipse.nebula.widgets.ganttchart.themes.ColorThemeSilver;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class OcupacionGantt {
	private Shell shell;
	private Display display;
	private Composite composite;
	
	public OcupacionGantt(Display display) {
		shell = new Shell(display, SWT.APPLICATION_MODAL | SWT.CLOSE | SWT.RESIZE);
		shell.setText("Gantt Chart Sample");
		shell.setSize(500, 400);		
		shell.setLayout(new FillLayout());
		generarGantt();
	}
	
	public OcupacionGantt(Composite composite) {
		this.composite = composite;
		generarGantt2();
	}
	
	private void generarGantt() {
		// Create a chart
		GanttChart ganttChart = new GanttChart(shell, SWT.NONE);
				
		// Create some calendars
		Calendar sdEventOne = Calendar.getInstance();
		Calendar edEventOne = Calendar.getInstance();
		edEventOne.add(Calendar.DATE, 10); 

		Calendar sdEventTwo = Calendar.getInstance();
		Calendar edEventTwo = Calendar.getInstance();
		sdEventTwo.add(Calendar.DATE, 11);
		edEventTwo.add(Calendar.DATE, 15);

		Calendar cpDate = Calendar.getInstance();
		cpDate.add(Calendar.DATE, 16);

		// Create events
//		GanttEvent eventOne = new GanttEvent(ganttChart, "Scope Event 1", sdEventOne, edEventOne, 35);
		GanttEvent eventOne = new GanttEvent(ganttChart, "Scope Event 1", sdEventOne, edEventOne, 0);
		GanttEvent eventTwo = new GanttEvent(ganttChart, "Scope Event 2", sdEventTwo, edEventTwo, 10);		
		GanttEvent eventThree = new GanttEvent(ganttChart, "Checkpoint", cpDate, cpDate, 75);
		eventThree.setCheckpoint(true);

		// Create connections
		ganttChart.addConnection(eventOne, eventTwo);
		ganttChart.addConnection(eventTwo, eventThree);
	}
	
	
	private void generarGantt2() {
		// Create a chart
		//GanttChart ganttChart = new GanttChart(composite, SWT.NONE);
		GanttChart ganttChart = new GanttChart(composite, SWT.NONE, null, new ColorThemeSilver());
		
		ganttChart.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
				
		// Create some calendars
		Calendar sdEventOne = Calendar.getInstance();
		Calendar edEventOne = Calendar.getInstance();
		edEventOne.add(Calendar.DATE, 10); 

		Calendar sdEventTwo = Calendar.getInstance();
		Calendar edEventTwo = Calendar.getInstance();
		sdEventTwo.add(Calendar.DATE, 11);
		edEventTwo.add(Calendar.DATE, 15);

		Calendar cpDate = Calendar.getInstance();
		cpDate.add(Calendar.DATE, 16);

		// Create events
//		GanttEvent eventOne = new GanttEvent(ganttChart, "Scope Event 1", sdEventOne, edEventOne, 35);
		GanttEvent eventOne = new GanttEvent(ganttChart, "Scope Event 1", sdEventOne, edEventOne, 0);
		GanttEvent eventTwo = new GanttEvent(ganttChart, "Scope Event 2", sdEventTwo, edEventTwo, 10);		
		GanttEvent eventThree = new GanttEvent(ganttChart, "Checkpoint", cpDate, cpDate, 75);
		eventThree.setCheckpoint(true);

		// Create connections
		ganttChart.addConnection(eventOne, eventTwo);
		ganttChart.addConnection(eventTwo, eventThree);
	}
	
	
	public void open() {
        //shell.pack();
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) display.sleep();
        }
    }

}
