package rcp.manticora.acciones;

import java.util.Date;


import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.ViewPart;

import rcp.manticora.controllers.CotizacionesController;
import rcp.manticora.editors.CommonEditorInput;
import rcp.manticora.editors.CotizacionesEditor;
import rcp.manticora.editors.SolicitudesEditor;
import rcp.manticora.model.Cotizacion;
import rcp.manticora.model.Pax;
import rcp.manticora.model.Solicitud;
import rcp.manticora.views.IRefreshView;

public class GenerarCotizacionAction implements IEditorActionDelegate {
	private CotizacionesController editorController;
	//private IWorkbenchPage wbp;
	private SolicitudesEditor targetEditor;
	//private EditorPart targetEditor;

	public void setActiveEditor(IAction action, IEditorPart targetEditor) {
		this.targetEditor = (SolicitudesEditor) targetEditor;
	}

	public void run(IAction action) {
		//controller = HibernateController.getInstance();
		
		if (targetEditor.isDirty()) {
			MessageDialog.openWarning(targetEditor.getSite().getShell(), "Informaci�n",
					"Debe guardar los cambios antes de generar la cotizaci�n.");
			return;
		} else {
			// falta agregar la validaci�n de la solicitud antes de generar la cotizaci�n
			Solicitud s = (Solicitud) ((CommonEditorInput) targetEditor.getEditorInput()).getElemento();
			Long pIdSolicitud = s.getIdSolicitud();
			Date pFechaIni = s.getFechaInicio();
			Date pFechaFin = s.getFechaFin();
			String pNombre = s.getNombre();
			String pApellido = s.getApellido();
			String pANombreDe = s.getNombre() + " " + s.getApellido();
			int pPaxs = 0;
			Long pIdVendedor = s.getIdVendedor();
			Date pFinalizacion = new Date();
			// confirmamos que se desea genera una cotizaci�n para la solicitud abierta
			boolean respuesta = MessageDialog.openConfirm(targetEditor.getSite().getShell(), "Confirmaci�n",
					"Desea generar una cotizaci�n para " + pANombreDe + "?");
			if (respuesta) {
				// actualizamos la solicitud y la grabamos con su nuevo status, agregando la
				// fecha de finalizaci�n y la cotizaci�n generado
				//targetEditor.setTxtEstado("Procesada");
				targetEditor.setFechaFinalizacion(pFinalizacion);
				targetEditor.addDirtyFlag();    // lo ensuciamos para poder grabar
				targetEditor.getSite().getPage().saveEditor(targetEditor, false);
				
				System.out.println("Generando cotizaci�n... " + ((SolicitudesEditor) targetEditor).getComentario());
				
				/*
				Cotizacion registro = controller.addCotizacion(-1L, pFechaIni, pFechaFin, pANombreDe, 0, 0, 0, 5, 0,
						0, 100, pPaxs, "A");
				registro.setIdVendedor(pIdVendedor);
				// agregamos el pasajero "default"
				Pax p = new Pax();
				p.setNombre(pNombre);
				p.setApellido(pApellido);
				p.setIdPais(s.getIdPais());
				registro.agregarPax(p);
				// y actualizamos los registros de la cotizaci�n
				controller.update(registro);
				*/
				
				editorController = new CotizacionesController(CotizacionesEditor.ID);
				Cotizacion registro = new Cotizacion();
				//registro.setCliente(cliente);
				//registro.setProspecto(pProspecto);
				registro.setNombre(pANombreDe);
				registro.setFechaInicio(pFechaIni);
				registro.setFechaFin(pFechaFin);
				registro.setSubtotal(0f);
				registro.setPorcHospedaje(0f);
				registro.setHospedaje(0f);
				registro.setPorcImpuesto(5f);
				registro.setImpuesto(0f);
				registro.setTotal(0f);
				registro.setPago(0f);
				registro.setPorcPago(0f);
				registro.setPaxs(pPaxs);
				registro.setIdVendedor(pIdVendedor);
				registro.setIdSolicitud(pIdSolicitud);
				System.out.println("Vendedor: " + pIdVendedor);
				//registro.setDspVendedor(pVendedor);
				registro.setEstado("A");
				
				Pax p = new Pax();
				p.setNombre(pNombre);
				p.setApellido(pApellido);
				p.setIdPais(s.getIdPais());
				registro.agregarPax(p);
				
				editorController.doSave(registro);
				
				System.out.println("Cotizaci�n generada: " + registro);
				// actualizamos la vista que presenta las cotizaciones
				actualizarVistas();
				// c�digo para abrir el nuevo documento de cotizaci�n
				CommonEditorInput input = new CommonEditorInput(registro);
				//input.setName("Nuevo");
				try {
					// abrimos un editor para la cotizaci�n
					targetEditor.getSite().getPage().openEditor(input, CotizacionesEditor.ID);
					// y cerramos el editor de la solicitud
					targetEditor.getSite().getPage().closeEditor(targetEditor, false);
				} catch (PartInitException e) {
					e.printStackTrace();
				}
			} else {
				MessageDialog.openInformation(targetEditor.getSite().getShell(), "Informaci�n", "La acci�n ha sido cancelada.");
			}
		}
	}

	public void selectionChanged(IAction action, ISelection selection) {
	}
	
	
//	TODO: evaluar poner esto en alguna clase en com�n
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
