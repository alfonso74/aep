package rcp.manticora.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import rcp.manticora.services.FechaUtil;


public class ReservaHospedaje extends ReservaAdapter {
	
	// idReserva, hoja y listaAsignaciones definidos en ReservaAdapter
	private Producto producto;
	private Date fechaDesde;
	private Date fechaHasta;
	private String noReserva;
	private String confirmadoPor;
	private Date fechaConfirmado;
	private String estado;
	private String dspEstado;
	private String comentario;
	
	private String tipoReserva = "Hospedaje";
	
	private Set<ILineaHospedaje> listaHabitaciones = new HashSet<ILineaHospedaje>();
	
	
//	 **************************** métodos especiales ***************************
	
	public String getTituloDocumento() {
		return "Reserva: " + getIdReserva();
	}
	
	@Override
	public String toString() {
		String texto = "ReservaHospedaje (id-Hoja): " + getIdReserva() + "-" + getHoja().getIdHoja();
		return texto;
	}
	
	public String getFechaDspReserva() {
		String fecha1 = FechaUtil.toString(getFechaDesde(), FechaUtil.formatoFecha);
		String fecha2 = FechaUtil.toString(getFechaHasta(), FechaUtil.formatoFecha);
		String fechaResTxt = fecha1 + " a " + fecha2; 
		return fechaResTxt;
	}
	
	public void agregarLineaHospedaje(ILineaHospedaje linea) {
		listaHabitaciones.add(linea);
	}
	
	public void eliminarLineaHospedaje(ILineaHospedaje linea) {
		listaHabitaciones.remove(linea);
	}
	
	
//	 ***************************** getters y setters ***************************
	
	public String getTipoReserva() {
		return tipoReserva;
	}

	public String getComentario() {
		return comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	public Date getFechaDesde() {
		return fechaDesde;
	}

	public void setFechaDesde(Date fechaDesde) {
		this.fechaDesde = fechaDesde;
	}

	public Date getFechaHasta() {
		return fechaHasta;
	}

	public void setFechaHasta(Date fechaHasta) {
		this.fechaHasta = fechaHasta;
	}

	public String getNoReserva() {
		return noReserva;
	}

	public void setNoReserva(String noReserva) {
		this.noReserva = noReserva;
	}
	
	public String getConfirmadoPor() {
		return confirmadoPor;
	}
	public void setConfirmadoPor(String confirmadoPor) {
		this.confirmadoPor = confirmadoPor;
	}

	public Date getFechaConfirmado() {
		return fechaConfirmado;
	}
	public void setFechaConfirmado(Date fechaConfirmacion) {
		this.fechaConfirmado = fechaConfirmacion;
	}

	/**
	 * Retorna el hotel al que aplica la reserva
	 * @return Producto (siendo siempre un hotel)
	 */
	public Producto getProducto() {
		return producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	public Set<ILineaHospedaje> getListaHabitaciones() {
		return listaHabitaciones;
	}

	public void setListaHabitaciones(Set<ILineaHospedaje> listaHabitaciones) {
		this.listaHabitaciones = listaHabitaciones;
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

	

}
