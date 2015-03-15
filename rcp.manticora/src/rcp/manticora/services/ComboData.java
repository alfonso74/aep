package rcp.manticora.services;

import java.util.Vector;


public class ComboData {
	private Vector<String> vTexto;
	private Vector<String> vKey;
	private Vector<Object> vObjeto;

	
	public ComboData() {
		vTexto = new Vector<String>();
		vKey = new Vector<String>();
		vObjeto = new Vector<Object>();
	}
	
	/**
	 * Agrega una entrada (item) al combo data
	 * @param cdItem Objeto que contiene un identificador (llave), texto y el objeto a partir del cual se creó
	 * el item.
	 */
	public void agregarComboDataItem(ComboDataItem cdItem) {
		vTexto.addElement(cdItem.getTexto());
		vKey.addElement(cdItem.getKey());
		vObjeto.addElement(cdItem.getObjeto());
	}
	
//	public void agregarItem(IComboDataItem item) {
//		ComboDataItem cdItem = item.toComboDataItem();
//		vTexto.addElement(cdItem.getTexto());
//		vKey.addElement(cdItem.getKey());
//		vObjeto.addElement(cdItem.getObjeto());
//	}
	
	/**
	 * Agrega una entrada (item) al combo data.
	 * @param texto texto que se presenta en el combodata
	 * @param key valor o código que representa al texto
	 */
	public void agregarItem(String texto, String key) {
		vTexto.addElement(texto);
		vKey.addElement(key);
	}
	
	/**
	 * Agrega una entrada (item) al combo data.
	 * @param texto texto que se presenta en el combodata
	 * @param key valor o código que representa al texto
	 */
	public void agregarItem(String texto, Long key) {
		vTexto.addElement(texto);
		vKey.addElement(key.toString());
	}
	
	/**
	 * Agrega una entrada (item) al combo data.
	 * @param texto texto que se presenta en el combodata
	 * @param key valor o código que representa al texto
	 * @param obj objeto de donde se sacó el texto y el código de la entrada
	 */
	public void agregarItem(String texto, Long key, Object obj) {
		vTexto.addElement(texto);
		vKey.addElement(key.toString());
		vObjeto.addElement(obj);
	}
	
	/**
	 * Agrega una entrada (item) al combo data.
	 * @param texto texto que se presenta en el combodata
	 * @param key valor o código que representa al texto
	 * @param obj objeto de donde se sacó el texto y el código de la entrada
	 */
	public void agregarItem(String texto, String key, Object obj) {
		vTexto.addElement(texto);
		vKey.addElement(key);
		vObjeto.addElement(obj);
	}
	
	/**
	 * Agrega una entrada (item) al combo data en la posición indicada (en base a 0).
	 * @param texto texto que se presenta en el combodata
	 * @param key valor o código que representa al texto
	 * @param obj objeto de donde se sacó el texto y el código de la entrada
	 * @param atPosition posición donde se va a agregar el item
	 */
	public void agregarItemAt(String texto, String key, Object obj, int atPosition) {
//		vTexto.addElement(texto);
//		vKey.addElement(key);
//		vObjeto.addElement(obj);
		vTexto.add(atPosition, texto);
		vKey.add(atPosition, key);
		vObjeto.add(atPosition, obj);
	}
	
	/**
	 * Retorna un array con la parte de texto de todos los items incluidos en el ComboData
	 * @return array de tipo String[]
	 */
	public String[] getTexto() {
		return (String[]) vTexto.toArray(new String[vTexto.size()]);
	}
	
	/**
	 * Retorna la Descripción del elemento indicado en el índice
	 * @param indice Posición a retornar
	 * @return Descripción (como String) o null si el índice es -1
	 */
	public String getTextoByIndex(int indice) {
		if (indice != -1) {
			return (String) vTexto.elementAt(indice);
		} else {
			return null;
		}
	}
	
	/**
	 * Retorna la Descripción del elemento seleccionado
	 * @param valor Código a buscar (de tipo Long)
	 * @return Descripción (como String) o null (si no encuentra nada)
	 */
	public String getTextoByKey(Long valor) {
		Long v;
		if (valor == null) return null;
		for (int n = 0; n < vKey.size(); n++) {
			v = Long.parseLong(((String) vKey.elementAt(n)));
			if (v == valor.longValue()) {
				return (String) vTexto.elementAt(n);
			}
		}
		return null;
	}

	/**
	 * Retorna la Descripción del elemento seleccionado
	 * @param codigo Código a buscar
	 * @return Descripción (como String) o null (si no encuentra nada)
	 */
	public String getTextoByKey(String codigo) {
		String v;
		if (codigo == null) return null;
		for (int n = 0; n < vKey.size(); n++) {
			System.out.println("getTextoByCode: " + vKey.elementAt(n));
			v = (String) vKey.elementAt(n);
			if (v.equals(codigo)) {
				return (String) vTexto.elementAt(n);
			}
		}
		return "Error";
	}

	/**
	 * Retorna el código del elemento seleccionado
	 * @param indice posición del elemento a utilizar
	 * @return código (como String) o null si no encuentra nada.
	 */
	public String getCodeByIndex(int indice) {
		if (indice != -1) {
			return (String) vKey.elementAt(indice);
		} else {
			return null;
		}
	}

	/**
	 * Retorna el código del elemento con el texto (Descripción) suministrado
	 * @param texto Descripción a buscar
	 * @return código (como String) o null si no encuentra nada
	 */
	public String getCodeByName(String texto) {
		for (int n = 0; n < vKey.size(); n++) {
			if (vTexto.elementAt(n).equals(texto)) {
				return (String) vKey.elementAt(n);
			}
		}
		return null;
	}

	/**
	 * Retorna el código o el id del elemento indicado como un valor de tipo Long
	 * @param indice posición del elemento a retornar
	 * @return código o id (transformado a Long)
	 */
	public Long getKeyAsLongByIndex(int indice) {
		if (indice == -1) return 0L;
		String codigo = (String) vKey.elementAt(indice);
		Long valCodigo = Long.parseLong(codigo);
		return valCodigo;
	}
	
	/**
	 * Retorna el código del elemento con el texto (Descripción) suministrado, como un valor de tipo Long
	 * @param texto Descripción a buscar
	 * @return código (como Long) o -1L si no encuentra nada
	 */
	public Long getKeyAsLongByTexto(String texto) {
		for (int n = 0; n < vKey.size(); n++) {
			if (vTexto.elementAt(n).equals(texto)) {
				String codigo = (String) vKey.elementAt(n);
				return (Long.parseLong(codigo));
			}
		}
		return -1L;
	}
	
	public Object getObjectByIndex(int indice) {
		if (indice != -1) {
			if (vObjeto.size() > 0) {
				return vObjeto.elementAt(indice);
			} else {
				System.err.println("El vector de objetos no ha sido inicializado (ComboData.vObjeto)");
				return null;
			}
		} else {
			return null;
		}
	}
}
