package rcp.manticora.services;

import java.util.ArrayList;
import java.util.List;

public class TourNumberValidator {

	private List<String> messageList = new ArrayList<String>();


	public boolean validate(String tourNumber) {
		boolean result = false;

		if (tourNumber != null) {
			if (tourNumber.equalsIgnoreCase("")) {
				messageList.add("Debe suministrar un número de gira.");
			}
			
			if (!hasOnlyNumbersAndDash(tourNumber)) {
				messageList.add("El número de gira tiene caracteres inválidos (no numéricos).");
			}

			if (!validarFormato(tourNumber)) {
				messageList.add("El número de gira tiene un formato incorrecto.  Debe ser del tipo ####-####.");
			}

			if (messageList.isEmpty()) {
				if (!hasValidMonth(tourNumber)) {
					messageList.add("Debe seleccionar un mes válido para el número de gira.");
				}

				if (!hasValidSequence(tourNumber)) {
					messageList.add("La secuencia no puede tener más de cuatro dígitos.");
				}
			}

		}

		if (messageList.isEmpty()) {
			result = true;
		}

		return result;
	}


	private boolean hasOnlyNumbersAndDash(String tourNumber) {
		String regex = "^[\\d-]+$";
		return tourNumber.matches(regex);
	}

	private boolean validarFormato(String tourNumber) {
		String regex = "^[0-9]{4}-\\d{1,4}$";
		return tourNumber.matches(regex);
	}

	private boolean hasValidMonth(String tourNumber) {
		Integer mes = Integer.parseInt(tourNumber.substring(2, 4));
		if (mes.intValue() >= 1 && mes.intValue() <= 12) {
			return true;
		}
		return false;
	}

	private boolean hasValidSequence(String tourNumber) {
		String s = tourNumber.substring(5, tourNumber.length());
		Integer secuencial = Integer.parseInt(s);
		if (secuencial > 9999) {
			return false;
		}
		return true;
	}

	public String getMessage() {
		String message = null;
		if (!messageList.isEmpty()) {
			message = messageList.get(0);
		}
		return message;
	}


}
