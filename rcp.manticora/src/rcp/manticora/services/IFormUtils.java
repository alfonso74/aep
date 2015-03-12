package rcp.manticora.services;

public interface IFormUtils {
	
	public Double txt2Currency(String valorCampo);
	
	public Float txt2Float(String valorCampo);
	
	public Integer txt2Integer(String valorCampo);
	
	public Long txt2Long(String valorCampo);
	
	public String valor2Txt(Object valorCampo);
	
	public String valor2Txt(Object valorCampo, String formato);
	
	public boolean valor2Bool(Boolean valorCampo);
}
