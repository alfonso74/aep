package rcp.manticora.controllers;

import rcp.manticora.dao.VendedorDAO;
import rcp.manticora.model.Vendedor;

public class VendedoresController extends AbstractControllerNew<Vendedor> {

	/**
	 * Crea una instancia de este controller
	 * @param editorID:  nombre del editor que ser� utilizado para identificar
	 * de manera �nica la sesi�n de este controller.
	 */
	public VendedoresController(String editorId) {
		super(editorId, new VendedorDAO());
	}
	
}
