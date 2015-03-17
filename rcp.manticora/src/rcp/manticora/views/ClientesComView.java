package rcp.manticora.views;


import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import rcp.manticora.controllers.ViewController;
import rcp.manticora.editors.ClientesComEditor;
import rcp.manticora.model.Cliente;
import rcp.manticora.model.ICliente;
import rcp.manticora.services.GenericSorter;

public class ClientesComView extends AbstractViewH implements IViewFilter {
	public static final String ID = "manticora.views.clientesComView";
	private Table tabla;
	private TableViewer viewer;
	private ViewController controller;
	private TableItem seleccion;

	public ClientesComView() {
		super();
		controller = new ViewController();
	}
	
	class ViewContentProvider implements IStructuredContentProvider {

        public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		}
        
		public void dispose() {
		}
        
		public Object[] getElements(Object parent) {
			Object[] resultados = (ICliente[]) parent;
			return resultados;
		}
	}
	
	class ViewLabelProvider extends LabelProvider implements ITableLabelProvider {
		public String getColumnText(Object obj, int index) {
			String resultado = "";
			ICliente c = (ICliente) obj;
			switch (index) {
			case 0:
				resultado = Long.toString(c.getIdCliente());
				break;
			case 1:
				resultado = c.getNombreCliente();
				break;
			case 2:
				resultado = c.getEmail();
				break;
			case 3:
				resultado = c.getTipo().getDescripcion();
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
		viewer.setInput(controller.getListadoClientesByClase(Cliente.Clase.JURIDICO));
		viewer.setSorter(new GenericSorter(viewer, 1));
		
		this.hookDoubleClickListener(viewer, ClientesComEditor.ID);
		
		// registramos el tableViewer como un selection provider
		getSite().setSelectionProvider(viewer);
	}

	
	private void crearTabla(Composite parent) {
		int style = SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.HIDE_SELECTION;
		tabla = new Table(parent, style);
		tabla.setLinesVisible(true);
		tabla.setHeaderVisible(true);
		
		TableColumn column = new TableColumn(tabla, SWT.RIGHT, 0);
		column.setText("Código");
		column.setWidth(50);
		column.setAlignment(SWT.RIGHT);  // aparentemente esto es ignorado
		column.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				((GenericSorter) viewer.getSorter()).doSort(0, GenericSorter.NUMERO, 1, true);
				viewer.refresh();
			}
		});
		
		column = new TableColumn(tabla, SWT.LEFT, 1);
		column.setText("Nombre");
		column.setWidth(250);
		column.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				((GenericSorter) viewer.getSorter()).doSort(1, GenericSorter.TEXTO, 0, true);
				viewer.refresh();
			}
		});
		
		column = new TableColumn(tabla, SWT.LEFT, 2);
		column.setText("email");
		column.setWidth(150);
		
		column = new TableColumn(tabla, SWT.LEFT, 3);
		column.setText("Tipo");
		column.setWidth(100);
	}
	
	public TableItem getSeleccion() {
		System.out.println("getSeleccion: " + tabla.getSelectionIndex());
		if (tabla.getSelectionIndex() != -1) {
			seleccion = tabla.getItem(tabla.getSelectionIndex());
		} else {
			seleccion = null;
		}
		return seleccion;
	}
	
	/*
	 * realiza un viewer.refresh()
	 */
	public void refrescar() {
		viewer.setInput(controller.getListadoClientesByClase(Cliente.Clase.JURIDICO));
		viewer.refresh();
	}


	public StructuredViewer getViewer() {
		return viewer;
	}
	

}
