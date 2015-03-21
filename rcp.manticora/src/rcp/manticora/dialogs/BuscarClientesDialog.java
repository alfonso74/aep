package rcp.manticora.dialogs;

import java.util.List;
import java.util.Vector;


import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import rcp.manticora.controllers.ClientesController;
import rcp.manticora.model.Cliente;
import rcp.manticora.model.ClienteJuridico;
import rcp.manticora.model.ICliente;
import rcp.manticora.services.HibernateUtil;

public class BuscarClientesDialog extends TitleAreaDialog {
	private static String ID = "BuscarClientesDialog";
	private Session session;
	//private Shell shell;
	
	private Combo comboTipo;
	private Combo comboCampos;
	private Text txtCadena;
	private Label lContador;
	private List<Cliente> lista;
	private Vector<Cliente> listadoClientes;
	private Object seleccion = null;
	private Cliente cliente;
	private List<Cliente> clienteSeleccionado;
	private String nombreDefault;
	
	private int contador;
	private Button bBuscar;
	
	private Table tabla;
	private TableViewer viewer;
	
	private ClientesController controller;
	

	public BuscarClientesDialog(Shell parentShell, List<Cliente> clienteSeleccionado) {
		super(parentShell);
		//this.shell = parentShell;
		// no queremos preservar el valor de cliente, sino preservar los cambios
		// que se hagan en el dialog
		this.clienteSeleccionado = clienteSeleccionado;
		session = HibernateUtil.getSessionFactory().openSession();
		listadoClientes = new Vector<Cliente>();
		controller = new ClientesController(ID);
	}
	
	protected Control createContents(Composite parent) {
		Control contents = super.createContents(parent);
		// Set the title
		setTitle("Búsqueda de cliente existente");
		// Set the message
		setMessage("Por favor, introduzca los criterios de búsqueda", IMessageProvider.INFORMATION);
		return contents;
	}
	
	protected Control createDialogArea(Composite parent) {
		Composite composite = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout(5, false);
		layout.marginWidth = 15;
		layout.marginHeight = 10;
		layout.verticalSpacing = 8;
		composite.setLayout(layout);
		
		Label l;
		GridData gridData;
		GridLayout gridLayout;
		
		
		l = new Label(composite, SWT.NONE);
		l.setText("Tipo:");
		comboTipo = new Combo(composite, SWT.READ_ONLY);
		comboTipo.setItems(new String[] {"Persona", "Compañía"});
		comboTipo.setLayoutData(new GridData(60,15));
		comboTipo.select(0);
		comboTipo.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				String pTipo = comboTipo.getText();
				if (pTipo.equals("Compañía")) {
					comboCampos.setItems(new String[] {"Nombre"});
					comboCampos.select(0);
				} else {
					comboCampos.setItems(new String[] {"Nombre", "Apellido", "Identificación"});
				}
			}
		});
		
		l = new Label(composite, SWT.NONE);
		gridData = new GridData(70,15);
		//gridData.horizontalAlignment = SWT.RIGHT;
		l.setLayoutData(gridData);
		l.setAlignment(SWT.RIGHT);
		l.setText("Buscar por:");
		comboCampos = new Combo(composite, SWT.READ_ONLY);
		comboCampos.setItems(new String[] {"Nombre", "Apellido", "Identificación"});
		gridData = new GridData(100,15);
		gridData.horizontalSpan = 2;
		comboCampos.setLayoutData(gridData);

		l = new Label(composite, SWT.NONE);
		l.setText("Texto:");
		txtCadena = new Text(composite, SWT.BORDER);
		gridData = new GridData(200,15);
		gridData.horizontalSpan = 3;
		txtCadena.setLayoutData(gridData);
		
		bBuscar = new Button(composite, SWT.NONE);
	    gridData = new GridData(50,19);
		gridData.horizontalSpan = 1;
	    bBuscar.setLayoutData(gridData);
	    bBuscar.setText("Buscar");
		bBuscar.addSelectionListener(new SelectionAdapter() {
			@SuppressWarnings("unchecked")
			public void widgetSelected(SelectionEvent e) {
				System.out.println("Botón de buscar cliente...");
				lista = buscarCliente(comboTipo.getText());
				listadoClientes.removeAllElements();
				listadoClientes.addAll(lista);
				contador = listadoClientes.size();
				lContador.setText(contador + " registro(s) encontrado(s)");
				//viewer.setInput(listadoClientes);
				viewer.refresh();
			}
		});
		
		Composite tablaComp = new Composite(composite, SWT.NONE);
		gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		gridLayout.marginWidth = 0;
		tablaComp.setLayout(gridLayout);
		gridData = new GridData(SWT.FILL, SWT.FILL, false, false);
		gridData.horizontalSpan = 5;
		tablaComp.setLayoutData(gridData);
		
		crearTabla(tablaComp);
		viewer = new TableViewer(tabla);
		viewer.setContentProvider(new ViewContentProvider());
		viewer.setLabelProvider(new ViewLabelProvider());
		viewer.setInput(listadoClientes);
		viewer.addSelectionChangedListener(this.createSelectionChangedListener());
		
		lContador = new Label(tablaComp, SWT.NONE);
		lContador.setLayoutData(new GridData(150,15));
		lContador.setText("Registros");
		
		if (nombreDefault != null) {
			comboCampos.select(0);
			txtCadena.setText(nombreDefault);
		}
		
		return composite;
	}
	
	private ISelectionChangedListener createSelectionChangedListener() {
		return new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				seleccion = ((IStructuredSelection) event.getSelection()).getFirstElement();
				System.out.println("Cambio de selección");
			}
		};
	}
	
	public boolean close() {
		if (getReturnCode() == IDialogConstants.OK_ID) {
			cliente = getClienteSeleccionado();
			clienteSeleccionado.add(cliente);
			System.out.println("Cliente seleccionado: " + cliente.getNombreCliente());
		};
		controller.finalizar(ID);
		return super.close();
	}
	
	private List buscarCliente(String tipoCliente) {
		if (tipoCliente.equals("Compañía")) {
			return buscarClienteJuridico();
		} else {
			return buscarClienteNatural();
		}
	}
	
	private List buscarClienteNatural2() {
		String pBuscarPor = comboCampos.getText();
		String pCadena = txtCadena.getText();
		return controller.buscarClientesNaturales(pBuscarPor, pCadena);
	}
	
	private List buscarClienteJuridico2() {
		String pBuscarPor = comboCampos.getText();
		String pCadena = txtCadena.getText();
		return controller.buscarClientesJuridicos(pBuscarPor, pCadena);
	}
	
	private List buscarClienteNatural() {
		Criteria criteria = session.createCriteria(Cliente.class);
		String pCampos = comboCampos.getText();
		String pCadena = txtCadena.getText();
		if (pCampos.equals("Nombre")) {
			criteria.add(Restrictions.like("nombre", "%" + pCadena + "%"));
		}
		if (pCampos.equals("Apellido")) {
			criteria.add(Restrictions.like("apellido", "%" + pCadena + "%"));
		}
		if (pCampos.equals("Identificación")) {
			criteria.add(Restrictions.like("identificacion", "%" + pCadena + "%"));
		}
		return criteria.list();
	}
	
	private List buscarClienteJuridico() {
		Criteria criteria = session.createCriteria(ClienteJuridico.class);
		String pCampos = comboCampos.getText();
		String pCadena = txtCadena.getText();
		if (pCampos.equals("Nombre")) {
			criteria.add(Restrictions.like("nombreCia", "%" + pCadena + "%"));
		}
		return criteria.list();
	}
	
	private Cliente getClienteSeleccionado() {
		if (seleccion != null) {
			cliente = (Cliente) seleccion;
			System.out.println("Nombre de cliente2: " + cliente.getNombreCliente());
		} else {
			System.out.println("seleccion es null");
		}
		return cliente;
	}
	
	public void setNombreDefault(String nombreDefault) {
		this.nombreDefault = nombreDefault;
	}
	
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	}
	
	protected void buttonPressed(int buttonId) {
		setReturnCode(buttonId);
		close();
	}
	
	private void crearTabla(Composite parent) {
		int style = SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.HIDE_SELECTION;
		tabla = new Table(parent, style);
		tabla.setLinesVisible(true);
		tabla.setHeaderVisible(true);
		GridData gridData = new GridData(SWT.FILL, SWT.FILL, false, false);
		gridData.widthHint = 470;
		gridData.heightHint = 100;
		tabla.setLayoutData(gridData);
		
		TableColumn column = new TableColumn(tabla, SWT.RIGHT, 0);
		column.setText("Código");
		column.setWidth(50);
		column.setAlignment(SWT.RIGHT);  // aparentemente esto es ignorado
		
		column = new TableColumn(tabla, SWT.LEFT, 1);
		column.setText("Nombre");
		column.setWidth(200);
		
		column = new TableColumn(tabla, SWT.LEFT, 2);
		column.setText("ID");
		column.setWidth(100);
		
		column = new TableColumn(tabla, SWT.LEFT, 3);
		column.setText("email");
		column.setWidth(120);
	}
	
	class ViewContentProvider implements IStructuredContentProvider {

        public void inputChanged(Viewer v, Object oldInput, Object newInput) {
        	System.out.println("Cambio de INPUT");
		}
        
		public void dispose() {
		}
        
		public Object[] getElements(Object parent) {
			Vector v = (Vector) parent;
			@SuppressWarnings("unchecked")
			Object[] resultados = v.toArray(new ICliente[listadoClientes.size()]);
			System.out.println("RETORNANDO...");
			return resultados;
		}
	}
	
	class ViewLabelProvider extends LabelProvider implements ITableLabelProvider {
		public String getColumnText(Object obj, int index) {
			System.out.println("RETORNANDO2...");
			String resultado = "";
			ICliente c = (ICliente) obj;
			switch (index) {
			case 0:
				resultado = Long.toString(c.getIdCliente());
				break;
			case 1:
				resultado = c.getNombreCliente();
				break;
			case 2:
				resultado = c.getIdentificacion();
				break;
			case 3:
				resultado = c.getEmail();
				break;
			}
			return resultado;
			
		}
		public Image getColumnImage(Object obj, int index) {
			return null;		
		}
	}
	
	/*
	public TableItem getSeleccion() {
		TableItem seleccion = null;
		System.out.println("getSeleccion: " + tabla.getSelectionIndex());
		if (tabla.getSelectionIndex() != -1) {
			seleccion = tabla.getItem(tabla.getSelectionIndex());
		} else {
			seleccion = null;
		}
		return seleccion;
	}
	*/
}
