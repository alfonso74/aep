package rcp.manticora.model;

import rcp.manticora.services.ComboDataItem;

public interface IComboDataItem {
	
	/**
	 * Formatea un keyword para ser agregado a un ComboData
	 * @return ComboDataItem listo para ser utilizado en un combo box
	 */
	ComboDataItem toComboDataItem();

}
