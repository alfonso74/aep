package rcp.manticora.editors;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;


import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.part.ViewPart;

import rcp.manticora.services.Calendario;
import rcp.manticora.views.IRefreshView;

public abstract class AbstractEditorHXXX extends EditorPart {
	//protected HibernateController controller;
	protected boolean isNewDoc = true;
	private boolean dirty = false;
	protected boolean filled = false;
	protected Text txtFocoInicial;

	public AbstractEditorHXXX() {
		super();
		//controller = HibernateController.getInstance();
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
	
	protected void setFocoInicial(Text txtFocoInicial) {
		this.txtFocoInicial = txtFocoInicial;
	}

	public void setFocus() {
		if (txtFocoInicial != null) {
			txtFocoInicial.setFocus();
		}
	}
	
	protected Integer txt2Integer(String valorCampo) {
		Integer valorInt = (valorCampo.equals("") ? null : Integer.valueOf(valorCampo));
		return valorInt;
	}
	
// usualmente utilizado cuando se va a guardar el registro en la DB
	protected Float txt2Float(String valorCampo) {
		NumberFormat nf = NumberFormat.getInstance();
		Float valorFloat = null;
// utilizamos NumberFormat para interpretar correctamente los separadores de miles (,)
// un simple Float.valueOf() falla al encontrar las comas separadoras de miles
		try {
			valorFloat = (valorCampo.equals("") ? null : nf.parse(valorCampo).floatValue());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		//Float valorFloat = (valorCampo.equals("") ? null : Float.valueOf(valorCampo));
		return valorFloat;
	}
	
// usado para presentar los valores en los formularios
	protected String valor2Txt(Object valorCampo) {
		String valorTxt = (valorCampo == null ? "" : valorCampo.toString());
		return valorTxt;
	}
	
// presentación de valores numéricos con formato en los formularios
	protected String valor2Txt(Object valorCampo, String formato) {
		if (valorCampo != null) {
			NumberFormat nf = NumberFormat.getInstance();
			//System.out.println("Número cargado: " + nf.format(valorCampo));
		}
		// formato usualmente se especifica para new DecimalFormat(".00")
		String valorTxt = (valorCampo == null ? "" : new DecimalFormat(formato).format(valorCampo));
		return valorTxt;
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
	
	protected SelectionAdapter crearCalendario(Text txtCampo) {
		return new Calendario(getSite().getShell(), txtCampo);
	}
	/*
	public SelectionAdapter crearCalendario(final Text txtCampo) {
		return new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				final SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
				final SWTCalendarDialog cal = new SWTCalendarDialog(getSite().getShell().getDisplay());
				cal.setLocation(300,300);
				cal.addDateChangedListener(new SWTCalendarListener() {
					public void dateChanged(SWTCalendarEvent calendarEvent) {
						txtCampo.setText(formatter.format(calendarEvent.getCalendar().getTime()));
					}
				});

                if (txtCampo.getText() != null && txtCampo.getText().length() > 0) {
                    try {
                        Date d = formatter.parse(txtCampo.getText());
                        cal.setDate(d);
                    } catch (ParseException pe) {

                    }
                }
                cal.open();
			}
		};
	}
	*/
}
