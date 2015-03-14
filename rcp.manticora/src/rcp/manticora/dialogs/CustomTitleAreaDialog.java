package rcp.manticora.dialogs;


import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class CustomTitleAreaDialog extends TitleAreaDialog {

	public CustomTitleAreaDialog(Shell parentShell) {
		super(parentShell);
	}
	
	protected Control createContents(Composite parent) {
		Control contents = super.createContents(parent);

	    // Set the title
	    setTitle("Definición de cliente");

	    // Set the message
	    setMessage("Por favor, introduzca los detalles del cliente", IMessageProvider.INFORMATION);

	    // Set the image
	    //if (image != null) setTitleImage(image);

	    return contents;
	  }

	  /**
	   * Creates the gray area
	   * 
	   * @param parent the parent composite
	   * @return Control
	   */
	  protected Control createDialogArea(Composite parent) {
	    Composite composite = (Composite) super.createDialogArea(parent);
	    
	    GridLayout layout = new GridLayout(4, false);
	    Group grupoTop = new Group(composite, SWT.NONE);
	    grupoTop.setLayout(layout);
	    grupoTop.setLayoutData(new GridData(GridData.FILL_BOTH));
	   
	    //GridLayout layout = new GridLayout(2, false);
	    //composite.setLayout(layout);
	    
	    Label l;
	    GridData gridData;
	    
	    l = new Label(grupoTop, SWT.NONE);
	    l.setText("Nombre:");
	    Text txtNombre = new Text(grupoTop, SWT.BORDER);
	    txtNombre.setLayoutData(new GridData(120,15));
	    
	    l = new Label(grupoTop, SWT.NONE);
	    l.setText("Apellido:");
	    Text txtApellido = new Text(grupoTop, SWT.BORDER);
	    txtApellido.setLayoutData(new GridData(120,15));
	    
	    l = new Label(grupoTop, SWT.NONE);
	    l.setText("Identificación:");
	    Text txtIdent = new Text(grupoTop, SWT.BORDER);
	    gridData = new GridData(80,15);
	    gridData.horizontalSpan = 3;
	    txtIdent.setLayoutData(gridData);
	    
	    l = new Label(grupoTop, SWT.NONE);
	    l.setText("País:");
	    Text txtPais = new Text(grupoTop, SWT.BORDER);
	    gridData = new GridData(80,15);
	    gridData.horizontalSpan = 3;
	    txtPais.setLayoutData(gridData);
	    
	    
	    
	    /*Label l = new Label(grupoTop, SWT.None);
	    l.setText("Recoger en:");
	    Text txtRecoger = new Text(grupoTop, SWT.BORDER);
	    txtRecoger.setLayoutData(new GridData(120, 15));
	    
	    l = new Label(grupoTop, SWT.None);
	    l.setText("Hora:");
	    Text txtHora = new Text(grupoTop, SWT.BORDER);
	    txtHora.setLayoutData(new GridData(50, 15));*/
	    
	/*
	    // Create a table
	    Table table = new Table(composite, SWT.FULL_SELECTION | SWT.BORDER);
	    table.setLayoutData(new GridData(GridData.FILL_BOTH));

	    // Create two columns and show
	    TableColumn one = new TableColumn(table, SWT.LEFT);
	    one.setText("Real Name");

	    TableColumn two = new TableColumn(table, SWT.LEFT);
	    two.setText("Preferred Name");

	    table.setHeaderVisible(true);

	    // Add some data
	    TableItem item = new TableItem(table, SWT.NONE);
	    item.setText(0, "Robert Harris");
	    item.setText(1, "Bobby");

	    item = new TableItem(table, SWT.NONE);
	    item.setText(0, "Robert Warner");
	    item.setText(1, "Rob");

	    item = new TableItem(table, SWT.NONE);
	    item.setText(0, "Gabor Liptak");
	    item.setText(1, "Gabor");

	    one.pack();
	    two.pack();
	*/
	    return composite;
	  }

	  /**
	   * Creates the buttons for the button bar
	   * 
	   * @param parent the parent composite
	   */
	  protected void createButtonsForButtonBar(Composite parent) {
	    createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
	  }
}
