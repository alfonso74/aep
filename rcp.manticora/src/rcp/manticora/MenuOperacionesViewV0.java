package rcp.manticora;

import java.util.ArrayList;


import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import rcp.manticora.views.ClientesComView;
import rcp.manticora.views.ClientesView;
import rcp.manticora.views.CotizacionesActivasView;
import rcp.manticora.views.CotizacionesProcesadasView;
import rcp.manticora.views.HojasView;
import rcp.manticora.views.ProductosView;
import rcp.manticora.views.SolicitudesView;
import rcp.manticora.views.TemplatesView;
import rcp.manticora.views.ToursView;

public class MenuOperacionesViewV0 extends ViewPart {
	public static final String ID = "manticora.menuOperacionesView";
	private TreeViewer viewer;

	public MenuOperacionesViewV0() {
		//super();
	}
	
	class TreeObject {
		private String name;
		private TreeParent parent;
		private String imageKey = ISharedImages.IMG_OBJ_ELEMENT;
		private Image image;
		private String viewId;
		
		public TreeObject(String name) {
			this.name = name;
			image = PlatformUI.getWorkbench().getSharedImages().getImage(imageKey);
		}
		public TreeObject(String name, String imageKey) {
			this.name = name;
			this.imageKey = imageKey;
			ImageDescriptor imageDescriptor = AbstractUIPlugin.imageDescriptorFromPlugin(Application.PLUGIN_ID, imageKey);
			image = imageDescriptor.createImage();
		}
		public TreeObject(String name, String viewId, String imageKey) {
			this.name = name;
			this.imageKey = imageKey;
			ImageDescriptor imageDescriptor = AbstractUIPlugin.imageDescriptorFromPlugin(Application.PLUGIN_ID, imageKey);
			image = imageDescriptor.createImage();
			this.viewId = viewId;
		}
		
		public String getName() {
			return name;
		}
		public void setParent(TreeParent parent) {
			this.parent = parent;
		}
		public TreeParent getParent() {
			return parent;
		}
		public Image getImage() {
			return image;
		}
		public void setImage(Image image) {
			this.image = image;
		}
		public String toString() {
			return getName();
		}
		public String getViewId() {
			return viewId;
		}
	}
	
	class TreeParent extends TreeObject {
		private ArrayList<TreeObject> children;
		private String imageKey = ISharedImages.IMG_OBJ_FOLDER;
		
		public TreeParent(String name) {
			super(name);
			setImage(PlatformUI.getWorkbench().getSharedImages().getImage(imageKey));
			children = new ArrayList<TreeObject>();
		}
		public TreeParent(String name, String imageKey) {
			super(name, imageKey);
		}
		public void addChild(TreeObject child) {
			children.add(child);
			child.setParent(this);
		}
		public void removeChild(TreeObject child) {
			children.remove(child);
			child.setParent(null);
		}
		public TreeObject[] getChildren() {
			return (TreeObject[]) children.toArray(new TreeObject[children.size()]);
		}
		public boolean hasChildren() {
			return children.size()>0;
		}
	}

	class ViewContentProvider implements IStructuredContentProvider, 
										   ITreeContentProvider {

        public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		}
        
		public void dispose() {
		}
        
		public Object[] getElements(Object parent) {
			return getChildren(parent);
		}
        
		public Object getParent(Object child) {
			if (child instanceof TreeObject) {
				return ((TreeObject)child).getParent();
			}
			return null;
		}
        
		public Object[] getChildren(Object parent) {
			if (parent instanceof TreeParent) {
				return ((TreeParent)parent).getChildren();
			}
			return new Object[0];
		}

        public boolean hasChildren(Object parent) {
			if (parent instanceof TreeParent)
				return ((TreeParent)parent).hasChildren();
			return false;
		}
	}
	
	class ViewLabelProvider extends LabelProvider {

		public String getText(Object obj) {
			return obj.toString();
		}
		public Image getImage(Object obj) {
			String imageKey = ISharedImages.IMG_OBJ_ELEMENT;
			Image image = PlatformUI.getWorkbench().getSharedImages().getImage(imageKey);;
			if (obj instanceof TreeObject) {
				if (((TreeObject) obj).toString().equals("")) {
					return null;
				} else {
					image = ((TreeObject) obj).getImage();
				}
			};
			if (obj instanceof TreeParent) {
				//imageKey = ISharedImages.IMG_OBJ_FOLDER;
				//image = PlatformUI.getWorkbench().getSharedImages().getImage(imageKey);
				image = ((TreeParent) obj).getImage();
			};
			return image;
		}
	}
	
	private TreeObject crearMenu() {
		TreeParent p1 = new TreeParent("Consultas");
		p1.addChild(new TreeObject("Productos", ProductosView.ID, IImageKeys.CONFIG));
		p1.addChild(new TreeObject("Clientes", ClientesView.ID, IImageKeys.CONFIG));
		p1.addChild(new TreeObject("Compañías", ClientesComView.ID, IImageKeys.CONFIG));
		
		TreeParent p2 = new TreeParent("Cotizaciones");
		p2.addChild(new TreeObject("Nuevas", CotizacionesActivasView.ID, "icons/menuOperaciones.gif"));
		p2.addChild(new TreeObject("Procesadas", CotizacionesProcesadasView.ID, "icons/menuOperaciones.gif"));
		
		TreeParent p3 = new TreeParent("Operaciones");
		p3.addChild(new TreeObject("Templates", TemplatesView.ID, "icons/menuOperaciones.gif"));
		p3.addChild(new TreeObject("Hojas de servicio", HojasView.ID, "icons/menuOperaciones.gif"));
		p3.addChild(new TreeObject("Tours", ToursView.ID, "icons/menuOperaciones.gif"));
		
		TreeParent p4 = new TreeParent("Reservas");
		p4.addChild(new TreeObject("Guías", "", "icons/menuReservas.gif"));
		p4.addChild(new TreeObject("Transporte", "", "icons/menuReservas.gif"));
		p4.addChild(new TreeObject("Hospedaje", "", "icons/menuReservas.gif"));
		p4.addChild(new TreeObject("Alimentación", "", "icons/menuReservas.gif"));
		
		TreeParent root = new TreeParent("");
		root.addChild(p1);
		root.addChild(new TreeObject(""));
		root.addChild(p2);
		root.addChild(new TreeObject(""));
		root.addChild(p3);
		root.addChild(new TreeObject(""));
		root.addChild(p4);
		
		return root;
	}

	public void createPartControl(Composite parent) {
		viewer = new TreeViewer(parent, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		viewer.setContentProvider(new ViewContentProvider());
		viewer.setLabelProvider(new ViewLabelProvider());
		viewer.setInput(crearMenu());
		viewer.expandAll();
		
		final Tree tree = viewer.getTree();
		
		tree.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				//String seleccion = (TreeItem[]) tree.getSelection();
				//System.out.println("JA!!: " + seleccion);
				String seleccion = "";
				String vista = "";
				IStructuredSelection elemento = (IStructuredSelection) viewer.getSelection();
				if (elemento.getFirstElement() instanceof TreeParent) {
					// no hacemos nada, es un TreeParent
				} else {   // es un TreeObject
					seleccion = ((TreeObject) elemento.getFirstElement()).getName();
					vista = ((TreeObject) elemento.getFirstElement()).getViewId();
					System.out.println("Un TreeObject: " + seleccion);
				}
				
				IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
				
				String filtro = "";
				if (seleccion.equals("")) {      
					// es parent
				} else {
					if (vista.equals("")) {
						// si seleccion tiene valor, pero vista es "", => vista no habilitada
						MessageDialog.openInformation(window.getShell(), "Aviso", "La vista " + seleccion + " no está habilitada.");
					}
				}

				if (!vista.equals("")) {
					try {
						if (filtro.equals("")) {
							getSite().getPage().showView(vista);
						} else {
							IViewPart view = getSite().getPage().showView(vista,filtro,1);
							System.out.println("Agregando filtro x status: " + filtro);
							((SolicitudesView) view).agregarFiltroByStatus(filtro);
						}
					} catch(PartInitException ex) {
						MessageDialog.openError(window.getShell(), "Error", "Error abriendo vista: " + "");
					};					
				}
			}
		});
		
		// registramos el tableViewer como un selection provider
		getSite().setSelectionProvider(viewer);
	}

	public void setFocus() {
	}
}
