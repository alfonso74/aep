package rcp.manticora.dialogs;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import rcp.manticora.IImageKeys;
import rcp.manticora.controllers.HojaServicioController;
import rcp.manticora.model.DisponibilidadTour;
import rcp.manticora.model.LineaActividad;
import rcp.manticora.model.ReservaTour;
import rcp.manticora.services.ComboData;
import rcp.manticora.services.FechaUtil;

public class ReservaTourDialog extends AbstractAEPTitleAreaDialog implements
	IReservaDialog {
	
	//private HibernateController controller;
	private HojaServicioController editorController;
	private Text txtFecha;
	private Button bFecha;
	private Text txtProducto;
	private Combo comboToursDisponibles;
	private Text txtCapacidad;
	private Text txtPaxs;
	private Text txtDisponibilidad;
	private Text txtReferencia;
	private Text txtComentario;
	
	private ImageDescriptor image;
	
	private boolean isNuevaReserva;
	
	private LineaActividad linea;
	private ReservaTour reserva;
	private Shell shell;
	private ComboData cdToursDisponibles;
	private List<DisponibilidadTour> listaDisponibilidad;
	private DisponibilidadTour dispTour;
	
	/**
	 * Constructor utilizado para crear/editar reservas a partir de una línea
	 * de actividades (usualmente en un tab de actividades).
	 * @param parentShell shell
	 * @param linea linea de actividades que seleccionó el usuario
	 * @param reserva reserva a ser editada o null en caso de no existir
	 */
	public ReservaTourDialog(Shell parentShell, HojaServicioController editorController,
			LineaActividad linea, ReservaTour reserva) {
		super(parentShell);
		this.shell = parentShell;
		this.linea = linea;
		if (reserva == null) {
			isNuevaReserva = true;
			this.reserva = new ReservaTour();
		} else {
			this.reserva = reserva;
		}
		this.editorController = editorController;
		cdToursDisponibles = inicializarComboDisponibilidad();
	}

	
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite composite = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout(6, false);
		layout.marginWidth = 15;
		layout.marginHeight = 10;
		layout.verticalSpacing = 8;
		composite.setLayout(layout);
		
		Label l;
		GridData gridData;
		
		/*
		l = new Label(composite, SWT.NONE);
		l.setText("Fecha:");
		
		Composite compFecha = new Composite(composite, SWT.NONE);
		layout = new GridLayout(2, false);
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		compFecha.setLayout(layout);
		gridData = new GridData();
		gridData.horizontalSpan = 5;
		compFecha.setLayoutData(gridData);
		
		txtFecha = new Text(compFecha, SWT.BORDER);
		gridData = new GridData(60,15);
		txtFecha.setLayoutData(gridData);
		
		bFecha = new Button(compFecha, SWT.NONE);
		gridData = new GridData(16,16);
		bFecha.setLayoutData(gridData);
		image = AbstractUIPlugin.imageDescriptorFromPlugin(pluginId, IImageKeys.CALENDARIO);
		bFecha.setImage(image.createImage());
		bFecha.addSelectionListener(this.crearCalendario(shell, txtFecha));
		
		l = new Label(composite, SWT.NONE);
		l.setText("Producto:");
		txtProducto = new Text(composite, SWT.BORDER);
		gridData = new GridData(120,15);
		gridData.horizontalSpan = 5;
		txtProducto.setLayoutData(gridData);
		*/
		
		l = new Label(composite, SWT.NONE);
		l.setText("Tour:");
		comboToursDisponibles = new Combo(composite, SWT.READ_ONLY);
		comboToursDisponibles.setItems(cdToursDisponibles.getTexto());
		gridData = new GridData(150, 15);
		gridData.horizontalSpan = 3;
		comboToursDisponibles.setLayoutData(gridData);
		comboToursDisponibles.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				System.out.println("Actualizando campos de disponibilidad");
				Long idDisp = cdToursDisponibles.getKeyAsLongByIndex((comboToursDisponibles.getSelectionIndex()));
				actualizarCamposDisp(idDisp);
			}
		});
		
		l = new Label(composite, SWT.NONE);
		l.setText("Capacidad:");
		txtCapacidad = new Text(composite, SWT.BORDER);
		gridData = new GridData(25, 15);
		txtCapacidad.setLayoutData(gridData);
		txtCapacidad.setEnabled(false);
		
		l = new Label(composite, SWT.NONE);
		l.setText("Reservar:");
		txtPaxs = new Text(composite, SWT.BORDER);
		gridData = new GridData(25,15);
		txtPaxs.setLayoutData(gridData);
		
		l = new Label(composite, SWT.NONE);
		l.setText("Disponibilidad:");
		txtDisponibilidad = new Text(composite, SWT.BORDER);
		gridData = new GridData(25, 15);
		txtDisponibilidad.setLayoutData(gridData);
		txtDisponibilidad.setEnabled(false);
		
		l = new Label(composite, SWT.NONE);
		l.setText("Referencia:");
		txtReferencia = new Text(composite, SWT.BORDER);
		gridData = new GridData(70, 15);
		txtReferencia.setLayoutData(gridData);
		txtReferencia.setEnabled(false);
		
		l = new Label(composite, SWT.NONE);
		l.setText("Comentarios:");
		gridData = new GridData();
		gridData.verticalIndent = 5;
		gridData.verticalAlignment = SWT.TOP;
		l.setLayoutData(gridData);
		txtComentario = new Text(composite, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.WRAP);
		gridData = new GridData(SWT.FILL, SWT.CENTER, false, false);
		gridData.horizontalSpan = 4;
		gridData.heightHint = 40;
		txtComentario.setLayoutData(gridData);
		
		Button b = new Button(composite, SWT.PUSH);
		b.setText("X");
		b.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				System.out.println("Botón X");
				tmpReservarTour();
			}
		});
		
		llenarCampos();
		
		return composite;
	}

	
	@Override
	protected void llenarCampos() {
		setTitle("Reserva de tour (" + linea.getDspProducto() + ")");		
		if (linea != null) {
			setMessage("Por favor, introduzca los detalles de la reserva para el " + FechaUtil.toString(linea.getFecha()) + ".", IMessageProvider.INFORMATION);
		} else {
			setMessage("Por favor, introduzca los detalles de la reserva", IMessageProvider.INFORMATION);
		}
		if (isNuevaReserva) {
			System.out.println("Nueva reserva...");
			reserva.setHoja(linea.getHoja());
			reserva.setEstado("A");
			// si el primer item tiene valor -1, indica que no se ha definido disponibilidad, y
			// seleccionamos el elemento para que el usuario vea inmediatamente esta condición
			if (cdToursDisponibles.getKeyAsLongByIndex(0) == -1) {
				comboToursDisponibles.select(0);
			}
		} else {
			System.out.println("Cargando información de campos...");
			/*
			Session session = HibernateUtil.getSessionFactory().openSession();
			session.refresh(reserva);
			*/
			//Session session = HibernateUtil.currentSession();
			//session.setFlushMode(FlushMode.NEVER);
			//session.refresh(reserva);
			//session.lock(reserva, LockMode.NONE);
			//editorController.getSession().refresh(reserva);
			dispTour = reserva.getDisponibilidad();
			System.out.println("ID de disp del tour: "+ dispTour.getIdTour());
			Long ocupacion = editorController.getOcupacionTour(dispTour);
			Integer capacidad = dispTour.getCapacidad();
			System.out.println("Reservados: " + capacidad);
			comboToursDisponibles.setText(dispTour.getTour().getNombre());
			txtCapacidad.setText(valor2Txt(capacidad));
			txtPaxs.setText(valor2Txt(reserva.getCantidad()));
			txtDisponibilidad.setText(valor2Txt(capacidad.intValue() - ocupacion.intValue()));
			txtReferencia.setText(valor2Txt(dispTour.getNumero()));
			txtComentario.setText(reserva.getComentario());
			/*
			session.flush();
			HibernateUtil.closeSession();
			*/
			//session.close();
		}
	}
	
	
	private boolean validarSave() {
		if (comboToursDisponibles.getSelectionIndex() == -1) {
			MessageDialog.openInformation(shell, "Validación de campos",
				"Debe seleccionar el tour a ser reservado.");
			return false;
		}
		if (cdToursDisponibles.getKeyAsLongByIndex(0) == -1L) {
			MessageDialog.openInformation(shell, "Validación de campos",
				"El tour no está habilitado para la fecha indicada.");
			return false;
		}
		if (txtPaxs.getText() == "") {
			MessageDialog.openInformation(shell, "Validación de campos",
				"Debe indicar la cantidad de espacios a reservar.");
			return false;
		}
		return true;
	}
	
	
	public boolean close() {
		if (getReturnCode() == IDialogConstants.OK_ID) {
			try {
				if (validarSave()) {
					guardarReserva();
					return super.close();
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
	
	
	
	private void guardarReserva() {
		int pCantidad = txt2Integer(txtPaxs.getText());
		String pComentario = txtComentario.getText();
		DisponibilidadTour pDisponibilidad = dispTour;

		reserva.setDisponibilidadX(pDisponibilidad);
		reserva.setIdCapitan(0);
		reserva.setCantidad(pCantidad);
		reserva.setComentario(pComentario);
	}
	
	
	
	// Usado por el botón X
	private void tmpReservarTour() {
		List<DisponibilidadTour> dispTours = editorController.findDisponibilidadTourByFecha(linea.getIdProducto(), linea.getFecha());
		Iterator<DisponibilidadTour> it = dispTours.iterator();
		System.out.println("Cantidad de tours: " + dispTours.size());
		while (it.hasNext()) {
			Long n = editorController.getOcupacionTour((DisponibilidadTour) it.next());
			System.out.println("Ocupación: " + n);
		}
	}
	
	
	/**
	 * Inicializa el listado de tours que están disponibles para el producto 
	 * y fecha de la actividad, y que aparecerán en un listado para ser
	 * seleccionados por el usuario
	 */
	private ComboData inicializarComboDisponibilidad() {
		ComboData comboData = new ComboData();
		listaDisponibilidad = editorController.findDisponibilidadTourByFecha(linea.getIdProducto(), linea.getFecha());
		if (listaDisponibilidad.size() > 0) {
			for (DisponibilidadTour d : listaDisponibilidad) {
				comboData.agregarItem(d.getTour().getNombre(), d.getIdDisponibilidad());
			}
		} else {
			comboData.agregarItem("No disponible", -1L);
		}
		return comboData;
	}
	
	
	/**
	 * Actualiza los campos del dialogBox de acuerdo con la disponibilidad
	 * seleccionada por el usuario
	 * @param idDisp
	 */
	private void actualizarCamposDisp(Long idDisp) {
		Iterator<DisponibilidadTour> it = listaDisponibilidad.iterator();
		DisponibilidadTour d = null;
		// TODO: esto se puede cambiar por un dao y hacemos load/get??
		while (it.hasNext()) {
			d = it.next();
			if (idDisp.longValue() == d.getIdDisponibilidad().longValue()) {
				dispTour = d;
				Long ocupacion = editorController.getOcupacionTour(d);
				txtCapacidad.setText(valor2Txt(d.getCapacidad()));
				txtDisponibilidad.setText(valor2Txt(d.getCapacidad().intValue() - ocupacion.intValue()));
				txtReferencia.setText(valor2Txt(d.getNumero()));
			}
		}
	}
	
	
	public ReservaTour getReserva() {
		return reserva;
	}
	
}
