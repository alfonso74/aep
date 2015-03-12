package rcp.manticora.controllers;

import rcp.manticora.dao.TipoHabitacionDAO;
import rcp.manticora.model.Producto;
import rcp.manticora.model.TipoHabitacion;
import rcp.manticora.services.Productos;

public class TipoHabitacionesController extends AbstractControllerNew<TipoHabitacion> {

	/**
	 * Crea una instancia de este controller
	 * @param editorID:  nombre del editor que será utilizado para identificar
	 * de manera única la sesión de este controller.
	 */
	public TipoHabitacionesController(String editorId) {
		super(editorId, new TipoHabitacionDAO());
	}

	/*
	private Session session;
	private TipoHabitacionDAO dao;
	
	public TipoHabitacionesController(String editorId) {
		session = HibernateUtil.getEditorSession(editorId);
		session.setFlushMode(FlushMode.NEVER);
		dao = new TipoHabitacionDAO();
		dao.setSession(session);
	}
	
	public void finalizar(String editorId) {
		System.out.println("Finalizando sesión: " + editorId);
		HibernateUtil.closeEditorSession(editorId);     // graba en la base de datos
	}
	
	public void doSave(TipoHabitacion registro) {
		session.beginTransaction();
		dao.makePersistent(registro);
		dao.flush();            // extiende las modificaciones al caché
		session.getTransaction().commit();
	}
	*/
	
	public TipoHabitacion getTipoHabitacionById(Long id) {
		TipoHabitacion registro = getDAO().findById(id, true);
		return registro;
	}
	
	/**
	 * Obtiene un Producto de acuerdo a su id.  Usado en el doSave() de
	 * TipoHabitaciones.
	 * @param id código del producto
	 * @return El Producto correspondiente al código
	 */
	public Producto getProductoById(Long id) {
		Productos productos = new Productos();
		return productos.getProductoByIdProducto(id);
	}
}
