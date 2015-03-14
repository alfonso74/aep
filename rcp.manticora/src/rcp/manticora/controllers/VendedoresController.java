package rcp.manticora.controllers;

import rcp.manticora.dao.VendedorDAO;
import rcp.manticora.model.Vendedor;

public class VendedoresController extends AbstractControllerNew<Vendedor> {

	/**
	 * Crea una instancia de este controller
	 * @param editorID:  nombre del editor que será utilizado para identificar
	 * de manera única la sesión de este controller.
	 */
	public VendedoresController(String editorId) {
		super(editorId, new VendedorDAO());
	}
	
}
