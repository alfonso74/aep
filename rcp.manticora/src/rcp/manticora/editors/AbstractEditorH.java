package rcp.manticora.editors;


import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.part.ViewPart;

import rcp.manticora.services.FormUtils;
import rcp.manticora.views.IRefreshView;

public abstract class AbstractEditorH extends EditorPart {
	//protected HibernateController controller;
	private FormUtils formUtils;
	protected boolean isNewDoc = true;
	private boolean dirty = false;
	protected boolean filled = false;
	protected Control campoFocoInicial;

	public AbstractEditorH() {
		super();
		//controller = HibernateController.getInstance();
		formUtils = new FormUtils();
	}
	
	public abstract void doSave(IProgressMonitor monitor);
	
	public void doSaveAs() {
	}

	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		setSite(site);
		setInput(input);
		setPartName(input.getName());
	}

	public boolean isDirty() {
		return dirty;
	}
	
	public void removeDirtyFlag() {
		dirty = false;
		firePropertyChange(PROP_DIRTY);
	}
	
	public void addDirtyFlag() {
		dirty = true;
		firePropertyChange(PROP_DIRTY);
	}
	
	public void removeFilledFlag() {
		filled = false;
	}
	
	public void addFilledFlag() {
		filled = true;
	}

	public boolean isSaveAsAllowed() {
		return false;
	}
	
	public void createPartControl(Composite parent) {
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		gridLayout.marginTop = 10;
		gridLayout.marginLeft = 5;
		parent.setLayout(gridLayout);
		agregarControles(parent);
		llenarControles();
	}

	protected abstract void agregarControles(Composite parent);
	
	protected abstract void llenarControles();

	protected void actualizarVistas() {
		IViewReference[] viewRef = getSite().getPage().getViewReferences();
		for (int n = 0; n < viewRef.length; n++) {
			ViewPart vista = (ViewPart) viewRef[n].getView(true);
			if (vista instanceof IRefreshView) {
				((IRefreshView) vista).refrescar();
			}
		}
	}
	
	protected void actualizarVista(String viewID) {
		IViewReference[] viewRef = getSite().getPage().getViewReferences();
		// ejecutamos el ciclo en busca de una vista con el viewID indicado
		for (int n = 0; n < viewRef.length; n++) {
			if (viewRef[n].getId().equals(viewID)) {
				// si encontramos el viewID, actualizamos el contenido
				ViewPart vista = (ViewPart) viewRef[n].getView(true);
				if (vista instanceof IRefreshView) {
					((IRefreshView) vista).refrescar();
				}
			}
		}
	}
	
	protected void setFocoInicial(Control campoFocoInicial) {
		this.campoFocoInicial = campoFocoInicial;
	}

	public void setFocus() {
		if (campoFocoInicial != null) {
			campoFocoInicial.setFocus();
		}
	}
	
	public CommonEditorInput getEditorInput() {
		return (CommonEditorInput) super.getEditorInput();
	}
	
	protected void mensajeError(Shell shell, Exception e) {
		String nombre = this.toString();
		int puntoFinal = nombre.indexOf(".") + 1;
		int arroba = nombre.indexOf("@");
		MessageDialog.openError(shell, "Error en " + nombre.substring(puntoFinal, arroba), "Error: " + e.toString() + "\n\nStack trace: " + e.getStackTrace()[0] + "\n" + e.getStackTrace()[1]);
		//MessageDialog.openError(shell, "Error en la aplicación", "Error: " + e.toString() + "\n\nStack trace: " + e.getStackTrace()[0]);
	}

	protected ModifyListener createModifyListener() {
		return new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				if (filled) {
					addDirtyFlag();
				}
			}
		};
	}
	
	protected Long txt2Long(String valorCampo) {
		return formUtils.txt2Long(valorCampo);
	}
	
	protected Integer txt2Integer(String valorCampo) {
		return formUtils.txt2Integer(valorCampo);
	}
	
	protected Float txt2Float(String valorCampo) {
		return formUtils.txt2Float(valorCampo);
	}
	
	protected String valor2Txt(Object valorCampo) {
		return formUtils.valor2Txt(valorCampo);
	}
	
	protected String valor2Txt(Object valorCampo, String formato) {
		return formUtils.valor2Txt(valorCampo, formato);
	}
	
	protected SelectionAdapter crearCalendario(Text txtCampo) {
		return formUtils.crearCalendario(getSite().getShell(), txtCampo);
	}

	protected SelectionAdapter crearCalendario(Text txtCampo, Text txtFechaDefault) {
		return formUtils.crearCalendario(getSite().getShell(), txtCampo, txtFechaDefault);
	}

	protected KeyAdapter crearKeyAdapter(Text txtCampo) {
		return formUtils.crearKeyAdapter(txtCampo);
	}
	
	protected Integer checkNull(Integer valorCampo) {
		return formUtils.checkNull(valorCampo);
	}
	
	protected Float checkNull(Float valorCampo) {
		return formUtils.checkNull(valorCampo);
	}
	
	protected String checkNull(String valorCampo) {
		return formUtils.checkNull(valorCampo);
	}
	
	protected boolean checkNull(Boolean valorCampo) {
		return formUtils.checkNull(valorCampo);
	}
}
