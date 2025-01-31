package com.easystock.exception.productException;

public class InsufficientQuantityException extends RuntimeException {

	public InsufficientQuantityException(String message) {
		super(message);
	}
}
