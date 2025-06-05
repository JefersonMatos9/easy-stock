package com.easystock.model;

public class Payment {

	// O método de pagamento utilizado para realizar o pagamento.
	private final MethodPayment methodPayment;
	// Valor recebido do cliente.
	private Double valueReceived;
	// Valor pago, que pode ser diferente do valor recebido em casos de troco ou descontos.
	private Double amountPaid;
	// Identificador único do pagamento.
	private Long id;
	// Indica se o pagamento está relacionado a alguma promoção.
	private Boolean promotion;

	// Construtor que recebe o método de pagamento como parâmetro obrigatório.
	public Payment(MethodPayment methodPayment) {
		this.methodPayment = methodPayment;
	}

	// Retorna o valor recebido do cliente.
	public Double getValueReceived() {
		return valueReceived;
	}

	// Define o valor recebido do cliente.
	public void setValueReceived(Double valueReceived) {
		this.valueReceived = valueReceived;
	}

	// Retorna o valor pago.
	public Double getAmountPaid() {
		return amountPaid;
	}

	// Define o valor pago.
	public void setAmountPaid(Double amountPaid) {
		this.amountPaid = amountPaid;
	}

	// Retorna o identificador único do pagamento.
	public Long getId() {
		return id;
	}

	// Define o identificador único do pagamento.
	public void setId(Long id) {
		this.id = id;
	}

	// Retorna um indicador se o pagamento está relacionado a alguma promoção.
	public Boolean getPromotion() {
		return promotion;
	}

	// Define se o pagamento está relacionado a alguma promoção.
	public void setPromotion(Boolean promotion) {
		this.promotion = promotion;
	}

	// Retorna o método de pagamento utilizado.
	public MethodPayment getMethodPayment() {
		return methodPayment;
	}
}