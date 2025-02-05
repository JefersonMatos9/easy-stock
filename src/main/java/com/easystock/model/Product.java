package com.easystock.model;

import com.easystock.enums.CategoryProduct;
import com.easystock.exception.productException.InsufficientQuantityException;
import com.easystock.exception.productException.InsufficientStockException;
import com.easystock.exception.productException.InvalidPriceException;
import com.easystock.exception.productException.ProductInvalidException;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name= "products")
public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(unique = true)
	private String name;
	
	private int quantity;
	private float price;
	private boolean available;
	private CategoryProduct category; // criar a classe no pacote enums
	
	
	
	
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

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity)throws ProductInvalidException {
		if(quantity > 0) {
		this.quantity = quantity;
		setAvailable();
		}else {
			throw new ProductInvalidException("Quantidade do produto não pode ser negativo ou zero");
		}
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price)throws InvalidPriceException {
		if(price > 0) {
			this.price = price;
		}else {
			throw new InvalidPriceException ("O preço não pode ser negativo ou zero");
		}
	}

	public boolean isAvailable() {
		return available;
	}

	public void setAvailable() {
		this.available = quantity > 0;
		}

	public CategoryProduct getCategory() {
		return category;
	}

	public void setCategory(CategoryProduct category) {
		this.category = category;
	}

	private void decreaseStock(int quantity)throws InsufficientStockException {
		if(this.quantity < quantity ) {
			throw new InsufficientStockException("Não há estoque suficiente para o produto '" + getName() + "'. Quantidade solicitada: " + quantity + ", quantidade disponível: " + getQuantity() + ".");
		}
	}

	private void increaseStock(int quantity)throws InsufficientQuantityException {
		if(quantity <= 0) {
			throw new InsufficientQuantityException("A quantidade adicionada deve ser maior que zero");
		}
		this.quantity += quantity;
	}
}
