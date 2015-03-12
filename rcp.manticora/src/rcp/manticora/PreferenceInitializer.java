package rcp.manticora;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;

public class PreferenceInitializer extends AbstractPreferenceInitializer {

	public PreferenceInitializer() {
		super();
	}

	@Override
	public void initializeDefaultPreferences() {
		//La definici�n default para la conexi�n se pas� a la clase LoginDialog, en el evento createDialogArea
		// pero si queremos inicializar algo, aqu� puede ser el lugar correcto
		/*
		IEclipsePreferences defaults = new DefaultScope().getNode(Application.PLUGIN_ID);
		defaults.put("servidor", "servidor");
		defaults.put("schema", "test");
		*/
		System.out.println("Preferencias default inicializadas (via extensi�n)");
	}
}
