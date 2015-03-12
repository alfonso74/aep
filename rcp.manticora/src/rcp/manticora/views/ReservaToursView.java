package rcp.manticora.views;


import java.util.Date;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import rcp.manticora.controllers.ViewController;
import rcp.manticora.editors.ToursEditor;
import rcp.manticora.model.DisponibilidadTour;
import rcp.manticora.services.FechaUtil;
import rcp.manticora.services.GenericSorter;

public class ReservaToursView extends AbstractViewH implements IViewFilter {
	public static final String ID = "manticora.reservaToursView";
	private ViewController controller;
	private TableViewer viewer;
	
	private Color color;
	private boolean fechaIgual;
	
	public ReservaToursView() {
		controller = new ViewController();
	}

	
	@Override
	public void createPartControl(Composite parent) {

		color = new Color(parent.getDisplay(), 180, 200, 250);

		int style = SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.HIDE_SELECTION;
		viewer = new TableViewer(parent, style);
		viewer.setLabelProvider(new ViewLabelProvider());
		viewer.setContentProvider(new ViewContentProvider());
		crearColumnasViewer(viewer);    // debe ir luego del setLabelProv para que aplique formato a la columna (TableViewerColumn)
		viewer.setSorter(new GenericSorter(viewer));
		viewer.setInput(controller.getListadoReservasTours("", FechaUtil.ajustarFecha(new Date(), 1), 6, false));
		// registramos el tableViewer como un selection provider
		// esto es requerido para obtener el elemento seleccionado en las acciones
		getSite().setSelectionProvider(viewer);
		
		// habilitamos el listener para doble clic
		hookDoubleClickListener(viewer, ToursEditor.ID);
	}
	
	
	public void crearColumnasViewer(final TableViewer tViewer) {
		Table tabla = tViewer.getTable();
		tabla.setLinesVisible(true);
		tabla.setHeaderVisible(true);

		TableViewerColumn vc = new TableViewerColumn(tViewer, SWT.RIGHT);
		vc.getColumn().setText("Fecha");
		vc.getColumn().setWidth(70);
		vc.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				DisponibilidadTour disp = (DisponibilidadTour) element;
				return FechaUtil.toString(disp.getFecha());
			}
			@Override
			public Color getBackground(Object element) {
				return fechaIgual ? null : color;
			}
			@Override
			public void update(ViewerCell cell) {
				TableItem item = (TableItem)cell.getItem();
				int pos = item.getParent().indexOf(item);
				if (pos > 0) {
					Date fechaActual = ((DisponibilidadTour) tViewer.getElementAt(pos)).getFecha();
					Date fechaPrevia = ((DisponibilidadTour) tViewer.getElementAt(pos - 1)).getFecha();
					if (fechaActual.getTime() != fechaPrevia.getTime()) {
						fechaIgual = fechaIgual ^ true;
					}
				}
				super.update(cell);
			}
		});
		
		vc = new TableViewerColumn(tViewer, SWT.LEFT);
		vc.getColumn().setText("Nombre del tour");
		vc.getColumn().setWidth(200);
		vc.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				String nombre = ((DisponibilidadTour) element).getTour().getNombre();
				return nombre;
			}
			@Override
			public Color getBackground(Object element) {
				return fechaIgual ? null : color;
			}
		});
		
		vc = new TableViewerColumn(tViewer, SWT.LEFT);
		vc.getColumn().setText("Cap/Disp");
		vc.getColumn().setWidth(60);
		vc.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				DisponibilidadTour disp = (DisponibilidadTour) element;
				return disp.getResumenDisp();
			}
			@Override
			public Color getBackground(Object element) {
				return fechaIgual ? null : color;
			}
		});
		
		vc = new TableViewerColumn(tViewer, SWT.LEFT);
		vc.getColumn().setText("Referencia");
		vc.getColumn().setWidth(75);
		vc.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				DisponibilidadTour disp = (DisponibilidadTour) element;
				return disp.getNumero();
			}
			@Override
			public Color getBackground(Object element) {
				return fechaIgual ? null : color;
			}
		});
		
		vc = new TableViewerColumn(tViewer, SWT.LEFT);
		vc.getColumn().setText("Tipo");
		vc.getColumn().setWidth(60);
		vc.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				DisponibilidadTour disp = (DisponibilidadTour) element;
				return disp.getTipo();
			}
			@Override
			public Color getBackground(Object element) {
				return fechaIgual ? null : color;
			}
		});
		
		vc = new TableViewerColumn(tViewer, SWT.LEFT);
		vc.getColumn().setText("Comentarios");
		vc.getColumn().setWidth(200);
		vc.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				DisponibilidadTour disp = (DisponibilidadTour) element;
				return disp.getComentario();
			}
			@Override
			public Color getBackground(Object element) {
				return fechaIgual ? null : color;
			}
		});
	}
	
	
	public void crearTabla(Composite parent) {
		int style = SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.HIDE_SELECTION;
		Table tabla = new Table(parent, style);
		tabla.setLinesVisible(true);
		tabla.setHeaderVisible(true);
		
		TableColumn column = new TableColumn(tabla, SWT.RIGHT, 0);
		column.setText("Fechas");
		column.setWidth(70);
		column.setAlignment(SWT.RIGHT);  // aparentemente esto es ignorado
		
		/*
		column = new TableColumn(tabla, SWT.LEFT, 1);
		column.setText("Nombre del tour");
		column.setWidth(200);
		*/
		
		TableViewerColumn vc = new TableViewerColumn((TableViewer) getViewer(), SWT.LEFT);
		vc.getColumn().setText("Nombre del tours");
		vc.getColumn().setWidth(200);
		vc.setLabelProvider(new ColumnLabelProvider() {

			@Override
			public Color getBackground(Object element) {
				if( true ) {
					return null;
				} else {
					return ((TableViewer) getViewer()).getTable().getDisplay().getSystemColor(SWT.COLOR_GRAY);
				}
				//return super.getBackground(element);
			}

			@Override
			public void update(ViewerCell cell) {
				super.update(cell);
			}
			
		});
		
		column = new TableColumn(tabla, SWT.LEFT, 2);
		column.setText("Cap/Disps");
		column.setWidth(60);
		
		column = new TableColumn(tabla, SWT.LEFT, 3);
		column.setText("Referencias");
		column.setWidth(75);
		
		column = new TableColumn(tabla, SWT.LEFT, 4);
		column.setText("Tipos");
		column.setWidth(60);
		
		column = new TableColumn(tabla, SWT.LEFT, 5);
		column.setText("Comentarioss");
		column.setWidth(200);
	}

	
	public void refrescar() {
		System.out.println("Refrescando HojasView");
		viewer.setInput(controller.getListadoReservasTours("", FechaUtil.ajustarFecha(new Date(), 1), 6, false));
		viewer.refresh();
	}
	
	
	class ViewContentProvider implements IStructuredContentProvider {

        public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		}
        
		public void dispose() {
		}
        
		public Object[] getElements(Object parent) {
			Object[] resultados = (Object[]) parent;
			return resultados;
		}
	}
	
	
	class ViewLabelProvider extends LabelProvider implements ITableLabelProvider {
		public String getColumnText(Object obj, int index) {
			String resultado = "";
			DisponibilidadTour disp = (DisponibilidadTour) obj;
			switch (index) {
			case 0:
				resultado = FechaUtil.toString(disp.getFecha());
				break;
			case 1:
				String nombre = disp.getTour().getNombre();
				//String tipo = disp.getTour().getDspTipoProducto();
				resultado = nombre;
				break;
			case 2:
				resultado = disp.getResumenDisp();
				break;
			case 3:
				resultado = disp.getNumero();
				break;
			case 4:
				resultado = disp.getTipo();
				break;
			case 5:
				resultado = disp.getComentario();
				break;
			}
			return resultado;
		}
		public Image getColumnImage(Object obj, int index) {
			return null;		
		}
		
	}


	public StructuredViewer getViewer() {
		return viewer;
	}


	@Override
	public void dispose() {
		color.dispose();
		super.dispose();
	}
	
}
