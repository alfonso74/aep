package rcp.manticora.views;


import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
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
import org.eclipse.ui.PartInitException;

import rcp.manticora.controllers.ViewController;
import rcp.manticora.editors.CommonEditorInput;
import rcp.manticora.editors.HojaOpsEditor;
import rcp.manticora.editors.HojaVentasEditor;
import rcp.manticora.model.HojaServicioTour;
import rcp.manticora.model.HojaServicioVentas;
import rcp.manticora.model.IHojaServicio;
import rcp.manticora.services.FechaUtil;
import rcp.manticora.services.GenericSorter;

public class HojasOpsNuevasView extends AbstractViewH {
	public static final String ID = "manticora.hojasOpsNuevasView";
	private ViewController controller;
	private Table tabla;
	private TableViewer viewer;
	
	public HojasOpsNuevasView() {
		super();
		controller = new ViewController();
	}

	public void createPartControl(Composite parent) {
		crearTabla(parent);
		viewer = new TableViewer(tabla);
		viewer.setContentProvider(new ViewContentProvider());
		viewer.setLabelProvider(new ViewLabelProvider());
		viewer.setInput(controller.getListadoHojasByStatus("N"));
		viewer.setSorter(new GenericSorter(viewer));
		// registramos el tableViewer como un selection provider
		// esto es requerido para obtener el elemento seleccionado en las acciones
		getSite().setSelectionProvider(viewer);
		
		// habilitamos el listener para doble clic
		this.hookDoubleClickListener(viewer);
	}
	
	public void crearTabla(Composite parent) {
		int style = SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.HIDE_SELECTION;
		tabla = new Table(parent, style);
		tabla.setLinesVisible(true);
		tabla.setHeaderVisible(true);
		
		TableColumn column = new TableColumn(tabla, SWT.RIGHT, 0);
		column.setText("Número");
		column.setWidth(50);
		column.setAlignment(SWT.RIGHT);  // aparentemente esto es ignorado
		
		column = new TableColumn(tabla, SWT.LEFT, 1);
		column.setText("Inicio");
		column.setWidth(70);
		column.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				((GenericSorter) viewer.getSorter()).doSort(1, GenericSorter.FECHA, 1, true);
				viewer.refresh();
			}
		});
		
		column = new TableColumn(tabla, SWT.LEFT, 2);
		column.setText("Fin");
		column.setWidth(70);
		
		column = new TableColumn(tabla, SWT.LEFT, 3);
		column.setText("Nombre");
		column.setWidth(200);
		column.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				((GenericSorter) viewer.getSorter()).doSort(3, GenericSorter.TEXTO, 1, true);
				viewer.refresh();
			}
		});
		
		column = new TableColumn(tabla, SWT.LEFT);
		column.setText("Clase");
		column.setWidth(75);
		
	}

	public void setFocus() {
	}
	
	
	protected void hookDoubleClickListener(final StructuredViewer viewer) {
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				System.out.println("Double click  :)");
				Object seleccion = ((StructuredSelection) viewer.getSelection()).getFirstElement();
				try {
					String editorId = HojaOpsEditor.ID;
					if (seleccion instanceof HojaServicioTour) {
						editorId = HojaOpsEditor.ID;
					} else if (seleccion instanceof HojaServicioVentas) {
						editorId = HojaVentasEditor.ID;
					}
					CommonEditorInput input = new CommonEditorInput(seleccion);
					getSite().getPage().openEditor(input, editorId);
				} catch(PartInitException e) {
					e.printStackTrace();
				}
			}
		});
	}
	

	public void refrescar() {
		System.out.println("Refrescando HojasView");
		viewer.setInput(controller.getListadoHojasByStatus("N"));
		viewer.refresh();
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
			IHojaServicio h = (IHojaServicio) obj;
			switch (index) {
			case 0:
				resultado = Long.toString(h.getIdHoja());
				break;
			case 1:
				resultado = FechaUtil.toString(h.getFechaInicio());
				break;
			case 2:
				resultado = FechaUtil.toString(h.getFechaFin());
				break;
			case 3:
				resultado = h.getNombre();
				break;
			case 4:
				String clase = h.getClase();
				if (clase.equals("V")) {
					resultado = "Ventas";
				} else if (clase.equals("T")) {
					resultado = "Operaciones";
				} else {
					resultado = clase;
				}
			}
			return resultado;
		}
		public Image getColumnImage(Object obj, int index) {
			return null;		
		}
	}

}
