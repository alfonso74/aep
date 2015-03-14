package rcp.manticora.controllers;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import rcp.manticora.dao.UsuarioDAO;
import rcp.manticora.model.Rol;
import rcp.manticora.model.Usuario;


public class UsuariosController extends AbstractControllerNew<Usuario> {

	/**
	 * Crea una instancia de este controller
	 * @param editorID:  nombre del editor que será utilizado para identificar
	 * de manera única la sesión de este controller.
	 */
	public UsuariosController(String editorId) {
		super(editorId, new UsuarioDAO());
	}
	
	
	/**
	 * Genera el listado de roles existentes en el sistema
	 * @return Array con roles ordenados
	 */
	public Rol[] getListadoRoles() {
		RolesController controller = new RolesController(getEditorId());
		List<Rol> listaRoles = controller.getListadoRoles();
		Rol[] roles = (Rol[]) listaRoles.toArray(new Rol[listaRoles.size()]);
		return roles;
	}
	
	
	/**
	 * Genera el listado de roles definidos en el sistema
	 * @return LinkedHashSet de roles
	 */
	public Set<Rol> getListaRoles() {
		RolesController controller = new RolesController(getEditorId());
		Set<Rol> listaRoles = new LinkedHashSet<Rol>();  // LinkedHashSet mantiene el ordenamiento de la lista original
		listaRoles.addAll(controller.getListadoRoles());
		return listaRoles;
	}
	
}