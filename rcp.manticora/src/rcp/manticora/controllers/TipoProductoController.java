package rcp.manticora.controllers;

import rcp.manticora.dao.TipoProductoDAO;
import rcp.manticora.model.TipoProducto;

public class TipoProductoController extends AbstractControllerNew<TipoProducto> {

	/**
	 * Crea una instancia de este controller
	 * @param editorID:  nombre del editor que será utilizado para identificar
	 * de manera única la sesión de este controller.
	 */
	public TipoProductoController(String editorId) {
		super(editorId, new TipoProductoDAO());
	}
	
}
