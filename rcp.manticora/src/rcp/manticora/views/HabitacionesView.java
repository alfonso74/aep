package rcp.manticora.views;


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
import rcp.manticora.editors.HabitacionesEditor;
import rcp.manticora.model.Habitacion;

public class HabitacionesView extends ViewPart implements IRefreshView {
	public static final String ID = "manticora.views.habitaciones";
	private ViewController controller;
	private Table tabla;
	private TableViewer viewer;

	public HabitacionesView() {
		controller = new ViewController();
	}

	@Override
	public void createPartControl(Composite parent) {
		crearTabla(parent);
		viewer = new TableViewer(tabla);
		viewer.setContentProvider(new ViewContentProvider());
		viewer.setLabelProvider(new ViewLabelProvider());
		viewer.setInput(controller.getListadoHabitaciones());
		// registramos el tableViewer como un selection provider
		// esto es requerido para obtener el elemento seleccionado en las acciones
		getSite().setSelectionProvider(viewer);
		
		// habilitamos el listener para doble clic
		this.hookDoubleClickListener(parent, HabitacionesEditor.ID);
	}
	
	public void crearTabla(Composite parent) {
		int style = SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.HIDE_SELECTION;
		tabla = new Table(parent, style);
		tabla.setLinesVisible(true);
		tabla.setHeaderVisible(true);
		
		TableColumn column = new TableColumn(tabla, SWT.RIGHT, 0);
		column.setText("Hotel");
		column.setWidth(120);
		column.setAlignment(SWT.RIGHT);  // aparentemente esto es ignorado
		
		column = new TableColumn(tabla, SWT.LEFT, 1);
		column.setText("Tipo");
		column.setWidth(60);
		
		column = new TableColumn(tabla, SWT.LEFT, 2);
		column.setText("Nombre");
		column.setWidth(120);
		
		column = new TableColumn(tabla, SWT.CENTER, 3);
		column.setText("No.");
		column.setWidth(35);
		
		column = new TableColumn(tabla, SWT.LEFT, 4);
		column.setText("Condiciones");
		column.setWidth(250);
	}

	@Override
	public void setFocus() {
	}

	public void refrescar() {
		viewer.setInput(controller.getListadoHabitaciones());
		viewer.refresh();
	}
	
	private void hookDoubleClickListener(Composite parent, final String editorID) {
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				System.out.println("Double click  :)");
				Object seleccion = ((StructuredSelection) viewer.getSelection()).getFirstElement();
				try {
					CommonEditorInput input = new CommonEditorInput(seleccion);
					getSite().getPage().openEditor(input, editorID);	
				} catch(PartInitException e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	class ViewContentProvider implements IStructuredContentProvider {

        public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		}
        
		public void dispose() {
		}
        
		public Object[] getElements(Object parent) {
			Object[] resultados = (Habitacion[]) parent;
			return resultados;
		}
	}
	
	class ViewLabelProvider extends LabelProvider implements ITableLabelProvider {
		public String getColumnText(Object obj, int index) {
			String resultado = "";
			Habitacion h = (Habitacion) obj;
			switch (index) {
			case 0:
				resultado = h.getHotel().getDescripcion();
				if (resultado.startsWith("Night at ")) {
					resultado = resultado.substring(9);
				}
				//resultado = "Hotel X";
				break;
			case 1:
				resultado = h.getTipo().getDescripcion();
				//resultado = "Tipo X";
				break;
			case 2:
				resultado = h.getNombre();
				break;
			case 3:
				resultado = h.getNumero().toString();
				break;
			case 4:
				resultado = h.getCondiciones();
				break;
			}
			return resultado;
		}
		public Image getColumnImage(Object obj, int index) {
			return null;		
		}
	}

}
