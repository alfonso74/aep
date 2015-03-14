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


/**
 * Clase abstracta para crear un menú en el sistema.  Requiere que
 * se implemente el método crearMenu() y que se defina la variable que
 * identifica a la vista en eclipse:
 *     public static final String ID = "identificador"
 * El menú resultante se puede expandir con el hook expandirMenu() o 
 * manipular con getTreeViewer().
 * @author Carlos Alfonso
 *
 */
public abstract class AbstractMenuView extends ViewPart {
	//public static final String ID = "";
	private TreeViewer viewer;

	
	public AbstractMenuView() {
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
     * Método invocado por la clase que implementa, para crear el menú
     */
	protected abstract TreeObject crearMenu();

	/**
	 * Método hook para expandir el menú
	 */
	protected void expandirMenu() {
		viewer.expandAll();
	}
	
	public void createPartControl(Composite parent) {
		viewer = new TreeViewer(parent, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		viewer.setContentProvider(new ViewContentProvider());
		viewer.setLabelProvider(new ViewLabelProvider());
		viewer.setInput(crearMenu());
		expandirMenu();
		
		final Tree tree = viewer.getTree();
		
		tree.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				String viewId = "";
				String viewName = "";
				Object seleccion = getElementoSeleccionado(viewer);
				if (seleccion instanceof TreeParent) {
					// no hacemos nada, es un TreeParent
				} else {   // es un TreeObject
					IWorkbenchWindow window = getSite().getWorkbenchWindow();
					viewId = ((TreeObject) seleccion).getViewId();
					viewName = ((TreeObject) seleccion).getName();
					if (viewId.equals("")) {
						MessageDialog.openInformation(window.getShell(), "Aviso", "La vista '" + viewName + "' no está habilitada.");
					} else {
						try {
							getSite().getPage().showView(viewId);
						} catch(PartInitException ex) {
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
	
	protected TreeViewer getTreeViewer() {
		return viewer;
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
