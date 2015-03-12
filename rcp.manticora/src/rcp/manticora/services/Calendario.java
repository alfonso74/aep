package rcp.manticora.services;

import java.util.Date;


import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import rcp.manticora.calendar.SWTCalendarDialog;
import rcp.manticora.calendar.SWTCalendarEvent;
import rcp.manticora.calendar.SWTCalendarListener;

public class Calendario extends SelectionAdapter {
	private Text txtCampo;
	private Shell shell;
	private Text txtFechaDefault;

	public Calendario(Shell shell, Text txtCampo) {
		this.shell = shell;
		this.txtCampo = txtCampo;
		this.txtFechaDefault = null;
		this.txtCampo.addKeyListener(this.crearKeyAdapter(this.txtCampo, txtFechaDefault));
	}
	
	public Calendario(Shell shell, Text txtCampo, Text txtFechaDefault) {
		this.shell = shell;
		this.txtCampo = txtCampo;
		this.txtFechaDefault = txtFechaDefault;
		this.txtCampo.addKeyListener(this.crearKeyAdapter(this.txtCampo, txtFechaDefault));
	}

	public void widgetSelected(SelectionEvent e) {
		final SWTCalendarDialog cal = new SWTCalendarDialog(shell.getDisplay());
		// determinamos la posición donde queremos ubicar el calendario
		Point pos = txtCampo.toDisplay(0,0);
		int altura = txtCampo.getSize().y;
		cal.setLocation(pos.x, pos.y + altura);
		cal.addDateChangedListener(new SWTCalendarListener() {
			public void dateChanged(SWTCalendarEvent calendarEvent) {
				//txtCampo.setText(formatter.format(calendarEvent.getCalendar().getTime()));
				Date d = calendarEvent.getCalendar().getTime();
				txtCampo.setText(FechaUtil.toString(d));
				txtCampo.setFocus();
			}
		});
		
		// determinamos la fecha default que debe presentar el calendario
		if (txtCampo.getText() != null && txtCampo.getText().length() > 0) {
			Date d = FechaUtil.toDate(txtCampo.getText());
			//Date d = formatter.parse(txtCampo.getText());
			cal.setDate(d);
		} else if (txtFechaDefault != null && txtFechaDefault.getText().length() > 0) {
			Date d = FechaUtil.toDate(txtFechaDefault.getText());
			cal.setDate(d);
		}
		cal.open();
	}
	
	
	/**
	 * Crea un KeyAdapter que responde a las flechas de arriba y abajo para
	 * incrementar/decrementar la fecha.  En caso de no haber una fecha, se
	 * verifica el campo campoFechaDefault para un valor default, y si tampoco
	 * hay fecha default, entonces se utiliza el día actual como default 
	 * @param campo el campo donde se especifica la fecha
	 */
	public KeyAdapter crearKeyAdapter(final Text campo, final Text campoFechaDefault) {
		return new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (!campo.getText().equals("")) {
					String nuevaFecha;
					switch(e.keyCode) {
					case SWT.ARROW_UP:
						System.out.println("Arriba");
						nuevaFecha = FechaUtil.ajustarFecha(campo.getText(), 1);
						campo.setText(nuevaFecha);
						break;
					case SWT.ARROW_DOWN:
						System.out.println("Abajo");
						nuevaFecha = FechaUtil.ajustarFecha(campo.getText(), -1);
						campo.setText(nuevaFecha);
						break;
					default: System.out.println(e.character);
					}
				} else {
					// verificamos si el campo de fecha default tiene algún valor y sino
					// agarramos la fecha actual del sistema
					if (campoFechaDefault != null && !campoFechaDefault.equals("")) {
						campo.setText(campoFechaDefault.getText());
					} else {
						campo.setText(FechaUtil.toString(new Date()));
					}
				}
			}
		};
	}

}
