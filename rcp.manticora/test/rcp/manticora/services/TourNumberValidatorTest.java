package rcp.manticora.services;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class TourNumberValidatorTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testValidate_Happy() {
		String tourNumber = "1402-111";
		TourNumberValidator validator = new TourNumberValidator();
		boolean result = validator.validate(tourNumber);
		assertTrue(validator.getMessage(), result);
	}
	
	@Test
	public void testValidate_EmptyTourNumber() {
		String tourNumber = "";
		TourNumberValidator validator = new TourNumberValidator();
		boolean result = validator.validate(tourNumber);
		assertFalse(result);
	}
	
	@Test
	public void testValidate_InvalidCharacters() {
		String tourNumber = "1A01-0999";
		TourNumberValidator validator = new TourNumberValidator();
		boolean result = validator.validate(tourNumber);
		assertFalse(result);
	}
	
	@Test
	public void testValidate_WrongTourFormat() {
		String tourNumber = "144444019-0999";
		TourNumberValidator validator = new TourNumberValidator();
		boolean result = validator.validate(tourNumber);
		assertFalse(result);
	}
	
	@Test
	public void testValidate_InvalidMonth() {
		String tourNumber = "0013-0999";
		TourNumberValidator validator = new TourNumberValidator();
		boolean result = validator.validate(tourNumber);
		assertFalse(result);
	}
	
}
