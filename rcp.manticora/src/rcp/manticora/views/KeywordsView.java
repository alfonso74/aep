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
import rcp.manticora.editors.KeywordsEditor;
import rcp.manticora.model.Keyword;

public class KeywordsView extends ViewPart implements IRefreshView {
	public static final String ID = "manticora.keywordsView";
	private ViewController controller;
	private Table tabla;
	private TableViewer viewer;

	public KeywordsView() {
		controller = new ViewController();
	}

	public void createPartControl(Composite parent) {
		crearTabla(parent);
		viewer = new TableViewer(tabla);
		viewer.setContentProvider(new ViewContentProvider());
		viewer.setLabelProvider(new ViewLabelProvider());
		viewer.setInput(controller.getListadoKeywords());
		// registramos el tableViewer como un selection provider
		// esto es requerido para obtener el elemento seleccionado en las acciones
		getSite().setSelectionProvider(viewer);
		
		// habilitamos el listener para doble clic
		this.hookDoubleClickListener(parent);
	}
	
	public void crearTabla(Composite parent) {
		int style = SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.HIDE_SELECTION;
		tabla = new Table(parent, style);
		tabla.setLinesVisible(true);
		tabla.setHeaderVisible(true);
		
		TableColumn column = new TableColumn(tabla, SWT.RIGHT, 0);
		column.setText("ID");
		column.setWidth(35);
		column.setAlignment(SWT.RIGHT);  // aparentemente esto es ignorado
		
		column = new TableColumn(tabla, SWT.LEFT, 1);
		column.setText("Código");
		column.setWidth(45);
		
		column = new TableColumn(tabla, SWT.LEFT, 2);
		column.setText("Descripcion");
		column.setWidth(240);
		
		column = new TableColumn(tabla, SWT.LEFT, 3);
		column.setText("Tipo");
		column.setWidth(120);
	}

	public void setFocus() {
	}

	public void refrescar() {
		viewer.setInput(controller.getListadoKeywords());
		viewer.refresh();
	}
	
	private void hookDoubleClickListener(Composite parent) {
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				System.out.println("Double click  :)");
				Object seleccion = ((StructuredSelection) viewer.getSelection()).getFirstElement();
				try {
					CommonEditorInput input = new CommonEditorInput(seleccion);
					getSite().getPage().openEditor(input, KeywordsEditor.ID);	
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
			Object[] resultados = (Keyword[]) parent;
			return resultados;
		}
	}
	
	class ViewLabelProvider extends LabelProvider implements ITableLabelProvider {
		public String getColumnText(Object obj, int index) {
			String resultado = "";
			Keyword k = (Keyword) obj;
			switch (index) {
			case 0:
				resultado = Long.toString(k.getIdKeyword());
				break;
			case 1:
				resultado = k.getCodigo();
				break;
			case 2:
				resultado = k.getDescripcion();
				break;
			case 3:
				resultado = k.getTipo();
				break;
			}
			return resultado;
		}
		public Image getColumnImage(Object obj, int index) {
			return null;		
		}
	}

}
