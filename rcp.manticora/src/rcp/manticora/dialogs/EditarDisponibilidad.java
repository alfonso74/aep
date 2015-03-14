package rcp.manticora.dialogs;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
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

import rcp.manticora.Application;
import rcp.manticora.IImageKeys;
import rcp.manticora.model.DisponibilidadTour;
import rcp.manticora.services.FechaUtil;

public class EditarDisponibilidad extends AbstractAEPTitleAreaDialog {
	private final String pluginId = Application.PLUGIN_ID;
	
	private Text txtFecha;
	private Text txtCapacidad;
	private Combo comboTipo;
	private Text txtComentario;
	private DisponibilidadTour linea;
	
	private ImageDescriptor image;
	private Shell shell;
	
	
	public EditarDisponibilidad(Shell parentShell, DisponibilidadTour linea) {
		super(parentShell);
		this.shell = parentShell;
		this.linea = linea;
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
		
		l = new Label(composite, SWT.NONE);
		l.setText("Fecha:");
		txtFecha = new Text(composite, SWT.BORDER);
		txtFecha.setLayoutData(new GridData(60,15));
		
		Button bFecha = new Button(composite, SWT.NONE);
		gridData = new GridData(16,16);
		gridData.horizontalSpan = 1;
		bFecha.setLayoutData(gridData);
		image = AbstractUIPlugin.imageDescriptorFromPlugin(pluginId, IImageKeys.CALENDARIO);
		bFecha.setImage(image.createImage());
		bFecha.addSelectionListener(this.crearCalendario(shell, txtFecha));
		
		l = new Label(composite, SWT.NONE);
		l.setText("     Tipo:");
		comboTipo = new Combo(composite, SWT.READ_ONLY);
		comboTipo.setItems(new String[] {"Abierto", "Privado"});
		comboTipo.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		
		l = new Label(composite, SWT.NONE);
		l.setText("Capacidad:");
		txtCapacidad = new Text(composite, SWT.BORDER);
		gridData = new GridData(35, 15);
		gridData.horizontalSpan = 5;
		txtCapacidad.setLayoutData(gridData);
		/*
		l = new Label(composite, SWT.NONE);
		l.setText("Tipo:");
		comboTipo = new Combo(composite, SWT.READ_ONLY);
		comboTipo.setItems(new String[] {"Abierto", "Privado"});
		comboTipo.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		*/
		l = new Label(composite, SWT.NONE);
		l.setText("Comentario:");
		txtComentario = new Text(composite, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.WRAP);
		gridData = new GridData(SWT.FILL, SWT.CENTER, false, false);
		gridData.horizontalSpan = 5;
		gridData.heightHint = 40;
		txtComentario.setLayoutData(gridData);
		
		llenarCampos();
		return composite;
	}

	
	@Override
	protected void llenarCampos() {
		setTitle("Editar registro");
		setMessage("Por favor, indique los parámetros de la disponibilidad.", IMessageProvider.INFORMATION);
		
		String pFecha = FechaUtil.toString(linea.getFecha());
		String pCapacidad = valor2Txt(linea.getCapacidad());
		String pTipo = linea.getTipo();
		String pComentario = linea.getComentario();
		
		txtFecha.setText(pFecha);
		txtCapacidad.setText(pCapacidad);
		comboTipo.setText(pTipo);
		txtComentario.setText(pComentario);
	}


	@Override
	public boolean close() {
		// si el botón presionado es OK validamos, guardamos y cerramos
		if (getReturnCode() == IDialogConstants.OK_ID) {
			try {
				if (validarSave()) {
					guardarLinea();
					return super.close();
				} else {
					return false;
				}
			} catch (Exception e) {
				mensajeError(shell, e);
				return false;
			}
		};
		return super.close();
	}
	
	
	private boolean validarSave() {
		return true;
	}
	
	
	private void guardarLinea() {
		linea.setFecha(FechaUtil.toDate(txtFecha.getText()));
		linea.setCapacidad(txt2Integer((txtCapacidad.getText())));
		linea.setTipo(comboTipo.getText());
		linea.setComentario(txtComentario.getText());
	}

}
