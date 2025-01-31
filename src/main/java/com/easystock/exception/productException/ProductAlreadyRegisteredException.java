package com.easystock.exception.productException;

public class ProductAlreadyRegisteredException extends RuntimeException {

	public ProductAlreadyRegisteredException(String message) {
		super(message);
	}
}
