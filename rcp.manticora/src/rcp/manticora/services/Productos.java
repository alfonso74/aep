package rcp.manticora.services;

import java.util.Vector;

import rcp.manticora.controllers.ViewController;
import rcp.manticora.model.Producto;


public class Productos {
	private ViewController controller;
	private Producto[] listaProductos;
	private Producto[] listaProdFiltrados;
	
	public Productos() {
		controller = new ViewController();
		listaProductos = controller.getListadoProductos();
		listaProdFiltrados = listaProductos;
	}
	
	
	private void resetFiltro() {
		listaProdFiltrados = listaProductos;
	}
	
	/**
	 * Filtra el listado de productos en base al tipo indicado. Si resetFiltro es false
	 * se agrega el filtro a un listado que ya ha sido filtrado, en caso contrario se
	 * realiza sobre el listado completo de productos.
	 * @param tipo tipo de producto a filtrar.
	 * @param resetFiltro permite mantener o eliminar los filtros previos.
	 */
	public void filtrarByTipo(Long tipo, boolean resetFiltro) {
		// un producto no puede tener varios tipos, así que no tiene sentido filtrar por un tipo y luego
		// filtrar por otro tipo, ya que el resultado sería nada.  Solo tiene sentido si filtramos primero
		// los tours y luego filtramos por un tipo de producto específico.
		if (resetFiltro)
			resetFiltro();
		listaProdFiltrados = getProductosByTipo(tipo);
	}
	
	
/**
 * Filtra el listado de productos para que solamente presente los que sean tours, y permite
 * mantener un filtro que se haya aplicado previamente.
 * @param resetFiltro permite mantener o eliminar los filtros previos.
 */
	public void filtrarTours(boolean resetFiltro) {
		if (resetFiltro)
			resetFiltro();
		Vector<Producto> listado = new Vector<Producto>();
		for (int i = 0; i < listaProductos.length; i++) {
			if (listaProductos[i].getIsTour().equals("Si")) {
				listado.add(listaProductos[i]);
			}
		}
		System.out.println("No. de tours encontrados: " + listado.size());
		listaProdFiltrados = (Producto[]) listado.toArray(new Producto[listado.size()]);
	}
	
	
	private Producto[] getProductosByTipo(long tipo) {
		Vector<Producto> listado = new Vector<Producto>();
		for (int i = 0; i < listaProdFiltrados.length; i++) {
			if (listaProdFiltrados[i].getIdTipo().longValue() == tipo) {
				listado.add(listaProdFiltrados[i]);
			}
		};
		return (Producto[]) listado.toArray(new Producto[listado.size()]);
	}
	
	
	public String[] getTexto() {
		String[] listaTexto = new String[listaProdFiltrados.length];
		for (int i = 0; i < listaProdFiltrados.length; i++) {
			listaTexto[i] = listaProdFiltrados[i].getDescripcion();
		}
		return listaTexto;
	}
	
	
	public Long getIdProductoByIndex(int indice) {
		Long[] listaId = new Long[listaProdFiltrados.length];
		for (int i = 0; i < listaProdFiltrados.length; i++) {
			listaId[i] = listaProdFiltrados[i].getIdProducto();
		}
		if (indice > -1 && indice < listaProdFiltrados.length) {
			return listaId[indice];
		} else {
			System.err.println("No se encontró el producto con el índice suministrado (" + indice + ")");
			return -1L;
		}
	}
	
	
	public Long getIdProductoByName(String nombre) {
		for (int i = 0; i < listaProdFiltrados.length; i++) {
			if (listaProdFiltrados[i].getDescripcion().equals(nombre)) {
				return listaProdFiltrados[i].getIdProducto();
			}
		}
		System.err.println("No se encontró el producto con el nombre suministrado (" + nombre + ")");
		return -1L;
	}
	
	
	public Producto getProductoByIdProducto(Long id) {
		for (int i = 0; i < listaProdFiltrados.length; i++) {
			if (listaProdFiltrados[i].getIdProducto().longValue() == id.longValue()) {
				return listaProdFiltrados[i];
			}
		}
		return null;
	}
}


