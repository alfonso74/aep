package rcp.manticora.views;


import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import rcp.manticora.controllers.ViewController;
import rcp.manticora.editors.UsuariosEditor;
import rcp.manticora.model.Usuario;

public class UsuariosView extends AbstractViewH {
	public static final String ID = "manticora.usuariosView";
	private ViewController controller;
	private Table tabla;
	private TableViewer viewer;

	public UsuariosView() {
		controller = new ViewController();
	}
	
	
	@Override
	public void createPartControl(Composite parent) {
		crearTabla(parent);
		viewer = new TableViewer(tabla);
		viewer.setContentProvider(new ViewContentProvider());
		viewer.setLabelProvider(new ViewLabelProvider());
		viewer.setInput(controller.getListadoUsuarios());
		// registramos el tableViewer como un selection provider
		// esto es requerido para obtener el elemento seleccionado en las acciones
		getSite().setSelectionProvider(viewer);
		
		// habilitamos el listener para doble clic
		hookDoubleClickListener(viewer, UsuariosEditor.ID);
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
		column.setText("Nombre y apellido");
		column.setWidth(240);
		
		column = new TableColumn(tabla, SWT.LEFT, 2);
		column.setText("Username");
		column.setWidth(120);
		
		column = new TableColumn(tabla, SWT.LEFT, 3);
		column.setText("Estado");
		column.setWidth(80);
	}

	
	public void refrescar() {
		System.out.println("Refrescando UsuariosView");
		viewer.setInput(controller.getListadoUsuarios());
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
			Usuario rol = (Usuario) obj;
			switch (index) {
			case 0:
				resultado = Long.toString(rol.getIdUsuario());
				break;
			case 1:
				resultado = rol.getNombreCompleto();
				break;
			case 2:
				resultado = rol.getUserName();
				break;
			case 3:
				resultado = rol.getDspEstado();
				break;
			}
			return resultado;
		}
		public Image getColumnImage(Object obj, int index) {
			return null;		
		}
	}

}

