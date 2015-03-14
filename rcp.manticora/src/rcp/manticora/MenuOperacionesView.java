package rcp.manticora;

import rcp.manticora.views.ClientesComView;
import rcp.manticora.views.ClientesView;
import rcp.manticora.views.HabitacionesView;
import rcp.manticora.views.HojasEnProcesoView;
import rcp.manticora.views.HojasFinalizadasView;
import rcp.manticora.views.HojasOpsNuevasView;
import rcp.manticora.views.HojasView;
import rcp.manticora.views.ProductosView;
import rcp.manticora.views.TemplatesView;
import rcp.manticora.views.TipoHabitacionesView;
import rcp.manticora.views.ToursView;

public class MenuOperacionesView extends AbstractMenuView {
	
	public static final String ID = "manticora.menuOperacionesView";
	TreeParent p1 = null;
	TreeObject elementoDefault = null;

	@Override
	protected TreeObject crearMenu() {
		p1 = new TreeParent("Consultas");
		p1.addChild(new TreeObject("Productos", ProductosView.ID, IImageKeys.CONFIG));
		p1.addChild(new TreeObject("Clientes", ClientesView.ID, IImageKeys.CONFIG));
		p1.addChild(new TreeObject("Compañías", ClientesComView.ID, IImageKeys.CONFIG));
		p1.addChild(new TreeObject("Tipos de habit.", TipoHabitacionesView.ID, IImageKeys.CONFIG));
		p1.addChild(new TreeObject("Habitaciones", HabitacionesView.ID, IImageKeys.CONFIG));
		
		TreeParent p2 = new TreeParent("Hojas de servicio");
		elementoDefault = new TreeObject("Nuevas", HojasOpsNuevasView.ID, "icons/menuOperaciones.gif");
		p2.addChild(elementoDefault);
		p2.addChild(new TreeObject("En proceso", HojasEnProcesoView.ID, "icons/menuOperaciones.gif"));
		p2.addChild(new TreeObject("Finalizadas", HojasFinalizadasView.ID, "icons/menuOperaciones.gif"));
		
		TreeParent p4 = new TreeParent("Operaciones");
		p4.addChild(new TreeObject("Templates", TemplatesView.ID, "icons/menuOperaciones.gif"));
		p4.addChild(new TreeObject("Hojas de servicio", HojasView.ID, "icons/menuOperaciones.gif"));
		p4.addChild(new TreeObject("Tours", ToursView.ID, "icons/menuOperaciones.gif"));
		
		TreeParent p5 = new TreeParent("Reservas");
		p5.addChild(new TreeObject("Guías", "", "icons/menuReservas.gif"));
		p5.addChild(new TreeObject("Transporte", "", "icons/menuReservas.gif"));
		p5.addChild(new TreeObject("Hospedaje", "", "icons/menuReservas.gif"));
		p5.addChild(new TreeObject("Alimentación", "", "icons/menuReservas.gif"));
		
		TreeParent root = new TreeParent("");
		root.addChild(p1);
		root.addChild(new TreeObject(""));
		root.addChild(p2);
		//root.addChild(new TreeObject(""));
		//root.addChild(p3);
		root.addChild(new TreeObject(""));
		root.addChild(p4);
		root.addChild(new TreeObject(""));
		root.addChild(p5);
		
		return root;
	}
	
	
	
	@Override
	protected void expandirMenu() {
		getTreeViewer().expandToLevel(p1, 1);
		getTreeViewer().expandToLevel(elementoDefault, 1);
	}
	

}
