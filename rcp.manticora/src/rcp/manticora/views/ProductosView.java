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
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.ViewPart;

import rcp.manticora.controllers.ViewController;
import rcp.manticora.editors.CommonEditorInput;
import rcp.manticora.editors.ProductosEditor;
import rcp.manticora.model.Producto;
import rcp.manticora.services.FormUtils;
import rcp.manticora.services.GenericSorter;

public class ProductosView extends ViewPart implements IRefreshView {
	public static final String ID = "manticora.productosView";
	private Table tabla;
	private TableColumn column;
	private TableViewer viewer;
	private ViewController controller;
	private TableItem seleccion;
	private FormUtils formUtils;

	public ProductosView() {
		controller = new ViewController();
		formUtils = new FormUtils();
	}
	
	
	class ViewContentProvider implements IStructuredContentProvider {

        public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		}
        
		public void dispose() {
		}
        
		public Object[] getElements(Object parent) {
			Object[] resultados = (Producto[]) parent;
			return resultados;
		}
	}
	
	class ViewLabelProvider extends LabelProvider implements ITableLabelProvider {

		public String getColumnText(Object obj, int index) {
			String resultado = "";
			Producto tipo = (Producto) obj;
			switch (index) {
			case 0:
				resultado = tipo.getIdTipo().toString();
				break;
			case 1:
				resultado = tipo.getIdProducto().toString();
				break;
			case 2:
				resultado = tipo.getDescripcion();
				break;
			case 3:
				resultado = formUtils.valor2Txt(tipo.getCosto(), "#,##0.00");
				break;
			case 4:
				resultado = formUtils.valor2Txt(tipo.getVenta1(), "#,##0.00");
				break;
			case 5:
				resultado = formUtils.valor2Txt(tipo.getVenta2(), "#,##0.00");
				break;
			case 6:
				resultado = formUtils.valor2Txt(tipo.getVenta3(), "#,##0.00");
				break;
			case 7:
				resultado = tipo.isModificable().booleanValue() == true ? "Si" : "No";
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
				//seleccion = Integer.parseInt(tabla.getItem(tabla.getSelectionIndex()).getText(0));
				//seleccion = tabla.getItem(tabla.getSelectionIndex());
				//System.out.println("Seleccion: " + seleccion);
			}
		});
		viewer = new TableViewer(tabla);
		viewer.setContentProvider(new ViewContentProvider());
		viewer.setLabelProvider(new ViewLabelProvider());
		viewer.setInput(controller.getListadoProductos());
		viewer.setSorter(new GenericSorter(viewer, 1, GenericSorter.NUMERO));
		
		// registramos el tableViewer como un selection provider
		getSite().setSelectionProvider(viewer);
		
		// habilitamos el listener para doble clic
		this.hookDoubleClickListener(parent, ProductosEditor.ID);

	}

	public void setFocus() {
	}
	
	private void crearTabla(Composite parent) {	
		int style = SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.HIDE_SELECTION;
		tabla = new Table(parent, style);
		tabla.setLinesVisible(true);
		tabla.setHeaderVisible(true);
		//tabla.setSortDirection(SWT.UP);
		
		//TableColumn column = new TableColumn(tabla, SWT.RIGHT, 0);
		column = new TableColumn(tabla, SWT.RIGHT, 0);
		column.setText("Tipo");
		column.setWidth(40);
		column.setAlignment(SWT.RIGHT);  // aparentemente esto es ignorado
		column.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				((GenericSorter) viewer.getSorter()).doSort(0, GenericSorter.NUMERO, 1, true);
				viewer.refresh();
			}
		});
		
		column = new TableColumn(tabla, SWT.RIGHT, 1);
		column.setText("Cód.");
		column.setWidth(50);
		column.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				((GenericSorter) viewer.getSorter()).doSort(1, GenericSorter.NUMERO, 1, true);
				viewer.refresh();
			}
		});
		
		column = new TableColumn(tabla, SWT.LEFT, 2);
		column.setText("Descripción");
		column.setWidth(235);
		column.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				((GenericSorter) viewer.getSorter()).doSort(2, GenericSorter.TEXTO, 1, true);
				viewer.refresh();
			}
		});
		
		column = new TableColumn(tabla, SWT.RIGHT, 3);
		column.setText("Costo");
		column.setWidth(65);
		column.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				((GenericSorter) viewer.getSorter()).doSort(3, GenericSorter.NUMERO, 1, true);
				viewer.refresh();
			}
		});
		
		column = new TableColumn(tabla, SWT.RIGHT, 4);
		column.setText("Público");
		column.setWidth(65);
		
		column = new TableColumn(tabla, SWT.RIGHT, 5);
		column.setText("Operador");
		column.setWidth(65);
		
		column = new TableColumn(tabla, SWT.RIGHT, 6);
		column.setText("Comisionable");
		column.setWidth(65);
		
		column = new TableColumn(tabla, SWT.CENTER, 7);
		column.setText("Mod");
		column.setWidth(35);
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
	
	public TableViewer getViewer() {
		return viewer;
	}
	
	public void refrescar() {
		viewer.setInput(controller.getListadoProductos());
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
