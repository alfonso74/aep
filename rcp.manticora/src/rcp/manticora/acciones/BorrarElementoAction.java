package rcp.manticora.acciones;


import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.hibernate.Session;

import rcp.manticora.controllers.CommonController;
import rcp.manticora.dao.CommonDAO;
import rcp.manticora.dao.CommonDAO2;
import rcp.manticora.services.HibernateUtil;
import rcp.manticora.views.IRefreshView;

public class BorrarElementoAction implements IViewActionDelegate {
	private Object seleccion;
	private CommonDAO daoController;
	private CommonDAO2 daoController2;
	private CommonController daoController3;
	private IRefreshView v;
	private Shell shell;

	public void init(IViewPart view) {
		daoController = new CommonDAO();
		daoController2 = new CommonDAO2();
		daoController3 = new CommonController();
		shell = view.getSite().getShell();
		v = (IRefreshView) view;
	}

	public void run(IAction action) {
		if (seleccion == null) {
			MessageDialog.openInformation(shell, "Aviso", "No ha seleccionado ningún registro.");
		} else {
			boolean respuesta = MessageDialog.openConfirm(shell, "Confirmación", "Desea borrar el registro seleccionado?");
			if (respuesta) {
				System.out.println("Borrando elemento seleccionado..." + seleccion);
				if (daoController != null) {
					/*
					Session s = HibernateUtil.currentSession();
					s.beginTransaction(); 
					daoController.setSession(s);
					daoController.makeTransient(seleccion);
					daoController.commit();
					HibernateUtil.closeSession();
					*/
					//daoController.doDelete(seleccion);
					//daoController2.doDelete(seleccion);
					daoController3.doDelete(seleccion);
					v.refrescar();
				} else {
					System.out.println("daoController es NULL, no se pudo borrar el registro");
				}		
			} else {
				MessageDialog.openInformation(shell, "Información", "La acción ha sido cancelada.");
			}
		}
	}

	public void selectionChanged(IAction action, ISelection selection) {
		seleccion = ((StructuredSelection) selection).getFirstElement();
	}

}
