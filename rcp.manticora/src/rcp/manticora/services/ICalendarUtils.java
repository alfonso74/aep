package rcp.manticora.services;

import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public interface ICalendarUtils {
	
	public SelectionAdapter crearCalendario(Shell shell, Text txtCampo);
	
	public SelectionAdapter crearCalendario(Shell shell, Text txtCampo, Text txtFechaDefault);
	
	public KeyAdapter crearKeyAdapter(Text txtCampo);

}
