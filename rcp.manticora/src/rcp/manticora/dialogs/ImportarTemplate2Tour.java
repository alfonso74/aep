package rcp.manticora.dialogs;


import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;

import rcp.manticora.controllers.ComboDataController;
import rcp.manticora.services.ComboData;

public class ImportarTemplate2Tour extends AbstractAEPDialog {
	private ComboDataController cdController;
	
	private List listaTemplates;
	
	private Long idTemplate;
	
	private ComboData cdTemplates;
	
	public ImportarTemplate2Tour(Shell parentShell, String titulo) {
		super(parentShell, titulo);
		cdController = new ComboDataController();
	}

	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		createButton(parent, IDialogConstants.CANCEL_ID, "Cancelar", false);
	}

	protected Control createDialogArea(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(2, false);
		composite.setLayout(layout);
		
		Label l;
		GridData gridData;
		
//	texto de leyenda y separador
		l = new Label(composite, SWT.NONE);
		l.setText("Por favor, seleccione el template a utilizar:");
		gridData = new GridData();
		gridData.horizontalSpan = 2;
		l.setLayoutData(gridData);
		l = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
		gridData = new GridData(GridData.FILL, GridData.CENTER, false, false);
		gridData.heightHint = 5;
		gridData.horizontalSpan = 2;
		l.setLayoutData(gridData);
		
// listado de templates para selección del usuario final
		cdTemplates = cdController.getComboDataTemplates();
		listaTemplates = new List(composite, SWT.SINGLE | SWT.BORDER | SWT.V_SCROLL);
		listaTemplates.setItems(cdTemplates.getTexto());
		gridData = new GridData();
		gridData.widthHint = 250;
		gridData.heightHint = 65;
		gridData.verticalSpan = 2;
		listaTemplates.setLayoutData(gridData);
		
		return composite;
	}
	
	protected void okPressed() {
		//codigoTemplate = cdTemplates.getCodeByIndex(listaTemplates.getSelectionIndex());
		idTemplate = cdTemplates.getKeyAsLongByIndex(listaTemplates.getSelectionIndex());
		super.okPressed();
	}

	public Long getIdTemplate() {
		return idTemplate;
	}
}

