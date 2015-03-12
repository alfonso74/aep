package rcp.manticora.controllers;

import rcp.manticora.dao.PaisDAO;
import rcp.manticora.model.Pais;

public class PaisesController extends AbstractControllerNew<Pais> {
	
	/**
	 * Crea una instancia de este controller
	 * @param editorID:  nombre del editor que será utilizado para identificar
	 * de manera única la sesión de este controller.
	 */
	public PaisesController(String editorId) {
		super(editorId, new PaisDAO());
	}
	
}
