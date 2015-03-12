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
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import rcp.manticora.views.ClientesComView;
import rcp.manticora.views.ClientesView;
import rcp.manticora.views.CotizacionesView;
import rcp.manticora.views.GuiasView;
import rcp.manticora.views.HabitacionesView;
import rcp.manticora.views.HojasView;
import rcp.manticora.views.KeywordsView;
import rcp.manticora.views.PaisesView;
import rcp.manticora.views.ProductosView;
import rcp.manticora.views.SolicitudesAsignadasView;
import rcp.manticora.views.SolicitudesView;
import rcp.manticora.views.TemplatesView;
import rcp.manticora.views.TipoClientesView;
import rcp.manticora.views.TipoHabitacionesView;
import rcp.manticora.views.TipoProductosView;
import rcp.manticora.views.ToursView;
import rcp.manticora.views.TransportesView;
import rcp.manticora.views.VendedoresView;

public class NavigationView4 extends ViewPart {
	public static final String ID = "manticora.navigationView";
	private TreeViewer viewer;
	private TreeParent p1;

	public NavigationView4() {
		super();
	}
	
	class TreeObject {
		private String name;
		private TreeParent parent;
		private String imageKey = ISharedImages.IMG_OBJ_ELEMENT;
		private Image image;
		
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

    /**
     * We will set up a dummy model to initialize tree hierarchy. In real
     * code, you will connect to a real model and expose its hierarchy.
     */
	private TreeObject crearMenu() {
		p1 = new TreeParent("Mantenimientos");
		TreeObject to11 = new TreeObject("Keywords", "icons/mantenimiento.gif");
		TreeObject to12 = new TreeObject("Países", IImageKeys.CONFIG);
		TreeObject to13 = new TreeObject("Tipo clientes", IImageKeys.CONFIG);
		TreeObject to14 = new TreeObject("Clientes", IImageKeys.CONFIG);
		TreeObject to15 = new TreeObject("Compañías", IImageKeys.CONFIG);
		TreeObject to16 = new TreeObject("Tipo productos", IImageKeys.CONFIG);
		TreeObject to17 = new TreeObject("Productos", IImageKeys.CONFIG);
		TreeObject to18 = new TreeObject("Vendedores", IImageKeys.CONFIG);
		TreeObject to19 = new TreeObject("Guías", IImageKeys.CONFIG);
		TreeObject to20 = new TreeObject("Transportistas", IImageKeys.CONFIG);
		
		TreeParent p2 = new TreeParent("Ventas");
		TreeObject to21 = new TreeObject("Solicitudes", "icons/menuVentas.gif");
		TreeObject to22 = new TreeObject("Sol. asignadas", "icons/menuVentas.gif");
		TreeObject to23 = new TreeObject("Cotizaciones", "icons/menuVentas.gif");
		TreeObject to24 = new TreeObject("Hojas de servicio", "icons/menuVentas.gif");
		
		TreeParent p3 = new TreeParent("Operaciones");
		TreeObject to31 = new TreeObject("Templates", "icons/menuOperaciones.gif");
		TreeObject to32 = new TreeObject("Hojas de servicio", "icons/menuOperaciones.gif");
		TreeObject to33 = new TreeObject("Tours", "icons/menuOperaciones.gif");
		
		TreeParent p4 = new TreeParent("Reservas");
		TreeObject to41 = new TreeObject("Tours", "icons/menuReservas.gif");
		TreeObject to42 = new TreeObject("Transporte", "icons/menuReservas.gif");
		TreeObject to43 = new TreeObject("Hospedaje", "icons/menuReservas.gif");
		TreeObject to44 = new TreeObject("Alimentación", "icons/menuReservas.gif");
		TreeObject to45 = new TreeObject("Guías", "icons/menuReservas.gif");
		
		p1.addChild(to11);
		p1.addChild(to12);
		p1.addChild(to13);
		p1.addChild(to14);
		p1.addChild(to15);
		p1.addChild(to16);
		p1.addChild(to17);
		p1.addChild(to18);
		p1.addChild(to19);
		p1.addChild(to20);
		p1.addChild(new TreeObject("Tipos de habit.", IImageKeys.CONFIG));
		p1.addChild(new TreeObject("Habitaciones", IImageKeys.CONFIG));
		
		p2.addChild(to21);
		p2.addChild(to22);
		p2.addChild(to23);
		p2.addChild(to24);
		
		p3.addChild(to31);
		p3.addChild(to32);
		p3.addChild(to33);
		
		p4.addChild(to41);
		p4.addChild(to42);
		p4.addChild(to43);
		p4.addChild(to44);
		p4.addChild(to45);
		
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
		//viewer.expandAll();
		viewer.expandToLevel(p1, 1);
		
		final Tree tree = viewer.getTree();
		
		tree.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				//String seleccion = (TreeItem[]) tree.getSelection();
				//System.out.println("JA!!: " + seleccion);
				String seleccion = "";
				IStructuredSelection elemento = (IStructuredSelection) viewer.getSelection();
				if (elemento.getFirstElement() instanceof TreeParent) {
					// no hacemos nada, es un TreeParent
				} else {   // es un TreeObject
					seleccion = ((TreeObject) elemento.getFirstElement()).getName();
					System.out.println("Un TreeObject: " + seleccion);
				}
				
				IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
				String vista = "";
				if (seleccion.equals("Tipo productos")) {
					vista = TipoProductosView.ID;
				} else if (seleccion.equals("Productos")) {
					vista = ProductosView.ID;
				} else if (seleccion.equals("Keywords")) {
					vista = KeywordsView.ID;
				} else if (seleccion.equals("Categorías")) {
					vista = TipoProductosView.ID;
				} else if (seleccion.equals("Países")) {
					vista = PaisesView.ID;
				} else if (seleccion.equals("Vendedores")) {
					vista = VendedoresView.ID;
				} else if (seleccion.equals("Guías")) {
					vista = GuiasView.ID;
				} else if (seleccion.equals("Transportistas")) {
					vista = TransportesView.ID;
				} else if (seleccion.equals("Tipos de habit.")) {
					vista = TipoHabitacionesView.ID;	
				} else if (seleccion.equals("Habitaciones")) {
					vista = HabitacionesView.ID;	
				} else if (seleccion.equals("Tipo clientes")) {
					vista = TipoClientesView.ID;
				} else if (seleccion.equals("Clientes")) {
					vista = ClientesView.ID;
				} else if (seleccion.equals("Compañías")) {
					vista = ClientesComView.ID;
				} else if (seleccion.equals("Solicitudes")) {
					vista = SolicitudesView.ID;
				} else if (seleccion.equals("Sol. asignadas")) {
					vista = SolicitudesAsignadasView.ID;
				} else if (seleccion.equals("Templates")) {
					vista = TemplatesView.ID;
				} else if (seleccion.equals("Cotizaciones")) {
					vista = CotizacionesView.ID;
				} else if (seleccion.equals("Hojas de servicio")) {
					vista = HojasView.ID;
				} else if (seleccion.equals("Tours")) {
					vista = ToursView.ID;
				} else if (seleccion.equals("")) {
					vista = "";
				} else {
					MessageDialog.openInformation(window.getShell(), "Aviso", "La vista " + seleccion + " no está habilitada.");
				};

				if (!vista.equals("")) {
					try {
						getSite().getPage().showView(vista);
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
		viewer.getControl().setFocus();
	}

}
