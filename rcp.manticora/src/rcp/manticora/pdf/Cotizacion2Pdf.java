package rcp.manticora.pdf;

import java.io.FileOutputStream;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import rcp.manticora.model.Cliente;
import rcp.manticora.model.Cotizacion;
import rcp.manticora.model.LineaCotizacion;
import rcp.manticora.services.CotizacionesComparator;
import rcp.manticora.services.FechaUtil;


import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.AcroFields;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfCopyFields;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;

public class Cotizacion2Pdf extends AbstractPDF {
	private Cotizacion cotizacion;
	private Cliente c;
	private String mensajeError = "";

	public Cotizacion2Pdf() {
		//super();
	}
	
	public Cotizacion2Pdf(Cotizacion cotizacion) {
		this.cotizacion = cotizacion;
		c = this.cotizacion.getCliente();
	}
	
	public boolean generarPdf(String directorio, boolean resumido) {
		System.out.println("Directorio: " + directorio);
		String pIdCotizacion = "00000" + valor2Txt(cotizacion.getIdCotizacion());
		pIdCotizacion = pIdCotizacion.substring(pIdCotizacion.length() - 5, pIdCotizacion.length());
		String pFecha = FechaUtil.toString(cotizacion.getFechaCreacion());
		String pVendedor = cotizacion.getDspVendedor();
		String pNombre = cotizacion.getNombre();
		String pNumeroTour = cotizacion.getNumeroTourAsString();
		if (pNumeroTour != null && !pNumeroTour.isEmpty()) {
			pNombre = pNombre + " (" + pNumeroTour + ')';
		}
		String pFecha1 = FechaUtil.toString(cotizacion.getFechaInicio(), "dd-MMM-yyyy");
		String pFecha2 = FechaUtil.toString(cotizacion.getFechaFin(), "dd-MMM-yyyy");
		String pSubTotal = valor2Txt(cotizacion.getSubtotal(), "#,##0.00");
		String pHospedaje = valor2Txt(cotizacion.getHospedaje(), "#,##0.00");
		String pImpuesto = valor2Txt(cotizacion.getImpuesto(), "#,##0.00");
		String pTotal = valor2Txt(cotizacion.getTotal(), "#,##0.00");
		int lineasTotal = 0;
		if (resumido) {
			lineasTotal = 1;   // si es consolidada solamente tenemos una l�nea
		} else {
			// sino, tenemos que contar las l�neas a generar
			for (LineaCotizacion linea : cotizacion.getListaActividades()) {
				lineasTotal += getLineasPdfOcupadas(linea);
			}
		}
		System.out.println("Total de l�neas a generar: " + lineasTotal);
		int lineasPorPagina = 23;
		int totalPaginas = ((lineasTotal - 1) / lineasPorPagina) + 1;
		try {
			PdfReader reader = null;
			// Dependiendo del n�mero de p�ginas que requiera la cotizaci�n, generamos diferentes PDFs
			if (totalPaginas > 1) {
				System.out.println("Generando PDF (m�ltiples p�ginas)");
				// Si tenemos m�s de una p�gina, creamos un pdf temporal donde agregamos todas las p�ginas
				// de detalle que sean necesarias, y al final agregamos la p�gina que presenta los totales.
				reader = new PdfReader("AEP Sales Quote v2 Detail.pdf");
				PdfCopyFields copy = new PdfCopyFields(new FileOutputStream("AEP Sales Quote Temporal.pdf"));
				for (int x = 1; x < totalPaginas; x++) {
					copy.addDocument(reader);
				}
				reader = new PdfReader("AEP Sales Quote v2.pdf");
				copy.addDocument(reader);
				copy.close();
				reader = new PdfReader("AEP Sales Quote Temporal.pdf");
			} else {
				// Para una sola p�gina, usamos directamente el pdf que presenta los totales.
				System.out.println("Generando PDF (una p�gina)");
				reader = new PdfReader("AEP Sales Quote v2.pdf");
			}
			
			// Ya que tenemos el pdf con las p�ginas necesarias, preparamos el pdf final al que se le
			// agrega la informaci�n de la cotizaci�n.
			PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(directorio + "\\AEP Sales Quote " + pIdCotizacion + ".pdf"));
			AcroFields form = stamper.getAcroFields();
			form.setField("noInvoice", pIdCotizacion);
			form.setField("date", pFecha);
			asignarInfoCliente(form);
			form.setField("salesperson", pVendedor);
			form.setField("tourName", pNombre);
			form.setField("tourDate", "From: " + pFecha1 + "   To: " + pFecha2);
			form.setField("subTotal", pSubTotal);
			form.setField("lodgingTax", pHospedaje);
			form.setField("salesTax", pImpuesto);
			form.setField("total", pTotal);
// se generan las l�neas de la cotizaci�n
			if (resumido) {
				generarTablaResumida(stamper);
			} else {
				Set<LineaCotizacion> lineasCotizacion = cotizacion.getListaActividades();
				Set<LineaCotizacion> lineas2Add = new TreeSet<LineaCotizacion>(new CotizacionesComparator());

				int indiceLinea = 0;
				int paginaActual = 1;
				for (LineaCotizacion linea : lineasCotizacion) {
					indiceLinea += getLineasPdfOcupadas(linea);
					lineas2Add.add(linea);
					if (indiceLinea >= paginaActual * lineasPorPagina) {
					//if (indiceLinea % lineasPorPagina == 0 || ((indiceLinea - 1) % lineasPorPagina) == 0) {
					//if (indiceLinea % lineasPorPagina == 0) {
						// si llegamos al l�mite de la p�gina: escribimos, reiniciamos el set de l�neas e incrementamos la p�gina.
						generarTablaDetalles(stamper, lineas2Add, paginaActual);
						lineas2Add.clear();
						paginaActual++;
					}
				}
				// si salimos del ciclo y hay l�neas pendientes, entonces las agregamos al pdf en la p�gina actual
				if (!lineas2Add.isEmpty()) {
					generarTablaDetalles(stamper, lineas2Add, paginaActual);
				}
			}
			stamper.close();
		} catch (Exception e) {
			mensajeError = "Error durante generaci�n de PDF: " + e.getMessage();
			mensajeError = "Error durante generaci�n de PDF: " + e.toString() + "\n\nStack trace: " + e.getStackTrace()[0];
			//System.out.println(mensajeError);
			e.printStackTrace();
			//TODO C�mo saco un shell aqu�????
			//MessageDialog.openError(getShell(), "Generar PDF", "Error durante generaci�n de PDF: " + e.getMessage());
			return false;
		}
		return true;
	}
	
	
	/**
	 * Permite saber cu�ntas l�neas del pdf puede ocupar una l�nea de detalle de la
	 * cotizaci�n.  Si tienen observaciones con "*" ocupan 2 l�neas en el pdf final.
	 * @param linea L�nea de cotizaci�n
	 * @return Cantidad de l�neas que ocupar� en el pdf final
	 */
	private int getLineasPdfOcupadas(LineaCotizacion linea) {
		int cantidad = 1;
		if (linea.getComentario().length() > 0 && linea.getComentario().startsWith("*")) {
			cantidad++;
		}
		/*
		if (linea.isVisible()) {
			if (linea.getComentario().length() > 0 && linea.getComentario().startsWith("*")) {
				cantidad++;
			}
		} else {
			// si no es visible, entonces no ocupa espacio en el pdf generado para la cotizaci�n
			cantidad = 0;
		}
		*/
		return cantidad;
	}
	
	
	public boolean generarPdfx(String directorio) {
		System.out.println("Directorio: " + directorio);
		String pIdCotizacion = "00000" + valor2Txt(cotizacion.getIdCotizacion());
		pIdCotizacion = pIdCotizacion.substring(pIdCotizacion.length() - 5, pIdCotizacion.length());
		String pFecha = FechaUtil.toString(cotizacion.getFechaCreacion());
		String pVendedor = cotizacion.getDspVendedor();
		String pNombre = cotizacion.getNombre();
		String pFecha1 = FechaUtil.toString(cotizacion.getFechaInicio(), "dd-MMM-yyyy");
		String pFecha2 = FechaUtil.toString(cotizacion.getFechaFin(), "dd-MMM-yyyy");
		String pSubTotal = valor2Txt(cotizacion.getSubtotal(), "#,##0.00");
		String pHospedaje = valor2Txt(cotizacion.getHospedaje(), "#,##0.00");
		String pImpuesto = valor2Txt(cotizacion.getImpuesto(), "#,##0.00");
		String pTotal = valor2Txt(cotizacion.getTotal(), "#,##0.00");
		try {
			PdfReader reader = new PdfReader("AEP Sales Quote v2.pdf");
			PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(directorio + "\\AEP Sales Quote " + pIdCotizacion + ".pdf"));
			AcroFields form = stamper.getAcroFields();
			form.setField("noInvoice", pIdCotizacion);
			form.setField("date", pFecha);
			asignarInfoCliente(form);
			form.setField("salesperson", pVendedor);
			form.setField("tourName", pNombre);
			form.setField("tourDate", "From: " + pFecha1 + "   To: " + pFecha2);
			//form.setField("description", "Jungle Boat");
			form.setField("subTotal", pSubTotal);
			form.setField("lodgingTax", pHospedaje);
			form.setField("salesTax", pImpuesto);
			form.setField("total", pTotal);
// se generan las l�neas de la cotizaci�n
			generarTablaDetalles(stamper, cotizacion.getListaActividades(), 1);
			stamper.close();
		} catch (Exception e) {
			mensajeError = "Error durante generaci�n de PDF: " + e.getMessage();
			mensajeError = "Error durante generaci�n de PDF: " + e.toString() + "\n\nStack trace: " + e.getStackTrace()[0];
			//System.out.println(mensajeError);
			e.printStackTrace();
			//TODO C�mo saco un shell aqu�????
			//MessageDialog.openError(getShell(), "Generar PDF", "Error durante generaci�n de PDF: " + e.getMessage());
			return false;
		}
		return true;
	}
	
	
	/**
	 * Agrega la informaci�n del cliente al encabezado de la cotizaci�n en PDF
	 * @param form Objeto que tiene acceso a los campos de la cotizaci�n en PDF
	 * @throws Exception
	 */
	private void asignarInfoCliente(AcroFields form) throws Exception {
		if (c == null) {
			String pProspecto = cotizacion.getProspecto();
			form.setField("soldTo1", pProspecto);
		} else {
			int n = 1;
			Iterator<String> pSoldTo = c.generarDireccion().iterator();
			while (pSoldTo.hasNext()) {
				form.setField("soldTo" + String.valueOf(n++), (String) pSoldTo.next());
			}
			String pIdCliente = valor2Txt(c.getIdCliente());
			form.setField("noCustomer", pIdCliente);
		}
	}
	
	
	/**
	 * Agrega l�neas de detalle a una cotizaci�n en formato PDF
	 * @param stamper Documento PDF a ser modificado
	 * @param lineasCotizacion l�neas de cotizaci�n que se van a agregar
	 * @param noPagina p�gina en la que se deben agregar las l�neas
	 * @throws DocumentException
	 */
	private void generarTablaDetalles(PdfStamper stamper, Set<LineaCotizacion> lineas, int noPagina) throws DocumentException {
		// Usamos un nuevo TreeSet porque el de la cotizaci�n puede estar
		// desordenado si se movieron l�neas hacia arriba o abajo
		//Set<LineaCotizacion> lineas = new TreeSet<LineaCotizacion>(new CotizacionesComparator());
		//lineas.addAll(lineasCotizacion);
		PdfContentByte cb = stamper.getOverContent(noPagina);
		PdfPTable table = new PdfPTable(5);
		float[] widths = {57f, 249f, 53f, 70f, 63f};
		table.setTotalWidth(widths);
		table.setLockedWidth(true);
		table.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
		table.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
		
		int n = 0;
		for (LineaCotizacion linea : lineas) {
			n += agregarLineaDetalle(table, linea);
		}
		
		table.writeSelectedRows(0, n, 72, 445, cb);
	}
	
	
	/**
	 * Agrega l�neas de detalle a la tabla que presenta el detalle de la
	 * cotizaci�n
	 * @param table Tabla de iText
	 * @param linea la l�nea de cotizaci�n a ser agregada
	 * @return n�mero de l�neas agregadas (hasta 2 si hay comentario)
	 */
	private int agregarLineaDetalle(PdfPTable table, LineaCotizacion linea) {
		int n = 1;
		PdfPCell cell;
		String cadena;
		Font font = new Font(Font.HELVETICA, 8);
		
		// columna de cantidad
		cadena = valor2Txt(cotizacion.getPaxs());
		cadena = valor2Txt(linea.getCantidad());
		cell = new PdfPCell(new Paragraph(cadena, font));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setBorder(PdfPCell.NO_BORDER);
		table.addCell(cell);
		
		// columna de descripci�n
		//cadena = linea.getDspProducto();
		cadena = linea.getProducto().getDescripcionHotel();
		cell = new PdfPCell(new Paragraph(cadena, font));
		cell.setPaddingLeft(5);
		cell.setBorder(PdfPCell.NO_BORDER);
		table.addCell(cell);
		
		// columna de pax/rooms
		cadena = valor2Txt(linea.getEspacios());
		cell = new PdfPCell(new Paragraph(cadena, font));
		cell.setPaddingRight(7);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell.setBorder(PdfPCell.NO_BORDER);
		table.addCell(cell);
		
		// columna de precio unitario
		if (linea.isVisible()) {
			cadena = valor2Txt(linea.getPrecio(), "#,##0.00");
		} else {
			cadena = "";
		}
		cell = new PdfPCell(new Paragraph(cadena, font));
		cell.setPaddingRight(7);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell.setBorder(PdfPCell.NO_BORDER);
		table.addCell(cell);

		// columna de precio total (l�nea)
		//cadena = valor2Txt((cotizacion.getPaxs().floatValue() * linea.getPrecio()), "#,##0.00");
		if (linea.isVisible()) {
			cadena = valor2Txt(linea.getPrecio() * linea.getCantidad() * linea.getEspacios(), "#,##0.00");
		} else {
			cadena = "";
		}
		cell = new PdfPCell(new Paragraph(cadena, font));
		cell.setPaddingRight(7);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell.setBorder(PdfPCell.NO_BORDER);
		table.addCell(cell);
		
		if (linea.getComentario().length() > 0 && linea.getComentario().startsWith("*")) {
			agregarLineaComentario(table, linea.getComentario().substring(1));
			n++;
		}
		return n;
	}
	
	
	private void agregarLineaComentario(PdfPTable table, String comentario) {
		PdfPCell cell;
		Font font = new Font(Font.HELVETICA, 8);
		
		table.addCell("");
		
		cell = new PdfPCell(new Paragraph(" -- " + comentario, font));
		cell.setPaddingLeft(5);
		cell.setBorder(PdfPCell.NO_BORDER);
		table.addCell(cell);
		
		table.addCell("");
		table.addCell("");
		table.addCell("");
	}
	
	
	/**
	 * Crea una l�nea consolidada en la tabla que presenta el detalle de la
	 * cotizaci�n
	 * @param table Tabla de iText
	 * @return n�mero de l�neas agregadas (por ahora siempre es 1)
	 */
	private int generarTablaResumida(PdfStamper stamper) throws DocumentException {
		
		// preparamos la tabla que va a presentar el listado de items de la cotizaci�n
		// solo que en este caso siempre tenemos una sola l�nea
		PdfContentByte cb = stamper.getOverContent(1);   // una consolidada siempre tiene una sola p�gina
		PdfPTable table = new PdfPTable(5);
		float[] widths = {57f, 249f, 53f, 70f, 63f};
		table.setTotalWidth(widths);
		table.setLockedWidth(true);
		table.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
		table.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
		
		// generamos las celdas de la l�nea
		int n = 1;
		
		PdfPCell cell;
		String cadena;
		Font font = new Font(Font.HELVETICA, 8);
		
		// columna de cantidad
		cadena = "1";
		cell = new PdfPCell(new Paragraph(cadena, font));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setBorder(PdfPCell.NO_BORDER);
		table.addCell(cell);
		
		// columna de descripci�n
		//cadena = linea.getDspProducto();
		cadena = cotizacion.getNombre();
		cell = new PdfPCell(new Paragraph(cadena, font));
		cell.setPaddingLeft(5);
		cell.setBorder(PdfPCell.NO_BORDER);
		table.addCell(cell);
		
		// columna de pax/rooms
		cadena = valor2Txt(cotizacion.getPaxs());
		cell = new PdfPCell(new Paragraph(cadena, font));
		cell.setPaddingRight(7);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell.setBorder(PdfPCell.NO_BORDER);
		table.addCell(cell);
		
		// columna de precio unitario
		cadena = "--";
		cell = new PdfPCell(new Paragraph(cadena, font));
		cell.setPaddingRight(7);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell.setBorder(PdfPCell.NO_BORDER);
		table.addCell(cell);

		// columna de precio total (l�nea)
		cadena = valor2Txt(cotizacion.getSubtotal(), "#,##0.00");
		cell = new PdfPCell(new Paragraph(cadena, font));
		cell.setPaddingRight(7);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell.setBorder(PdfPCell.NO_BORDER);
		table.addCell(cell);
		
		/*
		if (linea.getComentario().length() > 0 && linea.getComentario().startsWith("*")) {
			agregarLineaComentario(table, linea.getComentario().substring(1));
			n++;
		}
		*/
		
		// agrega la l�nea (y sus celdas) que ha sido creada a la tabla de iText
		table.writeSelectedRows(0, n, 72, 445, cb);
		
		return n;
	}
	
	
	public String getMensajeError() {
		return mensajeError;
	}
	
	
	public boolean generarPdfPrueba(String directorio) throws Exception {
		try {
			PdfReader reader = new PdfReader("AEP Sales Quote v2.pdf");
			PdfStamper stamper = new PdfStamper(reader, new FileOutputStream("AEP Sales Quote Filled.pdf"));
			AcroFields form = stamper.getAcroFields();
			form.setField("noInvoice", "" + cotizacion.getIdCotizacion());
			form.setField("date", "25-01-2007");
			form.setField("noCustomer", "0015");
			form.setField("soldTo1", "Country Walkers");
			//form.setField("soldTo2", "USA");
			form.setField("salesperson", "Carlos A. P�rez Q.");
			form.setField("tourName", "Jungle Boat");
			form.setField("tourDate", "From: 05-02-2007, to: 27-02-2007");
			form.setField("description", "Jungle Boat");
			form.setField("subTotal", "250.00");
			form.setField("salesTax", "12.50");
			form.setField("total", "262.50");
			stamper.close();
			return true;
		} catch (Exception e) {
			System.out.println("Error durante generaci�n de PDF: " + e.getMessage());
			return false;
		}
	}
}
