package rcp.manticora.controllers;

import rcp.manticora.dao.ReservaDAO;
import rcp.manticora.model.ILineaHospedaje;
import rcp.manticora.model.IReserva;
import rcp.manticora.model.ReservaHospedaje;

public class ReservaController extends AbstractControllerNew<IReserva> {
	
	/**
	 * Crea una instancia de este controller
	 * @param editorID:  nombre del editor que será utilizado para identificar
	 * de manera única la sesión de este controller.
	 */
	public ReservaController(String editorId) {
		super(editorId, new ReservaDAO());
		// TODO Auto-generated constructor stub
	}

	/*
	private ReservaDAO dao;

	public ReservaController(String editorId) {
		super(editorId);
		dao = new ReservaDAO();
		dao.setSession(session);
	}
	
	public void doSave(IReserva registro) {
		session.beginTransaction();
		dao.makePersistent(registro);
		dao.flush();            // extiende las modificaciones al caché
		session.getTransaction().commit();
	}
	
	*/
	
	/**
	 * Asigna una línea de detalle a una reserva de hospedaje. La relación se establece en
	 * ambos sentidos.
	 * @param reserva Reserva de hospedaje (ReservaHospedaje)
	 * @param linea Línea de detalle (ILineaHospedaje)
	 */
	public void agregarLinea(ReservaHospedaje reserva, ILineaHospedaje linea) {
		reserva.agregarLineaHospedaje(linea);
		linea.setReserva(reserva);
	}
	
	/**
	 * Elimina la asociación bidireccional entre una reserva y una línea de detalle.
	 * @param reserva Reserva de hospedaje (ReservaHospedaje)
	 * @param linea Línea de detalle (ILineaHospedaje)
	 */
	public void eliminarLinea(ReservaHospedaje reserva, ILineaHospedaje linea) {
		reserva.eliminarLineaHospedaje(linea);
		linea.setReserva(null);
	}

}
