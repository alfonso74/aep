package rcp.manticora.model;

import rcp.manticora.IEditableDocument;
import rcp.manticora.services.ComboDataItem;

public class TipoCliente implements IEditableDocument, IComboDataItem {
	private Long idTipo = -1L;
	private String descripcion;
	private String estado;
	private String dspEstado;

	public TipoCliente() {
	}
	
	public TipoCliente(Long idTipo, String descripcion) {
		this.idTipo = idTipo;
		this.descripcion = descripcion;
	}
	
// ************************** métodos especiales *****************************
	
	public String getTituloDocumento() {
		String tituloDocumento = "Nuevo tipo de cliente";
		tituloDocumento = descripcion == null ? tituloDocumento : "Tipo: " + descripcion;
		return tituloDocumento;
	}
	
	public ComboDataItem toComboDataItem() {
		ComboDataItem cdItem = new ComboDataItem();
		cdItem.setKey(getIdTipo().toString());
		cdItem.setTexto(getDescripcion());
		cdItem.setObjeto(this);
		return cdItem;
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
