package rcp.manticora.controllers;

import rcp.manticora.dao.TipoClienteDAO;
import rcp.manticora.model.TipoCliente;

public class TipoClientesController extends AbstractControllerNew<TipoCliente> {

	/**
	 * Crea una instancia de este controller
	 * @param editorID:  nombre del editor que ser� utilizado para identificar
	 * de manera �nica la sesi�n de este controller.
	 */
	public TipoClientesController(String editorId) {
		super(editorId, new TipoClienteDAO());
	}
	
}
