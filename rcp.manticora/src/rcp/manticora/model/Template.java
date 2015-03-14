package rcp.manticora.model;

import java.util.HashSet;
import java.util.Set;

import rcp.manticora.IEditableDocument;


public class Template implements IEditableDocument {
	private Long idTemplate;
	private Long idProducto;
	private String dspTipoProducto;      // utilizado por los combo box (generado por hibernate)
	private String dspProducto;        // utilizado por combo box (generado por hibernate)
	private String nombre;
	
	private Set<LineaTemplate> listaActividades;
	
	public Template() {
		listaActividades = new HashSet<LineaTemplate>();
	}

	public Template(Long idTemplate, Long idProducto, String nombre) {
		this.idTemplate = idTemplate;
		this.idProducto = idProducto;
		this.nombre = nombre;
		listaActividades = new HashSet<LineaTemplate>();
	}
	

// ************************** métodos especiales *****************************
	
	public String getTituloDocumento() {
		String tituloDocumento = "Nuevo";
		if (nombre == null) {
			tituloDocumento = "Nuevo template";
		} else {
			tituloDocumento = "Template: " + nombre;
		}
		return tituloDocumento;
	}
	
	@Override
	public String toString() {
		return "Template (id-nombre): " + getIdTemplate() + "-" + getNombre();
	}
	
// ************** métodos adicionales para el manejo de los templates *************
	
	public void agregarActividad(LineaTemplate linea) {
		listaActividades.add(linea);
	}
	
	public void eliminarActividad(LineaTemplate linea) {
		listaActividades.remove(linea);
	}
	
	
// *************************** getters y setters *****************************

	public Long getIdTemplate() {
		return idTemplate;
	}
	
	public Long getIdProducto() {
		return idProducto;
	}

	public String getNombre() {
		return nombre;
	}

	public void setIdProducto(Long idProducto) {
		this.idProducto = idProducto;
	}

	public void setIdTemplate(Long idTemplate) {
		this.idTemplate = idTemplate;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDspProducto() {
		return dspProducto;
	}

	public void setDspProducto(String producto) {
		this.dspProducto = producto;
	}

	public Set<LineaTemplate> getListaActividades() {
		return listaActividades;
	}

	public void setListaActividades(Set<LineaTemplate> listaActividades) {
		this.listaActividades = listaActividades;
	}
	
	public String getDspTipoProducto() {
		return dspTipoProducto;
	}

	public void setDspTipoProducto(String tipoProducto) {
		this.dspTipoProducto = tipoProducto;
	}
}
