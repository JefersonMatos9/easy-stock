package com.easystock.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "categorias_produtos")
public class Category {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "nome" , nullable = false)
	private String name;
	
	
	public Category() { }
	
	@JsonCreator
	public Category(@JsonProperty("id") Integer id, @JsonProperty("name") String name) {
		this.id = id;
		this.name = name;
	}
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	@JsonValue
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
