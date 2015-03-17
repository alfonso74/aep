package rcp.manticora.model;

import rcp.manticora.services.ComboDataItem;

public enum Fuente {
	
	EMAIL("Correo electrónico"),
	WEB("Página web"),
	TEL("Teléfono/fax"),
	WALK("Walk-in"),
	OTRO("Otros"),
	;
	
//	comboFuente.setItems(new String[] {"Correo electrónico", "Página web", "Teléfono/fax", "Walk-in"});

	private String descripcion;
	
	private Fuente(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getDescripcion() {
		return descripcion;
	}
	
	public ComboDataItem toComboDataItem() {
		ComboDataItem cdItem = new ComboDataItem();
		cdItem.setKey(this.name());
		cdItem.setTexto(getDescripcion());
		cdItem.setObjeto(this);
		return cdItem;
	}
	
	public static final String[] getAsStringArray() {
		String[] array = new String[values().length];
		for (int n=0; n < values().length; n++) {
			array[n] = values()[n].getDescripcion();
		}
		return array;
	}
	
}
