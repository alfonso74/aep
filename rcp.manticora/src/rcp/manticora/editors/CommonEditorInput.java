package rcp.manticora.editors;


import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

import rcp.manticora.IEditableDocument;

/**
 * Permite abrir un editor.  Provee funciones para asignar títulos y determinar si 
 * hay otro editor con el mismo nombre abierto.
 * <p>
 * También abre editores para PDFs, en cuyo caso se debe inicializar el nombre (setName) y
 * la ruta del reporte (setRutaReporte).
 *
 */
public class CommonEditorInput implements IEditorInput {
	private Object elemento;
	private String nombre;
	private String rutaReporte;    // usado en generación de reportes
	public boolean isNewDoc = true;

	public CommonEditorInput() {
		super();
		this.nombre = "Nuevo";
		this.isNewDoc = true;
	}
	
	/*public CommonEditorInput(String nombre) {
		super();
		this.nombre = nombre;
		this.isNewDoc = true;
	}*/
	
	public CommonEditorInput(Object elemento) {
		this.elemento = elemento;
		this.nombre = ((IEditableDocument) elemento).getTituloDocumento();
		this.isNewDoc = false;
	}

	public boolean exists()  {
		return false;
	}

	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	public String getName() {
		return nombre;
	}
	
	public void setName(String nombre) {
		this.nombre = nombre;
	}
	
	// usado en generación de reportes de BIRT
	public String getRutaReporte() {
		if (rutaReporte == null) {
			System.err.println("Error: No se ha especificado la ruta del reporte (usar función getRutaReporte())");
		}
		return rutaReporte;
	}
	
	// usado en generación de reportes de BIRT
	public void setRutaReporte(String rutaReporte) {
		this.rutaReporte = rutaReporte;
	}

	public Object getElemento() {
		return elemento;
	}

	public IPersistableElement getPersistable() {
		return null;
	}

	public String getToolTipText() {
		return nombre;
	}


	public Object getAdapter(Class adapter) {
		return null;
	}

	
	public boolean equals(Object obj) {
		if (super.equals(obj)) {
			System.out.println("Obj true: " + obj.getClass());
			return true;
		};
		
		if (!(obj instanceof CommonEditorInput)) return false;
		CommonEditorInput other = (CommonEditorInput) obj;
		System.out.println("This: " + this.getName());
		System.out.println("Other: " + other.getName());
		return getName().equals(other.getName());
	}

	
	/*
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof BillingDetails)) return false;

		final BillingDetails billingDetails = (BillingDetails) o;

		if (!getCreated().equals(billingDetails.getCreated())) return false;
		if (!getOwnerName().equals(billingDetails.getOwnerName())) return false;

		return true;
	}*/
}
