package rcp.manticora.model;

public class NumeroTour {

	private String numero;
	private String year;
	private String mes;
	private String secuencial;


	public NumeroTour() {
	}

	public NumeroTour(String numeroTour) {
		setNumero(numeroTour);
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		if (numero != null) {
			this.numero = numero;
			this.year = numero.substring(0, 2);
			this.mes = numero.substring(2, 4);
			this.secuencial = numero.substring(5);
		}
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getMes() {
		return mes;
	}

	public void setMes(String mes) {
		this.mes = mes;
	}
	public String getSecuencial() {
		return secuencial;
	}

	public void setSecuencial(String secuencial) {
		this.secuencial = secuencial;
	}

}
