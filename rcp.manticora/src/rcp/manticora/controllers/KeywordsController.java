package rcp.manticora.controllers;

import rcp.manticora.dao.KeywordDAO;
import rcp.manticora.model.Keyword;

public class KeywordsController extends AbstractControllerNew<Keyword> {
	
	/**
	 * Crea una instancia de este controller
	 * @param editorID:  nombre del editor que será utilizado para identificar
	 * de manera única la sesión de este controller.
	 */
	public KeywordsController(String editorId) {
		super(editorId, new KeywordDAO());
	}

}
