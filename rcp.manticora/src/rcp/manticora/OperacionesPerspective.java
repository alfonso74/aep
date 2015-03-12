package rcp.manticora;


import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import rcp.manticora.views.HojasOpsNuevasView;

public class OperacionesPerspective implements IPerspectiveFactory {
	public static final String ID = "manticora.operacionesPerspective";

	public void createInitialLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(true);
		layout.addStandaloneView(MenuOperacionesView.ID, false, IPageLayout.LEFT, 0.2f, editorArea);
		layout.addView(HojasOpsNuevasView.ID, IPageLayout.BOTTOM, 0.65f, editorArea);
	}

}
