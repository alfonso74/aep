package rcp.manticora.controllers;

import rcp.manticora.dao.TemplateDAO;
import rcp.manticora.model.LineaTemplate;
import rcp.manticora.model.Template;

public class TemplatesController extends AbstractControllerNew<Template> {

	/**
	 * Crea una instancia de este controller
	 * @param editorID:  nombre del editor que será utilizado para identificar
	 * de manera única la sesión de este controller.
	 */
	public TemplatesController(String editorId) {
		// el super() inicializa la sesión de hibernate con el editorId y template indicado
		super(editorId, new TemplateDAO());
	}
	
	public Template getTemplateById(Long id) {
		Template registro = getDAO().findById(id, true);
		return registro;
	}
	
	public void agregarActividad(Template registro, LineaTemplate linea) {
		registro.agregarActividad(linea);
	}
	
	public void eliminarActividad(Template registro, LineaTemplate linea) {
		registro.eliminarActividad(linea);
	}
}
