package rcp.manticora;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import rcp.manticora.views.ProductosView;

public class AdminPerspective implements IPerspectiveFactory {
	public static final String ID = "manticora.adminPerspective";

	public void createInitialLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(true);
		layout.addStandaloneView(NavigationView5.ID, false, IPageLayout.LEFT, 0.2f, editorArea);
		layout.addView(ProductosView.ID, IPageLayout.BOTTOM, 0.65f, editorArea);
	}

}
