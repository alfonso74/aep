package rcp.manticora.model;

import rcp.manticora.IEditableDocument;

public class Condicion implements IEditableDocument {
	private Long idCondicion = new Long(-1);
	private String descripcion;

// **************************** métodos especiales **************************
	
	public String getTituloDocumento() {
		String tituloDocumento = "Nuevo";
		if (getDescripcion() == null) {
			tituloDocumento = "Nueva condición";
		} else {
			tituloDocumento = "Condición: " + getDescripcion();
		}
		return tituloDocumento;
	}
	
	@Override
	public String toString() {
		return ("Condicion (id-desc): " + getIdCondicion() + "-" + getDescripcion());
	}

//	 ************************ getters y setters *******************************

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Long getIdCondicion() {
		return idCondicion;
	}

	public void setIdCondicion(Long idCondicion) {
		this.idCondicion = idCondicion;
	}

}
