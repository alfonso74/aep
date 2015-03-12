package rcp.manticora.acciones;

import java.util.Date;
import java.util.Set;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.ViewPart;

import rcp.manticora.controllers.HojaServicioController;
import rcp.manticora.editors.CommonEditorInput;
import rcp.manticora.editors.CotizacionesEditor;
import rcp.manticora.editors.HojaVentasEditor;
import rcp.manticora.model.Cotizacion;
import rcp.manticora.model.HojaServicioVentas;
import rcp.manticora.model.LineaCotizacion;
import rcp.manticora.model.Pax;
import rcp.manticora.views.IRefreshView;

public class GenerarHojaAction implements IEditorActionDelegate {
	//private HibernateController controller;
	private HojaServicioController hsController;
	//private IEditorPart targetEditor;
	private CotizacionesEditor targetEditor;

	public void setActiveEditor(IAction action, IEditorPart targetEditor) {
		this.targetEditor = (CotizacionesEditor) targetEditor;
	}

	public void run(IAction action) {
		//controller = HibernateController.getInstance();
		
		if (targetEditor.isDirty()) {
			MessageDialog.openWarning(targetEditor.getSite().getShell(), "Información",
					"Debe guardar los cambios antes de generar la hoja de servicio.");
			return;
		};
		if (!targetEditor.validarGeneracionHS()) {
			System.out.println("Error de validación");
			return;
		} else {
			Cotizacion c = (Cotizacion) ((CommonEditorInput) targetEditor.getEditorInput()).getElemento();
			//Cotizacion c = targetEditor.getCotizacion();
			Long pIdCotizacion = c.getIdCotizacion();
			Date pFechaIni = c.getFechaInicio();
			Date pFechaFin = c.getFechaFin();
			String pNombre = c.getNombre();
			Integer pPaxs = c.getPaxs();
			String pEstado = "N";
			Date pFechaFinalizacion = new Date();
			Set<Pax> paxs = c.getListaPaxs();
			Set<LineaCotizacion> lineas = c.getListaActividades();
			
			boolean respuesta = MessageDialog.openConfirm(targetEditor.getSite().getShell(), "Confirmación",
					"Desea generar la hoja de servicio para " + pNombre + "?");
			if (respuesta) {
				// actualizamos el status de la cotización y grabamos
				//targetEditor.setTxtEstado("Procesada");
				targetEditor.setFechaFinalizacion(pFechaFinalizacion);
				targetEditor.addDirtyFlag();
				targetEditor.getSite().getPage().saveEditor(targetEditor, false);
				
				// generamos la hoja de servicio
				System.out.println("Generando hoja de servicio...");
				/*
				HojaServicio registro = controller.addHojaServicio(-1L, pIdCotizacion,
						pFechaIni, pFechaFin, pNumero, pNombre, pPaxs, pEstado);
				*/
				hsController = new HojaServicioController(HojaVentasEditor.ID);
				HojaServicioVentas registro = new HojaServicioVentas();
				registro.setIdCotizacion(pIdCotizacion);
				registro.setIdProducto(0L);
				registro.setFechaInicio(pFechaIni);
				registro.setFechaFin(pFechaFin);
				registro.setNombre(pNombre);
				registro.setPaxs(pPaxs);
				registro.setEstado(pEstado);
				registro.setCliente(c.getCliente());
				registro.importarPAXsCotizacion(paxs);
				hsController.importarLineasCotizacion(registro, lineas);
				
				//copiarPAXs2HS(registro, paxs);
				//controller.update(registro);
				hsController.doSave(registro);
				// confirmamos la creación de la hoja de servicio
				System.out.println("Hoja de servicio generada: " + registro.getIdHoja());
				// actualizamos las vistas abiertas por el usuario final
				actualizarVistas();
				// abrimos la nueva hoja de servicio
				CommonEditorInput input = new CommonEditorInput(registro);
				//input.setName("Nueva");
				try {
					// abrimos un editor para la nueva hoja de servicio
					targetEditor.getSite().getPage().openEditor(input, HojaVentasEditor.ID);
					// cerramos la cotización
					targetEditor.getSite().getPage().closeEditor(targetEditor, false);
				} catch (PartInitException e) {
					e.printStackTrace();
				}
			} else {
				MessageDialog.openInformation(targetEditor.getSite().getShell(), "Información", "La acción ha sido cancelada.");
			}
		}
	}

	public void selectionChanged(IAction action, ISelection selection) {
	}
	
	/*
	public void copiarPAXs2HS(HojaServicio registro, Set<Pax> paxs) {
		Iterator iterator = paxs.iterator();
		while (iterator.hasNext()) {
			Pax pax = (Pax) iterator.next();
			registro.agregarPax(pax);
		}
	}
	*/
	
	//TODO: evaluar poner esto en alguna clase en común
	private void actualizarVistas() {
		IViewReference[] viewRef = targetEditor.getSite().getPage().getViewReferences();
		for (int n = 0; n < viewRef.length; n++) {
			ViewPart vista = (ViewPart) viewRef[n].getView(true);
			if (vista instanceof IRefreshView) {
				((IRefreshView) vista).refrescar();
			}
		}
	}
}
