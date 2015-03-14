package rcp.manticora.editors;

import java.io.IOException;
import java.net.URL;

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

public class Reporte01Editor extends EditorPart {
	
	public static final String ID = "manticora.editors.reporte";
	private String rutaReporte;
	
	private Browser browser;

	public Reporte01Editor() {
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
		
		//reporte = "reporte01.rptdesign";    // busca en el directorio del plugin de birt (no good!!)
		//reporte = "c:/reporte01.rptdesign";
		this.browser = new Browser(parent, SWT.NONE);
//		WebViewer.startup(browser);
//		System.setProperty( "RUN_UNDER_ECLIPSE", "true" );
		
		// ventana aparte sin frameset de BIRT
		//WebViewer.display(reporte, WebViewer.HTML, false);
		// ventana aparte con frameset de BIRT (navegador de rows) - excelente!!
		//WebViewer.display(reporte, WebViewer.HTML, true);
		
		// ventana SWT (parte de manticora) - casi excelente!
		//WebViewer.display(reporte, WebViewer.PDF, this.browser, "run");
		WebViewer.display(reporte, WebViewer.PDF, this.browser, "run");
		// ventana SWT con frameset de BIRT
		//WebViewer.display(reporte, WebViewer.HTML, this.browser, "frameset");
		
		
		/*
		Shell shell = new Shell(parent.getDisplay());
		shell.setText("JA!");
		shell.setSize(700, 550);
		
		//Composite compBirt = new Composite(shell, SWT.DIALOG_TRIM);
		this.browser = new Browser(shell, SWT.NONE);
		WebViewer.startup(browser);
		System.setProperty( "RUN_UNDER_ECLIPSE", "true" );
		WebViewer.display(reporte, WebViewer.HTML, this.browser, "run");
		shell.pack();
		shell.open();
		*/
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
