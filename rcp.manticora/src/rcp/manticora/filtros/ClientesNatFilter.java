package rcp.manticora.filtros;


import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import rcp.manticora.model.ClienteNatural;

public class ClientesNatFilter extends ViewerFilter {
	private Long codigo;
	private String nombre;
	private String apellido;
	private String identificacion;
	private String pais;
	
	public ClientesNatFilter(Long codigo, String nombre, String apellido,
			String identificacion, String pais) {
		super();
		this.codigo = codigo;
		this.nombre = nombre.toLowerCase();
		this.apellido = apellido.toLowerCase();
		this.identificacion = identificacion.toLowerCase();
		this.pais = pais.toLowerCase();
	}


	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		ClienteNatural c = (ClienteNatural) element;
		
		if (codigo != null) {
			if (!c.getIdCliente().equals(codigo)) return false;
		}
		if (!nombre.equals("")) {
			if (!c.getNombre().toLowerCase().contains(nombre)) return false;
		}
		if (!apellido.equals("")) {
			if (!c.getApellido().toLowerCase().contains(apellido)) return false;
		}
		if (!identificacion.equals("")) {
			if (!c.getIdentificacion().toLowerCase().contains(identificacion)) return false;
		}
		if (!pais.equals("")) {
			if (c.getDspPais() != null && !c.getDspPais().toLowerCase().contains(pais))
				return false;
		}		
		
		return true;
	}

}
