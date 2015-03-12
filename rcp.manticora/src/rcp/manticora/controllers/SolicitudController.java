package rcp.manticora.controllers;

import rcp.manticora.dao.SolicitudDAO;
import rcp.manticora.model.Solicitud;

public class SolicitudController extends AbstractControllerNew<Solicitud> {
	
	/**
	 * Crea una instancia de este controller
	 * @param editorID:  nombre del editor que será utilizado para identificar
	 * de manera única la sesión de este controller.
	 */
	public SolicitudController(String editorId) {
		super(editorId, new SolicitudDAO());
	}
	
}
