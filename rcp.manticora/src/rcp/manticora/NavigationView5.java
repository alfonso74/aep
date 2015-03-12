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
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import rcp.manticora.views.ClientesComView;
import rcp.manticora.views.ClientesView;
import rcp.manticora.views.CotizacionesParcialView;
import rcp.manticora.views.CotizacionesView;
import rcp.manticora.views.GuiasView;
import rcp.manticora.views.HabitacionesView;
import rcp.manticora.views.HojasView;
import rcp.manticora.views.KeywordsView;
import rcp.manticora.views.PaisesView;
import rcp.manticora.views.ProductosView;
import rcp.manticora.views.RedesView;
import rcp.manticora.views.ReservaToursView;
import rcp.manticora.views.RolesView;
import rcp.manticora.views.SolicitudesAsignadasView;
import rcp.manticora.views.SolicitudesView;
import rcp.manticora.views.TemplatesView;
import rcp.manticora.views.TipoClientesView;
import rcp.manticora.views.TipoHabitacionesView;
import rcp.manticora.views.TipoProductosView;
import rcp.manticora.views.ToursView;
import rcp.manticora.views.TransportesView;
import rcp.manticora.views.UsuariosView;
import rcp.manticora.views.VendedoresView;

public class NavigationView5 extends ViewPart {
	public static final String ID = "manticora.navigationView";
	private TreeViewer viewer;
	private TreeParent p1;

	public NavigationView5() {
		super();
	}
	
	class TreeObject {
		private String name;
		private String viewId;
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
		public TreeObject(String name, String viewId, String imageKey) {
			this.name = name;
			this.viewId = viewId;
			this.imageKey = imageKey;
			ImageDescriptor imageDescriptor = AbstractUIPlugin.imageDescriptorFromPlugin(Application.PLUGIN_ID, imageKey);
			image = imageDescriptor.createImage();
		}
		
		public String getName() {
			return name;
		}
		public String getViewId() {
			return viewId;
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
		TreeObject to101 = new TreeObject("Keywords", KeywordsView.ID, "icons/mantenimiento.gif");
		TreeObject to102 = new TreeObject("Países", PaisesView.ID, IImageKeys.CONFIG);
		TreeObject to103 = new TreeObject("Tipo clientes", TipoClientesView.ID, IImageKeys.CONFIG);
		TreeObject to104 = new TreeObject("Clientes", ClientesView.ID, IImageKeys.CONFIG);
		TreeObject to105 = new TreeObject("Compañías", ClientesComView.ID, IImageKeys.CONFIG);
		TreeObject to106 = new TreeObject("Redes de viajes", RedesView.ID, IImageKeys.CONFIG);
		TreeObject to107 = new TreeObject("Tipo productos", TipoProductosView.ID, IImageKeys.CONFIG);
		TreeObject to108 = new TreeObject("Productos", ProductosView.ID, IImageKeys.CONFIG);
		TreeObject to109 = new TreeObject("Vendedores", VendedoresView.ID, IImageKeys.CONFIG);
		TreeObject to110 = new TreeObject("Guías", GuiasView.ID, IImageKeys.CONFIG);
		TreeObject to111 = new TreeObject("Transportistas", TransportesView.ID, IImageKeys.CONFIG);
		TreeObject to112 = new TreeObject("Roles", RolesView.ID, IImageKeys.CONFIG);
		TreeObject to113 = new TreeObject("Usuarios", UsuariosView.ID, IImageKeys.CONFIG);
		
		TreeParent p2 = new TreeParent("Ventas");
		TreeObject to201 = new TreeObject("Solicitudes", SolicitudesView.ID, "icons/menuVentas.gif");
		TreeObject to202 = new TreeObject("Sol. asignadas", SolicitudesAsignadasView.ID, "icons/menuVentas.gif");
		TreeObject to203 = new TreeObject("Cotizaciones", CotizacionesView.ID, "icons/menuVentas.gif");
		TreeObject to204 = new TreeObject("Pago parcial", CotizacionesParcialView.ID, "icons/menuVentas.gif");
		TreeObject to205 = new TreeObject("Hojas de servicio", HojasView.ID, "icons/menuVentas.gif");
		
		TreeParent p3 = new TreeParent("Operaciones");
		TreeObject to301 = new TreeObject("Templates", TemplatesView.ID, "icons/menuOperaciones.gif");
		TreeObject to302 = new TreeObject("Hojas de servicio", HojasView.ID, "icons/menuOperaciones.gif");
		TreeObject to303 = new TreeObject("Tours", ToursView.ID, "icons/menuOperaciones.gif");
		
		TreeParent p4 = new TreeParent("Reservas");
		TreeObject to401 = new TreeObject("Tours", ReservaToursView.ID, "icons/menuReservas.gif");
		TreeObject to402 = new TreeObject("Transporte", "", "icons/menuReservas.gif");
		TreeObject to403 = new TreeObject("Hospedaje", "", "icons/menuReservas.gif");
		TreeObject to404 = new TreeObject("Alimentación", "", "icons/menuReservas.gif");
		TreeObject to405 = new TreeObject("Guías", "", "icons/menuReservas.gif");
		
		p1.addChild(to101);
		p1.addChild(to102);
		p1.addChild(to103);
		p1.addChild(to104);
		p1.addChild(to105);
		p1.addChild(to106);
		p1.addChild(to107);
		p1.addChild(to108);
		p1.addChild(to109);
		p1.addChild(to110);
		p1.addChild(to111);
		p1.addChild(to112);
		p1.addChild(to113);
		p1.addChild(new TreeObject("Tipos de habit.", TipoHabitacionesView.ID, IImageKeys.CONFIG));
		p1.addChild(new TreeObject("Habitaciones", HabitacionesView.ID, IImageKeys.CONFIG));
		
		p2.addChild(to201);
		p2.addChild(to202);
		p2.addChild(to203);
		p2.addChild(to204);
		p2.addChild(to205);
		
		p3.addChild(to301);
		p3.addChild(to302);
		p3.addChild(to303);
		
		p4.addChild(to401);
		p4.addChild(to402);
		p4.addChild(to403);
		p4.addChild(to404);
		p4.addChild(to405);
		
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
				String viewId = "";
				String viewName = "";
				Object seleccion = getElementoSeleccionado(viewer);
				if (seleccion instanceof TreeParent) {
					// no hacemos nada, es un TreeParent
				} else {   // es un TreeObject
					//IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
					IWorkbenchWindow window = getSite().getWorkbenchWindow();
					viewId = ((TreeObject) seleccion).getViewId();
					viewName = ((TreeObject) seleccion).getName();
					if (viewId.equals("")) {
						MessageDialog.openInformation(window.getShell(), "Aviso", "La vista '" + viewName + "' no está habilitada.");
					} else {
						try {
							getSite().getPage().showView(viewId);
							//TODO: eliminar esta prueba de perspectivas cuando finalizemos el layout del cliente
							if (viewName.equals("Guías")) {
								//IPerspectiveDescriptor perspectiva = PlatformUI.getWorkbench().getPerspectiveRegistry().findPerspectiveWithId(VentasPerspective.ID);
								//window.getActivePage().setPerspective(perspectiva);
							}
						} catch(PartInitException ex) {
							//MessageDialog.openError(window.getShell(), "Error", "Error abriendo vista: " + viewId);
							mensajeError(window.getShell(), ex);
							ex.printStackTrace();
						};	
					}
				}
				
				

			}
		});
		
		// registramos el tableViewer como un selection provider
		getSite().setSelectionProvider(viewer);
	}
	
	private Object getElementoSeleccionado(TreeViewer viewer) {
		Object seleccion = ((IStructuredSelection) viewer.getSelection()).getFirstElement();
		return seleccion;
	}

	public void setFocus() {
		viewer.getControl().setFocus();
	}
	
	private void mensajeError(Shell shell, Exception e) {
		String nombre = this.toString();
		int puntoFinal = nombre.indexOf(".") + 1;
		int arroba = nombre.indexOf("@");
		MessageDialog.openError(shell, "Error en " + nombre.substring(puntoFinal, arroba), "Error: " + e.toString() + "\n\nStack trace: " + e.getStackTrace()[0] + "\n" + e.getStackTrace()[1]);
	}

}
