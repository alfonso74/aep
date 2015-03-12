package rcp.manticora;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import rcp.manticora.views.CotizacionesContabilidadView;

public class ContabilidadPerspective implements IPerspectiveFactory {
	public static final String ID = "rcp.manticora.contabilidadPerspective";

	public void createInitialLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(true);
		layout.addStandaloneView(MenuContabilidadView.ID, false, IPageLayout.LEFT, 0.2f, editorArea);
		layout.addView(CotizacionesContabilidadView.ID, IPageLayout.BOTTOM, 0.65f, editorArea);
	}

}
