package rcp.manticora.model;


public class HibernateController {
	private HibernateModel hibModel;
	private static HibernateController hc;
	
	public HibernateController() {
		hc = this;
		hibModel = new HibernateModel();
		hibModel.open();
	}
	
	public void close() {
		hibModel.close();
	}
	
	public static HibernateController getInstance() {
		if (hc == null) {
			System.err.println("No se ha inicializado el controlador HibernateController()");
		};
		return hc;
	}
	
	
// ******************************* Tipo de Productos ****************************
	/*
	public TipoProducto[] getListadoTipoProductos() {
		TipoProductoDAO dao = new TipoProductoDAO();
		return dao.retrieveTipoProductos();
	}
	*/
	
	/*
	public TipoProducto addTipoProducto(Long id, String descripcion, boolean tour) {
		System.out.println("Agregando TP...");
		TipoProductoDAO dao = new TipoProductoDAO();
		TipoProducto tipo = new TipoProducto(id, descripcion, tour);
		//hibModel.crear(tipo);
		//hibModel.commit();
		dao.makePersistent(tipo);
		dao.commit();
		System.out.println("Código TP: " + tipo.getIdTipo());
		return tipo;
	}
	*/
	
	/*
	public ComboData getComboDataTipoProductos() {
		ComboData data = new ComboData();
		TipoProducto[] lista = getListadoTipoProductos();
		for (int n = 0; n < lista.length; n++) {
			data.agregarItem(lista[n].getDescripcion(), lista[n].getIdTipo());
		}
		return data;
	}
	*/
	
	/**
	 * Retorna un listado de productos.  El parámetro indica si se deben traer
	 * productos que sean o no sean tours.
	 * @param flagTour
	 * @return
	 */
	/*
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
	*/
	
// ******************************** Productos *****************************
	/*
	public Producto[] getListadoProductos() {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		ProductoDAO dao = new ProductoDAO();
		List<Producto> resultados = dao.findAll();
		session.getTransaction().commit();
		return (Producto[]) resultados.toArray(new Producto[resultados.size()]);
	}
	*/
	
	/*
	public Producto getProductoById(Long id) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		ProductoDAO dao = new ProductoDAO();
		Producto resultados = dao.findById(id, false);
		session.getTransaction().commit();
		return resultados;
	}
	*/
	
	/*
	public Producto addProducto(int idTipo, int idProducto, String descripcion,
			Float costo, Float venta0, Float venta1, Float venta2, Float venta3, 
			String reserva,	String isTour, String estado, String comentario) {
		System.out.println("Agregando producto...");
		ProductoDAO dao = new ProductoDAO();
		Producto p = new Producto(idTipo, idProducto, descripcion, costo,
				venta0, venta1, venta2, venta3, reserva, isTour, estado, comentario);
		dao.makePersistent(p);
		dao.commit();
		return p;
	}
	*/
	
	
// ****************************** Tipo de Clientes ************************
	/*
	public TipoCliente[] getListadoTipoClientes() {
		TipoClienteDAO dao = new TipoClienteDAO();
		return dao.retrieveTipoClientes();
	}
	
	public ComboData getComboDataTipoClientes() {
		ComboData data = new ComboData();
		TipoCliente[] tipo = getListadoTipoClientes();
		for (int n = 0; n < tipo.length; n++) {
			data.agregarItem(tipo[n].getDescripcion(), tipo[n].getIdTipo());
		}
		return data;
	}
	
	public TipoCliente addTipoCliente(int id, String descripcion) {
		System.out.println("Agregando tipo cliente...");
		TipoClienteDAO dao = new TipoClienteDAO();
		TipoCliente tipo = new TipoCliente(id, descripcion);
		dao.makePersistent(tipo);
		dao.commit();
		return tipo;
	}
	*/
	/*
	public void updateTipoCliente(TipoCliente tipo) {
		TipoClienteDAO dao = new TipoClienteDAO();
		dao.makePersistent(tipo);
		dao.commit();
	}
	
	public void deleteTipoCliente(Object obj) {
		TipoClienteDAO dao = new TipoClienteDAO();
		dao.makeTransient(obj);
		dao.commit();
	}
	*/
	
	
// ******************************** Clientes *******************************
	
	/*
	public ICliente[] getListadoClientes() {
		Session session = HibernateUtil.currentSession();
		ClienteDAO dao = new ClienteDAO();
		dao.setSession(session);
		List<ICliente> resultados = dao.findAll();
		HibernateUtil.closeSession();
		return (ICliente[]) resultados.toArray(new ICliente[resultados.size()]);
	}
	
	public ICliente[] getListadoClientes(String claseCliente) {
		Session session = HibernateUtil.currentSession();
		ClienteDAO dao = new ClienteDAO();
		dao.setSession(session);
		List<ICliente> resultados = dao.findByClase(claseCliente);
		HibernateUtil.closeSession();
		return (ICliente[]) resultados.toArray(new ICliente[resultados.size()]);
	}
	*/
	/*
	public Cliente addCliente (Long idCliente, String nombre, String apellido,
			String email, String identificacion, Long idPais) {
		System.out.println("Agregando cliente base...");
		ClienteDAO dao = new ClienteDAO();
		Cliente cliente = new ClienteNatural(idCliente, nombre, apellido);
		cliente.setEmail(email);
		cliente.setIdentificacion(identificacion);
		cliente.setIdPais(idPais);
		dao.makePersistent(cliente);
		dao.commit();
		return cliente;
	}
	
	public Cliente addCliente(Long idCliente, String nombre, String identificacion,
			String referencia, String contacto,
			String telefono, String email, Long idPais, String direccion1,
			String direccion2, String direccion3, String comentario, String estado) {
		System.out.println("Agregando cliente jurídico...");
		ClienteDAO dao = new ClienteDAO();
		Cliente cliente = new ClienteJuridico(idCliente, nombre, identificacion,
				referencia, contacto, telefono,
				"", email, idPais, direccion1, direccion2, direccion3, "", "",
				comentario, estado);
		dao.makePersistent(cliente);
		dao.commit();
		return cliente;
	}
	
	public Cliente addCliente(Long idCliente, String nombre, String apellido,
			String identificacion, String telefono, String celular, String email,  
			Long pais, String direccion1, String direccion2, String direccion3, 
			String apartado, String ciudad, String comentario, String estado) {
		System.out.println("Agregando cliente natural...");
		ClienteDAO dao = new ClienteDAO();
		Cliente cliente = new ClienteNatural(idCliente, nombre, apellido, identificacion,
				telefono, celular, email, pais, direccion1, direccion2, direccion3,
				apartado, ciudad, comentario, estado);
		dao.makePersistent(cliente);
		dao.commit();
		return cliente;
	}
	*/
	
// ********************************* Países *******************************
	/*
	public Pais[] getListadoPaises() {
		Session session = HibernateUtil.currentSession();
		PaisDAO dao = new PaisDAO();
		dao.setSession(session);
		List<Pais> resultados = dao.findAll();
		HibernateUtil.closeSession();
		return (Pais[]) resultados.toArray(new Pais[resultados.size()]);
	}
	
	public Pais[] getListadoPaises(String status) {
		Session session = HibernateUtil.currentSession();
		PaisDAO dao = new PaisDAO();
		dao.setSession(session);
		List<Pais> resultados = dao.findByStatus(status);
		HibernateUtil.closeSession();
		return (Pais[]) resultados.toArray(new Pais[resultados.size()]);
	}
	
	public Pais addPais(Long id, String descripcion) {
		System.out.println("Agregando país...");
		PaisDAO dao = new PaisDAO();
		Pais pais = new Pais(id, descripcion);
		dao.makePersistent(pais);
		dao.commit();
		return pais;
	}
	
	public ComboData getComboDataPaises() {
		ComboData data = new ComboData();
		Pais[] paises = getListadoPaises();
		for (int n = 0; n < paises.length; n++) {
			data.agregarItem(paises[n].getDescripcion(), paises[n].getIdPais());
		}
		return data;
	}
	*/
	
	
// **************************** Función polimórfica??? *********************
	/*
	public void update(Object obj) {
		GenericDAOImpl dao = new GenericDAOImpl();
		dao.makePersistent(obj);
		dao.commit();
	}
	
	public void delete(Object obj) {
		GenericDAOImpl dao = new GenericDAOImpl();
		dao.makeTransient(obj);
		dao.commit();
	}
	*/
	

	/*
	public List findByExample(Class clase, Object instanciaEjemplo, String... excludeProperty) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		Criteria criteria = session.createCriteria(clase);
		Example example = Example.create(instanciaEjemplo);
		for (String exclude : excludeProperty) {
			System.out.println("Exclude: " + exclude);
			example.excludeProperty(exclude);
		}
		criteria.add(example);
		return criteria.list();
	}
	*/
	/*
	public List<DisponibilidadTour> findDisponibilidad(int idProducto, Date fecha) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		Criteria criteria = session.createCriteria(DisponibilidadTour.class)
			.add(Restrictions.eq("fecha", fecha))
			.createCriteria("tour")
				.add(Restrictions.eq("idProducto", idProducto));
		return criteria.list();
	}
	*/
	
	/**
	 * Obtiene los puestos ocupados para la disponibilidad indicada
	 * @param disp
	 * @return
	 */
	/*
	public Integer getOcupacionTour(DisponibilidadTour disp) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		Integer ocupacion = (Integer) session.createQuery(
				"select sum(d.listaReservas.cantidad) from DisponibilidadTour d where d.idTour = ?")
					.setInteger(0, disp.getIdTour()).uniqueResult();
		// si nadie ha realizado reservas para este tour entonces se obtiene null
		if (ocupacion == null) {
			ocupacion = 0;
		}
		return ocupacion;
	}
	*/
	/*
	public Integer getOcupacionTour2(DisponibilidadTour disp) {
		Session session = HibernateUtil.currentSession();
		session.beginTransaction();
		Integer ocupacion = (Integer) session.createQuery(
				"select sum(d.listaReservas.cantidad) from DisponibilidadTour d where d.idTour = ?")
					.setInteger(0, disp.getIdTour()).uniqueResult();
		// si nadie ha realizado reservas para este tour entonces se obtiene null
		if (ocupacion == null) {
			ocupacion = 0;
		}
		//HibernateUtil.closeSession();
		return ocupacion;
	}
	*/
	/**
	 * Obtiene todos los tours que están disponibles para el producto y la
	 * fecha indicada
	 * @param idProducto código de producto (siempre debe ser un tour)
	 * @param fecha fecha para buscar disponibilidad
	 * @return
	 */
	/*
	public ComboData getComboDataToursDisponibles(int idProducto, Date fecha) {
		ComboData data = new ComboData();
		List<DisponibilidadTour> d = findDisponibilidad(idProducto, fecha);
		Iterator<DisponibilidadTour> it = d.iterator();
		DisponibilidadTour linea = null;
		while (it.hasNext()) {
			linea = it.next();
			data.agregarItem(linea.getTour().getNombre(), linea.getTour().getIdTour());	
		}
		System.out.println("Data no: " + d.size());
		return data;
	}
	*/
	
//	 ******************************** Templates ********************************
	/*
	public ComboData getComboDataTemplates() {
		ComboData data = new ComboData();
		Template[] templates = getListadoTemplates();
		for (int n = 0; n < templates.length; n++) {
			data.agregarItem(templates[n].getNombre(), templates[n].getIdTemplate());
		}
		return data;
	}
	
	public Template[] getListadoTemplates() {
		TemplateDAO dao = new TemplateDAO();
		return dao.retrieveTemplates();
	}
	*/
	
	/*
	public Template getTemplate(String codigoTemplate) {
		Session session = HibernateUtil.currentSession();
		TemplateDAO dao = new TemplateDAO();
		dao.setSession(session);
		Template resultado = dao.findById(Long.valueOf(codigoTemplate), false);
		HibernateUtil.closeSession();
		return resultado;
	}
	*/
	
	/*
	public Template addTemplate(Long idTemplate, Long idProducto, String nombre) {
		System.out.println("Agregando template...");
		TemplateDAO dao = new TemplateDAO();
		Template registro = new Template(idTemplate, idProducto, nombre);
		dao.makePersistent(registro);
		dao.commit();
		return registro;
	}
	*/


//	 ****************************** Solicitudes ****************************
	
	/*
	public Solicitud[] getListadoSolicitudes() {
		Session session = HibernateUtil.currentSession();
		SolicitudDAO dao = new SolicitudDAO();
		dao.setSession(session);
		List<Solicitud> resultados = dao.findAll();
		HibernateUtil.closeSession();
		return (Solicitud[]) resultados.toArray(new Solicitud[resultados.size()]);
	}

	public Solicitud[] getListadoSolicitudes(String status) {
		Session session = HibernateUtil.currentSession();
		SolicitudDAO dao = new SolicitudDAO();
		dao.setSession(session);
		List<Solicitud> resultados = dao.findByStatus(status);
		HibernateUtil.closeSession();
		return (Solicitud[]) resultados.toArray(new Solicitud[resultados.size()]);
	}

	public Solicitud addSolicitud(Long idSolicitud, String nombre, String apellido,
			String telefono, String celular, String email, Long idPais, Date fechaInicio,
			Date fechaFin, String programa, String comentario, String estado) {
		System.out.println("Agregando solicitud...");
		SolicitudDAO dao = new SolicitudDAO();
		Solicitud registro = new Solicitud(idSolicitud, nombre, apellido, telefono,
				celular, email, idPais, fechaInicio, fechaFin, programa, comentario, estado);
		dao.makePersistent(registro);
		dao.commit();
		return registro;
	}
	*/
		/*
		public Solicitud updateSolicitud(Solicitud registro) {
			SolicitudDAO dao = new SolicitudDAO();
			dao.makePersistent(registro);
			dao.commit();
			return registro;
		}
		*/
		
	
//	 ******************************** Cotizaciones ********************************
	/*
	public Cotizacion[] getListadoCotizaciones() {
		Session session = HibernateUtil.currentSession();
		CotizacionDAO dao = new CotizacionDAO();
		dao.setSession(session);
		List<Cotizacion> resultados = dao.findAll();
		HibernateUtil.closeSession();
		return (Cotizacion[]) resultados.toArray(new Cotizacion[resultados.size()]);
	}
	
	public Cotizacion[] getListadoCotizaciones(String status) {
		Session session = HibernateUtil.currentSession();
		CotizacionDAO dao = new CotizacionDAO();
		dao.setSession(session);
		List<Cotizacion> resultados = dao.findByStatus(status);
		HibernateUtil.closeSession();
		return (Cotizacion[]) resultados.toArray(new Cotizacion[resultados.size()]);
	}
	*/
	
	/*
	public Cotizacion addCotizacion(Long idCotizacion, Date fechaInicio, Date fechaFin,
			String nombre, float subtotal, float porcDescuento, float descuento, 
			float porcImpuesto, float impuesto,	float total, float pago, Integer paxs,
			String estado) {
		System.out.println("Agregando cotización..." + pago);
		CotizacionDAO dao = new CotizacionDAO();
		Cotizacion registro = new Cotizacion(idCotizacion, fechaInicio, fechaFin,
				nombre, subtotal, porcDescuento, descuento, porcImpuesto, impuesto,
				total, pago, paxs, estado);
		dao.makePersistent(registro);
		dao.commit();
		return registro;
	}
	*/
	
	
	
//   ****************************** Hojas de Servicio ***************************
	/*
	public HojaServicio[] getListadoHojas() {
		Session session = HibernateUtil.currentSession();
		HojaServicioDAO dao = new HojaServicioDAO();
		dao.setSession(session);
		List<HojaServicio> resultados = dao.findAll();
		HibernateUtil.closeSession();
		return (HojaServicio[]) resultados.toArray(new HojaServicio[resultados.size()]);
	}
	
	public HojaServicio addHojaServicio(Long idHoja, Long idCotizacion, Date fechaInicio,
			Date fechaFin, String numero, String nombre, Integer paxs, String estado) {
		System.out.println("Agregando hoja de servicio...");
		HojaServicioDAO dao = new HojaServicioDAO();
		HojaServicio registro = new HojaServicio(idHoja, idCotizacion, fechaInicio,
				fechaFin, numero, nombre, paxs, estado);
		dao.makePersistent(registro);
		dao.commit();
		return registro;
	}
	*/
	
//  ********************************* Giras *****************************
	/*
	public HojaServicio[] getListadoGiras() {
		Session session = HibernateUtil.currentSession();
		Criterion criterion = Restrictions.eq("clase", "T");
		HojaServicioDAO dao = new HojaServicioDAO();
		dao.setSession(session);
		List<HojaServicio> resultados = dao.retrieveHojas(criterion);
		HibernateUtil.closeSession();
		return (HojaServicio[]) resultados.toArray(new HojaServicio[resultados.size()]);
	}
	*/
	
//	 ********************************** Vendedores *****************************	
	/*
	public Vendedor[] getListadoVendedores() {
		Session session = HibernateUtil.currentSession();
		VendedorDAO dao = new VendedorDAO();
		dao.setSession(session);
		List<Vendedor> resultados = dao.findAll();
		return (Vendedor[]) resultados.toArray(new Vendedor[resultados.size()]);
	}
	
	public ComboData getComboDataVendedores() {
		ComboData data = new ComboData();
		Vendedor[] vendedores = getListadoVendedores();
		for (int n = 0; n < vendedores.length; n++) {
			data.agregarItem(vendedores[n].getNombre() + " " + vendedores[n].getApellido(), vendedores[n].getIdVendedor());
		}
		return data;
	}
	*/
	/*
	public Vendedor addVendedor(int idVendedor, String nombre, String apellido,
			String estado) {
		System.out.println("Agregando vendedor...");
		VendedorDAO dao = new VendedorDAO();
		Vendedor registro = new Vendedor(idVendedor, nombre, apellido, estado);
		dao.makePersistent(registro);
		dao.commit();
		return registro;
	}
	*/
	
//	 ********************************** Tranportes *****************************
	/*
	public Transporte[] getListadoTransportes() {
		Session session = HibernateUtil.currentSession();
		TransporteDAO dao = new TransporteDAO();
		dao.setSession(session);
		List<Transporte> resultados = dao.findAll();
		HibernateUtil.closeSession();
		return (Transporte[]) resultados.toArray(new Transporte[resultados.size()]);
	}
	
	public ComboData getComboDataTransportes() {
		ComboData data = new ComboData();
		Transporte[] transportes = getListadoTransportes();
		for (int n = 0; n < transportes.length; n++) {
			data.agregarItem(transportes[n].getNombre() + " " + transportes[n].getApellido(), transportes[n].getIdTransporte());
		}
		return data;
	}

	public Transporte addTransporte(int idTransporte, String nombre, String apellido,
			String estado) {
		System.out.println("Agregando transporte...");
		TransporteDAO dao = new TransporteDAO();
		Transporte registro = new Transporte(idTransporte, nombre, apellido, estado);
		dao.makePersistent(registro);
		dao.commit();
		return registro;
	}
	*/
	
	
	
	
//	 ******************* Reservas de transporte terrestre ************************
	/*
	public ReservaTransporte getReservaTransporte(int idReserva) {
		ReservaTransporteDAO dao = new ReservaTransporteDAO();
		return dao.retrieveReserva(idReserva);
	}
	
	public ReservaTransporte addReservaTransporte(Long idReserva, String origen,
			String destino, Date fechaOrigen, Date fechaDestino, Long idTransporte,
			String tipo, String servicio, String comentario) {
		System.out.println("Agregando reserva de transporte...");
		ReservaTransporteDAO dao = new ReservaTransporteDAO();
		ReservaTransporte registro = new ReservaTransporte(idReserva, origen, destino,
				fechaOrigen, fechaDestino, idTransporte, tipo, servicio, comentario);
		dao.makePersistent(registro);
		dao.commit();
		return registro;
	}
	*/
	
//	 ***************************** Tours *********************************
	/*
	public Tour[] getListadoTours() {
		TourDAO dao = new TourDAO();
		return dao.retrieveTours();
	}
	*/
	
	
//	 *********************** Reservas de tours ***************************
	/*
	public ReservaTour getReservaTour(int idReserva) {
		ReservaTourDAO dao = new ReservaTourDAO();
		return dao.retrieveTour(idReserva);
	}
	
	public ReservaTour[] getListadoReservaTours() {
		ReservaTourDAO dao = new ReservaTourDAO();
		return dao.retrieveTours();
	}
	*/
	
	
//	 ******************************* Keywords **********************************
	/*
	public Keyword[] getListadoKeywords() {
		HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
		KeywordDAO dao = new KeywordDAO();
		List resultados = dao.findAll();
		HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
		return (Keyword[]) resultados.toArray(new Keyword[resultados.size()]);
	}
	
	public Keyword[] getListadoKeyword(String tipoKeyword) {
		Session session = HibernateUtil.currentSession();
		session.beginTransaction();
		KeywordDAO dao = new KeywordDAO();
		dao.setSession(session);
		List<Keyword> resultados = dao.retrieveKeyword(tipoKeyword);
		return (Keyword[]) resultados.toArray(new Keyword[resultados.size()]);
	}
	
	public ComboData getComboDataKeyword(String tipoKeyword) {
		ComboData data = new ComboData();
		Keyword[] keyword = getListadoKeyword(tipoKeyword);
		for (int n = 0; n < keyword.length; n++) {
			data.agregarItem(keyword[n].getDescripcion(), keyword[n].getCodigo());
		}
		return data;
	}
	
	public Keyword addKeyword(int idKeyword, String codigo, String descripcion, String tipo) {
		System.out.println("Agregando keyword...");
		KeywordDAO dao = new KeywordDAO();
		Keyword registro = new Keyword(idKeyword, codigo, descripcion, tipo);
		dao.makePersistent(registro);
		dao.commit();
		return registro;
	}
	*/
}





