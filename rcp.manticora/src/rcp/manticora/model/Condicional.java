package rcp.manticora.model;

public enum Condicional {

	SI(1, "true", "Sí"),
	NO(0, "false", "No");
	
	private int databaseId;
	private String code;
	private String descripcion;
	
	
	private Condicional(int databaseId, String code, String descripcion) {
		this.databaseId = databaseId;
		this.code = code;
		this.descripcion = descripcion;
	}
	
	
	public int getDatabaseId() {
		return databaseId;
	}

	public String getCode() {
		return code;
	}

	public String getDescripcion() {
		return descripcion;
	}


	public static Condicional getFromDatabaseId(int id) {
		for (Condicional v : Condicional.values()) {
			if (v.databaseId == id) {
				return v;
			}
		}
		throw new IllegalArgumentException("El Condicional con id " + id + " no existe.");
	}
}
