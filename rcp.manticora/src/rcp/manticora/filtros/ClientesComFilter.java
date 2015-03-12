package rcp.manticora.filtros;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import rcp.manticora.model.ClienteJuridico;

public class ClientesComFilter extends ViewerFilter {
	private Long codigo;
	private String nombre;
	private String contacto;
	private String pais;
	
	
	public ClientesComFilter(Long codigo, String nombre, String contacto, String pais) {
		super();
		this.codigo = codigo;
		this.nombre = nombre.toLowerCase();
		this.contacto = contacto.toLowerCase();
		this.pais = pais.toLowerCase();
	}

	
	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		
		ClienteJuridico c = (ClienteJuridico) element;
		
		if (codigo != null) {
			if (!c.getIdCliente().equals(codigo)) return false;
		}
		if (!nombre.equals("")) {
			if (!c.getNombreCliente().toLowerCase().contains(nombre)) return false;
		}
		if (!contacto.equals("")) {
			if (!c.getContacto().toLowerCase().contains(contacto)) return false;
		}
		if (!pais.equals("")) {
			if (c.getDspPais() != null && !c.getDspPais().toLowerCase().contains(pais))
				return false;
		}		
		
		return true;
	}

}
