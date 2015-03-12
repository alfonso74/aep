package rcp.manticora.model;

import rcp.manticora.IEditableDocument;

public class Comision implements IEditableDocument {
	
	private Long idComision = -1L;
	
	private Cliente cliente;
	private Cotizacion cotizacion;
	
	private Float monto = 0F;
	

	public String getTituloDocumento() {
		String tituloDocumento = "Nueva";
		if (cliente == null && cotizacion == null) {
			tituloDocumento = "Nueva comision";
		} else {
			tituloDocumento = "Comision: " + getMonto();
		}
		return tituloDocumento;
	}


	public Long getIdComision() {
		return idComision;
	}


	public void setIdComision(Long idComision) {
		this.idComision = idComision;
	}


	public Cliente getCliente() {
		return cliente;
	}


	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}


	public Cotizacion getCotizacion() {
		return cotizacion;
	}


	public void setCotizacion(Cotizacion cotizacion) {
		this.cotizacion = cotizacion;
	}


	public Float getMonto() {
		return monto;
	}


	public void setMonto(Float monto) {
		this.monto = monto;
	}
	
}
