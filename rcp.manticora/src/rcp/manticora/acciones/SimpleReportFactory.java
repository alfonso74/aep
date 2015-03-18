package rcp.manticora.acciones;

import org.eclipse.jface.action.IAction;
import org.eclipse.ui.IWorkbenchWindow;

/**
 * Clase usada por el ApplicationActionBarAdvisor.java para crear los reportes.
 * @author Carlos Alfonso
 *
 */
public class SimpleReportFactory {
	
	public IAction createAction(String nombreAccion, IWorkbenchWindow window) {
		GenericAction action = new GenericAction(nombreAccion, window);
		if (nombreAccion.equals("Productos")) {
			action.setTituloReporte("Listado de Productos");
			action.setRutaReporte("reports/prodPorCodigo.rptdesign");
		} else if (nombreAccion.equals("Cotizaciones x Mes")) {
			action.setTituloReporte("Cotizaciones x Mes");
			action.setRutaReporte("reports/cotPorMes.rptdesign");
		} else if (nombreAccion.equals("Cotizaciones x Mes de Inicio")) {
			action.setTituloReporte("Cotizaciones x Mes de Inicio");
			action.setRutaReporte("reports/cotPorMesInicio.rptdesign");
		} else if (nombreAccion.equals("Cotizaciones x Mes de Gira")) {
			action.setTituloReporte("Cotizaciones x Mes de Gira");
			action.setRutaReporte("reports/cotPorMesGira.rptdesign");
		} else if (nombreAccion.equals("Cotizaciones x Vendedor")) {
			action.setTituloReporte("Cotizaciones x Vendedor");
			action.setRutaReporte("reports/cotPorVendedor.rptdesign");
		} else if (nombreAccion.equals("Cotizaciones x Red de Viajes")) {
			action.setTituloReporte("Cotizaciones x Red de Viajes");
			action.setRutaReporte("reports/cotPorRedViajes.rptdesign");
		} else if (nombreAccion.equals("Cotizaciones con Comisiones")) {
			action.setTituloReporte("Cotizaciones con Comisiones");
			action.setRutaReporte("reports/cotComisiones.rptdesign");	
		} else if (nombreAccion.equals("Paxs x Mes")) {
			action.setTituloReporte("Paxs x Mes");
			action.setRutaReporte("reports/paxsPorMesTipoCliente.rptdesign");
		} else if (nombreAccion.equals("Paxs x Giras")) {
			action.setTituloReporte("Paxs x Giras");
			action.setRutaReporte("reports/paxsPorGira.rptdesign");
		} else if (nombreAccion.equals("Ventas por Vendedor")) {
			action.setTituloReporte("Ventas por Vendedor");
			action.setRutaReporte("reports/ventasPorVendedor.rptdesign");
		} else if (nombreAccion.equals("Comparativo de Ventas Anuales")) {
			action.setTituloReporte("Comparativo de Ventas Anuales");
			action.setRutaReporte("reports/comparativoVentasAnuales.rptdesign");
		} else {
			System.err.println("No se encontró el reporte con nombre: " + nombreAccion);
		}
		return action;
	}

}
