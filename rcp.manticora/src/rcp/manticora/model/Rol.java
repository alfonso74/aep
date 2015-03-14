package rcp.manticora.model;

import rcp.manticora.IEditableDocument;

public class Rol implements IEditableDocument {
	private Long idRol = -1L;
	private String descripcion;
	private String estado;
	private String dspEstado;
	
	public Rol() {
	}
	
// **************************** métodos especiales *****************************
	
	public String getTituloDocumento() {
		String titulo = "Nuevo rol";
		titulo = getDescripcion() == null ? titulo : getDescripcion();
		return titulo;
	}
	
	@Override
	public String toString() {
		return "Rol (id-nombre): " + getIdRol() + "-" + getDescripcion();
	}
	
// ******************************* getters y setters ***************************
	
	public Long getIdRol() {
		return idRol;
	}

	public void setIdRol(Long idRol) {
		this.idRol = idRol;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getDspEstado() {
		return dspEstado;
	}

	public void setDspEstado(String dspEstado) {
		this.dspEstado = dspEstado;
	}

}
