package rcp.manticora.controllers;

import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import rcp.manticora.dao.CotizacionDAO;
import rcp.manticora.model.Cotizacion;
import rcp.manticora.model.LineaCotizacion;
import rcp.manticora.model.LineaTemplate;
import rcp.manticora.model.Pax;
import rcp.manticora.model.Template;
import rcp.manticora.services.CotizacionesComparator;
import rcp.manticora.services.FechaUtil;


public class CotizacionesController extends AbstractControllerNew<Cotizacion>{
	
	
	public CotizacionesController(String editorId) {
		super(editorId, new CotizacionDAO());
	}
	

	public Template getTemplateById(Long id) {
		TemplatesController tc = new TemplatesController(getEditorId());
		Template registro = tc.getTemplateById(id);
		return registro;
	}
	
	
	public Set<LineaCotizacion> generarLineasFromTemplate(Cotizacion registro, Long idTemplate,
			Date fechaBase, Integer noPaxs, String tipoPrecio) {
		ProductosController pc = new ProductosController(getEditorId());
		Template t = getTemplateById(idTemplate);
		Set<LineaTemplate> actividadesTemplate = t.getListaActividades();
		//Set<LineaCotizacion> actividadesCotizacion = new LinkedHashSet<LineaCotizacion>();
		Iterator<LineaTemplate> it = actividadesTemplate.iterator();
		LineaTemplate lt = null;
		LineaCotizacion lc = null;
		int seq = 1000;    // la reenumeración asignará la secuencia correcta 
		while (it.hasNext()) {
			lt = it.next();
			lc = new LineaCotizacion();
			lc.setIdProducto(lt.getIdProducto());
			lc.setDspProducto(lt.getDspProducto());
			lc.setProducto(lt.getProducto());
			lc.setFecha(FechaUtil.ajustarFecha(fechaBase, lt.getDia() - 1));
			lc.setSecuencia(seq++);
			lc.setCantidad(1);
			lc.setPrecio(pc.obtenerPrecio(lt.getIdProducto(), tipoPrecio));
			lc.setEspacios(noPaxs);
			lc.setVisible(true);
			lc.setComentario(lt.getComentario());
			//actividadesCotizacion.add(lc);
			System.out.println(lc);
			registro.agregarActividad(lc);
		}
		//return actividadesCotizacion;
		return null;
	}
	
	
	public Set<LineaCotizacion> reenumerarLineas(Set<LineaCotizacion> lineas) {
		TreeSet<LineaCotizacion> ts = new TreeSet<LineaCotizacion>(new CotizacionesComparator());
		ts.addAll(lineas);
		
		Date fechaBase = new Date();
		Date fechaLinea = fechaBase;
		int secuenciaBase = 1;
		int secuencia = secuenciaBase;
		System.out.println("Luego de agregar al TreeSet:");
		for (LineaCotizacion linea : ts) {
			fechaLinea = linea.getFecha();
			if (fechaLinea.compareTo(fechaBase) == 0) {
			//if (fechaLinea.getTime() == fechaBase.getTime()) {
			//if (fechaLinea.equals(fechaBase)) {              // esta forma de comparar fechas no siempre se compara correctamente
				System.out.println("Base igual a línea (" + linea.getDspProducto() + "): " + fechaBase + ", " + fechaLinea);
				linea.setSecuencia(secuencia++);
			} else {
				System.out.println("Base diferente a línea (" + linea.getDspProducto() + "): " + fechaBase + ", " + fechaLinea);
				fechaBase = fechaLinea;
				secuencia = secuenciaBase;
				linea.setSecuencia(secuencia++);
			}
		}
		return ts;
	}
	
	
	/**
	 * Permite agregar una o varias líneas de cotización a la vez
	 * @param registro Cotización a la que se agregan las líneas
	 * @param lineas línea(s) a ser agregadas (tipo Set)
	 */
	public void agregarActividad(Cotizacion registro, Set<LineaCotizacion> lineas) {
		for (LineaCotizacion linea : lineas) {
			registro.agregarActividad(linea);
		}
	}
	
	public void eliminarActividad(Cotizacion registro, LineaCotizacion linea) {
		registro.eliminarActividad(linea);
	}
	
	public void agregarPax(Cotizacion registro, Pax linea) {
		registro.agregarPax(linea);
	}
	
	public void eliminarPax(Cotizacion registro, Pax linea) {
		registro.eliminarPax(linea);
	}
}
