package rcp.manticora.dialogs;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import rcp.manticora.Application;
import rcp.manticora.IImageKeys;
import rcp.manticora.model.DisponibilidadTour;
import rcp.manticora.model.Tour;
import rcp.manticora.services.FechaUtil;

public class DefinirDisponibilidad extends AbstractAEPTitleAreaDialog {
	private final String pluginId = Application.PLUGIN_ID;
	
	private Text txtFechaIni;
	private Text txtFechaFin;
	private Text txtCapacidad;
	private Combo comboTipo;
	private Text txtComentario;
	private Set<DisponibilidadTour> vDisponibilidad;
	private Tour tour;
	
	private Button bLunes;
	private Button bMartes;
	private Button bMiercoles;
	private Button bJueves;
	private Button bViernes;
	private Button bSabado;
	private Button bDomingo;
	
	private ImageDescriptor image;
	private Shell shell;

	public DefinirDisponibilidad(Shell parentShell) {
		super(parentShell);
		this.shell = parentShell;
	}
	
	public DefinirDisponibilidad(Shell parentShell, Tour tour, Set<DisponibilidadTour> vDisponibilidad) {
		super(parentShell);
		this.shell = parentShell;
		this.tour = tour;
		this.vDisponibilidad = vDisponibilidad;
	}
	

	protected Control createDialogArea(Composite parent) {
		Composite composite = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout(7, false);
		layout.marginWidth = 15;
		layout.marginHeight = 10;
		layout.verticalSpacing = 8;
		composite.setLayout(layout);
		
		Label l;
		GridData gridData;
		
		l = new Label(composite, SWT.NONE);
		l.setText("Desde:");
		txtFechaIni = new Text(composite, SWT.BORDER);
		txtFechaIni.setLayoutData(new GridData(60,15));
		
		Button bFechaIni = new Button(composite, SWT.NONE);
		gridData = new GridData(16,16);
		gridData.horizontalSpan = 1;
		bFechaIni.setLayoutData(gridData);
		image = AbstractUIPlugin.imageDescriptorFromPlugin(pluginId, IImageKeys.CALENDARIO);
		bFechaIni.setImage(image.createImage());
		bFechaIni.addSelectionListener(this.crearCalendario(shell, txtFechaIni));
		
		l = new Label(composite, SWT.NONE);
		l.setLayoutData(new GridData(30, 15));
		
		l = new Label(composite, SWT.NONE);
		l.setText("Hasta:");
		txtFechaFin = new Text(composite, SWT.BORDER);
		txtFechaFin.setLayoutData(new GridData(60,15));
		
		Button bFechaFin = new Button(composite, SWT.PUSH);
		bFechaFin.setLayoutData(new GridData(16,16));
		bFechaFin.setImage(image.createImage());
		bFechaFin.addSelectionListener(this.crearCalendario(shell, txtFechaFin, txtFechaIni));
		
		
		Group gFechas = new Group(composite, SWT.NONE);
		layout = new GridLayout(7, false);
		gridData = new GridData(SWT.FILL, SWT.FILL, false, false);
		gridData.horizontalSpan = 7;
		gridData.verticalSpan = 1;
		gridData.verticalIndent = 5;
		gFechas.setLayout(layout);
		gFechas.setLayoutData(gridData);
		gFechas.setText(" Selección de días que no aplican ");
	
		l = new Label(gFechas, SWT.NONE);
		l.setText("Excluir días:");
		
		Composite grupoSemana = new Composite(gFechas, SWT.NONE);
		layout = new GridLayout(7, true);
		gridData = new GridData(SWT.FILL, SWT.FILL, false, false);
		gridData.horizontalSpan = 6;
		gridData.verticalSpan = 1;
		grupoSemana.setLayout(layout);
		grupoSemana.setLayoutData(gridData);
		
		bLunes = new Button(grupoSemana, SWT.CHECK);
		bLunes.setText("L");
		bLunes.setToolTipText("Lunes");
		bMartes = new Button(grupoSemana, SWT.CHECK);
		bMartes.setText("M");
		bMartes.setToolTipText("Martes");
		bMiercoles = new Button(grupoSemana, SWT.CHECK);
		bMiercoles.setText("M");
		bMiercoles.setToolTipText("Miércoles");
		bJueves = new Button(grupoSemana, SWT.CHECK);
		bJueves.setText("J");
		bJueves.setToolTipText("Jueves");
		bViernes = new Button(grupoSemana, SWT.CHECK);
		bViernes.setText("V");
		bViernes.setToolTipText("Viernes");
		bSabado = new Button(grupoSemana, SWT.CHECK);
		bSabado.setText("S");
		bSabado.setToolTipText("Sábado");
		bDomingo = new Button(grupoSemana, SWT.CHECK);
		bDomingo.setText("D");
		bDomingo.setToolTipText("Domingo");
		
		l = new Label(composite, SWT.NONE);
		l.setText("Capacidad:");
		txtCapacidad = new Text(composite, SWT.BORDER);
		gridData = new GridData(35, 15);
		gridData.horizontalSpan = 3;
		gridData.verticalIndent = 5;
		txtCapacidad.setLayoutData(gridData);
		
		l = new Label(composite, SWT.NONE);
		l.setText("Tipo:");
		comboTipo = new Combo(composite, SWT.READ_ONLY);
		comboTipo.setItems(new String[] {"Abierto", "Privado"});
		comboTipo.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		
		l = new Label(composite, SWT.NONE);
		l.setText("Comentario:");
		txtComentario = new Text(composite, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.WRAP);
		gridData = new GridData(SWT.FILL, SWT.CENTER, false, false);
		gridData.horizontalSpan = 6;
		gridData.heightHint = 40;
		txtComentario.setLayoutData(gridData);
		
		llenarCampos();
		return composite;
	}

	protected void llenarCampos() {
		setTitle("Definición de disponibilidad");
		setMessage("Por favor, defina la fecha o rangos de disponibilidad del tour.", IMessageProvider.INFORMATION);
		
		String pCapacidad = valor2Txt(tour.getCapacidad());
		txtCapacidad.setText(pCapacidad);
	}
	
	/**
	 * Encargado de habilitar la disponibilidad del tour en las fechas indicadas
	 * por el usuario final
	 *
	 */
	private void aplicarDisponibilidad() {
		Date fechaInicial = FechaUtil.toDate(txtFechaIni.getText());
		Date fechaFinal = FechaUtil.toDate(txtFechaFin.getText());
		if (!existenLineasDisponibilidad()) {
			while (fechaInicial.getTime() <= fechaFinal.getTime()) {
				if (diaSemanaHabilitado(fechaInicial)) {
					DisponibilidadTour linea = new DisponibilidadTour();
					linea.setTour(tour);
					linea.setFecha(fechaInicial);
					linea.setCapacidad(txt2Integer((txtCapacidad.getText())));
					linea.setTipo(comboTipo.getText());
					linea.setComentario(txtComentario.getText());

					if (vDisponibilidad.contains(linea)) {
						// esto nunca debería suceder... pero just in case...
						MessageDialog.openInformation(shell, "Definición de disponibilidad",
								"No se pudo habilitar la disponibilidad para la siguiente fecha: " + FechaUtil.toString(linea.getFecha()));
					} else {
						vDisponibilidad.add(linea);
						System.out.println("Disponibilidad habilitada: " + FechaUtil.toString(linea.getFecha()));
					}			
				}
				fechaInicial = FechaUtil.ajustarFecha(fechaInicial, 1);
			}	
		} else {
			MessageDialog.openError(shell, "Definición de disponibilidad", 
					"El rango definido es inválido.  Ya se ha definido la disponibilidad para\n" +
					"algunas de las fechas dentro del rango suministrado.");
		}
	}
	
	/**
	 * Indica si la fecha suministrada puede habilitarse para hacer reservas 
	 * de tour.
	 * @param fecha
	 * @return true si es válida, false si no debe habilitarse
	 */
	private boolean diaSemanaHabilitado(Date fecha) {
		int noDia = FechaUtil.getDiaSemana(fecha);
		// cuando está check significa que el día está excluido, así que
		// negamos la selección.
		if (noDia == 1) return !bDomingo.getSelection();
		if (noDia == 2) return !bLunes.getSelection();
		if (noDia == 3) return !bMartes.getSelection();
		if (noDia == 4) return !bMiercoles.getSelection();
		if (noDia == 5) return !bJueves.getSelection();
		if (noDia == 6) return !bViernes.getSelection();
		if (noDia == 7) return !bSabado.getSelection();
		return false;
	}
	
	
	/**
	 * Método encargado de determinar si el rango especificado ya tiene alguna fecha
	 * definida a nivel de disponibilidad
	 * @return
	 */
	private boolean existenLineasDisponibilidad() {
		Date fechaInicial = FechaUtil.toDate(txtFechaIni.getText());
		Date fechaFinal = FechaUtil.toDate(txtFechaFin.getText());
		Iterator it = vDisponibilidad.iterator();
		while (it.hasNext()) {
			DisponibilidadTour linea = (DisponibilidadTour) it.next();
			long fechaLinea = linea.getFecha().getTime();
			if (fechaLinea >= fechaInicial.getTime() && fechaLinea <= fechaFinal.getTime()) {
				return true;
			}
		}
		return false;
	}
	
	
	private void borrarRango() throws Exception {
		Set<DisponibilidadTour> vBorrar = new HashSet<DisponibilidadTour>();
		Date fechaInicial = FechaUtil.toDate(txtFechaIni.getText());
		Date fechaFinal = FechaUtil.toDate(txtFechaFin.getText());
		Iterator it = vDisponibilidad.iterator();
		while (it.hasNext()) {
			DisponibilidadTour linea = (DisponibilidadTour) it.next();
			long fechaLinea = linea.getFecha().getTime();
			System.out.println("FL: " + fechaLinea + ", FI: " + fechaInicial.getTime() + ", FF: " + fechaFinal.getTime());
			if (fechaLinea >= fechaInicial.getTime() && fechaLinea <= fechaFinal.getTime()) {
				// TODO código para verificar que la línea se puede borrar
				vBorrar.add(linea);
			}
		}
		vDisponibilidad.removeAll(vBorrar);
	}
	
	public boolean close() {
		return super.close();
	}
	
	
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, "Agregar rango", true);
		createButton(parent, IDialogConstants.CANCEL_ID, "Borrar rango", false);
		createButton(parent, IDialogConstants.CLOSE_ID, "Cerrar", false);
	}
	
	
	/*
	 * No queremos que la ventana se cierre con cualquier botón como sucede
	 * con el buttonPressed default.  Así que implementamos un buttonPressed
	 * que solamente cierra la ventana al seleccionar el botón de "Cerrar".
	 */
	protected void buttonPressed(int buttonId) {
		try {
			setReturnCode(buttonId);
			if (buttonId == IDialogConstants.OK_ID) {
				if (validarOK()) {
					aplicarDisponibilidad();
				}
			}
			if (buttonId == IDialogConstants.CANCEL_ID) {
				borrarRango();
			}
			if (buttonId == IDialogConstants.CLOSE_ID) {
				close();
			}
		} catch (Exception e) {
			//MessageDialog.openError(shell, "Error en la aplicación", "Error: " + e.toString() + "\n\nStack trace: " + e.getStackTrace()[0]);
			mensajeError(shell, e);
		}
	}
	
	
	public boolean validarOK() {
		String pDesde = valor2Txt(txtFechaIni.getText());
		String pHasta = valor2Txt(txtFechaFin.getText());
		String pCapacidad = txtCapacidad.getText();
		int pTipo = comboTipo.getSelectionIndex();
		
		if (pDesde == null || pDesde.equals("")) {
			MessageDialog.openInformation(shell, "Validación de campos",
				"Debe indicar la fecha inicial de disponibilidad.");
			txtFechaIni.setFocus();
			return false;
		}
		if (pHasta == null || pHasta.equals("")) {
			MessageDialog.openInformation(shell, "Validación de campos",
				"Debe indicar la fecha final de disponibilidad.");
			txtFechaFin.setFocus();
			return false;
		}
		if (pCapacidad.equals("")) {
			MessageDialog.openInformation(shell, "Validación de campos",
				"Debe indicar la capacidad de paxs para la fecha o rango indicado.");
			txtCapacidad.setFocus();
			return false;
		}
		if (pTipo == -1) {
			MessageDialog.openInformation(shell, "Validación de campos",
				"Debe indicar el tipo de la disponibilidad.");
			comboTipo.setFocus();
			return false;
		}
		
		return true;
	}
}
