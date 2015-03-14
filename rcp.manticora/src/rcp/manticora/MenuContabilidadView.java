package rcp.manticora;

import rcp.manticora.views.ClientesComView;
import rcp.manticora.views.ClientesView;
import rcp.manticora.views.CotizacionesContabilidadView;
import rcp.manticora.views.CotizacionesParcialView;
import rcp.manticora.views.CotizacionesRegistradasView;
import rcp.manticora.views.ProductosView;


public class MenuContabilidadView extends AbstractMenuView {
	
	public static final String ID = "rcp.manticora.menuContabilidadView";
	TreeParent p1 = null;
	
	@Override
	protected TreeObject crearMenu() {
		p1 = new TreeParent("Consultas");
		p1.addChild(new TreeObject("Productos", ProductosView.ID, IImageKeys.CONFIG));
		p1.addChild(new TreeObject("Clientes", ClientesView.ID, IImageKeys.CONFIG));
		p1.addChild(new TreeObject("Compañías", ClientesComView.ID, IImageKeys.CONFIG));
		
		TreeParent p2 = new TreeParent("Cotizaciones");
		p2.addChild(new TreeObject("Todas", CotizacionesContabilidadView.ID, "icons/menuVentas.gif"));
		p2.addChild(new TreeObject("Registradas", CotizacionesRegistradasView.ID, "icons/menuVentas.gif"));
		p2.addChild(new TreeObject("Pago parcial", CotizacionesParcialView.ID, "icons/menuVentas.gif"));
		
		TreeParent root = new TreeParent("");
		root.addChild(p1);
		root.addChild(new TreeObject(""));
		root.addChild(p2);
		
		return root;
	}


	/*
	@Override
	protected void expandirMenu() {
		getTreeViewer().expandToLevel(p1, 1);
	}
	*/
	
}

