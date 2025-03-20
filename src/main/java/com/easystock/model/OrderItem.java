package com.easystock.model;

import java.math.BigDecimal;

public class OrderItem {

	private Long id;
	private Product product;
	private Integer quantity;
	private String observation;
	private BigDecimal subTotal;
	
	
	public OrderItem(Product product, Integer quantity, String observation) {

		this.product = product;
		this.quantity = quantity;
		this.observation = observation;
		this.subTotal = calculateSubTotal();
	}
	
	public BigDecimal calculateSubTotal() {
		if(product == null || product.getPrice() == null || quantity == null || quantity <= 0) {
			return BigDecimal.ZERO;
		}
		return product.getPrice().multiply(BigDecimal.valueOf(quantity));
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
		this.subTotal = calculateSubTotal();
	}

	public Integer getQuantity() {
		return quantity;
		
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
		this.subTotal = calculateSubTotal();
	}

	public String getObservation() {
		return observation;
	}

	public void setObservation(String observation) {
		this.observation = observation;
	}

	public BigDecimal getSubTotal() {
		return calculateSubTotal();
	}
}
