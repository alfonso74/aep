package rcp.manticora.model;

import rcp.manticora.IEditableDocument;

public class Guia implements IEditableDocument {
	private Long idGuia = -1L;
	private String nombre;
	private String apellido;
	private String estado;
	private String dspEstado;
	
	public Guia() {
	}

	public Guia(Long idGuia, String nombre, String apellido, String estado) {
		this.idGuia = idGuia;
		this.nombre = nombre;
		this.apellido = apellido;
		this.estado = estado;
	}
	

// *************************** métodos especiales *****************************

	public String getTituloDocumento() {
		String tituloDocumento = "Nuevo";
		if (nombre == null && apellido == null) {
			tituloDocumento = "Nuevo guía";
		} else {
			tituloDocumento = "Guía: " + nombre + " " + apellido;
		}
		return tituloDocumento;
	}

	@Override
	public String toString() {
		return "Guía (id-nombre): " + getIdGuia() + "-" + getNombre();
	}
	
	public String getNombreCompleto() {
		return getNombre() + " " + getApellido();
	}
	
	
// **************************** getters y setters *****************************

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

	public Long getIdGuia() {
		return idGuia;
	}

	public void setIdGuia(Long idGuia) {
		this.idGuia = idGuia;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	

}
