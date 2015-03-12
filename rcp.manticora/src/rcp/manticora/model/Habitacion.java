package rcp.manticora.model;

import rcp.manticora.IEditableDocument;

public class Habitacion implements IEditableDocument {
	private Long idHabitacion = new Long(-1);
	private Producto hotel;
	private TipoHabitacion tipo;
	private String nombre;
	private Integer numero;
	private Integer piso;
	private String estado;
	private String dspEstado;
	private String condiciones;

// **************************** métodos especiales **************************
	
	public String getTituloDocumento() {
		String tituloDocumento = "Nuevo";
		if (getNombre() == null) {
			tituloDocumento = "Nueva habitación";
		} else {
			tituloDocumento = "Habit: " + getNombre();
		}
		return tituloDocumento;
	}
	
	@Override
	public String toString() {
		return "Habitación (id-hotel-tipo): " + idHabitacion  + "-" + hotel.getIdProducto() + "-" + tipo.getDescripcion();
	}
	
	public String[] getListaCondiciones() {
		// separación por comas
		if (getCondiciones() == null) {
			System.out.println("Condiciones: NULL");
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
	
//	 no queremos exponer el campo de condiciones directamente
	private void setCondiciones(String condiciones) {
		this.condiciones = condiciones;
	}
	
	public String getCondiciones() {
		return condiciones;
	}
	
// ***************************** getters y setters **************************

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Producto getHotel() {
		return hotel;
	}

	public void setHotel(Producto hotel) {
		this.hotel = hotel;
	}

	public Long getIdHabitacion() {
		return idHabitacion;
	}

	public void setIdHabitacion(Long idHabitacion) {
		this.idHabitacion = idHabitacion;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public TipoHabitacion getTipo() {
		return tipo;
	}

	public void setTipo(TipoHabitacion tipo) {
		this.tipo = tipo;
	}

	public String getDspEstado() {
		return dspEstado;
	}

	public void setDspEstado(String dspEstado) {
		this.dspEstado = dspEstado;
	}

	public Integer getNumero() {
		return numero;
	}

	public void setNumero(Integer numero) {
		this.numero = numero;
	}

	public Integer getPiso() {
		return piso;
	}

	public void setPiso(Integer piso) {
		this.piso = piso;
	}
	
	

}
