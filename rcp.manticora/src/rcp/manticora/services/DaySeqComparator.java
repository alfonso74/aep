package rcp.manticora.services;


import org.eclipse.jface.viewers.ViewerComparator;

import rcp.manticora.model.LineaTemplate;
import rcp.manticora.model.LineaTour;

public class DaySeqComparator extends ViewerComparator {
	
	public DaySeqComparator() {
		super();
	}

	@Override
	public int category(Object element) {
		int valor = 0;
		if (element instanceof LineaTemplate) {
			LineaTemplate linea = (LineaTemplate) element;
			valor = linea.getDia().intValue() * 1000 + linea.getSecuencia().intValue();
		} else if (element instanceof LineaTour){
			LineaTour linea = (LineaTour) element;
			valor = linea.getDia().intValue() * 1000 + linea.getSecuencia().intValue();
		}
		return valor;
	}
	
}
