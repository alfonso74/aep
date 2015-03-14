package rcp.manticora.model;

import rcp.manticora.IEditableDocument;

public class TipoCliente implements IEditableDocument {
	private Long idTipo = -1L;
	private String descripcion;

	public TipoCliente() {
	}
	
	public TipoCliente(Long idTipo, String descripcion) {
		this.idTipo = idTipo;
		this.descripcion = descripcion;
	}
	
// ************************** métodos especiales *****************************
	
	public String getTituloDocumento() {
		String tituloDocumento = "Nuevo";
		if (descripcion == null) {
			tituloDocumento = "Nuevo tipo de cliente";
		} else {
			tituloDocumento = "Tipo: " + descripcion;
		}
		return tituloDocumento;
	}
	
	@Override
	public String toString() {
		return "Tipo cliente (id-desc): " + getIdTipo() + "-" + getDescripcion();
	}

	
// *************************** getters y setters *****************************

	public String getDescripcion() {
		return descripcion;
	}

	public Long getIdTipo() {
		return idTipo;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public void setIdTipo(Long idTipo) {
		this.idTipo = idTipo;
	}
}
