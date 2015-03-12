package rcp.manticora.controllers;

import rcp.manticora.dao.TransporteDAO;
import rcp.manticora.model.Transporte;

public class TransporteController extends AbstractControllerNew<Transporte> {

	/**
	 * Crea una instancia de este controller
	 * @param editorID:  nombre del editor que ser� utilizado para identificar
	 * de manera �nica la sesi�n de este controller.
	 */
	public TransporteController(String editorId) {
		super(editorId, new TransporteDAO());
	}

}
