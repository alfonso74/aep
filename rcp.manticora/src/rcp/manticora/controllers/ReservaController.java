package rcp.manticora.controllers;

import rcp.manticora.dao.ReservaDAO;
import rcp.manticora.model.ILineaHospedaje;
import rcp.manticora.model.IReserva;
import rcp.manticora.model.ReservaHospedaje;

public class ReservaController extends AbstractControllerNew<IReserva> {
	
	/**
	 * Crea una instancia de este controller
	 * @param editorID:  nombre del editor que ser� utilizado para identificar
	 * de manera �nica la sesi�n de este controller.
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
		dao.flush();            // extiende las modificaciones al cach�
		session.getTransaction().commit();
	}
	
	*/
	
	/**
	 * Asigna una l�nea de detalle a una reserva de hospedaje. La relaci�n se establece en
	 * ambos sentidos.
	 * @param reserva Reserva de hospedaje (ReservaHospedaje)
	 * @param linea L�nea de detalle (ILineaHospedaje)
	 */
	public void agregarLinea(ReservaHospedaje reserva, ILineaHospedaje linea) {
		reserva.agregarLineaHospedaje(linea);
		linea.setReserva(reserva);
	}
	
	/**
	 * Elimina la asociaci�n bidireccional entre una reserva y una l�nea de detalle.
	 * @param reserva Reserva de hospedaje (ReservaHospedaje)
	 * @param linea L�nea de detalle (ILineaHospedaje)
	 */
	public void eliminarLinea(ReservaHospedaje reserva, ILineaHospedaje linea) {
		reserva.eliminarLineaHospedaje(linea);
		linea.setReserva(null);
	}

}
