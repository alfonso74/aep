package rcp.manticora.editors;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;

import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.EngineConstants;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportEngineFactory;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunAndRenderTask;
import org.eclipse.birt.report.engine.api.PDFRenderOption;
import org.eclipse.birt.report.engine.api.RenderOption;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.browser.Browser;
import org.osgi.framework.Bundle;

import rcp.manticora.services.HibernateUtil;
import org.eclipse.swt.custom.ScrolledComposite;


public class ReportesEditor extends EditorPart {

	public static final String ID = "manticora.editors.reporte";
	
	private String rutaReporte;
	

	public ReportesEditor() {
	}

	/**
	 * Create contents of the editor part.
	 * @param parent
	 */
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

		previewProforma(browser, rutaReporte);
	}

	@Override
	public void setFocus() {
		// Set the focus
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		// Do the Save operation
	}

	@Override
	public void doSaveAs() {
		// Do the Save As operation
	}

	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		setSite(site);
		setInput(input);
		setPartName(input.getName()); 
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
	
	public void dispose() {
		super.dispose();
	}
	
	private void previewProforma(Browser browser, String rutaReporte) {

		IReportEngine engine = inicializarEngine();
		String rpt = getReportURL(rutaReporte);
		RenderOption options = configurarRendererPDF(rpt);
		//RenderOption options = configurarRendererHTML(rpt);
		String reporteFileName = ejecutarReporte(engine, options, rpt);

		System.out.println("Archivo de salida: " + reporteFileName);

		browser.setUrl("file://" + reporteFileName);
		engine.destroy();
	}
	
	
	private IReportEngine inicializarEngine() {
		EngineConfig config = new EngineConfig();
		//add to the classpath-Set Parent Classloader		
		config.getAppContext().put(EngineConstants.APPCONTEXT_CLASSLOADER_KEY, this.getClass().getClassLoader());
		//config.getAppContext().put(EngineConstants.WEBAPP_CLASSPATH_KEY, "c:/jars/mjo.jar");
		IReportEngineFactory factory = (IReportEngineFactory) org.eclipse.birt.core.framework.Platform
				.createFactoryObject( IReportEngineFactory.EXTENSION_REPORT_ENGINE_FACTORY );
		IReportEngine engine = factory.createReportEngine( config );
		return engine;
	}
	
	
	/**
	 * Obtiene la ubicación del reporte (dentro del bundle de eclipse)
	 * @param rutaReporte Ruta del reporte dentro del bundle de eclipse
	 * @return Ruta para accesar el reporte
	 */
	private String getReportURL(String rutaReporte) {
		//use this to set the resource path	
		//Bundle bundle = org.eclipse.core.runtime.Platform.getBundle("org.eclipse.birt.examples.rcpengine");		
		//URL url = FileLocator.find(bundle, new Path("/resources"), null);
		//String myresourcepath = FileLocator.toFileURL(url).getPath();		
		//config.setResourcePath(myresourcepath);

		Bundle bundle = org.eclipse.core.runtime.Platform.getBundle("rcp.manticora");		
		URL url = FileLocator.find(bundle, new Path(rutaReporte), null);
		String rpt = "";
		try {
			rpt = FileLocator.toFileURL(url).getPath();
		} catch (IOException e) {
			MessageDialog.openError(getSite().getShell(), "Error en: " + this.getClass().getName(), "Error buscando archivo: " + rutaReporte + ".\n" +
					"Error: " + e.toString() + "\n\nStack trace: " + e.getStackTrace()[0] + "\n" + e.getStackTrace()[1]);
			e.printStackTrace();
		}
		System.out.println("Ruta del reporte (bundle): " + rpt);
		return rpt;
	}

	
	/**
	 * Configura un renderer de excel para generar una hoja de cálculo basada en el reporte indicado
	 * @param rpt Ruta del reporte usado como template
	 * @return RenderOption configurado para excel
	 */
	private RenderOption configurarRendererPDF(String rpt) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		String outputPDF = rpt.replaceFirst( ".rptdesign", ".pdf" );
		PDFRenderOption options = new PDFRenderOption();
		options.setOutputStream(bos);
		options.setOutputFormat(HTMLRenderOption.OUTPUT_FORMAT_PDF);
		options.setOutputFileName( outputPDF );
		System.out.println("Ruta del archivo a generar: " + outputPDF);
		return options;
	}
	
	
	private RenderOption configurarRendererHTML(String rpt) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		//String outputPDF = rpt.replaceFirst( ".rptdesign", ".pdf" );
		String outputHTML = rpt.replaceFirst( ".rptdesign", ".html" );
		HTMLRenderOption options = new HTMLRenderOption();
		options.setOutputStream(bos);
		options.setOutputFormat(HTMLRenderOption.OUTPUT_FORMAT_HTML);
		options.setOutputFileName( outputHTML );
		System.out.println("Ruta del archivo a generar: " + outputHTML);
		return options;
	}
	
	
	/**
	 * Gnenera un archivo de acuerdo al reporte y formato suministrados
	 * @param engine Engine de BIRT
	 * @param options renderer configurado para generar el archivo en un formato específico
	 * @param rpt ruta del reporte de BIRT a usar como template
	 * @return
	 */
	private String ejecutarReporte(IReportEngine engine, RenderOption options, String rpt) {
		// Pasamos los parámetros para el reporte y lo ejecutamos
		try {
			IReportRunnable design = engine.openReportDesign(rpt);
			IRunAndRenderTask task = engine.createRunAndRenderTask(design);
//			task.setParameterValue("connectionURL", HibernateUtil.getConnectionURL());
			task.setRenderOption(options);
			task.run();
			task.close();
		} catch (EngineException e) {
			MessageDialog.openError(getSite().getShell(), "Error en: " + this.getClass().getName(), "Error al generar el reporte: " + rpt + ".\n" +
					"Error: " + e.toString() + "\n\nStack trace: " + e.getStackTrace()[0] + "\n" + e.getStackTrace()[1]);
			e.printStackTrace();
		}
		return options.getOutputFileName();
	}
	
	
	
	
	public String getRutaReporte() {
		return rutaReporte;
	}

	public void setRutaReporte(String rutaReporte) {
		this.rutaReporte = rutaReporte;
	}

}
