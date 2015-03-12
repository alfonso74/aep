package rcp.manticora.services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class FechaUtil {
	// formatoFecha y FechaHora son public para ser utilizados como argumentos
	// al llamar a FechaUtil.  Por ejemplo:  FechaUtil.toString(fecha, FechaUtil.formatoFecha);
	public static String formatoFecha = "dd-MM-yyyy";
	public static String formatoHora = "hh:mm a";
	public static String formatoFechaHora = "dd-MM-yyyy hh:mm a";
	private static SimpleDateFormat formatter = new SimpleDateFormat(formatoFecha);
	
	public FechaUtil() {
	}

	
	/**
	 * Retorna el milisegundo de la hora actual.  Es utilizado para ayudar a separar
	 * los identificadores de sesiones generadas por un editor 
	 */
	public static String getMilisegundos() {
		String formatoSegundo = "S";
		formatter.applyPattern(formatoSegundo);
		return formatter.format(new Date());
	}
	
	public static String toString(Date fecha) {
		if (fecha != null) {
			formatter.applyPattern(formatoFecha);
			return formatter.format(fecha);
		} else {
			return "";
		}
	}
	
	public static String toString(Date fecha, String formato) {
		formatter.applyPattern(formato);
		if (fecha != null) {
			String fechaTxt = formatter.format(fecha);
			return fechaTxt;
		} else {
			return "";
		}
	}
	
	public static Date toDate(String fecha) {
		if (fecha != null && fecha != "") {
			try {
				formatter.applyPattern(formatoFecha);
				return formatter.parse(fecha);
			} catch (ParseException e) {
				System.err.println("Error durante transformación de fechas: " + e);
				return null;
			}
		} else {
			return null;
		}
	}
	
	public static Date toHour(String fecha) {
		if (fecha != null && fecha != "") {
			try {
				formatter.applyPattern(formatoHora);
				return formatter.parse(fecha);
			} catch (ParseException e) {
				System.err.println("Error durante transformación de hora: " + e);
				return null;
			}
		} else {
			return null;
		}
	}
	
	public static Date toDateHour(String fecha) {
		if (fecha != null && fecha != "") {
			try {
				formatter.applyPattern(formatoFechaHora);
				return formatter.parse(fecha);
			} catch (ParseException e) {
				System.err.println("Error durante transformación de fecha-hora: " + e);
				return null;
			}
		} else {
			return null;
		}
	}
	
	public static String ajustarFecha(String fechaTxt, int dias) {
		Calendar calendar = Calendar.getInstance();
		Date fecha = FechaUtil.toDate(fechaTxt);
		long timeInMillis = fecha.getTime();
		calendar.setTimeInMillis(timeInMillis);
		calendar.add(Calendar.DATE, dias);
		fecha = calendar.getTime();
		String fechaAjustada = FechaUtil.toString(fecha);
		return fechaAjustada;
	}
	
	public static Date ajustarFecha(Date fecha, int dias) {
		Calendar calendar = Calendar.getInstance();
		long timeInMillis = fecha.getTime();
		calendar.setTimeInMillis(timeInMillis);
		calendar.add(Calendar.DATE, dias);
		Date fechaAjustada = calendar.getTime();
		return fechaAjustada;
	}
	
	public static int getDiaSemana(Date fecha) {
		Calendar calendar = Calendar.getInstance();
		long timeInMillis = fecha.getTime();
		calendar.setTimeInMillis(timeInMillis);
		int diaSemana = calendar.get(Calendar.DAY_OF_WEEK);
		return diaSemana;
	}

}
