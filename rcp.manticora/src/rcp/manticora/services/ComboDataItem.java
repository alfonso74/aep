package rcp.manticora.services;

/**
 * Clase utilizada para agregar items a un ComboData
 * Ej:  key:  A
 *      texto:  Activo
 * @author Carlos Alfonso
 *
 */
public class ComboDataItem {
	
	private String key;
	private String texto;
	private Object objeto;
	
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getTexto() {
		return texto;
	}
	public void setTexto(String texto) {
		this.texto = texto;
	}
	public Object getObjeto() {
		return objeto;
	}
	public void setObjeto(Object objeto) {
		this.objeto = objeto;
	}
	
}
