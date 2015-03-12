package rcp.manticora.controllers;

import rcp.manticora.dao.GuiaDAO;
import rcp.manticora.model.Guia;

public class GuiasController extends AbstractControllerNew<Guia> {
	
	/**
	 * Crea una instancia de este controller
	 * @param editorID:  nombre del editor que será utilizado para identificar
	 * de manera única la sesión de este controller.
	 */
	public GuiasController(String editorId) {
		super(editorId, new GuiaDAO());
	}
	
}
