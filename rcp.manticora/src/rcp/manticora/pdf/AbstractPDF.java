package rcp.manticora.pdf;

import rcp.manticora.services.FormUtils;
import rcp.manticora.services.IFormUtils;

public abstract class AbstractPDF implements IFormUtils {
	FormUtils formUtils;

	public AbstractPDF() {
		formUtils = new FormUtils();
	}

	public Double txt2Currency(String valorCampo) {
		return formUtils.txt2Currency(valorCampo);
	}
	
	public Integer txt2Integer(String valorCampo) {
		return formUtils.txt2Integer(valorCampo);
	}
	
	public Float txt2Float(String valorCampo) {
		return formUtils.txt2Float(valorCampo);
	}
	
	public Long txt2Long(String valorCampo) {
		return formUtils.txt2Long(valorCampo);
	}

	public String valor2Txt(Object valorCampo) {
		return formUtils.valor2Txt(valorCampo);
	}

	public String valor2Txt(Object valorCampo, String formato) {
		return formUtils.valor2Txt(valorCampo, formato);
	}
	
	public boolean valor2Bool(Boolean valorCampo) {
		return formUtils.valor2Bool(valorCampo);
	}
}
