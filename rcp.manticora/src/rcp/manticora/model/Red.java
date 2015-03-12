package rcp.manticora.model;

import rcp.manticora.IEditableDocument;

public class Red implements IEditableDocument {
	private Long idRed = -1L;
	private String descripcion;
	private String estado;
	private String dspEstado;
	
	public Red() {
	}

	// **************************** métodos especiales *****************************
	
	public String getTituloDocumento() {
		String titulo = "Nueva red de viajes";
		titulo = getDescripcion() == null ? titulo : getDescripcion();
		return titulo;
	}
	
	@Override
	public String toString() {
		return "Red (id-nombre): " + getIdRed() + "-" + getDescripcion();
	}

	
	// ******************************* getters y setters ***************************
	
	public Long getIdRed() {
		return idRed;
	}

	public void setIdRed(Long idRed) {
		this.idRed = idRed;
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
