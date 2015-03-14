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
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.ViewPart;

import rcp.manticora.controllers.ViewController;
import rcp.manticora.editors.CommonEditorInput;
import rcp.manticora.editors.TipoProductosEditor;
import rcp.manticora.model.TipoProducto;

public class TipoProductosView extends ViewPart implements IRefreshView {
	public static final String ID = "manticora.tipoProductosView";
	private Table tabla;
	private TableViewer viewer;
	private ViewController controller;
	private int seleccion;

	public TipoProductosView() {
		super();
		controller = new ViewController();
	}
	
	class ViewContentProvider implements IStructuredContentProvider {

        public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		}
        
		public void dispose() {
		}
        
		public Object[] getElements(Object parent) {
			Object[] resultados = (TipoProducto[]) parent;
			return resultados;
		}
	}
	
	class ViewLabelProvider extends LabelProvider implements ITableLabelProvider {
		public String getColumnText(Object obj, int index) {
			String resultado = "";
			TipoProducto tipo = (TipoProducto) obj;
			switch (index) {
			case 0:
				resultado = tipo.getIdTipo().toString();
				break;
			case 1:
				resultado = tipo.getDescripcion();
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
		tabla.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				//System.out.println("Elemento seleccionado: " + tabla.getSelectionIndex());
				seleccion = Integer.parseInt(tabla.getItem(tabla.getSelectionIndex()).getText(0));
				System.out.println("Seleccion: " + seleccion);
			}
		});
		viewer = new TableViewer(tabla);
		viewer.setContentProvider(new ViewContentProvider());
		viewer.setLabelProvider(new ViewLabelProvider());
		viewer.setInput(controller.getListadoTipoProductos());
		
		// registramos el tableViewer como un selection provider
		getSite().setSelectionProvider(viewer);
		
		// y lo habilitamos para doubleclick
		this.hookDoubleClickListener(parent, TipoProductosEditor.ID);
	}

	public void setFocus() {

	}
	
	private void crearTabla(Composite parent) {
		int style = SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.HIDE_SELECTION;
		tabla = new Table(parent, style);
		tabla.setLinesVisible(true);
		tabla.setHeaderVisible(true);
		
		TableColumn column = new TableColumn(tabla, SWT.RIGHT, 0);
		column.setText("ID");
		column.setWidth(35);
		column.setAlignment(SWT.RIGHT);  // aparentemente esto es ignorado
		
		column = new TableColumn(tabla, SWT.CENTER, 1);
		column.setText("Descripción");
		column.setWidth(180);
		column.setAlignment(SWT.LEFT);
	}
	
	public int getSeleccion() {
		return seleccion;
	}
	
	public void refrescar() {
		viewer.setInput(controller.getListadoTipoProductos());
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
}
