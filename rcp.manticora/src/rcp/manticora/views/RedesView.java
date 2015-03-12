package rcp.manticora.views;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import rcp.manticora.controllers.ViewController;
import rcp.manticora.editors.RedesEditor;
import rcp.manticora.model.Red;

import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.jface.viewers.TableViewerColumn;


public class RedesView extends AbstractViewH {

	public static final String ID = "rcp.manticora.views.RedesView"; //$NON-NLS-1$
	private ViewController controller;
	private Table table;
	private TableViewer viewer;
	

	public RedesView() {
		controller = new ViewController();
	}

	/**
	 * Create contents of the view part.
	 * @param parent
	 */
	@Override
	public void createPartControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		viewer = new TableViewer(container, SWT.BORDER | SWT.FULL_SELECTION);
		table = viewer.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		
		TableViewerColumn tableViewerColumn = new TableViewerColumn(viewer, SWT.NONE);
		TableColumn tblclmnId = tableViewerColumn.getColumn();
		tblclmnId.setWidth(50);
		tblclmnId.setText("ID");
		
		TableViewerColumn tableViewerColumn_2 = new TableViewerColumn(viewer, SWT.NONE);
		TableColumn tblclmnDescripcion = tableViewerColumn_2.getColumn();
		tblclmnDescripcion.setWidth(200);
		tblclmnDescripcion.setText("Nombre de la red");
		
		TableViewerColumn tableViewerColumn_1 = new TableViewerColumn(viewer, SWT.NONE);
		TableColumn tblclmnEstado = tableViewerColumn_1.getColumn();
		tblclmnEstado.setWidth(70);
		tblclmnEstado.setText("Estado");

		createActions();
		initializeToolBar();
		initializeMenu();
		
		inicializarViewer();
	}
	
	
	protected void inicializarViewer() {
		viewer.setContentProvider(new ViewContentProvider());
		viewer.setLabelProvider(new ViewLabelProvider());
		viewer.setInput(controller.getListadoRedes());
		// registramos el tableViewer como un selection provider
		// esto es requerido para obtener el elemento seleccionado en las acciones
		getSite().setSelectionProvider(viewer);
		// habilitamos el listener para doble clic
		this.hookDoubleClickListener(viewer, RedesEditor.ID);
	}

	
	/**
	 * Create the actions.
	 */
	private void createActions() {
		// Create the actions
	}

	/**
	 * Initialize the toolbar.
	 */
	private void initializeToolBar() {
		@SuppressWarnings("unused")
		IToolBarManager toolbarManager = getViewSite().getActionBars()
				.getToolBarManager();
	}

	/**
	 * Initialize the menu.
	 */
	private void initializeMenu() {
		@SuppressWarnings("unused")
		IMenuManager menuManager = getViewSite().getActionBars()
				.getMenuManager();
	}

	@Override
	public void setFocus() {
		// Set the focus
	}

	public void refrescar() {
		System.out.println("Refrescando RedesView");
		viewer.setInput(controller.getListadoRedes());
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
			Red k = (Red) obj;
			switch (index) {
			case 0:
				resultado = Long.toString(k.getIdRed());
				break;
			case 1:
				resultado = k.getDescripcion();
				break;
			case 2:
				resultado = k.getEstado();
				break;
			}
			return resultado;
		}
		
		public Image getColumnImage(Object obj, int index) {
			return null;		
		}
	}
}
