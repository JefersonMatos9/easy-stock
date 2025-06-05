package com.easystock.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.easystock.model.Category;
import com.easystock.model.Product;

// Esta interface define o repositório para a entidade Product.
// Ela estende JpaRepository, que fornece métodos para operações CRUD (Create, Read, Update, Delete)
// e outros métodos úteis para interagir com o banco de dados.
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

	// Este método busca um produto pelo nome, com correspondência exata.
	// Retorna um Optional<Product> para indicar que o produto pode ou não ser encontrado.
	Optional<Product> findByName(String name);

	// Este método busca um produto pelo seu ID.
	// Retorna um Optional<Product> para indicar que o produto pode ou não ser encontrado.
	Optional<Product> findById(Long id);

	// Este método busca um produto pelo nome, ignorando a caixa alta/baixa.
	// Retorna um Optional<Product> para indicar que o produto pode ou não ser encontrado.
	Optional<Product> findByNameIgnoreCase(String name);

	// Novo método: Encontra produtos cujo nome contém a string de pesquisa (case-insensitive)
	List<Product> findByNameContainingIgnoreCase(String name);

	// Novo método: Encontra produtos por categoria
	List<Product> findByCategory(Category category);

	// Novo método: Encontra produtos com quantidade abaixo de um determinado limite
	List<Product> findByQuantityLessThan(Integer quantity);

	// Por padrão, JpaRepository fornece métodos como:
	// - save(Product entity): Salva ou atualiza um produto.
	// - findAll(): Retorna todos os produtos.
	// - delete(Product entity): Exclui um produto.
	// - deleteById(Long id): Exclui um produto pelo seu ID.
	// Outros métodos podem ser derivados dos nomes dos métodos ou usando a anotação @Query.

	// Você pode adicionar métodos personalizados aqui, se precisar de funcionalidades específicas do repositório de produtos.
	// Por exemplo:
	// - List<Product> findByPriceGreaterThan(BigDecimal price);
	// - List<Product> findByAvailableTrue();
	// - List<Product> findByCategory(Category category);
}