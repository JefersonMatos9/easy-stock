package com.easystock.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.Valid; // Importe esta, se ainda não estiver importada para @Valid em Category
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome é obrigatório.") // Validação para o nome
    @Column(unique = true)
    private String name;

    @NotNull(message = "A quantidade é obrigatória.") // Validação para quantidade não nula
    @Min(value = 0, message = "A quantidade não pode ser negativa.") // Validação para quantidade mínima de 0
    private Integer quantity;

    @NotNull(message = "O preço é obrigatório.") // Validação para preço não nulo
    @DecimalMin(value = "0.01", message = "O preço deve ser maior que zero.") // Validação para preço mínimo (0.01 ou mais)
    private BigDecimal price;

    private Boolean available;

    @ManyToOne
    @JoinColumn(name = "category", nullable = false)
    @NotNull(message = "A categoria é obrigatória.") // Validação para categoria não nula
    @Valid // Usar @Valid se você tiver validações dentro da classe Category também
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

    // Remova o 'throws ProductInvalidException' daqui, pois a validação será feita pelas anotações
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
        setAvailable();
    }

    public BigDecimal getPrice() {
        return price;
    }

    // Remova o 'throws InvalidPriceException' e a lógica de validação manual daqui.
    // A validação agora será feita pelas anotações.
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public boolean isAvailable() {
        return Boolean.TRUE.equals(available);
    }

    private void setAvailable() {
        this.available = (quantity != null && quantity > 0);
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}