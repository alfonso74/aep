package rcp.manticora.model;

import rcp.manticora.IEditableDocument;

public class TipoProducto implements IEditableDocument {
	private Long idTipo = new Long(-1);
	private String descripcion;
	private Boolean tour;
	
	public TipoProducto() {};
	
	public TipoProducto(Long idTipo, String descripcion, Boolean tour) {
		this.idTipo = idTipo;
		this.descripcion = descripcion;
		this.tour = tour;
	}
	
	
// ************************** métodos especiales ******************************
	
	public String getTituloDocumento() {
		String tituloDocumento = "Nuevo";
		if (descripcion == null) {
			tituloDocumento = "Nuevo tipo de producto";
		} else {
			tituloDocumento = "Tipo: " + descripcion;
		}
		return tituloDocumento;
	}
	
	@Override
	public String toString() {
		return "TipoProducto (id-desc): " + getIdTipo() + "-" + getDescripcion();
	}
	
	
// **************************** getters y setters *****************************

	public Long getIdTipo() {
		return idTipo;
	}
	
	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public void setIdTipo(Long idTipo) {
		this.idTipo = idTipo;
	}

	public Boolean isTour() {
		return (tour == null) ? Boolean.FALSE : tour;
		//return tour;
	}

	public void setTour(Boolean tour) {
		this.tour = tour;
	}
}
