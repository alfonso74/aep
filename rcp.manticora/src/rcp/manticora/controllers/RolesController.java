package rcp.manticora.controllers;

import java.util.List;

import rcp.manticora.dao.RolDAO;
import rcp.manticora.model.Rol;


public class RolesController extends AbstractControllerNew<Rol> {
	//private RolDAO dao;
	
	/**
	 * Crea una instancia de este controller
	 * @param editorID:  nombre del editor que será utilizado para identificar
	 * de manera única la sesión de este controller.
	 */
	public RolesController(String editorId) {
		super(editorId, new RolDAO());
		//dao = new RolDAO();
		//dao.setSession(session);
	}
	
	public List<Rol> getListadoRoles() {
		return ((RolDAO) getDAO()).findAllAscending();
	}

}
