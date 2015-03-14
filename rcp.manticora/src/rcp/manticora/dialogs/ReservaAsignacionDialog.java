package rcp.manticora.dialogs;

import java.util.Set;


import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import rcp.manticora.model.IReserva;
import rcp.manticora.model.ReservaHospedaje;
import rcp.manticora.model.ReservaTour;
import rcp.manticora.model.ReservaTransporte;
import rcp.manticora.services.FechaUtil;

/**
 * <p>Dialogo para asignar líneas de actividad a una reserva existente.</p>
 * 
 * @param parentShell shell para dibujar el diálogo
 * @param linea de actividad sobre la que vamos a trabajar
 *
 */

public class ReservaAsignacionDialog extends AbstractAEPTitleAreaDialog 
	implements IReservaDialog {
	
	private Set<IReserva> listaReservas;
	private IReserva reserva;
	
	private TableViewer viewer;
	
	private Shell shell;
	
	/**
	 * Constructor
	 * @param parentShell shell para dibujar el diálogo
	 * @param linea actividad sobre la que vamos a trabajar
	 */
	public ReservaAsignacionDialog(Shell parentShell, Set<IReserva> listaReservas) {
		super(parentShell);
		this.shell = parentShell;
		this.listaReservas = listaReservas;
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite composite = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout(1, false);
		layout.marginWidth = 15;
		layout.marginHeight = 10;
		layout.verticalSpacing = 8;
		composite.setLayout(layout);
		
		viewer = new TableViewer(crearTablaReservas(composite));
		viewer.setContentProvider(new ViewContentProviderRes());
		viewer.setLabelProvider(new ViewLabelProviderRes());
		
		llenarCampos();
		
		return composite;
	}
	
	
	private Table crearTablaReservas(Composite parent) {
		int style = SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.HIDE_SELECTION;
		Table tabla = new Table(parent, style);
		tabla.setLinesVisible(true);
		tabla.setHeaderVisible(true);
		GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		tabla.setLayoutData(gridData);
		
		TableColumn column = new TableColumn(tabla, SWT.LEFT, 0);
		column.setText("Clase");
		column.setWidth(130);
		
		column = new TableColumn(tabla, SWT.LEFT, 1);
		column.setText("Fecha");
		column.setWidth(150);
		
		column = new TableColumn(tabla, SWT.LEFT, 2);
		column.setText("No.");
		column.setWidth(40);
		
		column = new TableColumn(tabla, SWT.LEFT, 3);
		column.setText("Comentarios");
		column.setWidth(150);
		
		return tabla;
	}

	
	@Override
	protected void llenarCampos() {
		setTitle("Asignación de Reserva Existente");
		setMessage("Por favor, seleccione la reserva a asignar.", IMessageProvider.INFORMATION);
		
		viewer.setInput(listaReservas);
		viewer.refresh();
	}
	
	
	@Override
	public boolean close() {
		if (getReturnCode() == IDialogConstants.OK_ID) {
			try {
				if (validarCampos()) {
					reserva = getReservaSeleccionada();
				} else {
					return false;
				}
			} catch (Exception e) {
				mensajeError(shell, e);
				return false;
			}
			
		}
		return super.close();
	}
	
	
	private boolean validarCampos() {
		if (getReservaSeleccionada() == null) {
			MessageDialog.openError(shell, "Selección de reserva",
				"No se ha seleccionado ninguna reserva.");
			return false;
		}
		return true;
	}
	
	
	/**
	 * Retorna la reserva seleccionada por el usuario final.
	 * @return IReserva seleccionada
	 */
	public IReserva getReserva() {
		return reserva;
	}
	
	
	class ViewContentProviderRes implements IStructuredContentProvider {

        public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		}
        
		public void dispose() {
		}
        
		public Object[] getElements(Object inputElement) {
			Object[] resultados = ((Set) inputElement).toArray();
			return resultados;
		}
	}
	
	
	class ViewLabelProviderRes extends LabelProvider implements ITableLabelProvider {

		public String getColumnText(Object obj, int index) {
			String resultado = "";
			IReserva reserva = (IReserva) obj;
			switch (index) {
			case 0:
				resultado = reserva.getTipoReserva();
				if (reserva instanceof ReservaHospedaje) {
					if (((ReservaHospedaje) reserva).getProducto().isHotelAEP()) {
						resultado += " (AEP)";
					} else {
						resultado += " (Externo)";
					}
				}
				break;
			case 1:
				resultado = getFechaReservaTxt(reserva);
				break;
			case 2:
				resultado = reserva.getIdReserva().toString();
				break;
			case 3:
				resultado = reserva.getComentario();
				break;
			}
			return resultado;
			
		}
		public Image getColumnImage(Object obj, int index) {
			return null;		
		}
	}
	
	
	public Object getElementoSeleccionado(TableViewer viewer) {
		Object seleccion = ((IStructuredSelection) viewer.getSelection()).getFirstElement();
		return seleccion;
	}
	
	/**
	 * Retorna la reserva que ha sido seleccionada. Si no hay selección
	 * retorna null.
	 * @return IReserva que ha sido seleccionada por el usuario.
	 */
	public IReserva getReservaSeleccionada() {
		Object seleccion = getElementoSeleccionado(viewer);
		IReserva reserva = (IReserva) seleccion;
		System.out.println("Reserva: " + reserva);
		return reserva;
	}
	
	/**
	 * Retorna la fecha para una reserva en particular.  Cada reserva tiene diferentes
	 * formas de establecer las fechas, y esta función arma el formato adecuado.
	 * @return Fecha de la reserva (tipo String)
	 */
	private String getFechaReservaTxt(IReserva reserva) {
		String fechaTxt = "";
		if (reserva instanceof ReservaTour) {
			fechaTxt = FechaUtil.toString(((ReservaTour) reserva).getFecha());
		} else if (reserva instanceof ReservaHospedaje) {
			String fecha1 = FechaUtil.toString(((ReservaHospedaje) reserva).getFechaDesde());
			String fecha2 = FechaUtil.toString(((ReservaHospedaje) reserva).getFechaHasta());
			fechaTxt = fecha1 + " a " + fecha2; 
		} else if (reserva instanceof ReservaTransporte) {
			fechaTxt = FechaUtil.toString(((ReservaTransporte) reserva).getFechaOrigen());
		} else {
			fechaTxt = "No disponible";
		}
		return fechaTxt;
	}

}
