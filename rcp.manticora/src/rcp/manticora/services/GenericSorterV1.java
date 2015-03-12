package rcp.manticora.services;

import java.text.Collator;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Date;

import org.eclipse.jface.viewers.ContentViewer;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;



public class GenericSorterV1 extends ViewerSorter {
	public final static int AUTOMATICO = -1;
	public final static int NUMERO = 0;
	public final static int TEXTO = 1;
	public final static int FECHA = 2;
	public final static int FECHAHORA = 3;
	private int columnaDefault = 0;
	private int column;
	private int tipo = AUTOMATICO;   // 0 es numérico, 1 es alfanumérico, -1 es automático
	private int direccion = -1;  // 0 es sin ordenar, 1 es ascendente, 2 es descendente
	private boolean indicador = true;
	
	private Table t;
	private TableColumn tc;
	
	
	/**
	 * Clase utilizada para permitir ordenamiento de columnas en un viewer.
	 * Por default ordena en base a la primera columna del viewer, pero
	 * el parámetro columna permite indicar la columna default a utilizar para
	 * el ordenamiento.
	 * @param viewer Viewer al que se aplica el sorter
	 * @param columna Número de columna que rige el ordenamiento (en base a 0)
	 */

	public GenericSorterV1(TableViewer viewer, int columna) {
		super();
		if (viewer instanceof TableViewer) {
			t = ((TableViewer) viewer).getTable();
		} else {
			t = null;
			tc = null;
		}
		columnaDefault = columna;
		aplicarDefault();
	}
	
	
	/**
	 * Clase utilizada para permitir ordenamiento de columnas en un viewer.
	 * Por default ordena en base a la primera columna del viewer, pero
	 * con el método doSort() se puede indicar la columna a utilizar para el
	 * ordenamiento y el tipo de datos de la misma.
	 * @param viewer Viewer al que se aplica el sorter
	 */
	public GenericSorterV1(TableViewer viewer) {
		super();
		if (viewer instanceof TableViewer) {
			t = ((TableViewer) viewer).getTable();
		} else {
			t = null;
			tc = null;
		}
		aplicarDefault();
	}
	

	public GenericSorterV1(TableViewer viewer, Collator collator) {
		super(collator);
		if (viewer instanceof TableViewer) {
			t = ((TableViewer) viewer).getTable();
		} else {
			t = null;
			tc = null;
		}
		aplicarDefault();
	}
	
	
	/**
	 * Utilizado para permitir ordenamiento de columnas en un viewer.
	 * Se debe especificar la columna y el tipo de la misma para realizar
	 * el ordenamiento.
	 * @param column Número de columna que rige el ordenamiento (en base a 0)
	 * @param tipo Tipo de datos que contiene la columna
	 */
	public GenericSorterV1(TableViewer viewer, int column, int tipo) {
		super();
		if (viewer instanceof TableViewer) {
			t = ((TableViewer) viewer).getTable();
		} else {
			t = null;
			tc = null;
		}
		// en lugar de llamar a aplicarDefault() suministramos los parámetros
		// de acuerdo a la columna y tipo indicado
		this.direccion = 0;
		this.column = column;
		this.tipo = tipo;
		this.indicador = false;
		doSort(column, tipo);
	}
	
	// por default se ordena en base a la primera columna de manera ascendente
	private void aplicarDefault() {
		direccion = 1;
		column = columnaDefault;
		tipo = AUTOMATICO;
		indicador = false;
		System.out.println("Aplicando defaults (GenericSorter)...");
	}
	
	/**
	 * Ordena al viewer en base a la columna y al tipo de datos especificado
	 * @param column Número de columna que rige el ordenamiento (en base a 0)
	 * @param tipo Tipo de datos que contiene la columna
	 */
	public void doSort(int column, int tipo) {
		//indicador = true;
		this.tipo = tipo;
		// si se hace click a una nueva columna, se resetea la direccion y
		// se guarda la referencia de la columna actual
		if (this.column != column) {
			this.column = column;
			direccion = 0;
		}
		if (direccion == 0) {
			direccion = 1;
		} else if (direccion == 1) {
			direccion = 2;
		} else if (direccion == 2) {
			aplicarDefault();
		} else {
			direccion = 1;
		}
	}
	
	/**
	 * Ordena al viewer en base a la columna y tipo de datos especificado,
	 * en la dirección indicada.
	 * @param column Número de columna que rige el ordenamiento (en base a 0)
	 * @param tipo Tipo de datos que contiene la columna
	 * @param direccion Dirección inicial de ordenamiento (0 - sin ordenar, 1 - ascendente, 2 - descendente)
	 */
	public void doSort(int column, int tipo, int direccionInicial) {
		//indicador = true;
		this.tipo = tipo;
		// si se hace click a una nueva columna, se resetea la direccion y
		// se guarda la referencia de la columna actual
		if (this.column != column) {
			this.column = column;
			direccion = direccionInicial;
		} else {
		// si no, determinamos la nueva dirección de ordenamiento
			if (direccion == 0) {
				direccion = 1;
			} else if (direccion == 1) {
				direccion = 2;
			} else if (direccion == 2) {
				aplicarDefault();
			} else {
				direccion = direccionInicial;
			}
		}
	}
	
	
	/**
	 * Ordena al viewer en base a la columna y tipo de datos especificado,
	 * en la dirección indicada.
	 * @param column Número de columna que rige el ordenamiento (en base a 0)
	 * @param tipo Tipo de datos que contiene la columna
	 * @param direccion Dirección inicial de ordenamiento (0 - sin ordenar, 1 - ascendente, 2 - descendente)
	 * @param indicador Flag que indica si se debe mostrar la dirección de ordenamiento de la columna
	 */
	public void doSort(int column, int tipo, int direccionInicial, boolean mostrarIndicador) {
		this.tipo = tipo;
		// si se hace click a una nueva columna, se resetea la direccion y
		// se guarda la referencia de la columna actual
		if (this.column != column) {
			this.column = column;
			direccion = direccionInicial;
			indicador = mostrarIndicador;
		} else {
		// si no, determinamos la nueva dirección de ordenamiento
			if (direccion == 0) {
				direccion = 1;
			} else if (direccion == 1) {
				direccion = 2;
			} else if (direccion == 2) {
				aplicarDefault();
			} else {
				direccion = direccionInicial;
			}
		}
		// si el indicador está habilitado, lo presentamos en la columna
		if (indicador) {
			tc = t.getColumn(column);
			t.setSortColumn(tc);
			if (direccion == 0) {
				t.setSortDirection(SWT.NONE);
			} else if (direccion == 1 || direccion == -1) {
				t.setSortDirection(SWT.UP);
			} else {
				t.setSortDirection(SWT.DOWN);
			}
		} else {
			t.setSortDirection(SWT.NONE);
		}
		//actualizarIndicador(tViewer, indicador);
	}
	
	
	private void actualizarIndicador(Viewer tViewer, boolean indicador) {
		if (tViewer instanceof TableViewer) {
			Table t = ((TableViewer) tViewer).getTable();
			TableColumn tc = null;
			if (indicador) {
				tc = t.getColumn(column);
				t.setSortColumn(tc);
				if (direccion == 0) {
					t.setSortDirection(SWT.NONE);
				} else if (direccion == 1 || direccion == -1) {
					t.setSortDirection(SWT.UP);
				} else {
					t.setSortDirection(SWT.DOWN);
				}
			} else {
				t.setSortDirection(SWT.NONE);
			}
		} if (tViewer instanceof TreeViewer) {
			Tree t = ((TreeViewer) tViewer).getTree();
			TreeColumn tc = null;
			if (indicador) {
				tc = t.getColumn(column);
				t.setSortColumn(tc);
				if (direccion == 0) {
					t.setSortDirection(SWT.NONE);
				} else if (direccion == 1 || direccion == -1) {
					t.setSortDirection(SWT.UP);
				} else {
					t.setSortDirection(SWT.DOWN);
				}
			} else {
				t.setSortDirection(SWT.NONE);
			}
		}
	}
	
	
	public int compare(Viewer viewer, Object e1, Object e2) {
		int resultado = 0;
		int cat1 = category(e1);
		int cat2 = category(e2);
		//System.out.println("Categorías: " + cat1 + ", " + cat2);
		if (cat1 != cat2) return cat1 - cat2;
		String name1, name2;
		if (viewer == null || !(viewer instanceof ContentViewer)) {
			name1 = e1.toString();
			name2 = e2.toString();
		} else {
			IBaseLabelProvider prov = ((ContentViewer)viewer).getLabelProvider();
			if (prov instanceof ITableLabelProvider) {
				ITableLabelProvider lprov = (ITableLabelProvider) prov;
				name1 = lprov.getColumnText(e1, column);
				name2 = lprov.getColumnText(e2, column);
				//System.out.println("Label provider: " + name1 + ", " + name2);
			} else {
				name1 = e1.toString();
				name2 = e2.toString();
			}
		}
		
		if(name1 == null) name1 = "";
		if(name2 == null) name2 = "";
		
		// si no se ha indicado un tipo, entonces determinamos el tipo de la columna default
		if (tipo == AUTOMATICO) {
			System.out.println("Detección automática de tipo de columna");
			tipo = determinarTipoColumna(name1);
		}
		
		if (tipo == NUMERO) {
			// transformamos y comparamos en base a números
			NumberFormat nf = NumberFormat.getInstance();
			double numero1 = 0;
			double numero2 = 0;
			try {
				numero1 = nf.parse(name1).doubleValue();
				numero2 = nf.parse(name2).doubleValue();
			} catch (ParseException e) {
				System.out.println("Error en transformación de números");
			}
			//double numero1 = Double.parseDouble(name1);
			//double numero2 = Double.parseDouble(name2);
			resultado = numero1 > numero2 ? 1 : -1;
		} else {
			if (tipo == FECHA) {
				// transformamos a milisegundos y comparamos
				Date fecha1 = FechaUtil.toDate(name1);
				Date fecha2 = FechaUtil.toDate(name2);
				long time1 = fecha1.getTime();
				long time2 = fecha2.getTime();
				resultado = time1 > time2 ? 1 : -1;
			} else {
				if (tipo == FECHAHORA) {
					//System.out.println("Usando ordenamiento de Fecha-Hora");
					Date fecha1 = FechaUtil.toDateHour(name1);
					Date fecha2 = FechaUtil.toDateHour(name2);
					// puede que aunque la columna sea fecha-hora en algunos casos solamente haya 
					// una fecha (ej. actividades en hojas de ventas como hospedaje) así que verificamos esto
					if (fecha1 == null) fecha1 = FechaUtil.toDate(name1);
					if (fecha2 == null) fecha2 = FechaUtil.toDate(name2);
					long time1 = fecha1.getTime();
					long time2 = fecha2.getTime();
					resultado = time1 > time2 ? 1 : -1;
				} else {
					//comparamos como strings normales
					//System.out.println("Usando ordenamiento de String");
					resultado = collator.compare(name1, name2);
				}
			}
		}
		
		if (direccion == 0) {
			return 0;
		} else if (direccion == 1 || direccion == -1) {   // ascendente o default (valor -1)
			return resultado;
		} else if (direccion == 2){
			return (-resultado);
		} else {
			return 0;
		}

	}

	/*
	private int determinarTipoColumna(String cadena) {
		int tipoColumna = TEXTO;  // default de tipo alfanumérico
		if (isNumber(cadena)) {  // cadena numérica
			tipoColumna = NUMERO;
		} else if (isDate(cadena)) {  // es una cadena de fecha
			tipoColumna = FECHA;
			if (isDateHour(cadena)) {  // cadena de fecha y hora
				tipoColumna = FECHAHORA;
			};
		}
		return tipoColumna;
	}
	*/
	
	private int determinarTipoColumna(String cadena) {
		int tipoColumna = TEXTO;  // default de tipo alfanumérico
		if (isDate(cadena)) {  // es una cadena de fecha
			tipoColumna = FECHA;
			if (isDateHour(cadena)) {  // cadena de fecha y hora
				tipoColumna = FECHAHORA;
			};
		} else if (isNumber(cadena)) {
			tipoColumna = NUMERO;
		}
		return tipoColumna;
	}
	
	private boolean isNumber(String cadena) {
		NumberFormat nf = NumberFormat.getInstance();
		boolean resultado = false;
		try {
			nf.parse(cadena).doubleValue();
			resultado = true;
		} catch(ParseException e) {
			resultado = false;
		}
		return resultado;
	}
	
	private boolean isDate(String cadena) {
		boolean resultado = false;
		if (FechaUtil.toDate(cadena) != null) {
			resultado = true;
		} else {
			resultado = false;
		}
		return resultado;
	}
	
	private boolean isDateHour(String cadena) {
		boolean resultado = false;
		if (FechaUtil.toDateHour(cadena) != null) {
			resultado = true;
		} else {
			resultado = false;
		}
		return resultado;
	}
}
