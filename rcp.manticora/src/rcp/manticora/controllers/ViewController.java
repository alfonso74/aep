package rcp.manticora.controllers;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;

import rcp.manticora.dao.ClienteDAO;
import rcp.manticora.dao.CotizacionDAO;
import rcp.manticora.dao.GuiaDAO;
import rcp.manticora.dao.HabitacionDAO;
import rcp.manticora.dao.HojaServicioDAO;
import rcp.manticora.dao.KeywordDAO;
import rcp.manticora.dao.PaisDAO;
import rcp.manticora.dao.ProductoDAO;
import rcp.manticora.dao.RedDAO;
import rcp.manticora.dao.RolDAO;
import rcp.manticora.dao.SolicitudDAO;
import rcp.manticora.dao.TemplateDAO;
import rcp.manticora.dao.TipoClienteDAO;
import rcp.manticora.dao.TipoHabitacionDAO;
import rcp.manticora.dao.TipoProductoDAO;
import rcp.manticora.dao.TourDAO;
import rcp.manticora.dao.TransporteDAO;
import rcp.manticora.dao.UsuarioDAO;
import rcp.manticora.dao.VendedorDAO;
import rcp.manticora.model.BuscarCotizacionDatos;
import rcp.manticora.model.BuscarResToursDTO;
import rcp.manticora.model.Cliente;
import rcp.manticora.model.Cotizacion;
import rcp.manticora.model.DisponibilidadTour;
import rcp.manticora.model.Guia;
import rcp.manticora.model.Habitacion;
import rcp.manticora.model.ICliente;
import rcp.manticora.model.IHojaServicio;
import rcp.manticora.model.Keyword;
import rcp.manticora.model.Pais;
import rcp.manticora.model.Producto;
import rcp.manticora.model.Red;
import rcp.manticora.model.Rol;
import rcp.manticora.model.Solicitud;
import rcp.manticora.model.Template;
import rcp.manticora.model.TipoCliente;
import rcp.manticora.model.TipoHabitacion;
import rcp.manticora.model.TipoProducto;
import rcp.manticora.model.Tour;
import rcp.manticora.model.Transporte;
import rcp.manticora.model.Usuario;
import rcp.manticora.model.Vendedor;
import rcp.manticora.services.FechaUtil;
import rcp.manticora.services.HibernateUtil;

public class ViewController {

	/**
	 * getCurrentSession() solo requiere un s.getTransaction.commit() y no necesita el s.close()
	 * openSession() requiere s.getTransaction.commit() y luego un s.close()
	 * 
	 * El session.getTransaction().commit() evalúa todos los setCampoXXX y actualiza el valor de acuerdo con
	 * la lógica del set.  En otras palabras, si quiero agregar a todos los tipos de producto la letra "A" al
	 * inicio, entonces modifico el setDescripcion() para que haga un ("A" + descripcion) y al ejecutar el
	 * session.getTransaction().commit() se actualizan todas las descripciones en la tabla.
	 * Para evitar que se ejecute el setCampoXXX puedo reemplazar el commit() x un session.close().
	 */
	
	public ICliente[] xxx_getListadoClientes() {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		ClienteDAO dao = new ClienteDAO();
		dao.setSession(session);
		List<ICliente> resultados = dao.findAll();
		session.getTransaction().commit();
		return (ICliente[]) resultados.toArray(new ICliente[resultados.size()]);
	}
	
	
	public ICliente[] getListadoClientesByClase(Cliente.Clase clase) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		ClienteDAO dao = new ClienteDAO();
		dao.setSession(session);
		List<ICliente> resultados = dao.findOrderedByClase(clase);
		session.getTransaction().commit();
		return (ICliente[]) resultados.toArray(new ICliente[resultados.size()]);
	}
	
	
	public ICliente[] getListadoClientesComisionables() {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		ClienteDAO dao = new ClienteDAO();
		dao.setSession(session);
		List<ICliente> resultados = dao.findOrderedComisionable();
		session.getTransaction().commit();
		return (ICliente[]) resultados.toArray(new ICliente[resultados.size()]);
	}
	
	
	/**
	 * Genera un listado de todas las cotizaciones del sistema
	 * @return Array de cotizaciones
	 */
	public Cotizacion[] getListadoCotizaciones() {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		CotizacionDAO dao = new CotizacionDAO();
		dao.setSession(session);
		List<Cotizacion> resultados = dao.findAll();
		session.getTransaction().commit();
		return (Cotizacion[]) resultados.toArray(new Cotizacion[resultados.size()]);
	}
	
	public Cotizacion[] getListadoCotizacionesRegistradas() {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		CotizacionDAO dao = new CotizacionDAO();
		dao.setSession(session);
		List<Cotizacion> resultados = dao.findRegistradas();
		session.getTransaction().commit();
		return (Cotizacion[]) resultados.toArray(new Cotizacion[resultados.size()]);
	}
	
	public Cotizacion[] getListadoCotizacionesByStatus(String status) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		CotizacionDAO dao = new CotizacionDAO();
		dao.setSession(session);
		List<Cotizacion> resultados = dao.findByStatus(status);
		session.getTransaction().commit();
		return (Cotizacion[]) resultados.toArray(new Cotizacion[resultados.size()]);
	}
	
	public Guia[] getListadoGuias() {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		GuiaDAO dao = new GuiaDAO();
		dao.setSession(session);
		List<Guia> resultados = dao.findAll();
		session.getTransaction().commit();
		return (Guia[]) resultados.toArray(new Guia[resultados.size()]);
	}
	
	public Habitacion[] getListadoHabitaciones() {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		HabitacionDAO dao = new HabitacionDAO();
		dao.setSession(session);
		List<Habitacion> resultados = dao.findAll();
		session.getTransaction().commit();
		return (Habitacion[]) resultados.toArray(new Habitacion[resultados.size()]);
	}
	
	public Habitacion[] getListadoHabitacionesByHotel(Producto hotel) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		HabitacionDAO dao = new HabitacionDAO();
		dao.setSession(session);
		List<Habitacion> resultados = dao.findByHotel(hotel);
		session.getTransaction().commit();
		return (Habitacion[]) resultados.toArray(new Habitacion[resultados.size()]);
	}
	
	public IHojaServicio[] getListadoHojas() {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		//session.setCacheMode(CacheMode.REFRESH);
		System.out.println("Creando DAO (HojaServicio)");
		HojaServicioDAO dao = new HojaServicioDAO();
		dao.setSession(session);
		List<IHojaServicio> resultados = dao.findAll();
		System.out.println("CANTIDAD: " + resultados.size());
		session.getTransaction().commit();
		return (IHojaServicio[]) resultados.toArray(new IHojaServicio[resultados.size()]);
	}
	
	public IHojaServicio[] getListadoHojasByStatus(String status) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		HojaServicioDAO dao = new HojaServicioDAO();
		dao.setSession(session);
		List<IHojaServicio> resultados = dao.findByStatus(status);
		session.getTransaction().commit();
		return (IHojaServicio[]) resultados.toArray(new IHojaServicio[resultados.size()]);
	}
	
	public IHojaServicio[] getListadoHojasVentasByStatus(String status) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		HojaServicioDAO dao = new HojaServicioDAO();
		dao.setSession(session);
		List<IHojaServicio> resultados = dao.findHojasVentasByStatus(status);
		session.getTransaction().commit();
		return (IHojaServicio[]) resultados.toArray(new IHojaServicio[resultados.size()]);
	}
	
	public IHojaServicio[] getListadoHojasOpsByStatus(String status) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		HojaServicioDAO dao = new HojaServicioDAO();
		dao.setSession(session);
		List<IHojaServicio> resultados = dao.findHojasOpsByStatus(status);
		session.getTransaction().commit();
		return (IHojaServicio[]) resultados.toArray(new IHojaServicio[resultados.size()]);
	}

	public Keyword[] getListadoKeywords() {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		KeywordDAO dao = new KeywordDAO();
		dao.setSession(session);
		List<Keyword> resultados = dao.findAll();
		session.getTransaction().commit();
		return (Keyword[]) resultados.toArray(new Keyword[resultados.size()]);
	}
	
	public Keyword[] getListadoKeyword(String tipoKeyword) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		KeywordDAO dao = new KeywordDAO();
		dao.setSession(session);
		List<Keyword> resultados = dao.findByStatus(tipoKeyword);
		session.getTransaction().commit();
		return (Keyword[]) resultados.toArray(new Keyword[resultados.size()]);
	}
	
	public Pais[] getListadoPaises() {
		System.out.println("Cargando listado de países...");
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		PaisDAO dao = new PaisDAO();
		dao.setSession(session);
		List<Pais> resultados = dao.findAllAscending();
		System.out.println("Total de países: " + resultados.size());
		session.getTransaction().commit();
		return (Pais[]) resultados.toArray(new Pais[resultados.size()]);
	}
	
	public Pais[] getListadoPaisesByStatus(String status) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		PaisDAO dao = new PaisDAO();
		dao.setSession(session);
		List<Pais> resultados = dao.findByStatus(status);
		session.getTransaction().commit();
		return (Pais[]) resultados.toArray(new Pais[resultados.size()]);
	}
	
	public Producto[] getListadoProductos() {
		// getCurrentSession solo requiere un s.getTransaction.commit() y no necesita el s.close()
		// openSession requiere s.getTransaction.commit() y luego un s.close()
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		ProductoDAO dao = new ProductoDAO();
		dao.setSession(session);
		List<Producto> resultados = dao.findAllAscending();
		session.getTransaction().commit();
		session.close();
		return (Producto[]) resultados.toArray(new Producto[resultados.size()]);
	}
	
	public Producto[] getListadoProductosHoteles() {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		ProductoDAO dao = new ProductoDAO();
		dao.setSession(session);
		List<Producto> resultados = dao.findProductosByTipoReserva("Hospedaje");
		session.getTransaction().commit();
		return (Producto[]) resultados.toArray(new Producto[resultados.size()]);
	}
	
	
	public Rol[] getListadoRoles() {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		RolDAO dao = new RolDAO();
		dao.setSession(session);
		List<Rol> resultados = dao.findAllAscending();
		session.getTransaction().commit();
		return (Rol[]) resultados.toArray(new Rol[resultados.size()]);
	}
	
	public Rol[] getListadoRolesByStatus(String status) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		RolDAO dao = new RolDAO();
		dao.setSession(session);
		List<Rol> resultados = dao.findByStatus(status);
		session.getTransaction().commit();
		return (Rol[]) resultados.toArray(new Rol[resultados.size()]);
	}
	
	public Red[] getListadoRedes() {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		RedDAO dao = new RedDAO();
		dao.setSession(session);
		List<Red> resultados = dao.findAll();
		session.getTransaction().commit();
		return (Red[]) resultados.toArray(new Red[resultados.size()]);
	}
	
	public Solicitud[] getListadoSolicitudes() {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		SolicitudDAO dao = new SolicitudDAO();
		dao.setSession(session);
		List<Solicitud> resultados = dao.findAll();
		session.getTransaction().commit();
		return (Solicitud[]) resultados.toArray(new Solicitud[resultados.size()]);
	}
	
	public Solicitud[] getListadoSolicitudesByStatus(String status) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		SolicitudDAO dao = new SolicitudDAO();
		dao.setSession(session);
		List<Solicitud> resultados = dao.findByStatus(status);
		session.getTransaction().commit();
		return (Solicitud[]) resultados.toArray(new Solicitud[resultados.size()]);
	}

	public Template[] getListadoTemplates() {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		TemplateDAO dao = new TemplateDAO();
		dao.setSession(session);
		List<Template> resultados = dao.findAll();
		session.getTransaction().commit();
		return (Template[]) resultados.toArray(new Template[resultados.size()]);
	}
	
	public TipoCliente[] getListadoTipoClientes() {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		TipoClienteDAO dao = new TipoClienteDAO();
		dao.setSession(session);
		List<TipoCliente> resultados = dao.findAll();
		session.getTransaction().commit();
		return (TipoCliente[]) resultados.toArray(new TipoCliente[resultados.size()]);
	}
	
	public TipoHabitacion[] getListadoTipoHabitaciones() {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		TipoHabitacionDAO dao = new TipoHabitacionDAO();
		dao.setSession(session);
		List<TipoHabitacion> resultados = dao.findAll();
		session.getTransaction().commit();
		return (TipoHabitacion[]) resultados.toArray(new TipoHabitacion[resultados.size()]);
	}
	
	public TipoHabitacion[] getListadoTipoHabitacionesByHotel(Producto hotel) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		TipoHabitacionDAO dao = new TipoHabitacionDAO();
		dao.setSession(session);
		List<TipoHabitacion> resultados = dao.findByHotel(hotel);
		session.getTransaction().commit();
		return (TipoHabitacion[]) resultados.toArray(new TipoHabitacion[resultados.size()]);
	}
	
	public TipoProducto[] getListadoTipoProductos() {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		TipoProductoDAO dao = new TipoProductoDAO();
		dao.setSession(session);
		List<TipoProducto> resultados = dao.findAll();
		/**
		 * El session.getTransaction().commit() evalúa todos los setCampoXXX y actualiza el valor de acuerdo con
		 * la lógica del set.  En otras palabras, si quiero agregar a todos los tipos de producto la letra "A" al
		 * inicio, entonces modifico el setDescripcion() para que haga un ("A" + descripcion) y al ejecutar el
		 * session.getTransaction().commit() se actualizan todas las descripciones en la tabla.
		 * Para evitar que se ejecute el setCampoXXX puedo reemplazar el commit() x un session.close().
		 */
		session.getTransaction().commit();
		//session.close();
		return (TipoProducto[]) resultados.toArray(new TipoProducto[resultados.size()]);
	}
	
	public Tour[] getListadoTours() {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		TourDAO dao = new TourDAO();
		dao.setSession(session);
		List<Tour> resultados = dao.findAll();
		session.getTransaction().commit();
		return (Tour[]) resultados.toArray(new Tour[resultados.size()]);
	}
	
	public Transporte[] getListadoTransportes() {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		TransporteDAO dao = new TransporteDAO();
		dao.setSession(session);
		List<Transporte> resultados = dao.findAll();
		session.getTransaction().commit();
		return (Transporte[]) resultados.toArray(new Transporte[resultados.size()]);
	}
	
	public Usuario[] getListadoUsuarios() {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		UsuarioDAO dao = new UsuarioDAO();
		dao.setSession(session);
		List<Usuario> resultados = dao.findAll();
		session.getTransaction().commit();
		return (Usuario[]) resultados.toArray(new Usuario[resultados.size()]);
	}
	
	public Vendedor[] getListadoVendedores() {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		VendedorDAO dao = new VendedorDAO();
		dao.setSession(session);
		List<Vendedor> resultados = dao.findAllAscending();
		session.getTransaction().commit();
		return (Vendedor[]) resultados.toArray(new Vendedor[resultados.size()]);
	}
	
	public Vendedor[] getListadoVendedoresByStatus(String status) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		VendedorDAO dao = new VendedorDAO();
		dao.setSession(session);
		List<Vendedor> resultados = dao.findByStatus(status);
		session.getTransaction().commit();
		return (Vendedor[]) resultados.toArray(new Vendedor[resultados.size()]);
	}
	
	public Cotizacion[] buscarCotizaciones(BuscarCotizacionDatos busqueda) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		Criteria criteria = busqueda.generarCriteria(session);
		List<Cotizacion> resultados = criteria.list();
		session.getTransaction().commit();
		return (Cotizacion[]) resultados.toArray(new Cotizacion[resultados.size()]);
	}
	
	/**
	 * Retorna un listado array de tours disponibles para el producto y rango de fechas indicadas.
	 * @param producto Tour a mostrar, si queda en blanco se muestran todos los tours
	 * @param fechaInicial fecha a partir de la cual se debe mostrar la disponibilidad
	 * @param dias cantidad de días que se muestran a partir de la fecha inicial
	 * @param reservas indica si solamente se deben mostrar tours con reservas realizadas o también tours sin reservas
	 * @return
	 */
	public DisponibilidadTour[] getListadoReservasTours(String producto, Date fechaInicial, Integer dias, boolean reservas) {
		BuscarResToursDTO buscar = new BuscarResToursDTO();
		buscar.setProducto(producto);
		buscar.setFechaDesde(fechaInicial);
		buscar.setReservas(reservas);
		if (fechaInicial != null && !fechaInicial.equals("")) {
			if (dias != null && dias.intValue() != 0) {
				Date fechaHasta = FechaUtil.ajustarFecha(fechaInicial, dias.intValue());
				buscar.setFechaHasta(fechaHasta);
			}
		}
		return buscar.buscarReservasTours();
	}
}

