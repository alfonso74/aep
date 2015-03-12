package rcp.manticora;


import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import rcp.manticora.views.CotizacionesActivasView;

public class VentasPerspective implements IPerspectiveFactory {
	public static final String ID = "manticora.ventasPerspective";

	public void createInitialLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(true);
		layout.addStandaloneView(MenuVentasView.ID, false, IPageLayout.LEFT, 0.2f, editorArea);
		layout.addView(CotizacionesActivasView.ID, IPageLayout.BOTTOM, 0.65f, editorArea);
		/*
		IFolderLayout folder = layout.createFolder("folder1", IPageLayout.BOTTOM, 0.65f, editorArea);
		folder.addPlaceholder(SolicitudesView.ID + ":*");
		folder.addView(ProductosView.ID);
		*/
	}

}
