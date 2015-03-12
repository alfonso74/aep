package rcp.manticora.views;

import java.text.SimpleDateFormat;


import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.ViewPart;

import rcp.manticora.controllers.ViewController;
import rcp.manticora.editors.CommonEditorInput;
import rcp.manticora.editors.SolicitudesEditor;
import rcp.manticora.model.Solicitud;

public class SolicitudesAsignadasView extends ViewPart implements IRefreshView {
	public static final String ID = "manticora.solicitudesAsignadasView";
	private Table tabla;
	private TableViewer viewer;
	private ViewController controller;

	public SolicitudesAsignadasView() {
		super();
		controller = new ViewController();
	}
	
	class ViewContentProvider implements IStructuredContentProvider {

        public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		}
        
		public void dispose() {
		}
        
		public Object[] getElements(Object parent) {
			Object[] resultados = (Solicitud[]) parent;
			return resultados;
			//return (Object[]) parent;
		}
	}
	
	class ViewLabelProvider extends LabelProvider implements ITableLabelProvider {
		public String getColumnText(Object obj, int index) {
			String resultado = "";
			Solicitud linea = (Solicitud) obj;
			switch (index) {
			case 0:
				resultado = new SimpleDateFormat("dd/MM/yyyy hh:mm a").format(linea.getFechaCreacion());
				break;
			case 1:
				resultado = linea.getNombre() + " " + linea.getApellido();
				break;
			case 2:
				resultado = linea.getDspPais();
				break;
			case 3:
				resultado = linea.getDspVendedor();
				break;
			}
			return resultado;
		}
		public Image getColumnImage(Object obj, int index) {
			return null;		
		}
	}

	public void createPartControl(Composite parent) {
		crearTabla(parent);

		viewer = new TableViewer(tabla);
		viewer.setContentProvider(new ViewContentProvider());
		viewer.setLabelProvider(new ViewLabelProvider());
		viewer.setInput(controller.getListadoSolicitudesByStatus("A"));
		
		// registramos el tableViewer como un selection provider
		// esto es requerido para obtener el elemento seleccionado en las acciones
		getSite().setSelectionProvider(viewer);
		
		// habilitamos el listener para doble clic
		this.hookDoubleClickListener(parent);
	}

	public void setFocus() {
	}
	
	private void crearTabla(Composite parent) {
		int style = SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.HIDE_SELECTION;
		tabla = new Table(parent, style);
		tabla.setLinesVisible(true);
		tabla.setHeaderVisible(true);
		
		TableColumn column = new TableColumn(tabla, SWT.RIGHT, 0);
		column.setText("Fecha creación");
		column.setWidth(115);
		column.setAlignment(SWT.RIGHT);  // aparentemente esto es ignorado
		
		column = new TableColumn(tabla, SWT.CENTER, 1);
		column.setText("Nombre del cliente");
		column.setWidth(180);
		column.setAlignment(SWT.LEFT);
		
		column = new TableColumn(tabla, SWT.CENTER, 2);
		column.setText("País");
		column.setWidth(90);
		column.setAlignment(SWT.LEFT);
		
		column = new TableColumn(tabla, SWT.CENTER, 3);
		column.setText("Asignada a");
		column.setWidth(130);
		column.setAlignment(SWT.LEFT);
	}

	public void refrescar() {
		controller.getListadoSolicitudesByStatus("A");
		viewer.refresh();
	}
	
	private void hookDoubleClickListener(Composite parent) {
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				System.out.println("Double click  :)");
				Object seleccion = ((StructuredSelection) viewer.getSelection()).getFirstElement();
				try {
					Solicitud elemento = (Solicitud) seleccion;
					CommonEditorInput input = new CommonEditorInput(elemento);
					getSite().getPage().openEditor(input, SolicitudesEditor.ID);	
				} catch(PartInitException e) {
					e.printStackTrace();
				}
			}
		});
	}

}
