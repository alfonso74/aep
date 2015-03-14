package rcp.manticora.views;


import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.ViewPart;

import rcp.manticora.editors.CommonEditorInput;

/**
 * Implementa un setFocus() y un hookDoubleClickListener() por default
 *
 */
public abstract class AbstractViewH extends ViewPart implements IRefreshView {

	
	@Override
	public void setFocus() {
	}
	
	/**
	 * Agrega a un viewer la capacidad de abrir un registro al dar un doble clic.
	 * @param viewer Viewer al que se agrega el listener
	 * @param editorId ID del editor que será utilizado para editar el registro
	 */
	protected void hookDoubleClickListener(final StructuredViewer viewer, final String editorId) {
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				System.out.println("Double click  :)");
				Object seleccion = ((StructuredSelection) viewer.getSelection()).getFirstElement();
				try {
					CommonEditorInput input = new CommonEditorInput(seleccion);
					getSite().getPage().openEditor(input, editorId);
				} catch(PartInitException e) {
					e.printStackTrace();
				}
			}
		});
	}

}
