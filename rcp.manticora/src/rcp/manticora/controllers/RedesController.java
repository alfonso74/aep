package rcp.manticora.controllers;

import rcp.manticora.dao.RedDAO;
import rcp.manticora.model.Red;


public class RedesController extends AbstractControllerNew<Red> {
	
	/**
	 * Crea una instancia de este controller
	 * @param editorID:  nombre del editor que será utilizado para identificar
	 * de manera única la sesión de este controller.
	 */
	
	public RedesController(String editorId) {
		super(editorId, new RedDAO());
	}

}
