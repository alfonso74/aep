package rcp.manticora.dao;

import java.util.List;


import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import rcp.manticora.model.Cliente;
import rcp.manticora.model.ICliente;
import rcp.manticora.model.Pax;

public class ClienteDAO extends NewGenericDAOImpl<ICliente, Long> {
	
	public List<ICliente> findByClaseX(String claseCliente) {
		Criterion c = Restrictions.eq("clase", claseCliente);
		List<ICliente> resultados = findByCriteria(c);
		return resultados;
	}
	
	
	public List<ICliente> findOrderedByClase(Cliente.Clase claseCliente) {
		Criterion c = Restrictions.eq("clase", claseCliente.getCodigo());
		List<ICliente> resultados = null;
		if (claseCliente == Cliente.Clase.NATURAL) {
			resultados = findByOrderedCriteria("nombre", true, c);
		} else if (claseCliente == Cliente.Clase.JURIDICO) {
			resultados = findByOrderedCriteria("nombreCia", true, c);
		}
		return resultados;
	}
	
	
	public List<ICliente> findOrderedComisionable() {
		Criterion c = Restrictions.eq("comision", Boolean.TRUE);
		List<ICliente> resultados = findByOrderedCriteria("nombre", true, c);
		return resultados;
	}
	
	
//	public List<ICliente> findOrderedByClase(Cliente.Clase claseCliente) {
//		Criterion c = Restrictions.eq("clase", claseCliente.getCodigo());
//		List<ICliente> resultados = null;
//		if (claseCliente.equals("N")) {
//			resultados = findByOrderedCriteria("nombre", true, c);
//		} else if (claseCliente.equals("C")) {
//			resultados = findByOrderedCriteria("nombreCia", true, c);
//		}
//		return resultados;
//	}
	
	
	public List buscarClienteNatural(String campo, String cadena) {
		Criterion c = Restrictions.eq("clase", "N");            // solamente buscamos clientes naturales
		if (campo.equals("Nombre")) {
			c = Restrictions.like("nombre", "%" + cadena + "%");
		}
		if (campo.equals("Apellido")) {
			c = Restrictions.like("apellido", "%" + cadena + "%");
		}
		if (campo.equals("Identificación")) {
			c = Restrictions.like("identificacion", "%" + cadena + "%");
		}
		List<ICliente> resultados = findByOrderedCriteria("nombre", true, c);
		return resultados;
	}
	
	public List buscarClienteJuridico(String campo, String cadena) {
		Criterion c = Restrictions.eq("clase", "C");            // solamente buscamos clientes jurídicos (compañías)
		if (campo.equals("Nombre")) {
			c = Restrictions.like("nombreCia", "%" + cadena + "%");
		}
		List<ICliente> resultados = findByOrderedCriteria("nombreCia", true, c);
		return resultados;
	}
	
}
