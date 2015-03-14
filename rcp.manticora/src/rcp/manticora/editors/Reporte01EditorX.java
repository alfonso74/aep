package rcp.manticora.editors;

import java.io.IOException;
import java.net.URL;

import org.apache.xerces.impl.dv.util.Base64;
import org.eclipse.birt.report.viewer.utilities.WebViewer;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.Bundle;

import rcp.manticora.Activator;
import rcp.manticora.Application;

public class Reporte01EditorX extends EditorPart {
	
	public static final String ID = "manticora.editors.reporte";
	private String rutaReporte;
	
	private Browser browser;

	public Reporte01EditorX() {
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
			System.out.println("URL> " + reporte);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		URL imageURL = FileLocator.find(bundle, new Path("icons/menuOperaciones.gif"), null);
		String imageURL2 = "";
		try {
			imageURL2 = FileLocator.toFileURL(imageURL).getPath();
			System.out.println("URL2> " + imageURL2);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		ImageDescriptor imageDescriptor = AbstractUIPlugin.imageDescriptorFromPlugin(Application.PLUGIN_ID, "icons/red-dot.png");
		Image image = imageDescriptor.createImage();

		final ImageData imageData = image.getImageData();
		String image64 = Base64.encode(imageData.data).replace("\n", "");
		System.out.println("URL64> " + new String(imageData.data));
		System.out.println("URL64> " + image64);
		
		
		
		//reporte = "reporte01.rptdesign";    // busca en el directorio del plugin de birt (no good!!)
		//reporte = "c:/reporte01.rptdesign";
		this.browser = new Browser(parent, SWT.NONE);
		String content = "<html><body><img src=\"data:image/png;base64," + image64 + "\" border=\"0\" width=\"60\" height=\"60\"></body></html>";
		content = "<img src=\"data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAUAAAAFCAYAAACNbyblAAAAHElEQVQI12P4//8/w38GIAXDIBKE0DHxgljNBAAO9TXL0Y4OHwAAAABJRU5ErkJggg==\" alt=\"Red dot\" />";
//		image64 = "iVBORw0KGgoAAAANSUhEUgAAAAUAAAAFCAYAAACNbyblAAAAHElEQVQI12P4//8/w38GIAXDIBKE0DHxgljNBAAO9TXL0Y4OHwAAAABJRU5ErkJggg==";
		content = "<img src=\"data:image/png;base64," + image64 + "\" alt=\"JA!\" />";
		browser.setText(content);
//		browser.setBackgroundImage(image);

//		WebViewer.startup(browser);
//		System.setProperty( "RUN_UNDER_ECLIPSE", "true" );
		
		// ventana aparte sin frameset de BIRT
		//WebViewer.display(reporte, WebViewer.HTML, false);
		// ventana aparte con frameset de BIRT (navegador de rows) - excelente!!
//		WebViewer.display(reporte, WebViewer.HTML, true);
		
		// ventana SWT (parte de manticora) - casi excelente!
		//WebViewer.display(reporte, WebViewer.PDF, this.browser, "run");
		WebViewer.display(reporte, WebViewer.PDF, this.browser, "run");
		// ventana SWT con frameset de BIRT
//		WebViewer.display(reporte, WebViewer.HTML, this.browser, "frameset");
		System.out.println("Luego de webviewer!");
		
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
