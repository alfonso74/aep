package rcp.manticora.services;

import java.text.Collator;


import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;

import rcp.manticora.model.LineaTemplate;

public class DaySeqSorterXXX extends ViewerSorter {
	
	public DaySeqSorterXXX() {
		super();
	}
	
	public DaySeqSorterXXX(Collator collator) {
		super(collator);
	}
	

	@Override
	public int category(Object element) {
		LineaTemplate linea = (LineaTemplate) element;
		int valor = linea.getDia().intValue() * 1000 + linea.getSecuencia().intValue();
		return valor;
	}

	@Override
	public int compare(Viewer viewer, Object e1, Object e2) {
		int cat1 = category(e1);
        int cat2 = category(e2);

        return cat1 - cat2;
	}
}
