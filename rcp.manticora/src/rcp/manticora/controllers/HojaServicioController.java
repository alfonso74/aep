package rcp.manticora.controllers;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;

import rcp.manticora.dao.HojaServicioDAO;
import rcp.manticora.model.AsignacionReserva;
import rcp.manticora.model.DisponibilidadTour;
import rcp.manticora.model.HojaServicioTour;
import rcp.manticora.model.IHojaServicio;
import rcp.manticora.model.IReserva;
import rcp.manticora.model.LineaActividad;
import rcp.manticora.model.LineaCotizacion;
import rcp.manticora.model.LineaTour;
import rcp.manticora.model.Pax;
import rcp.manticora.model.ReservaGuia;
import rcp.manticora.model.ReservaTour;
import rcp.manticora.model.Tour;
import rcp.manticora.services.AutenticacionUtil;
import rcp.manticora.services.ComboData;
import rcp.manticora.services.FechaUtil;

public class HojaServicioController extends AbstractControllerNew<IHojaServicio> {
	private ComboDataController cdController;
	
	public HojaServicioController(String editorId) {
		super(editorId, new HojaServicioDAO());
		cdController = new ComboDataController();
	}
	
	
	/*
	private Session session; 
	private HojaServicio hoja;
	private HojaServicioDAO dao;
	private ComboDataController cdController;
	private String editorId;
	
	// usado por vistas y algunos dialogs de reservas
	public HojaServicioController() {
		cdController = new ComboDataController();
	}
	
	public HojaServicioController(String editorId) {
		this.editorId = editorId;
		cdController = new ComboDataController();
		session = HibernateUtil.getEditorSession(editorId);
		session.setFlushMode(FlushMode.NEVER);
		dao = new HojaServicioDAO();
		dao.setSession(session);
	}

	public HojaServicio inicializarHojaServicio(Long id) {
		hoja = dao.findById(id, true);
		return hoja;
	}
	*/
	/*
	public void makePersistent() {
		if (hoja == null) {System.out.println("HOJA NULL"); };
		if (dao == null) {System.out.println("DAO NULL"); };
		dao.makePersistent(hoja);
	}
	*/
	/*
	public void finalizar(String editorId) {
		System.out.println("Finalizando sesión: " + editorId);
		HibernateUtil.closeEditorSession(editorId);
	}
	
	public void doSave(HojaServicio registro) {
		session.beginTransaction();
		dao.makePersistent(registro);
		dao.flush();            // extiende las modificaciones al caché
		session.getTransaction().commit();
	}
	
	public HojaServicio getHojaById(Long id) {
		return dao.findById(id, true);
	}
	
	public String getEditorId() {
		return editorId;
	}
	
	public Session getSession() {
		return session;
	}
	*/
	
	
	
	public ReservaController getReservaController() {
		ReservaController resController = new ReservaController(getEditorId());
		return resController;
	}
	
	/**
	 * Obtiene el número de puestos ocupados para el tour en la fecha indicada
	 * (DisponibilidadTour)
	 * @param disp
	 * @return
	 */
	public Long getOcupacionTour(DisponibilidadTour disp) {
		Long ocupacion = (Long) getSession().createQuery("select sum(reservas.cantidad) " +
				"from DisponibilidadTour d " +
				"join d.listaReservas reservas " +
				"where d.idDisponibilidad = ?")
				.setLong(0, disp.getIdDisponibilidad()).uniqueResult();
		/*
		Long ocupacion = (Long) getSession().createQuery("select sum(reservas.cantidad) " +
				"from DisponibilidadTour d " +
				"join d.listaReservas reservas " +
				"where d.idTour = ?")
				.setLong(0, disp.getIdTour()).uniqueResult();
		*/

		// si nadie ha realizado reservas para este tour entonces se obtiene null
		if (ocupacion == null) {
			ocupacion = 0L;
		}
		return ocupacion;
	}

	/**
	 * Retorna un listado de tours disponibles (DisponibilidadTour) para la fecha
	 * indicada por el usuario
	 * @param idProducto
	 * @param fecha
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<DisponibilidadTour> findDisponibilidadTourByFecha(Long idProducto, Date fecha) {
		Criteria criteria = getSession().createCriteria(DisponibilidadTour.class)
			.add(Restrictions.eq("fecha", fecha))
			.createCriteria("tour")
				.add(Restrictions.eq("idProducto", idProducto));
		List<DisponibilidadTour> resultados = criteria.list();
		return resultados;
	}
	

	public HojaServicioTour findHojaServicioByDisponibilidad(DisponibilidadTour disponibilidad) {
		Criteria criteria = getSession().createCriteria(HojaServicioTour.class)
			.add(Restrictions.eq("disponibilidad", disponibilidad));
		HojaServicioTour resultado = (HojaServicioTour) criteria.uniqueResult();
		return resultado;
	}

	public ComboData getComboDataVendedores() {
		return cdController.getComboDataVendedoresActivos();
	}
	
	public ComboData getComboDataKeyword(String tipoKeyword) {
		return cdController.getComboDataKeyword(tipoKeyword);
	}
	
	public void agregarActividad(IHojaServicio registro, LineaActividad linea) {
		linea.setHoja(registro);
		registro.agregarActividad(linea);
	}
	
	public void eliminarActividad(IHojaServicio registro, LineaActividad linea) {
		if (linea.hasReservas()) {
			// para cada asignación que tiene la línea de actividad...
			for (AsignacionReserva asignacion : linea.getListaAsignaciones()) {
				// obtenemos la reserva...
				IReserva reserva = asignacion.getReserva();
				// eliminamos la asignación del lado de la reserva...
				reserva.eliminarAsignacion(asignacion);
				// y eliminanos la asignación de lado de la línea de actividad
				linea.eliminarAsignacionReserva(asignacion);
				if (!reserva.hasActividades()) {
					// si la reserva queda sin actividades asociadas se debe borrar
					registro.eliminarReserva(reserva);
				}
			}
		}
		// y finalmente borramos la línea de actividad
		registro.eliminarActividad(linea);
	}
	
	public void agregarPax(IHojaServicio registro, Pax pax) {
		registro.agregarPax(pax);
	}
	
	/**
	 * Usado para "excluir" (no borrar) un pax de una hoja de servicio de
	 * ventas u operaciones.  Solamente se elimina la asociación a esta hoja,
	 * para borrar el pax se debe usar otro procedimiento.
	 * @param registro Hoja de servicio con listado de paxs
	 * @param pax Pax que se va a excluir
	 */
	public void excluirPax(IHojaServicio registro, Pax pax) {
		registro.excluirPax(pax);
	}
	
	/**
	 * Elimina la asociación de una reserva a la hoja de servicio y a sus actividades
	 * @param reserva Reserva que se va a "desasociar"
	 */
	public void eliminarReserva(IReserva reserva) {
		// verificamos si hay actividades asociadas a la reserva
		if (reserva.hasActividades()) {
			// en caso positivo, debemos borrar las asignaciones de las actividades hacia la reserva a ser borrada
			for (AsignacionReserva asignacion : reserva.getListaAsignaciones()) {
				// obtenemos la línea de actividad...
				LineaActividad linea = asignacion.getActividad();
				// y eliminamos la asociación hacia la reserva
				linea.eliminarAsignacionReserva(asignacion);
				asignacion.setActividad(null);
				asignacion.setReserva(null);
			}
		}
		// habiendo eliminado las asociaciones, podemos proceder a eliminar la asociación reserva-hoja
		IHojaServicio hoja = reserva.getHoja();
		hoja.eliminarReserva(reserva);
		/*
		System.out.println("Res: " + reserva);
		System.out.println("Hoja: " + reserva.getHoja());
		System.out.println("Disp: " + ((ReservaTour) reserva).getDisponibilidad());
		System.out.println("Asig (" + reserva.getListaAsignaciones().size() + "): " + reserva.getListaAsignaciones());
		*/
		// y finalmente, eliminamos la asociación a la disponibilidad (toda reserva de tour SIEMPRE tiene una disponibilidad)
		if (reserva instanceof ReservaTour) {
			((ReservaTour) reserva).getDisponibilidad().eliminarReserva(reserva);
			((ReservaTour) reserva).setDisponibilidad(null);
		}
		reserva.setHoja(null);
		reserva.setListaAsignaciones(null);
	}
	
	/*
	public void eliminarReserva(HojaServicio registro, IReserva reserva) {
		// borramos la asociación de la actividad hacia la reserva
		reserva.getActividad().eliminarReserva();
		// y borramos la asociación de la hoja hacia la reserva
		registro.eliminarReserva(reserva);
	}
	*/
	
	/**
	 * Asigna una reserva a una actividad y la asocia a la hoja de servicio.
	 * Se crea un objeto AsignacionReserva para realizar el link y se agrega la reserva
	 * al listado de reservas de la hoja de servicio.
	 * @param actividad Actividad a la cual se va a asociar una reserva
	 * @param reserva Reserva a ser asociada
	 */
	public AsignacionReserva agregarAsignacionReserva(LineaActividad actividad,
			IReserva reserva) {
		// se crea el objeto de asignación
		AsignacionReserva asignacion = new AsignacionReserva();
		asignacion.setActividad(actividad);
		asignacion.setReserva(reserva);
		asignacion.setUsuario(AutenticacionUtil.getUsuario().getUserName());
		asignacion.setFechaCreacion(new Date());
		// se crea la asociación entre la reserva y la actividad
		actividad.agregarAsignacionReserva(asignacion);
		reserva.agregarAsignacion(asignacion);
		// y se asocia la reserva a la hoja de servicio
		actividad.getHoja().agregarReserva(reserva);
		return asignacion;
	}
	
	/**
	 * Elimina todas las asignaciones que tenga una actividad, pero no borra
	 * las reservas.
	 * @param actividad Actividad a la que deben borrársele las asignaciones
	 */
	public void eliminarAsignacionesReserva(LineaActividad actividad) {
		// borramos la asociación de la actividad hacia la reserva
		//actividad.eliminarAsignacionReserva(reserva);
		for (AsignacionReserva asignacion : actividad.getListaAsignaciones()) {
			//IReserva reserva = asignacion.getReserva();
			//reserva.eliminarAsignacion(asignacion);
			//actividad.eliminarAsignacionReserva(asignacion);
			asignacion.eliminar();
		}
	}
	
	/**
	 * Elimina una asignación de reserva en particular, y deja sin tocar la línea
	 * de actividad ni la reserva. 
	 * @param asignacion Asignación de reserva a ser borrada
	 */
	public void eliminarAsignacionReserva(AsignacionReserva asignacion) {
		//LineaActividad linea = asignacion.getActividad();
		//IReserva reserva = asignacion.getReserva();
		//linea.eliminarAsignacionReserva(asignacion);
		//reserva.eliminarAsignacion(asignacion);
		asignacion.eliminar();
	}
	
	
	/**
	 * Agrega un guía a la hoja de operaciones
	 * @param hoja Hoja de operaciones
	 * @param reserva Reserva que se ha definido para el guía
	 */
	public void agregarGuia(HojaServicioTour hoja, ReservaGuia reserva) {
		reserva.setHoja(hoja);
		hoja.agregarReservaGuia(reserva);
	}
	
	/**
	 * Permite eliminar la asignación de un guía a la hoja de operaciones
	 * @param hoja Hoja de operaciones
	 * @param reserva Reserva del guía
	 */
	public void eliminarGuia(HojaServicioTour hoja, ReservaGuia reserva) {
		hoja.eliminarReservaGuia(reserva);
		reserva.setHoja(null);
	}
	
	
	@Deprecated public void agregarLineasFromTour(IHojaServicio registro, Tour tour,
			DisponibilidadTour disp) {
		Iterator<LineaTour> it = tour.getListaActividades().iterator();
		LineaTour linea = null;
		LineaActividad lineaAct = null;
		while (it.hasNext()) {
			linea = it.next();
			lineaAct = new LineaActividad();
			//lineaAct.setIdProducto(linea.getIdProducto());
			//lineaAct.setDspProducto(linea.getDspProducto());
			lineaAct.setProducto(linea.getProducto());
			lineaAct.setFecha(FechaUtil.ajustarFecha(disp.getFecha(), linea.getDia() - 1));
			lineaAct.setTipoReserva(linea.getTipoReserva());
			lineaAct.setComidas(linea.getComidas());
			lineaAct.setComentario(linea.getComentario());
			this.agregarActividad(registro, lineaAct);
		}
	}
	
	public void importarLineasCotizacion(IHojaServicio registro, Set<LineaCotizacion> lineas) {
		//Productos productos = new Productos();
		//Producto producto;
		for (LineaCotizacion lc : lineas) {
			for (int n = 0; n < lc.getCantidad().intValue(); n++) {
				//producto = productos.getProductoByIdProducto(lc.getIdProducto());
				LineaActividad la = new LineaActividad();
				//la.setIdProducto(lc.getIdProducto());
				la.setProducto(lc.getProducto());
				//la.setDspProducto(producto.getDescripcionHotel());
				la.setFecha(FechaUtil.ajustarFecha(lc.getFecha(), n));
				la.setPrecio(lc.getPrecio());        // ????????????
				la.setTipoReserva(lc.getProducto().getTipoReserva());
				la.setComentario(lc.getComentario());
				this.agregarActividad(registro, la);
			}
		}
	}
	
	public void importarLineasFromTour(IHojaServicio registro, Tour tour) {
		Set<LineaTour> lineas = tour.getListaActividades();
		for (LineaTour lineaTour : lineas) {
			LineaActividad lineaAct = new LineaActividad();
			lineaAct.setProducto(lineaTour.getProducto());
			Date fechaBase = registro.getFechaInicio();
			lineaAct.setFecha(FechaUtil.ajustarFecha(fechaBase, lineaTour.getDia().intValue() - 1));
			lineaAct.setPrecio(0F);
			lineaAct.setTipoReserva(lineaTour.getProducto().getTipoReserva());
			lineaAct.setComentario(lineaTour.getComentario());
			this.agregarActividad(registro, lineaAct);
		}
	}

	/**
	 * Genera el número de tour que corresponde a una reserva formalizada en operaciones
	 * El número toma el año y mes del día en curso, y le agrega un secuencial, quedando
	 * un formato de año, mes y secuencial.  Ejm:  0805-036 (año 2008, mes 05, secuencial 36).
	 * @return Número de tour.  Ejm:  0805-037
	 */
	public String generarNumeroTour() {
		String numeroReserva = "";
		String aa = String.valueOf(Calendar.getInstance().get(Calendar.YEAR)).substring(2);
		String yy = ("00" + String.valueOf(Calendar.getInstance().get(Calendar.MONTH) + 1));
		yy = yy.substring(yy.length() - 2);  // extraemos los dos últimos caracteres
		//String mm = ("00" + String.valueOf(Calendar.getInstance().get(Calendar.MINUTE))).substring(1);
		//numeroReserva = aa + yy + "-" + mm;
		Query q = getSession().createSQLQuery("select max(mid(numero, instr(numero, '-') + 1)) as max " +
				"from hojas_servicios where clase = 'T' and numero is not null");
		String secuenciaTxt = (String) q.uniqueResult();
		if (secuenciaTxt == null) {  // solo ocurre si no hay registros en la tabla (ningún tour tiene número)
			secuenciaTxt = "0";
		} else if (secuenciaTxt.equals("")) {  // esto no debería pasar, pero en pruebas lo logré, así que nos aseguramos  :(
			secuenciaTxt = "0";
		}
		Long secuencia = Long.valueOf(secuenciaTxt) + 1;
		secuenciaTxt = "000" + secuencia.toString();
		numeroReserva = aa + yy + "-" + secuenciaTxt.substring(secuenciaTxt.length() - 3);
		System.out.println("Número de reserva generado: " + numeroReserva);
		return numeroReserva;
	}
	
	
	/*
	public void getHojasAsociadas(HojaServicioTour hojaTour) {
		Criteria criteria = getSession().createCriteria(LineaActividad.class)
			.add(Restrictions.eq("hojaTour", hojaTour));
			//.createCriteria("hojaTour");
			//.setProjection(Projections.groupProperty("idTour").as("idTourx"));
		List<String> resultados = criteria.list();
		System.out.println("LLL: " + resultados);
	}
	*/
	
	public String getHojasAsociadas(HojaServicioTour hojaTour) {
		Query q = getSession().createQuery("select distinct(hoja.idHoja) " +
				"from IHojaServicio hoja, LineaActividad linea " +
				"where hoja = linea.hoja and linea.hojaTour = ? " +
				"order by hoja.idHoja")
				.setParameter(0, hojaTour);
		List<Long> resultados = q.list();
		String listaHojas = "";
		for (Long s : resultados) {
			if (listaHojas.equals("")) {
				listaHojas = s.toString();
			} else {
				listaHojas += "\n" + s.toString();
			}
		}
		return listaHojas;
	}
	
	
}
