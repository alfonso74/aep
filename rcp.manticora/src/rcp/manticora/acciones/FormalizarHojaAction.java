package rcp.manticora.acciones;

import java.util.Date;
import java.util.Set;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.part.ViewPart;

import rcp.manticora.controllers.HojaServicioController;
import rcp.manticora.editors.CommonEditorInput;
import rcp.manticora.editors.HojaVentasEditor;
import rcp.manticora.model.AsignacionReserva;
import rcp.manticora.model.DisponibilidadTour;
import rcp.manticora.model.HojaServicioTour;
import rcp.manticora.model.IHojaServicio;
import rcp.manticora.model.IReserva;
import rcp.manticora.model.LineaActividad;
import rcp.manticora.model.ReservaTour;
import rcp.manticora.model.Tour;
import rcp.manticora.views.IRefreshView;


public class FormalizarHojaAction implements IEditorActionDelegate {
	
	private HojaServicioController hsController;
	private HojaVentasEditor targetEditor;
	

	public void setActiveEditor(IAction action, IEditorPart targetEditor) {
		this.targetEditor = (HojaVentasEditor) targetEditor;
	}

	public void run(IAction action) {
		if (targetEditor.isDirty()) {
			MessageDialog.openWarning(targetEditor.getSite().getShell(), "Información",
					"Debe guardar los cambios antes de generar la hoja de servicio.");
			return;
		};
		
		// realizar validaciones antes de pasar a operaciones
		
		
		
// ************************************* pase a operaciones ***********************************
		System.out.println("1");
		hsController = new HojaServicioController(targetEditor.getIdSession());
		System.out.println("2");
		// Obtenemos la hoja de servicio de ventas que estamos por pasar a operaciones, y sus reservas
		IHojaServicio registro = (IHojaServicio) ((CommonEditorInput) targetEditor.getEditorInput()).getElemento();
		System.out.println("3");
		Set<LineaActividad> lineas = registro.getListaActividades();
		for (LineaActividad linea : lineas) {
			for (AsignacionReserva asignacion : linea.getListaAsignaciones()) {
				IReserva reserva = asignacion.getReserva();
				if (reserva instanceof ReservaTour | reserva.getTipoReserva().equals("Tour")) {
					System.out.println("Tour encontrado");
					ReservaTour resTour = ((ReservaTour) reserva);
					DisponibilidadTour dispTour = resTour.getDisponibilidad();
					Tour tour = dispTour.getTour();
					
					//HojaServicioTour hoja = hsController.findHojaServicioByDisponibilidad(dispTour);
					HojaServicioTour hoja = dispTour.getHoja();
					if (hoja != null) {
						// si ya existe una hoja de servicios para la disponibilidad, solo agregamos los paxs
						System.out.println("Importando paxs...");
						hoja.importarPAXsCotizacion(registro.getListaPaxs());
					} else {
						hoja = new HojaServicioTour();
						hoja.setIdProducto(tour.getIdProducto());
						//hoja.setDisponibilidad(dispTour);
						hoja.setNombre(tour.getNombre());
						hoja.setFechaInicio(resTour.getFecha());
						hoja.setFechaFin(resTour.getFecha());
						hoja.setCapacidad(dispTour.getCapacidad());
						hoja.setComentario("NADA");
						
						String numeroTour = hsController.generarNumeroTour();
						hoja.setNumero(numeroTour);
						//dispTour.setNumero(numeroTour);
						dispTour.setHoja(hoja);
						System.out.println("Importando paxs...");
						hoja.importarPAXsCotizacion(registro.getListaPaxs());
						
						hoja.setEstado("N");
						hoja.setFechaCreacion(new Date());
						
						System.out.println("Importando líneas...");
						hsController.importarLineasFromTour(hoja, tour);
					}
					System.out.println("Grabando...");
					hsController.doSave(hoja);
					System.out.println("Hoja generada: " + hoja);
					// indicamos que la línea de actividad tiene una hoja de servicio de operaciones (tour) asociada
					linea.setHojaTour(hoja);
				}
			}
		}
		
		
		// para cada reserva de tour generamos una hoja de servicio de operaciones
		/*
		Set<IReserva> listaReservas = registro.getListaReservas();
		
		for (IReserva reserva : listaReservas) {
			if (reserva instanceof ReservaTour) {
				ReservaTour resTour = ((ReservaTour) reserva);
				DisponibilidadTour dispTour = resTour.getDisponibilidad();
				Tour tour = dispTour.getTour();
				
				HojaServicioTour hoja = new HojaServicioTour();
				hoja.setIdProducto(tour.getIdProducto());
				hoja.setNombre(tour.getNombre());
				hoja.setFechaInicio(resTour.getFecha());
				hoja.setFechaFin(resTour.getFecha());
				hoja.setCapacidad(dispTour.getCapacidad());
				hoja.setComentario("NADA");
				
				hoja.setNumero("0807-058");
				hoja.importarPAXsCotizacion(registro.getListaPaxs());
				
				hoja.setEstado("N");
				hoja.setFechaCreacion(new Date());
				
				System.out.println("Importando...");
				hsController.importarLineasFromTour(hoja, tour);
				System.out.println("Grabando...");
				hsController.doSave(hoja);
				System.out.println("Hoja generada: " + hoja);
			}
		}
		*/
		
		// finalmente actualizamos la hoja de servicio.  Fecha de entrada a operaciones y nuevo estado.
		registro.setFechaOperaciones(new Date());
		//registro.setEstado("P");
		hsController.doSave(registro);
		
		actualizarVistas();
	}

	
	public void selectionChanged(IAction action, ISelection selection) {
	}
	
	
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
