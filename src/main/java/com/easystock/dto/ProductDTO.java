package com.easystock.dto;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import com.easystock.model.Category;
import com.easystock.model.Product;

@Component
public class ProductDTO {

	private String name;
	private BigDecimal price;
	private Integer quantity;
	private String categoryName;
	
	 // Construtor vazio - é como criar uma caixa vazia para depois colocar coisas dentro
	public ProductDTO(){}
	
	 // Construtor que transforma um Product em ProductDTO
    // É como pegar alguns brinquedos da caixa grande e colocar na caixa pequena
	public ProductDTO(Product product) {
		this.name = product.getName();
        this.price = product.getPrice();
        this.quantity = product.getQuantity();
        this.categoryName = product.getCategory().getName();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	

    // Método para transformar o DTO de volta em Product
    // É como pegar as coisas da caixa pequena e colocar na caixa grande
	public Product toProduct() {
		Product product = new Product();
		  product.setName(this.name);
	        product.setPrice(this.price);
	        product.setQuantity(this.quantity);
	        
	        Category category = new Category();
	        category.setName(this.categoryName);
	        product.setCategory(category);
	        
	        return product;     //EDITAR ANOTAÇÕES DO SPRIING
	}
			
}
