package rcp.manticora.views;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import rcp.manticora.controllers.ViewController;
import rcp.manticora.editors.CotizacionesEditor;
import rcp.manticora.model.Cotizacion;
import rcp.manticora.services.FechaUtil;
import rcp.manticora.services.GenericSorter;

public class CotizacionesView extends AbstractViewH implements IViewFilter {
	public static final String ID = "manticora.cotizacionesView";
	private ViewController controller;
	private Table tabla;
	private TableViewer viewer;

	public CotizacionesView() {
		super();
		controller = new ViewController();
	}

	public void createPartControl(Composite parent) {
		crearTabla(parent);
		viewer = new TableViewer(tabla);
		viewer.setContentProvider(new ViewContentProvider());
		viewer.setLabelProvider(new ViewLabelProvider());
		viewer.setInput(controller.getListadoCotizaciones());
		viewer.setSorter(new GenericSorter(viewer));
		// registramos el tableViewer como un selection provider
		// esto es requerido para obtener el elemento seleccionado en las acciones
		getSite().setSelectionProvider(viewer);
		
		// habilitamos el listener para doble clic
		this.hookDoubleClickListener(viewer, CotizacionesEditor.ID);
	}
	
	
	public void crearTabla(Composite parent) {
		int style = SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.HIDE_SELECTION;
		//style = SWT.VIRTUAL;
		tabla = new Table(parent, style);
		tabla.setLinesVisible(true);
		tabla.setHeaderVisible(true);
		
		TableColumn column = new TableColumn(tabla, SWT.RIGHT, 0);
		column.setText("ID");
		column.setWidth(35);
		column.setAlignment(SWT.RIGHT);  // aparentemente esto es ignorado
		
		column = new TableColumn(tabla, SWT.LEFT, 1);
		column.setText("Inicio");
		column.setWidth(75);
		column.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				((GenericSorter) viewer.getSorter()).doSort(1, GenericSorter.FECHA, 1, true);
				viewer.refresh();
			}
		});
		
		column = new TableColumn(tabla, SWT.LEFT, 2);
		column.setText("Fin");
		column.setWidth(75);
		
		column = new TableColumn(tabla, SWT.LEFT, 3);
		column.setText("Nombre del tour");
		column.setWidth(200);
		column.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				((GenericSorter) viewer.getSorter()).doSort(3, GenericSorter.TEXTO, 1, true);
				viewer.refresh();
			}
		});
		
		column = new TableColumn(tabla, SWT.LEFT, 4);
		column.setText("N�mero");
		column.setWidth(75);
		column.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				((GenericSorter) viewer.getSorter()).doSort(4, GenericSorter.TEXTO, 1, true);
				viewer.refresh();
			}
		});
		
		column = new TableColumn(tabla, SWT.LEFT, 5);
		column.setText("Vendedor asignado");
		column.setWidth(180);
	}


	public void refrescar() {
		viewer.setInput(controller.getListadoCotizaciones());
		viewer.refresh();
	}
	
	
	public TableViewer getViewer() {
		return viewer;
	}

	
	class ViewContentProvider implements IStructuredContentProvider {

        public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		}
        
		public void dispose() {
		}
        
		public Object[] getElements(Object parent) {
			Object[] resultados = (Object[]) parent;
			return resultados;
		}
	}
	
	class ViewLabelProvider extends LabelProvider implements ITableLabelProvider {
		public String getColumnText(Object obj, int index) {
			String resultado = "";
			Cotizacion c = (Cotizacion) obj;
			switch (index) {
			case 0:
				resultado = Long.toString(c.getIdCotizacion());
				break;
			case 1:
				resultado = FechaUtil.toString(c.getFechaInicio());
				break;
			case 2:
				resultado = FechaUtil.toString(c.getFechaFin());
				break;
			case 3:
				resultado = c.getNombre();
				break;
			case 4:
				resultado = c.getNumeroTourAsString();
				break;
			case 5:
				resultado = c.getDspVendedor();
				break;
			}
			return resultado;
		}
		public Image getColumnImage(Object obj, int index) {
			return null;		
		}
	}

}
