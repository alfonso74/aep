package rcp.manticora.model;

import rcp.manticora.IEditableDocument;

public class TipoHabitacion implements IEditableDocument {
	private Long idTipoHabitacion = new Long(-1);
	private Producto hotel;
	private String descripcion;
	private String comentario;
	private String condiciones;

// **************************** métodos especiales **************************
	
	public String getTituloDocumento() {
		String tituloDocumento = "Nuevo";
		if (descripcion == null) {
			tituloDocumento = "Nuevo tipo de hab.";
		} else {
			tituloDocumento = "Tipo: " + descripcion;
		}
		return tituloDocumento;
	}
	
	@Override
	public String toString() {
		return "TipoHabitación (id-Prod-Desc): " + idTipoHabitacion + "-" + hotel.getIdProducto() + "-" + descripcion;
	}

	public String[] getListaCondiciones() {
		// separación por comas
		if (getCondiciones() == null) {
			return new String[]{};
		} else {
			return getCondiciones().split(",");
		}
	}
	
	public void setListaCondiciones(String[] listaCondiciones) {
		String cadena = "";
		// Si no se detallan condiciones, listaCondiciones tendrá largo 1
		// y su valor será ""
		for (int n = 0; n < listaCondiciones.length; n++) {
			if (cadena.equals("")) {
				cadena = listaCondiciones[n];
			} else {
				cadena += "," + listaCondiciones[n];
			}
		}
		setCondiciones(cadena);
	}
	
	// no queremos exponer el campo de condiciones directamente
	private void setCondiciones(String condiciones) {
		this.condiciones = condiciones;
	}
	
	public String getCondiciones() {
		return condiciones;
	}
	

// *************************** getters y setters ******************************
	
	public String getComentario() {
		return comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Long getIdTipoHabitacion() {
		return idTipoHabitacion;
	}

	public void setIdTipoHabitacion(Long idTipoHabitacion) {
		this.idTipoHabitacion = idTipoHabitacion;
	}

	public Producto getHotel() {
		return hotel;
	}

	public void setHotel(Producto productoHospedaje) {
		this.hotel = productoHospedaje;
	}

	

	
	
}
