package rcp.manticora;

import java.io.IOException;

import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.preferences.ScopedPreferenceStore;

public class GeneralPreferencePage extends FieldEditorPreferencePage implements
		IWorkbenchPreferencePage {
	public static final String AUTO_LOGIN = "prefs_auto_login";
	private ScopedPreferenceStore preferences;

	public GeneralPreferencePage() {
		super(GRID);
		preferences = new ScopedPreferenceStore(new ConfigurationScope(), Application.PLUGIN_ID);
		setPreferenceStore(preferences);
	}
	
	public GeneralPreferencePage(int style) {
		super(style);
	}

	public GeneralPreferencePage(String title, int style) {
		super(title, style);
	}

	public GeneralPreferencePage(String title, ImageDescriptor image, int style) {
		super(title, image, style);
	}
	
	@Override
	protected Control createContents(Composite parent) {
		Composite composite = (Composite) super.createContents(parent);
		((GridLayout) composite.getLayout()).verticalSpacing = 20;
		((GridLayout) composite.getLayout()).marginTop = 20;
		return composite;
	}

	@Override
	protected void createFieldEditors() {
		
		Group grupoConexion = new Group(getFieldEditorParent(), SWT.NONE);
		grupoConexion.setText("Conexión a Base de Datos");
		GridLayout layoutConn = new GridLayout(3, false);
		
		StringFieldEditor strServidor = new StringFieldEditor("servidor", "Nombre del servidor:", 20, grupoConexion);
		strServidor.setTextLimit(20);
		strServidor.setEmptyStringAllowed(false);
		strServidor.setErrorMessage("El campo de \"Nombre del servidor\" no puede quedar en blanco");
		StringFieldEditor strSchema = new StringFieldEditor("schema", "Schema a utilizar:", 20, grupoConexion);
		strSchema.setTextLimit(20);
		strSchema.setEmptyStringAllowed(false);
		strSchema.setErrorMessage("El campo de \"Schema a utilizar\" no puede quedar en blanco");
		addField(strServidor);
		addField(strSchema);

		// la asignación de layout debe ir luego de los addField porque estos 
		// modifican los márgenes a 0
		//layoutConn.numColumns = 1;
		grupoConexion.setLayout(layoutConn);
		grupoConexion.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));		
		
		
		Group grupoPdf = new Group(getFieldEditorParent(), SWT.NONE);
		grupoPdf.setText("Generación de PDF");
		GridLayout layoutPdf = new GridLayout(3, false);
		
		DirectoryFieldEditor strRutaPdf = new DirectoryFieldEditor("rutaPdf", "Ruta de generación:", grupoPdf);
		addField(strRutaPdf);
		// asignación de layouts
		//layoutPdf.numColumns = 3;
		grupoPdf.setLayout(layoutPdf);
		grupoPdf.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
	}
	
	public boolean performOk() {
		try {
			preferences.save();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return super.performOk();
	}

	public void init(IWorkbench workbench) {
	}
}
