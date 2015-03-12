package rcp.manticora.controllers;

import rcp.manticora.dao.RedDAO;
import rcp.manticora.model.Red;


public class RedesController extends AbstractControllerNew<Red> {
	
	/**
	 * Crea una instancia de este controller
	 * @param editorID:  nombre del editor que ser� utilizado para identificar
	 * de manera �nica la sesi�n de este controller.
	 */
	
	public RedesController(String editorId) {
		super(editorId, new RedDAO());
	}

}
