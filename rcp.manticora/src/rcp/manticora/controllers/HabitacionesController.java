package rcp.manticora.controllers;

import rcp.manticora.dao.HabitacionDAO;
import rcp.manticora.model.Habitacion;
import rcp.manticora.model.Producto;
import rcp.manticora.model.TipoHabitacion;
import rcp.manticora.services.Productos;

public class HabitacionesController extends AbstractControllerNew<Habitacion> {
	
	/**
	 * Crea una instancia de este controller
	 * @param editorID:  nombre del editor que ser� utilizado para identificar
	 * de manera �nica la sesi�n de este controller.
	 */
	public HabitacionesController(String editorId) {
		super(editorId, new HabitacionDAO());
	}
	

	/**
	 * Obtiene un Producto de acuerdo a su id.  Usado en el doSave() de
	 * Habitaciones.
	 * @param id c�digo del producto
	 * @return El Producto correspondiente al c�digo
	 */
	public Producto getProductoById(Long id) {
		Productos productos = new Productos();
		return productos.getProductoByIdProducto(id);
	}
	
	
	public TipoHabitacion getTipoHabitacionById(Long id) {
		TipoHabitacionesController thController = new TipoHabitacionesController(getEditorId());
		TipoHabitacion tipo = thController.getTipoHabitacionById(id);
		return tipo;
	}
}
