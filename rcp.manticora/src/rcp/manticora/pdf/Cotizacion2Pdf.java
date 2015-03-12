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
			lineasTotal = 1;   // si es consolidada solamente tenemos una línea
		} else {
			// sino, tenemos que contar las líneas a generar
			for (LineaCotizacion linea : cotizacion.getListaActividades()) {
				lineasTotal += getLineasPdfOcupadas(linea);
			}
		}
		System.out.println("Total de líneas a generar: " + lineasTotal);
		int lineasPorPagina = 23;
		int totalPaginas = ((lineasTotal - 1) / lineasPorPagina) + 1;
		try {
			PdfReader reader = null;
			// Dependiendo del número de páginas que requiera la cotización, generamos diferentes PDFs
			if (totalPaginas > 1) {
				System.out.println("Generando PDF (múltiples páginas)");
				// Si tenemos más de una página, creamos un pdf temporal donde agregamos todas las páginas
				// de detalle que sean necesarias, y al final agregamos la página que presenta los totales.
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
				// Para una sola página, usamos directamente el pdf que presenta los totales.
				System.out.println("Generando PDF (una página)");
				reader = new PdfReader("AEP Sales Quote v2.pdf");
			}
			
			// Ya que tenemos el pdf con las páginas necesarias, preparamos el pdf final al que se le
			// agrega la información de la cotización.
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
// se generan las líneas de la cotización
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
						// si llegamos al límite de la página: escribimos, reiniciamos el set de líneas e incrementamos la página.
						generarTablaDetalles(stamper, lineas2Add, paginaActual);
						lineas2Add.clear();
						paginaActual++;
					}
				}
				// si salimos del ciclo y hay líneas pendientes, entonces las agregamos al pdf en la página actual
				if (!lineas2Add.isEmpty()) {
					generarTablaDetalles(stamper, lineas2Add, paginaActual);
				}
			}
			stamper.close();
		} catch (Exception e) {
			mensajeError = "Error durante generación de PDF: " + e.getMessage();
			mensajeError = "Error durante generación de PDF: " + e.toString() + "\n\nStack trace: " + e.getStackTrace()[0];
			//System.out.println(mensajeError);
			e.printStackTrace();
			//TODO Cómo saco un shell aquí????
			//MessageDialog.openError(getShell(), "Generar PDF", "Error durante generación de PDF: " + e.getMessage());
			return false;
		}
		return true;
	}
	
	
	/**
	 * Permite saber cuántas líneas del pdf puede ocupar una línea de detalle de la
	 * cotización.  Si tienen observaciones con "*" ocupan 2 líneas en el pdf final.
	 * @param linea Línea de cotización
	 * @return Cantidad de líneas que ocupará en el pdf final
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
			// si no es visible, entonces no ocupa espacio en el pdf generado para la cotización
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
// se generan las líneas de la cotización
			generarTablaDetalles(stamper, cotizacion.getListaActividades(), 1);
			stamper.close();
		} catch (Exception e) {
			mensajeError = "Error durante generación de PDF: " + e.getMessage();
			mensajeError = "Error durante generación de PDF: " + e.toString() + "\n\nStack trace: " + e.getStackTrace()[0];
			//System.out.println(mensajeError);
			e.printStackTrace();
			//TODO Cómo saco un shell aquí????
			//MessageDialog.openError(getShell(), "Generar PDF", "Error durante generación de PDF: " + e.getMessage());
			return false;
		}
		return true;
	}
	
	
	/**
	 * Agrega la información del cliente al encabezado de la cotización en PDF
	 * @param form Objeto que tiene acceso a los campos de la cotización en PDF
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
	 * Agrega líneas de detalle a una cotización en formato PDF
	 * @param stamper Documento PDF a ser modificado
	 * @param lineasCotizacion líneas de cotización que se van a agregar
	 * @param noPagina página en la que se deben agregar las líneas
	 * @throws DocumentException
	 */
	private void generarTablaDetalles(PdfStamper stamper, Set<LineaCotizacion> lineas, int noPagina) throws DocumentException {
		// Usamos un nuevo TreeSet porque el de la cotización puede estar
		// desordenado si se movieron líneas hacia arriba o abajo
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
	 * Agrega líneas de detalle a la tabla que presenta el detalle de la
	 * cotización
	 * @param table Tabla de iText
	 * @param linea la línea de cotización a ser agregada
	 * @return número de líneas agregadas (hasta 2 si hay comentario)
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
		
		// columna de descripción
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

		// columna de precio total (línea)
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
	 * Crea una línea consolidada en la tabla que presenta el detalle de la
	 * cotización
	 * @param table Tabla de iText
	 * @return número de líneas agregadas (por ahora siempre es 1)
	 */
	private int generarTablaResumida(PdfStamper stamper) throws DocumentException {
		
		// preparamos la tabla que va a presentar el listado de items de la cotización
		// solo que en este caso siempre tenemos una sola línea
		PdfContentByte cb = stamper.getOverContent(1);   // una consolidada siempre tiene una sola página
		PdfPTable table = new PdfPTable(5);
		float[] widths = {57f, 249f, 53f, 70f, 63f};
		table.setTotalWidth(widths);
		table.setLockedWidth(true);
		table.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
		table.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
		
		// generamos las celdas de la línea
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
		
		// columna de descripción
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

		// columna de precio total (línea)
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
		
		// agrega la línea (y sus celdas) que ha sido creada a la tabla de iText
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
			form.setField("salesperson", "Carlos A. Pérez Q.");
			form.setField("tourName", "Jungle Boat");
			form.setField("tourDate", "From: 05-02-2007, to: 27-02-2007");
			form.setField("description", "Jungle Boat");
			form.setField("subTotal", "250.00");
			form.setField("salesTax", "12.50");
			form.setField("total", "262.50");
			stamper.close();
			return true;
		} catch (Exception e) {
			System.out.println("Error durante generación de PDF: " + e.getMessage());
			return false;
		}
	}
}
