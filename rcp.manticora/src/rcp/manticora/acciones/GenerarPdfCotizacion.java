package rcp.manticora.acciones;


import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.core.runtime.preferences.IPreferencesService;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

import rcp.manticora.Application;
import rcp.manticora.editors.CotizacionesEditor;
import rcp.manticora.model.Cotizacion;
import rcp.manticora.pdf.Cotizacion2Pdf;

public class GenerarPdfCotizacion implements IEditorActionDelegate {
	private CotizacionesEditor targetEditor;

	public void setActiveEditor(IAction action, IEditorPart targetEditor) {
		this.targetEditor = (CotizacionesEditor) targetEditor;		
	}

	public void run(IAction action) {
		boolean resumida = false;
		System.out.println("ID de la accion: " + action.getId());
		if (action.getId().toLowerCase().contains("resumida")) {
			resumida = true;
		}
		
		if (targetEditor.isDirty()) {
			MessageDialog.openWarning(targetEditor.getSite().getShell(), "Información",
					"Debe guardar los cambios antes de generar la cotización.");
			return;
		};
		if (!targetEditor.validarGeneracionPdf()) {
			System.out.println("Error de validación");
			return;
		}

		DirectoryDialog dlg = new DirectoryDialog(targetEditor.getSite().getShell());
		//dlg.setFilterPath("Ruta guardada");
		dlg.setText("Generar archivo PDF");
		dlg.setMessage("Seleccione el directorio destino");
		dlg.setFilterPath(obtenerDirectorioDefault());
		String directorio = dlg.open();
		if (directorio == null) {
			// mensaje de acción cancelada por el usuario final
			MessageDialog.openInformation(targetEditor.getSite().getShell(), "Generar archivo PDF",
					"La acción ha sido cancelada.");
			return;
		}
		actualizarDirectorioDefault(directorio);
		
		String pNombre = targetEditor.getPartName();
		System.out.println("Generando pdf para cotización: " + pNombre);
		Cotizacion cotizacion = targetEditor.getCotizacion();
		System.out.println("Subtotal: " + cotizacion.getSubtotal());
		if (cotizacion == null) {
			System.out.println("Cotización NULL");
		}
		Cotizacion2Pdf cot2PDF = new Cotizacion2Pdf(cotizacion);
		if (cot2PDF.generarPdf(directorio, resumida)) {	
			MessageDialog.openInformation(targetEditor.getSite().getShell(), "Generar archivo PDF",
				"Se ha generado exitosamente la cotización en formato PDF.\n\nDirectorio: " + directorio);
		} else {
			MessageDialog.openError(targetEditor.getSite().getShell(), "Generar archivo PDF",
				cot2PDF.getMensajeError());
		}
	}

	public void selectionChanged(IAction action, ISelection selection) {
	}
	
	/**
	 * Accesa el preference store de Eclipse y retorna el directorio default para la
	 * generación de los archivos PDF
	 * @return
	 */
	private String obtenerDirectorioDefault() {
		IPreferencesService service = Platform.getPreferencesService();
		String directorio = service.getString(Application.PLUGIN_ID, "rutaPdf", null, null);
		return directorio;
	}
	
	/**
	 * Guarda en el preference store de Eclipse el directorio que ha especificado
	 * el usuario final para la generación de los archivos PDF
	 * @param directorio Directorio seleccionado por el usuario
	 */
	private void actualizarDirectorioDefault(String directorio) {
		// también podía usar IEclipsePreferences pero aparentemente no ofrece mayores
		// beneficios en esta función
		Preferences preferences = new ConfigurationScope().getNode(Application.PLUGIN_ID);
		preferences.put("rutaPdf", directorio);
		try {
			preferences.flush();
		} catch (BackingStoreException e) {
			e.printStackTrace();
		}
	}

}
