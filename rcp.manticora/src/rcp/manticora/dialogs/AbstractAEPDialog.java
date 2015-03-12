package rcp.manticora.dialogs;

import java.util.ArrayList;
import java.util.StringTokenizer;


import org.eclipse.core.runtime.IProduct;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.branding.IProductConstants;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import rcp.manticora.services.FormUtils;
import rcp.manticora.services.ICalendarUtils;
import rcp.manticora.services.IFormUtils;

public abstract class AbstractAEPDialog extends Dialog implements IFormUtils, ICalendarUtils {
	private String titulo;
	
	private Image[] images;
	private FormUtils formUtils;
	

	public AbstractAEPDialog(Shell parentShell, String titulo) {
		super(parentShell);
		this.titulo = titulo;
		formUtils = new FormUtils();
	}
	
	
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		if (titulo.equals("")) {
			newShell.setText("Ancon Expeditions of Panama");
		} else {
			newShell.setText(titulo);
		}
//		carga la imagen de la ventana desde la definición del producto
		IProduct product = Platform.getProduct();
		if (product != null) {
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
		while (tokens.hasMoreTokens())
			array.add(tokens.nextToken().trim());
		return (String[]) array.toArray(new String[array.size()]);
	}

	protected abstract Control createDialogArea(Composite parent);
	
	protected abstract void createButtonsForButtonBar(Composite parent);
	
	/**
	 * Helper method para presentar un error durante la ejecución del dialog. El
	 * título de la ventana siempre es "Error en la aplicación".
	 * @param shell shell
	 * @param e Exception
	 */
	protected void mensajeError(Shell shell, Exception e) {
		MessageDialog.openError(shell, "Error en la aplicación", "Error: " + e.toString() + "\n\nStack trace: " + e.getStackTrace()[0]);
	}
	
	/**
	 * Helper method para presentar un error durante la ejecución del dialog
	 * @param shell shell
	 * @param titulo título que se quiere presentar, se agrega al texto "Error en
	 * la aplicación:".
	 * @param e Exception
	 */
	protected void mensajeError(Shell shell, String titulo, Exception e) {
		MessageDialog.openError(shell, "Error en la aplicación: " + titulo, "Error: " + e.toString() + "\n\nStack trace: " + e.getStackTrace()[0]);
	}
	
	public Double txt2Currency(String valorCampo) {
		return formUtils.txt2Currency(valorCampo);
	}
	
	public Float txt2Float(String valorCampo) {
		return formUtils.txt2Float(valorCampo);
	}
	
	public Long txt2Long(String valorCampo) {
		return formUtils.txt2Long(valorCampo);
	}

	public Integer txt2Integer(String valorCampo) {
		return formUtils.txt2Integer(valorCampo);
	}

	public String valor2Txt(Object valorCampo, String formato) {
		return formUtils.valor2Txt(valorCampo, formato);
	}

	public String valor2Txt(Object valorCampo) {
		return formUtils.valor2Txt(valorCampo);
	}
	
	public boolean valor2Bool(Boolean valorCampo) {
		return formUtils.valor2Bool(valorCampo);
	}
	
	public SelectionAdapter crearCalendario(Shell shell, Text txtCampo) {
		return formUtils.crearCalendario(shell, txtCampo);
	}
	
	public SelectionAdapter crearCalendario(Shell shell, Text txtCampo, Text txtFechaDefault) {
		return formUtils.crearCalendario(shell, txtCampo, txtFechaDefault);
	}
	
	public KeyAdapter crearKeyAdapter(Text txtCampo) {
		return formUtils.crearKeyAdapter(txtCampo);
	}
}
