package rcp.manticora.editors;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

import org.eclipse.birt.report.viewer.utilities.WebViewer;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;
import org.osgi.framework.Bundle;

import rcp.manticora.Activator;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.FillLayout;

public class Reporte02Editor extends EditorPart {
	
	public static final String ID = "manticora.editors.reporte";
	private String rutaReporte;

	public Reporte02Editor() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
	}

	@Override
	public void doSaveAs() {
	}

	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		setSite(site);
	    setInput(input);
	    setPartName(input.getName());  // para presentar un título en el editor
	    setRutaReporte(((CommonEditorInput) input).getRutaReporte()); 
	}

	@Override
	public boolean isDirty() {
		return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	@Override
	public void createPartControl(Composite parent) {
		
		ScrolledComposite scrolledComposite = new ScrolledComposite(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		
		Composite composite = new Composite(scrolledComposite, SWT.NONE);
		composite.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		Browser browser = new Browser(composite, SWT.NONE);
		scrolledComposite.setContent(composite);
		scrolledComposite.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
//		browser.setUrl("http://www.google.com");
		browser.setText("<html><body>This is Unicode HTML content from memory</body></html>");
		Bundle bundle = Platform.getBundle(Activator.PLUGIN_ID);
		//permite encontrar un archivo en el proyecto, en este caso un reporte de BIRT
		//URL url = FileLocator.find(bundle, new Path("reports/cotPorMes.rptdesign"), null);
		URL url = FileLocator.find(bundle, new Path(getRutaReporte()), null);
		String reporte = "";
		
		try {
			//transforma la ruta de un archivo del proyecto a una ruta dentro del file system del sistema operativo
			//reporte = FileLocator.resolve(url).getPath();
			reporte = FileLocator.toFileURL(url).getPath();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// ventana SWT con frameset de BIRT
//		WebViewer.display(reporte, WebViewer.PDF, browser, "run");
		
		HashMap<String, Object> myparms = new HashMap<String, Object>();
		HashMap<String, String> emitmap = new HashMap<String, String>();

		myparms.put("SERVLET_NAME_KEY", "run");
		myparms.put("FORMAT_KEY", "pdf");
		//myparms.put("RESOURCE_FOLDER_KEY", "c:/myresources");
		myparms.put("ALLOW_PAGE", "false");
		myparms.put("SHOW_PARAMETER_PAGE", "true");
		//myparms.put(WebViewer.MAX_ROWS_KEY, "5");

		try {
			emitmap.put("addtorul", URLEncoder.encode("addtourl", "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		myparms.put("EMITTER_OPTIONS_KEY", emitmap);
		//myparms.put("MAX_ROWS_KEY", "500");
		WebViewer.display(reporte, browser, myparms);
	}

	@Override
	public void setFocus() {
	}
	
	public void setRutaReporte(String rutaReporte) {
		this.rutaReporte = rutaReporte;
	}
	
	public String getRutaReporte() {
		return rutaReporte;
	}
}
