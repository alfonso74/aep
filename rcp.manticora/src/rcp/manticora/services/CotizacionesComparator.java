package rcp.manticora.services;

import java.util.Comparator;

import rcp.manticora.model.LineaCotizacion;


public class CotizacionesComparator implements Comparator<LineaCotizacion> {

	public int compare(LineaCotizacion arg0, LineaCotizacion arg1) {
		//System.out.println("Usando Comparator(): " + arg0 + "  vs  " + arg1);
		int fecha = arg0.getFecha().compareTo(arg1.getFecha());
		//int fecha = arg0.getFecha().getTime() - arg1.getFecha().getTime();
		if (fecha == 0) {
			int seq0 = arg0.getSecuencia().intValue();
			int seq1 = arg1.getSecuencia().intValue();
			// para reenumerar en Ancon
			//int seq0 = arg0.getIdDetalle().intValue();
			//int seq1 = arg1.getIdDetalle().intValue();
			if (seq0 > seq1) {
				return 1;
			} else if (seq0 < seq1) {
				return -1;
			} else {
				return 0;
			}
		} else {
			return fecha;
		}
	}
}
