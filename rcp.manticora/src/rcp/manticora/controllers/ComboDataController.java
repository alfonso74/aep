package rcp.manticora.controllers;

import java.util.Arrays;

import rcp.manticora.model.Condicional;
import rcp.manticora.model.Guia;
import rcp.manticora.model.Habitacion;
import rcp.manticora.model.ICliente;
import rcp.manticora.model.Keyword;
import rcp.manticora.model.Pais;
import rcp.manticora.model.Producto;
import rcp.manticora.model.Red;
import rcp.manticora.model.Template;
import rcp.manticora.model.TipoCliente;
import rcp.manticora.model.TipoHabitacion;
import rcp.manticora.model.TipoKeyword;
import rcp.manticora.model.TipoProducto;
import rcp.manticora.model.Transporte;
import rcp.manticora.model.Vendedor;
import rcp.manticora.services.ComboData;


public class ComboDataController extends ViewController {
	
	
	public ComboData getComboDataCondicional() {
		ComboData data = new ComboData();
		for (int n=0; n < Condicional.values().length; n++) {
			Condicional v = Condicional.values()[n];
			data.agregarItem(v.getDescripcion(), v.getCode());
		}
		return data;
	}
	
	
	public ComboData getComboDataGuias() {
		ComboData data = new ComboData();
		Guia[] lista = getListadoGuias();
		for (int n=0; n < lista.length; n++) {
			//data.agregarItem(lista[n].getNombreCompleto(), lista[n].getIdGuia());
			data.agregarItem(lista[n].getNombreCompleto(), lista[n].getIdGuia(), lista[n]);
		}
		return data;
	}
	
	/**
	 * Retorna un ComboData de habitaciones para el hotel indicado.  Este hotel
	 * siempre va a ser interno (de AEP), porque las habitaciones se definen
	 * únicamente para hoteles internos.
	 * @param hotel del cual queremos obtener las habitaciones
	 * @return ComboData con listado de habitaciones
	 */
	public ComboData getComboDataHabitacionesByHotel(Producto hotel) {
		ComboData data = new ComboData();
		Habitacion[] habitaciones = getListadoHabitacionesByHotel(hotel);
		for (int n = 0; n < habitaciones.length; n++) {
			data.agregarItem(habitaciones[n].getNombre(), habitaciones[n].getIdHabitacion(), habitaciones[n]);
		}
		return data;
	}
	
	public ComboData getComboDataTipoHabitacionesByHotel(Producto hotel) {
		ComboData data = new ComboData();
		TipoHabitacion[] tipos = getListadoTipoHabitacionesByHotel(hotel);
		System.out.println("Tipos definidos: " + tipos.length);
		for (int n = 0; n < tipos.length; n++) {
			//data.agregarItem(tipos[n].getDescripcion(), tipos[n].getIdTipoHabitacion());
			data.agregarItem(tipos[n].getDescripcion(), tipos[n].getIdTipoHabitacion(), tipos[n]);
		}
		return data;
	}
	
	public ComboData getComboDataHoteles() {
		ComboData data = new ComboData();
		for (Producto hotel : getListadoProductosHoteles()) {
			data.agregarItem(hotel.getDescripcion(), hotel.getIdProducto());
		}
		return data;
	}
	
	public ComboData getComboDataHotelesAEP() {
		ComboData data = new ComboData();
		for (Producto hotel : getListadoProductosHoteles()) {
			if (hotel.isHotelAEP()) {
				data.agregarItem(hotel.getDescripcion(), hotel.getIdProducto());
			}
		}
		return data;
	}
	
	public ComboData getComboDataKeyword(TipoKeyword tipoKeyword) {
		ComboData data = new ComboData();
		Keyword[] keyword = getListadoKeyword(tipoKeyword);
		for (int n = 0; n < keyword.length; n++) {
			data.agregarComboDataItem(keyword[n].toComboDataItem());
		}
		return data;
	}
	
	/**
	 * En un ComboData usualmente solo nos interesan los países activos
	 * @return ComboData con países activos
	 */
	public ComboData getComboDataPaises() {
		ComboData data = new ComboData();
		Pais[] lista = getListadoPaisesByStatus("A");
		for (int n=0; n < lista.length; n++) {
			data.agregarItem(lista[n].getDescripcion(), lista[n].getIdPais());
		}
		return data;
	}
	
	public ComboData getComboDataTemplates() {
		ComboData data = new ComboData();
		Template[] lista = getListadoTemplates();
		for (int n = 0; n < lista.length; n++) {
			data.agregarItem(lista[n].getNombre(), lista[n].getIdTemplate());
		}
		return data;
	}
	
	public ComboData getComboDataTipoClientes() {
		ComboData data = new ComboData();
		TipoCliente[] lista = getListadoTipoClientes();
		for (int n = 0; n < lista.length; n++) {
			data.agregarComboDataItem(lista[n].toComboDataItem());
		}
		return data;
	}
	
	public ComboData getComboDataTipoClientesActivos() {
		ComboData data = new ComboData();
		TipoCliente[] lista = getListadoTipoClientesByStatus("A");
		for (int n = 0; n < lista.length; n++) {
			data.agregarComboDataItem(lista[n].toComboDataItem());
		}
		return data;
	}
	
	public ComboData getComboDataClientesComisionables() {
		ComboData data = new ComboData();
		ICliente[] lista = getListadoClientesComisionables();
		Arrays.sort(lista);
		for (int n = 0; n < lista.length; n++) {
			data.agregarItem(lista[n].getNombreCliente(), lista[n].getIdCliente(), lista[n]);
		}
		return data;
	}
	
	public ComboData getComboDataTipoProductos() {
		ComboData data = new ComboData();
		TipoProducto[] lista = getListadoTipoProductos();
		for (int n = 0; n < lista.length; n++) {
			data.agregarItem(lista[n].getDescripcion(), lista[n].getIdTipo());
		}
		return data;
	}
	
	/**
	 * Retorna un listado de productos.  El parámetro indica si se deben traer
	 * productos que sean o no sean tours.
	 * @param flagTour
	 * @return Objeto ComboData
	 */
	public ComboData getComboDataTipoProductosTour(boolean flagTour) {
		ComboData data = new ComboData();
		TipoProducto[] lista = getListadoTipoProductos();
		for (int n = 0; n < lista.length; n++) {
			if (lista[n].isTour() == flagTour) {
				data.agregarItem(lista[n].getDescripcion(), lista[n].getIdTipo());
			}
		}
		return data;
	}
	
	public ComboData getComboDataTransportes() {
		ComboData data = new ComboData();
		Transporte[] transportes = getListadoTransportes();
		for (int n = 0; n < transportes.length; n++) {
			data.agregarItem(transportes[n].getNombre() + " " + transportes[n].getApellido(), transportes[n].getIdTransporte());
		}
		return data;
	}
	
	public ComboData getComboDataVendedores() {
		ComboData data = new ComboData();
		Vendedor[] vendedores = getListadoVendedores();
		for (int n = 0; n < vendedores.length; n++) {
			data.agregarItem(vendedores[n].getNombre() + " " + vendedores[n].getApellido(), vendedores[n].getIdVendedor());
		}
		return data;
	}
	
	public ComboData getComboDataVendedoresActivos() {
		ComboData data = new ComboData();
		Vendedor[] vendedores = getListadoVendedoresByStatus("A");
		for (int n = 0; n < vendedores.length; n++) {
			data.agregarItem(vendedores[n].getNombre() + " " + vendedores[n].getApellido(), vendedores[n].getIdVendedor());
		}
		return data;
	}
	
	public ComboData getComboDataRedes() {
		ComboData data = new ComboData();
		Red[] redes = getListadoRedes();
		for (int n = 0; n < redes.length; n++) {
			data.agregarItem(redes[n].getDescripcion(), redes[n].getIdRed(), redes[n]);
		}
		return data;
	}

}
