package rcp.manticora.model;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import rcp.manticora.IEditableDocument;


public class Usuario implements IEditableDocument {
	private Long idUsuario = -1L;
	private String nombre;
	private String apellido;
	private String userName;
	private String password;
	private String estado;
	private String dspEstado;
	
	private Set<Rol> listaRoles;
	
	public Usuario() {
		listaRoles = new HashSet<Rol>();
	}
	
	
// ******************************** métodos especiales ********************************

	public String getTituloDocumento() {
		String titulo = "Nuevo usuario";
		titulo = getNombre() == null ? titulo : getNombreCompleto();
		return titulo;
	}
	
	public String getNombreCompleto() {
		return getNombre() + " " + getApellido();
	}
	
	@Override
	public String toString() {
		return "Usuario (id-nombre): " + getIdUsuario() + "-" + getNombreCompleto();
	}
	
	/**
	 * Permite indicar los roles que han sido asignados a este usuario
	 * @param roles Listado (Set) de roles que tiene habilitados el usuario
	 */
	public void asignarRoles(Set<Rol> roles) {
		listaRoles.clear();
		listaRoles.addAll(roles);
	}
	
	/**
	 * Permite obtener un listado de roles en un array de tipo String
	 * @return Array tipo String con los roles del usuario
	 */
	public String[] getListaRolesAsString() {
		String[] roles = new String[listaRoles.size()];
		Iterator<Rol> it = listaRoles.iterator();
		int n = 0;
		while (it.hasNext()) {
			roles[n++] = it.next().getDescripcion();
		}
		return roles;
	}
	
	/**
	 * Determina si el usuario tiene asignado el rol indicado
	 * @param rol Rol que queremos determinar si está asignado al usuario
	 * @return true o false
	 */
	public boolean hasRol(String rol) {
		for (Rol rolAsignado : listaRoles) {
			if (rolAsignado.getDescripcion().equalsIgnoreCase(rol)) {
				return true;
			}
		}
		return false;
	}
	
// *********************************** getters y setters ******************************
	
	
	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}


	public String getDspEstado() {
		return dspEstado;
	}

	public void setDspEstado(String dspEstado) {
		this.dspEstado = dspEstado;
	}


	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}


	public Long getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Long idUsuario) {
		this.idUsuario = idUsuario;
	}


	public Set<Rol> getListaRoles() {
		return listaRoles;
	}

	public void setListaRoles(Set<Rol> listaRoles) {
		this.listaRoles = listaRoles;
	}


	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}


	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}


	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
}

