package com.easystock.model;

import java.math.BigDecimal;

import com.easystock.exception.productException.InsufficientQuantityException;
import com.easystock.exception.productException.InsufficientStockException;
import com.easystock.exception.productException.InvalidPriceException;
import com.easystock.exception.productException.ProductInvalidException;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "products")
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true)
	private String name;

	private Integer quantity;
	private BigDecimal price;
	private Boolean available;

	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "category", nullable = false)
	private Category category;

	public Product() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) throws ProductInvalidException {
		this.quantity = quantity;
		setAvailable();
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) throws InvalidPriceException {
		this.price = price;
	}

	public boolean isAvailable() {
		return Boolean.TRUE.equals(available);  // se o available for nulo ele retorna um false automaticamente
	}

	public void setAvailable() {
		this.available = (quantity > 0 || quantity != null);
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	private void decreaseStock(int quantity) throws InsufficientStockException {
		if (this.quantity < quantity) {
			throw new InsufficientStockException("Não há estoque suficiente para o produto '" + getName()
					+ "'. Quantidade solicitada: " + quantity + ", quantidade disponível: " + getQuantity() + ".");
		}
		this.quantity -= quantity;
	}

	private void increaseStock(int quantity) throws InsufficientQuantityException {
		if (quantity <= 0) {
			throw new InsufficientQuantityException("A quantidade adicionada deve ser maior que zero");
		}
		this.quantity += quantity;
	}
}
