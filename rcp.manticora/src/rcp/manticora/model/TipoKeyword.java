package rcp.manticora.model;

public enum TipoKeyword {
	
	FORMA_PAGO("Forma de pago"),
	SEXO("Sexo"),
	STATUS_COTIZACION("Status de cotizaciones"),
	STATUS_GENERAL("Status general"),
	STATUS_HOJA_SERV("Status hoja de servicios"),
	STATUS_SOLICITUD("Status de solicitudes"),
	TIPO_AVION("Tipo de avión"),
	TIPO_IMPUESTO("Tipo de impuesto"),
	TIPO_VEHICULO("Tipo de vehículo"),
	;
	
//	comboTipo.setItems(new String[] {"Forma de pago", "Sexo", "Status de cotizaciones", "Status general", "Status hoja de servicios", 
//	"Status de solicitudes", "Tipo de avión", "Tipo de impuesto", "Tipo de vehículo"});
	
	private String descripcion;
	
	private TipoKeyword(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getDescripcion() {
		return descripcion;
	}
	
	public static final String[] getTipoKeywordList() {
		String[] tipoKeywordList = new String[values().length];
		for (int n=0; n < values().length; n++) {
			tipoKeywordList[n] = values()[n].getDescripcion();
		}
		return tipoKeywordList;
	}

}
