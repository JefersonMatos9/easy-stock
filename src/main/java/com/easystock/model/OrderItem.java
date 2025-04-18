package com.easystock.model;

import java.math.BigDecimal;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "order_items")
public class OrderItem {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "product_id")
	private Product product;

	@ManyToOne
	@JoinColumn(name = "order_id", nullable = false)
	private Order order;

	private int quantity;

	private String observation;

	private BigDecimal subTotal;

	public OrderItem() {
		// Construtor padrão necessário para JPA
	}

	public OrderItem(Product product, int quantity, String observation) {
		this.product = product;
		this.quantity = quantity;
		this.observation = observation;
		this.calculateSubTotal();
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
		calculateSubTotal();
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
		calculateSubTotal();
	}

	public String getObservation() {
		return observation;
	}

	public void setObservation(String observation) {
		this.observation = observation;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public BigDecimal getSubTotal() {
		return subTotal;
	}

	public void setSubTotal(BigDecimal subTotal) {
		this.subTotal = subTotal;
	}

	private void calculateSubTotal() {
		if (product != null && product.getPrice() != null) {
			this.subTotal = product.getPrice().multiply(BigDecimal.valueOf(quantity));
		} else {
			this.subTotal = BigDecimal.ZERO;
		}
	}
}