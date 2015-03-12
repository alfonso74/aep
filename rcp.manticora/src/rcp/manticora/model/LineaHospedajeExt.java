package rcp.manticora.model;

public class LineaHospedajeExt extends LineaHospedajeAdapter {
	
	private TipoHabitacion tipoHabitacion;
	private String noHabitacion;
	
	/*
	public LineaHospedajeExt() {
		listaPaxs = new HashSet<Pax>();
	}
	*/
	
	
//	 *************************** métodos especiales ***************************
	
	@Override
	public String toString() {
		return "HospExt (id-tipoHab): " + getIdLinea() + "-" + getTipoHabitacion().getIdTipoHabitacion();
	}
	
	
//	 ***************************** getters y setters ***************************

	public TipoHabitacion getTipoHabitacion() {
		return tipoHabitacion;
	}
	public void setTipoHabitacion(TipoHabitacion tipoHabitacion) {
		this.tipoHabitacion = tipoHabitacion;
	}

	public String getNoHabitacion() {
		return noHabitacion;
	}
	public void setNoHabitacion(String noHabitacion) {
		this.noHabitacion = noHabitacion;
	}

}

