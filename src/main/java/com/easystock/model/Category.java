package com.easystock.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "categorias_produtos")
public class Category {

	// Identificador único da categoria. Gerado automaticamente pelo banco de dados.
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	// Nome da categoria. Não pode ser nulo.
	@Column(name = "nome", nullable = false)
	private String name;

	// Construtor padrão sem argumentos. Necessário para o JPA.
	public Category() {
	}

	// Construtor com anotação @JsonCreator para desserialização do JSON.
	// Usado quando a categoria é criada a partir de um JSON, como em uma requisição de API.
	public Category(@JsonProperty("id") Integer id, @JsonProperty("name") String name) {
		this.id = id;
		this.name = name;
	}

	// Lista de produtos que pertencem a esta categoria.
	// O relacionamento é OneToMany (uma categoria para muitos produtos).
	// CascadeType.ALL significa que se uma categoria for persistida, atualizada ou removida,
	// os produtos associados também serão.
	// orphanRemoval=true significa que se um produto for removido da lista de produtos de uma categoria,
	// ele será automaticamente removido do banco de dados.
	@OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Product> products = new ArrayList<>();

	// Retorna a lista de produtos desta categoria.
	public List<Product> getProducts() {
		return products;
	}

	// Retorna o ID da categoria.
	public Integer getId() {
		return id;
	}

	// Define o ID da categoria.
	public void setId(Integer id) {
		this.id = id;
	}

	// Retorna o nome da categoria.
	// A anotação @JsonValue indica que este método deve ser usado para serializar
	// o nome da categoria para JSON. Útil se você quiser que o nome da categoria
	// seja a representação padrão da categoria em JSON em alguns contextos.
	@JsonValue
	public String getName() {
		return name;
	}

	// Define o nome da categoria.
	public void setName(String name) {
		this.name = name;
	}
}