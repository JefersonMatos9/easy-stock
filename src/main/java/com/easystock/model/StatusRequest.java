package com.easystock.model;

// Enumeração que representa os possíveis status de um pedido.
public enum StatusRequest {

	// O pedido foi recebido.
	RECEBIDO,
	// O pedido está em processo de preparação.
	EM_PREPARACAO,
	// O pedido está pronto para entrega.
	PRONTO,
	// O pedido foi entregue.
	ENTREGUE,
	// O pedido foi cancelado.
	CANCELADO;

	// Método estático para desserializar o status do pedido a partir de uma string.
	// Utilizado pelo Jackson para converter strings JSON em valores do enum.
	// A anotação @JsonCreator indica que este método deve ser usado para a desserialização.
	public static StatusRequest fromString(String value) {
		return StatusRequest.valueOf(value.toUpperCase());
	}

	// Método para serializar o status do pedido para uma string em lowercase.
	// Utilizado pelo Jackson para converter valores do enum em strings JSON.
	// A anotação @JsonValue indica que este método deve ser usado para a serialização.
	public String toLowerCase() {
		return this.name().toLowerCase();
	}
}