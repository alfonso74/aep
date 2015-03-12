package rcp.manticora.controllers;

import java.util.Iterator;
import java.util.Set;

import rcp.manticora.dao.TourDAO;
import rcp.manticora.model.DisponibilidadTour;
import rcp.manticora.model.LineaTemplate;
import rcp.manticora.model.LineaTour;
import rcp.manticora.model.Template;
import rcp.manticora.model.Tour;


public class ToursController extends AbstractControllerNew<Tour> {

	/**
	 * Crea una instancia de este controller
	 * @param editorID:  nombre del editor que será utilizado para identificar
	 * de manera única la sesión de este controller.
	 */
	public ToursController(String editorId) {
		super(editorId, new TourDAO());
	}
	
	
	public Template getTemplateById(Long id) {
		TemplatesController tc = new TemplatesController(getEditorId());
		Template registro = tc.getTemplateById(id);
		return registro;
	}

	public void agregarActividad(Tour registro, LineaTour linea) {
		registro.agregarActividad(linea);
	}
	
	public void eliminarActividad(Tour registro, LineaTour linea) {
		registro.eliminarActividad(linea);
	}
	
	public void eliminarDisponibilidad(Tour registro, DisponibilidadTour linea) {
		registro.eliminarDisponibilidad(linea);
	}
	
	public void importarLineasFromTemplate(Tour registro, Long idTemplate) {
		Template t = getTemplateById(idTemplate);
		Set listaActividades = t.getListaActividades();
		Iterator it = listaActividades.iterator();
		LineaTemplate lineaTemplate = null;
		LineaTour lineaTour = null;
		while (it.hasNext()) {
			lineaTemplate = (LineaTemplate) it.next();
			lineaTour = new LineaTour();
			//lineaTour.setIdProducto(lineaTemplate.getIdProducto());
			//lineaTour.setDspProducto(lineaTemplate.getDspProducto());
			lineaTour.setProducto(lineaTemplate.getProducto());
			lineaTour.setDia(lineaTemplate.getDia());
			lineaTour.setSecuencia(lineaTemplate.getSecuencia());
			lineaTour.setTipoReserva(lineaTemplate.getTipoReserva());
			System.out.println("Comidas: " + lineaTemplate.getComidas());
			lineaTour.setComidas(lineaTemplate.getComidas());
			lineaTour.setComentario(lineaTemplate.getComentario());
			registro.agregarActividad(lineaTour);
		}
	}
}
