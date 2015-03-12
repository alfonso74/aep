package rcp.manticora.acciones;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;

import rcp.manticora.editors.CommonEditorInput;


public class GenericAction extends Action {
	private IWorkbenchWindow window;
	private String tituloReporte;
	private String rutaReporte;
	private final String idReporteEditor = "manticora.editors.reporte";
	
	public GenericAction(IWorkbenchWindow window) {
		super();
		this.window = window;
	}
	
	public GenericAction(String nombreAccion, IWorkbenchWindow window) {
		super();
		this.window = window;
		this.setText(nombreAccion);
	}

	@Override
	public void run() {
		CommonEditorInput input = new CommonEditorInput();
		input.setName(getTituloReporte());
		input.setRutaReporte(getRutaReporte());
		try {
			window.getActivePage().openEditor(input, idReporteEditor);
		} catch (PartInitException e) {
			e.printStackTrace();
		}
	}

	public String getTituloReporte() {
		return tituloReporte;
	}

	public void setTituloReporte(String tituloReporte) {
		this.tituloReporte = tituloReporte;
	}

	public String getRutaReporte() {
		return rutaReporte;
	}

	public void setRutaReporte(String rutaReporte) {
		this.rutaReporte = rutaReporte;
	}
	
}
