package com.easystock.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum CategoryProduct {

    SALGADOS("Salgados"),
    LANCHES("Lanches"),
    BEBIDAS("Bebidas"),
    SOBREMESAS("Sobremesas"),
    DOCES("Doces");

    private final String descricao;

    // Construtor para associar a descrição com cada categoria
    CategoryProduct(String descricao) {
        this.descricao = descricao;
    }

    
    // Método para obter a descrição de cada categoria
    @JsonValue
    public String getDescricao() {
        return descricao;
    }
    
    @JsonCreator
    public static CategoryProduct fromValue(String value) {
        for (CategoryProduct category : CategoryProduct.values()) {
            if (category.descricao.equalsIgnoreCase(value)) {
                return category;
            }
        }
        throw new IllegalArgumentException("Categoria inválida: " + value);
    }
    
    
}
