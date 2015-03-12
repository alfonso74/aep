package rcp.manticora.model;

public class LineaHospedajeInt extends LineaHospedajeAdapter {
	
	private Habitacion habitacion;
	
	
//	 *************************** métodos especiales ***************************
	
	@Override
	public String toString() {
		return "HospAEP (id-idHabit): " + getIdLinea() + "-" + getHabitacion().getIdHabitacion();
	}
	
//	 ***************************** getters y setters ***************************

	public Habitacion getHabitacion() {
		return habitacion;
	}

	public void setHabitacion(Habitacion habitacion) {
		this.habitacion = habitacion;
	}
	
}
