package com.easystock.model;

public class Payment {

	private final MethodPayment methodPayment;
	
	private Double valueReceived; // valor recebido
	
	private Double amountPaid; // valor pago
	
	private Long id;
	
	private Boolean promotion;
	
	public Payment(MethodPayment methodPayment) {
		this.methodPayment = methodPayment;
		}

	public Double getValueReceived() {
		return valueReceived;
	}

	public void setValueReceived(Double valueReceived) {
		this.valueReceived = valueReceived;
	}

	public Double getAmountPaid() {
		return amountPaid;
	}

	public void setAmountPaid(Double amountPaid) {
		this.amountPaid = amountPaid;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Boolean getPromotion() {
		return promotion;
	}

	public void setPromotion(Boolean promotion) {
		this.promotion = promotion;
	}

	public MethodPayment getMethodPayment() {
		return methodPayment;
	}
}
