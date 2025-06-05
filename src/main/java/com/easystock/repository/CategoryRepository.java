package com.easystock.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.easystock.model.Category;

// Esta interface define o repositório para a entidade Category.
// Ela estende JpaRepository, que fornece métodos para operações CRUD (Create, Read, Update, Delete)
// e outros métodos úteis para interagir com o banco de dados.
public interface CategoryRepository extends JpaRepository<Category, Integer> {

	// Este método busca uma categoria pelo nome, a correspondência deve ser exata.
	// Retorna um Optional<Category> para indicar que a categoria pode ou não ser encontrada.
	public Optional<Category> findByName(String name);

	// Este método busca uma categoria pelo nome, ignorando a caixa alta/baixa.
	// Retorna um Optional<Category> para indicar que a categoria pode ou não ser encontrada.
	Optional<Category> findByNameIgnoreCase(String name);

	//Este método busca uma categoria pelo ID.
	Optional<Category> findById(Integer id);
}