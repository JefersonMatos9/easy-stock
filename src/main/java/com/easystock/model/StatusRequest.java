package com.easystock.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum StatusRequest {

	RECEBIDO,

	EM_PREPARACAO,

	PRONTO,

	ENTREGUE,

	CANCELADO;

	@JsonCreator
	public static StatusRequest fromString(String value) {
		return StatusRequest.valueOf(value.toUpperCase());
	}

	@JsonValue
	public String toLowerCase() {
		return this.name().toLowerCase();
	}
}
 	