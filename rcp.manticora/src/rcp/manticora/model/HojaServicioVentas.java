package rcp.manticora.model;

public class HojaServicioVentas extends HojaServicio {
	private Long idCotizacion;
	private Integer paxs;
	private String dspVendedor;
	
	public HojaServicioVentas() {
		super();
		//setClase("V");
	}
	
	
// ***************************** métodos especiales *************************
	
	@Override
	public String toString() {
		return "HojaServicioVentas (id-idCot-nombre): " + getIdHoja() + "-" + getIdCotizacion() + "-" + getNombre();
	}
	
	
// ***************************** getters y setters **************************
	
	public Long getIdCotizacion() {
		return idCotizacion;
	}
	
	public void setIdCotizacion(Long idCotizacion) {
		this.idCotizacion = idCotizacion;
	}
	
	public Integer getPaxs() {
		return paxs;
	}

	public void setPaxs(Integer paxs) {
		this.paxs = paxs;
	}
	
	public String getDspVendedor() {
		String resultado = dspVendedor == null ? "No encontrado" : dspVendedor;
		return resultado;
	}

	public void setDspVendedor(String dspVendedor) {
		this.dspVendedor = dspVendedor;
	}

}
