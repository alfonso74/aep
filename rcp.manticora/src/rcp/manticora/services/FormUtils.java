package rcp.manticora.services;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Date;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/*
 * Métodos para facilitar la presentación de datos en un formulario y el envío de
 * datos hacia la base de datos
 */

public class FormUtils {

	public FormUtils() {
	}
	
	public Double txt2Currency(String valorCampo) {
		NumberFormat nf = NumberFormat.getInstance();
		DecimalFormat df = (DecimalFormat) nf;
		//df.setMaximumFractionDigits(2);
		df.applyPattern("#,##0.##");
		Double valorFloat = null;
		try {
			valorFloat = (valorCampo.equals("") ? null : df.parse(valorCampo).doubleValue());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return valorFloat;
	}
	
	public Long txt2Long(String valorCampo) {
		Long valorLong = (valorCampo.equals("") ? null : Long.valueOf(valorCampo));
		return valorLong;
	}
	
	public Integer txt2Integer(String valorCampo) {
		Integer valorInt = (valorCampo.equals("") ? null : Integer.valueOf(valorCampo));
		return valorInt;
	}
	
// usualmente utilizado cuando se va a guardar el registro en la DB
	public Float txt2Float(String valorCampo) {
		NumberFormat nf = NumberFormat.getInstance();
		Float valorFloat = null;
// utilizamos NumberFormat para interpretar correctamente los separadores de miles (,)
// un simple Float.valueOf() falla al encontrar las comas separadoras de miles
		try {
			valorFloat = (valorCampo.equals("") ? null : nf.parse(valorCampo).floatValue());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		//Float valorFloat = (valorCampo.equals("") ? null : Float.valueOf(valorCampo));
		return valorFloat;
	}
	
// usado para presentar los valores en los formularios
	public String valor2Txt(Object valorCampo) {
		String valorTxt = (valorCampo == null ? "" : valorCampo.toString());
		return valorTxt;
	}
	
	/** presentación de valores numéricos con formato en los formularios
	 * 
	 * @param valorCampo
	 * @param formato ejm: "0.00", "#,##0.00"
	 * @return
	 */
	public String valor2Txt(Object valorCampo, String formato) {
		/*
		if (valorCampo != null) {
			NumberFormat nf = NumberFormat.getInstance();
			System.out.println("Número cargado: " + nf.format(valorCampo));
		}
		*/
		// formato usualmente se especifica para new DecimalFormat(".00")
		String valorTxt = (valorCampo == null ? "" : new DecimalFormat(formato).format(valorCampo));
		return valorTxt;
	}
	
	/**
	 * Manejo de campos tipo Boolean donde puede existir null.  Si el valor
	 * es null entonces retorna false, sino retorna booleanValue() 
	 * @param valorCampo
	 * @return true o false
	 */
	public boolean valor2Bool(Boolean valorCampo) {
		return (valorCampo == null ? false : valorCampo.booleanValue()); 
	}
	
	/**
	 * Genera un calendario para asignar gráficamente una fecha.  Utiliza como
	 * default la fecha que haya sido introducida en el parámetro txtCampo
	 * Si no se ha introducido ninguna fecha, el calendario selecciona la
	 * fecha actual.
	 */
	public SelectionAdapter crearCalendario(Shell shell, Text txtCampo) {
		return new Calendario(shell, txtCampo);
	}
	
	/**
	 * Genera un calendario para asignar gráficamente una fecha.  Utiliza como
	 * default la fecha que haya sido introducida en el parámetro txtCampo o
	 * la fecha indicada en txtFechaDefault.
	 * Si no se ha introducido ninguna fecha, el calendario selecciona la
	 * fecha actual.
	 */
	public SelectionAdapter crearCalendario(Shell shell, Text txtCampo, Text txtFechaDefault) {
		return new Calendario(shell, txtCampo, txtFechaDefault);
	}
	
	/**
	 * Crea un KeyAdapter que responde a las flechas de arriba y abajo para
	 * incrementar/decrementar la fecha.  En caso de no haber una fecha, se
	 * utiliza el día de hoy como default 
	 * @param campo el campo donde se especifica la fecha
	 */
	public KeyAdapter crearKeyAdapter(final Text campo) {
		return new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (!campo.getText().equals("")) {
					String nuevaFecha;
					switch(e.keyCode) {
					case SWT.ARROW_UP:
						System.out.println("Arriba");
						nuevaFecha = FechaUtil.ajustarFecha(campo.getText(), 1);
						campo.setText(nuevaFecha);
						break;
					case SWT.ARROW_DOWN:
						System.out.println("Abajo");
						nuevaFecha = FechaUtil.ajustarFecha(campo.getText(), -1);
						campo.setText(nuevaFecha);
						break;
					default: System.out.println(e.character);
					}
				} else {
					campo.setText(FechaUtil.toString(new Date()));
				}
			}
		};
	}
	
	
	public Integer checkNull(Integer valorCampo) {
		return valorCampo == null ? 0 : valorCampo;
	}
	
	public Float checkNull(Float valorCampo) {
		return valorCampo == null ? 0 : valorCampo;
	}
	
	public String checkNull(String valorCampo) {
		return valorCampo == null ? "" : valorCampo;
	}
	
	public boolean checkNull(Boolean valorCampo) {
		return valorCampo == null ? false : valorCampo.booleanValue();
	}
}
