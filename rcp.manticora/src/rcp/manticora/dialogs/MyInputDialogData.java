package rcp.manticora.dialogs;

public class MyInputDialogData {
	private String textResponse;
	private String codigoTemplate;
	private String fechaBase;
	private String noPaxs;
	private String tipoPrecio;
	
	boolean buttonResponse;
	
	MyInputDialogData(){
		setTextResponse("");
		setButtonResponse(false);
	}
	public boolean isButtonResponse() {
		return buttonResponse;
	}
	public void setButtonResponse(boolean b) {
		buttonResponse = b;
	}
	
// *************************** getters y setters ****************************
	
	public String getTextResponse() {
		return textResponse;
	}
	public void setTextResponse(String textResponse) {
		this.textResponse = textResponse;
	}
	public String getCodigoTemplate() {
		return codigoTemplate;
	}
	public void setCodigoTemplate(String codigoTemplate) {
		this.codigoTemplate = codigoTemplate;
	}
	public String getFechaBase() {
		return fechaBase;
	}
	public void setFechaBase(String fechaBase) {
		this.fechaBase = fechaBase;
	}
	public String getNoPaxs() {
		return noPaxs;
	}
	public void setNoPaxs(String noPaxs) {
		this.noPaxs = noPaxs;
	}
	public String getTipoPrecio() {
		return tipoPrecio;
	}
	public void setTipoPrecio(String tipoPrecio) {
		this.tipoPrecio = tipoPrecio;
	}
}
