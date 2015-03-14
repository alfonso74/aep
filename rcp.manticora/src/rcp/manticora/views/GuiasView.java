package rcp.manticora.views;

import java.util.List;


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
import org.hibernate.Session;

import rcp.manticora.dao.GuiaDAO;
import rcp.manticora.editors.GuiasEditor;
import rcp.manticora.model.Guia;
import rcp.manticora.services.HibernateUtil;

/**
 * Clase utilizada para presentar los datos de un guía.  Especial porque en lugar de utilizar
 * un controller, accesa los datos a través de un DAO.  Por ahora no parece muy prometedor,
 * sigue pareciendo más útil y flexible el uso del controller.
 * @author Carlos Alfonso
 */

public class GuiasView extends AbstractViewH {
	public static final String ID = "manticora.guiasView";
	private Table tabla;
	private TableViewer viewer;
	private List<Guia> listaGuias;   // estamos omitiendo el uso de un controller
	private GuiaDAO guiaDAO;
	
	public GuiasView() {
		guiaDAO = new GuiaDAO();
	}
	
	
	@Override
	public void createPartControl(Composite parent) {
		HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
		listaGuias = guiaDAO.findAll();       // estamos omitiendo el uso de un controller
		crearTabla(parent);
		viewer = new TableViewer(tabla);
		viewer.setContentProvider(new ViewContentProvider());
		viewer.setLabelProvider(new ViewLabelProvider());
		//viewer.setInput(guiaDAO.findAll());
		viewer.setInput(listaGuias);
		// registramos el tableViewer como un selection provider
		// esto es requerido para obtener el elemento seleccionado en las acciones
		getSite().setSelectionProvider(viewer);
		
		// habilitamos el listener para doble clic
		this.hookDoubleClickListener(viewer, GuiasEditor.ID);
		HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
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
		column.setText("Nombre");
		column.setWidth(240);
		
		column = new TableColumn(tabla, SWT.LEFT, 2);
		column.setText("Estado");
		column.setWidth(80);
	}


	public void refrescar() {
		System.out.println("Refrescando GuiasView");
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		guiaDAO.setSession(session);
		viewer.setInput(guiaDAO.findAll());
		viewer.refresh();
		session.getTransaction().commit();
	}
	

	class ViewContentProvider implements IStructuredContentProvider {

        public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		}
        
		public void dispose() {
			System.out.println("Cerrando viewContentProvider");
		}
        
		public Object[] getElements(Object parent) {
			// estamos omitiendo el uso de un controller y el listado viene como un List en lugar de un Array de Guia
			Object[] resultados = ((List) parent).toArray(new Guia[((List) parent).size()]);
			return resultados;
		}
	}
	
	class ViewLabelProvider extends LabelProvider implements ITableLabelProvider {
		public String getColumnText(Object obj, int index) {
			String resultado = "";
			Guia g = (Guia) obj;
			switch (index) {
			case 0:
				resultado = g.getIdGuia().toString();
				break;
			case 1:
				resultado = g.getNombre() + " " + g.getApellido();
				break;
			case 2:
				resultado = g.getDspEstado();
				break;
			}
			return resultado;
		}
		public Image getColumnImage(Object obj, int index) {
			return null;		
		}
	}

}
