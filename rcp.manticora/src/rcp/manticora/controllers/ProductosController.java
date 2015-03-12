package rcp.manticora.controllers;

import rcp.manticora.dao.ProductoDAO;
import rcp.manticora.model.Producto;
import rcp.manticora.services.Productos;

public class ProductosController extends AbstractControllerNew<Producto> {
	private Productos productos;
	
	/**
	 * Crea una instancia de este controller
	 * @param editorID:  nombre del editor que será utilizado para identificar
	 * de manera única la sesión de este controller.
	 */
	public ProductosController(String editorId) {
		super(editorId, new ProductoDAO());
	}
	
	
	/**
	 * Obtiene el precio de un producto
	 * @param idProducto código del producto
	 * @param tipoPrecio tipo de precio que se desea obtener
	 * @return el precio del producto
	 */
	public Float obtenerPrecio(Long idProducto, String tipoPrecio) {
		Producto p = productos.getProductoByIdProducto(idProducto);
		Float precio = p.getPrecioByTipo(tipoPrecio);
		return precio;
	}

}
