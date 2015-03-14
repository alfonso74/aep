
package rcp.manticora;

import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.IPreferencesService;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.splash.AbstractSplashHandler;

import rcp.manticora.Application;
import rcp.manticora.dialogs.ConnectionData;
import rcp.manticora.services.AutenticacionUtil;

/**
 * @since 3.3
 * 
 */
public class InteractiveSplashHandler extends AbstractSplashHandler {
	
	private final static int F_LABEL_HORIZONTAL_INDENT = 245;

	private final static int F_BUTTON_WIDTH_HINT = 80;

	private final static int F_TEXT_WIDTH_HINT = 150;
	
	private final static int F_COLUMN_COUNT = 3;
	
	private Composite fCompositeLogin;
	
	private Text fTextUsername;
	
	private Text fTextPassword;
	
	private Text fTextServidor;
	
	private Button fButtonOK;
	
	private Button fButtonCancel;
	
	private boolean fAuthenticated;
	
	
// ************************* Progress label y bar *************************
	
	private Label progressLabel;
	
	private ProgressBar progressBar;
	
	private int loginStatus;
	
	
	/**
	 * 
	 */
	public InteractiveSplashHandler() {
		fCompositeLogin = null;
		fTextUsername = null;
		fTextPassword = null;
		fButtonOK = null;
		fButtonCancel = null;
		fAuthenticated = false;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.splash.AbstractSplashHandler#init(org.eclipse.swt.widgets.Shell)
	 */
	public void init(final Shell splash) {
		System.out.println("Init de InteractiveSplashHandler...");
		// Store the shell
		super.init(splash);
		// Configure the shell layout
		configureUISplash();
		// Create UI
		createUI();		
		// Create UI listeners
		createUIListeners();
		// Force the splash screen to layout
		splash.layout(true);
		// Keep the splash screen visible and prevent the RCP application from 
		// loading until the close button is clicked.
		doEventLoop();
	}
	
	/**
	 * 
	 */
	/*
	private void doEventLoop() {
		Shell splash = getSplash();
		System.out.println("JA!!!");
		while (fAuthenticated == false) {
			if (splash.getDisplay().readAndDispatch() == false) {
				splash.getDisplay().sleep();
			}
		}
	}
	*/
	private void doEventLoop() {
	    Shell splash = getSplash();
	    while (fAuthenticated == false) {
	    	System.out.println("JA!!!!");
	        if (splash.getDisplay().readAndDispatch() == false) {
	            if( loginStatus == 1 ) {
	                loginSuccess();
	            } else if( loginStatus == 2 ) {
	                loginFailure();
	            }
	               
	            splash.getDisplay().sleep();
	        }
	    }
	}

	/**
	 * 
	 */
	private void createUIListeners() {
		// Create the OK button listeners
		createUIListenersButtonOK();
		// Create the cancel button listeners
		createUIListenersButtonCancel();
	}

	/**
	 * 
	 */
	private void createUIListenersButtonCancel() {
		fButtonCancel.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				handleButtonCancelWidgetSelected();
			}
		});		
	}

	/**
	 * 
	 */
	private void handleButtonCancelWidgetSelected() {
		// Abort the loading of the RCP application
		getSplash().getDisplay().close();
		System.exit(0);		
	}
	
	/**
	 * 
	 */
	private void createUIListenersButtonOK() {
		fButtonOK.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				handleButtonOKWidgetSelected();
			}
		});				
	}

	/**
	 * 
	 */
	/*
	private void handleButtonOKWidgetSelected() {
		String username = fTextUsername.getText();
		String password = fTextPassword.getText();
		// Aunthentication is successful if a user provides any username and
		// any password
		if ((username.length() > 0) &&
				(password.length() > 0)) {
			fAuthenticated = true;
		} else {
			MessageDialog.openError(
					getSplash(),
					"Authentication Failed",  //NON-NLS-1
					"A username and password must be specified to login.");  //NON-NLS-1
		}
	}
	*/
	
	private void handleButtonOKWidgetSelected() {
	    final String username = fTextUsername.getText();
	    final String password = fTextPassword.getText();
	       
	    toggleCheckProgress(true);
	       
	    Thread t = new Thread() {

	        public void run() {
	            if( login(username, password, "localhost") ) {
	                loginStatus = 1;
	            } else {
	                loginStatus = 2;
	            }
	            getSplash().getDisplay().wake();
	        }
	    };
	    t.start();
	}
	
	
	private boolean login(String usuario, String password, String servidor) {
		ConnectionData datos = new ConnectionData(usuario, password, servidor);
		boolean resultado = AutenticacionUtil.verificarUsuario(datos);
		return resultado;
	}
	
	/**
	 * 
	 */
	private void createUI() {
		// Create the login panel
		createUICompositeLogin();
		// Create the blank spanner
		createUICompositeBlank();
		// Create the user name label
		createUILabelUserName();
		// Create the user name text widget
		createUITextUserName();
		// Create the password label
		createUILabelPassword();
		// Create the password text widget
		createUITextPassword();
		// Crear label para "Servidor"
		createUILabelServidor();
		// Crear campo de texto para "Servidor"
		createUITextServidor();
		// Create the blank label
		createUILabelBlank();
		// Create the OK button
		createUIButtonOK();
		// Create the cancel button
		createUIButtonCancel();
		// Crear barra de progreso
		createProgressInfo();
		
		// asignamos el foco al campo de username, y ponemos como default al botón "OK"
		fTextUsername.setFocus();
		getSplash().setDefaultButton(fButtonOK);
	}		
	
	/**
	 * 
	 */
	private void createUIButtonCancel() {
		// Create the button
		fButtonCancel = new Button(fCompositeLogin, SWT.PUSH);
		fButtonCancel.setText("&Cancelar"); //NON-NLS-1
		// Configure layout data
		GridData data = new GridData(SWT.NONE, SWT.NONE, false, false);
		data.widthHint = F_BUTTON_WIDTH_HINT;	
		data.verticalIndent = 10;
		fButtonCancel.setLayoutData(data);
	}

	/**
	 * 
	 */
	private void createUIButtonOK() {
		// Create the button
		fButtonOK = new Button(fCompositeLogin, SWT.PUSH);
		fButtonOK.setText("&Login"); //NON-NLS-1
		// Configure layout data
		GridData data = new GridData(SWT.NONE, SWT.NONE, false, false);
		data.widthHint = F_BUTTON_WIDTH_HINT;
		data.verticalIndent = 10;
		fButtonOK.setLayoutData(data);
	}

	/**
	 * 
	 */
	private void createUILabelBlank() {
		Label label = new Label(fCompositeLogin, SWT.NONE);
		label.setVisible(false);
	}

	/**
	 * 
	 */
	private void createUITextPassword() {
		// Create the text widget
		int style = SWT.PASSWORD | SWT.BORDER;
		fTextPassword = new Text(fCompositeLogin, style);
		// Configure layout data
		GridData data = new GridData(SWT.NONE, SWT.NONE, false, false);
		data.widthHint = F_TEXT_WIDTH_HINT;
		data.horizontalSpan = 2;
		fTextPassword.setLayoutData(data);		
	}

	/**
	 * 
	 */
	private void createUILabelPassword() {
		// Create the label
		Label label = new Label(fCompositeLogin, SWT.NONE);
		label.setText("&Password:"); //NON-NLS-1
		// Configure layout data
		GridData data = new GridData();
		data.horizontalIndent = F_LABEL_HORIZONTAL_INDENT;
		label.setLayoutData(data);					
	}

	/**
	 * 
	 */
	private void createUITextUserName() {
		// Create the text widget
		fTextUsername = new Text(fCompositeLogin, SWT.BORDER);
		// Configure layout data
		GridData data = new GridData(SWT.NONE, SWT.NONE, false, false);
		data.widthHint = F_TEXT_WIDTH_HINT;
		data.horizontalSpan = 2;
		fTextUsername.setLayoutData(data);		
	}

	/**
	 * 
	 */
	private void createUILabelUserName() {
		// Create the label
		Label label = new Label(fCompositeLogin, SWT.NONE);
		label.setText("&Usuario:"); //NON-NLS-1
		// Configure layout data
		GridData data = new GridData();
		data.horizontalIndent = F_LABEL_HORIZONTAL_INDENT;
		label.setLayoutData(data);		
	}

	/**
	 * 
	 */
	private void createUICompositeBlank() {
		Composite spanner = new Composite(fCompositeLogin, SWT.NONE);
		GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
		data.horizontalSpan = F_COLUMN_COUNT;
		spanner.setLayoutData(data);
	}

	/**
	 * 
	 */
	private void createUICompositeLogin() {
		// Create the composite
		fCompositeLogin = new Composite(getSplash(), SWT.BORDER);
		GridLayout layout = new GridLayout(F_COLUMN_COUNT, false);
		fCompositeLogin.setLayout(layout);		
	}

	/**
	 * 
	 */
	private void configureUISplash() {
		// Configure layout
		FillLayout layout = new FillLayout(); 
		getSplash().setLayout(layout);
		// Force shell to inherit the splash background
		getSplash().setBackgroundMode(SWT.INHERIT_DEFAULT);
	}
	
	
	
//****************************** Progress bar *****************************
	
	private void createUILabelServidor() {
		// Create the label
		Label label = new Label(fCompositeLogin, SWT.NONE);
		label.setText("&Servidor:"); //NON-NLS-1
		// Configure layout data
		GridData data = new GridData();
		data.horizontalIndent = F_LABEL_HORIZONTAL_INDENT;
		label.setLayoutData(data);		
	}
	
	private void createUITextServidor() {		
		// cargamos las preferencias y las presentamos en el LoginDialog
		IPreferencesService service = Platform.getPreferencesService();
		String s = service.getString(Application.PLUGIN_ID, "servidor", "error", null);		
		// Create the text widget
		fTextServidor = new Text(fCompositeLogin, SWT.BORDER);
		// Configure layout data
		GridData data = new GridData(SWT.NONE, SWT.NONE, false, false);
		data.widthHint = F_TEXT_WIDTH_HINT;
		data.horizontalSpan = 2;
		fTextServidor.setLayoutData(data);
		fTextServidor.setEnabled(false);
		fTextServidor.setText(s);
	}
	
	private void createProgressInfo() {
	    progressLabel = new Label(fCompositeLogin,SWT.NONE);
	    progressLabel.setText("Autenticando:");
	    GridData data = new GridData();
	    data.horizontalIndent = F_LABEL_HORIZONTAL_INDENT;
	    progressLabel.setLayoutData(data);
	    progressLabel.setVisible(false);
	       
	    progressBar = new ProgressBar(fCompositeLogin,SWT.NONE|SWT.INDETERMINATE);
	    data = new GridData(SWT.NONE, SWT.NONE, false, false);
	    data.widthHint = F_TEXT_WIDTH_HINT + 10;
	    data.horizontalSpan = 2;
	    progressBar.setLayoutData(data);
	    progressBar.setVisible(false);
	}

	private void toggleCheckProgress(boolean state) {
	    progressLabel.setVisible(state);
	    progressBar.setVisible(state);
	    fCompositeLogin.layout();
	}
	
	private void loginSuccess() {
	    toggleCheckProgress(false);
	    fCompositeLogin.setVisible(false);
	    fAuthenticated = true;
	    loginStatus = -1;
	}
	   
	private void loginFailure() {
	    toggleCheckProgress(false);
	    loginStatus = -1;
	    MessageDialog.openError(
	        getSplash(),
	        "Falla de autenticación",
	        "Usuario no autenticado correctamente: " + fTextUsername.getText());
	}
	
}
