package rcp.manticora.dialogs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import org.eclipse.core.runtime.IProduct;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.core.runtime.preferences.IPreferencesService;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.branding.IProductConstants;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.ui.preferences.ScopedPreferenceStore;

import rcp.manticora.Application;


public class LoginDialog extends Dialog {
	private Text txtUsuario;
	private Text txtPassword;
	private Text txtServidor;
	private Text txtSchema;
	
	private Image[] images;
	
	private ConnectionData datos;

	public LoginDialog(Shell parentShell) {
		super(parentShell);
	}

	/*
	// autogenerado, se comenta porque es ambiguo
	public LoginDialog(IShellProvider parentShell) {
		super(parentShell);
	}
	*/
	
	
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("AEP Login");
//		carga la imagen de la ventana desde la definición del producto
		IProduct product = Platform.getProduct();
		if (product != null) {
			System.out.println("Obteniendo objeto producto...");
			System.out.println("Nombre de aplicación: " + product.getApplication());
			System.out.println("Imágenes: " + product.getProperty(IProductConstants.WINDOW_IMAGES));
			String[] imageURLs = parseCSL(product.getProperty(IProductConstants.WINDOW_IMAGES));
			if (imageURLs != null && imageURLs.length > 0) {
				images = new Image[imageURLs.length];
				for (int i = 0; i < imageURLs.length; i++) {
					String url = imageURLs[i];
					ImageDescriptor descriptor = AbstractUIPlugin
							.imageDescriptorFromPlugin(product
									.getDefiningBundle().getSymbolicName(), url);
					images[i] = descriptor.createImage(true);
				}
				newShell.setImages(images);
			}
		}
		
		
	}
	
	
	public static String[] parseCSL(String csl) {
		if (csl == null)
			return null;
		StringTokenizer tokens = new StringTokenizer(csl, ","); //$NON-NLS-1$
		ArrayList<String> array = new ArrayList<String>(10);
		while (tokens.hasMoreTokens()) {
			String tt = tokens.nextToken().trim();
			System.out.println("Token: " + tt);
			array.add(tt);
			//array.add(tokens.nextToken().trim());
		}
		return (String[]) array.toArray(new String[array.size()]);
	}


	protected Control createDialogArea(Composite parent) {
		//return super.createDialogArea(parent);
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout gridLayout = new GridLayout(2, false);
		composite.setLayout(gridLayout);
		
		Label l;
		GridData gridData;
		
		l = new Label(composite, SWT.NONE);
		l.setText("Por favor, introduzca los datos de conexión");
		gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1);
		l.setLayoutData(gridData);
		
		l= new Label(composite, SWT.NONE);
		l.setText("&Usuario:");
		gridData = new GridData(SWT.RIGHT, SWT.CENTER, false, false);
		l.setLayoutData(gridData);
		
		txtUsuario = new Text(composite, SWT.BORDER);
		gridData = new GridData(200,15);
		txtUsuario.setLayoutData(gridData);
		
		l= new Label(composite, SWT.NONE);
		l.setText("&Password:");
		gridData = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		l.setLayoutData(gridData);
		
		txtPassword = new Text(composite, SWT.BORDER | SWT.PASSWORD);
		gridData = new GridData(SWT.FILL, SWT.CENTER, true, false);
		txtPassword.setLayoutData(gridData);
		
		l= new Label(composite, SWT.NONE);
		l.setText("&Servidor:");
		gridData = new GridData(SWT.RIGHT, SWT.CENTER, false, false);
		l.setLayoutData(gridData);
		
		txtServidor = new Text(composite, SWT.BORDER);
		gridData = new GridData(SWT.FILL, SWT.CENTER, true, false);
		txtServidor.setLayoutData(gridData);
		
		// cargamos las preferencias y las presentamos en el LoginDialog
		IPreferencesService service = Platform.getPreferencesService();
		String s = service.getString(Application.PLUGIN_ID, "servidor", "servidor ", null);
		String h = service.getString(Application.PLUGIN_ID, "schema", "test2", null);
		System.out.println("Servidor a utilizar: " + s);
		System.out.println("Schema a utilizar: " + h);
		
		txtServidor.setText(s);
		// si s contiene el valor default "servidor " entonces presentamos el campo de servidor y de schema,
		// en caso contrario, mostramos el campo servidor como campo de solo lectura.
		if (s.equals("servidor ")) {
			l= new Label(composite, SWT.NONE);
			l.setText("Sc&hema:");
			gridData = new GridData(SWT.RIGHT, SWT.CENTER, false, false);
			l.setLayoutData(gridData);
			
			txtSchema = new Text(composite, SWT.BORDER);
			gridData = new GridData(SWT.FILL, SWT.CENTER, true, false);
			txtSchema.setLayoutData(gridData);
			txtSchema.setText(h);
		} else {
			txtServidor.setEnabled(false);
		}
		
		return composite;
	}

	
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, "&Login", true);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	}

	
	protected void okPressed() {
		String usuario = txtUsuario.getText();
		String password = txtPassword.getText();
		String servidor = txtServidor.getText().trim();
		
		if (usuario.equals("")) {
			MessageDialog.openError(getShell(), "Error de validación",
			"El campo de usuario no puede quedar en blanco.");
			txtUsuario.setFocus();
			return;
		}
		if (servidor.equals("")) {
			MessageDialog.openError(getShell(), "Error de validación",
			"El campo de servidor no puede quedar en blanco.");
			txtServidor.setFocus();
			return;
		}
		// si txtServidor está habilitado, guardar los valores introducidos
		if (txtServidor.getEnabled()) {
			ScopedPreferenceStore store = new ScopedPreferenceStore(new ConfigurationScope(), Application.PLUGIN_ID);
			store.setValue("servidor", txtServidor.getText());
			store.setValue("schema", txtSchema.getText());
			try {
				store.save();
			} catch (IOException e) {
				MessageDialog.openError(getShell(), "Error en preferencias", "No se ha podido guardar las preferencias.");
				e.printStackTrace();
				return;
			}
		}
		
		datos = new ConnectionData(usuario, password, servidor);
		super.okPressed();       // requerido porque sino no funciona el botón OK
	}
	
	
	public ConnectionData getDatos() {
		return datos;
	}
	
}

