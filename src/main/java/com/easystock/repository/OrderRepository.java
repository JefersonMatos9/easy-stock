package com.easystock.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.easystock.model.Order;
import com.easystock.model.Product;

// Esta interface define o repositório para a entidade Order.
// Ela estende JpaRepository, que fornece métodos para operações CRUD (Create, Read, Update, Delete)
// e outros métodos úteis para interagir com o banco de dados.
@Repository // Embora o JpaRepository já forneça a funcionalidade de repositório, o @Repository pode ser usado para fornecer mais semântica ou para permitir a personalização, se necessário.
public interface OrderRepository extends JpaRepository<Order, Long>{

	// Consulta para obter os produtos mais vendidos (top N)
	// Soma as quantidades de cada produto em todos os itens de pedido,
	// agrupa por produto e ordena de forma decrescente.
	@Query("SELECT oi.product, SUM(oi.quantity) " +
			"FROM Order o JOIN o.items oi " +
			"GROUP BY oi.product " +
			"ORDER BY SUM(oi.quantity) DESC")
	List<Object[]> findTopSellingProducts(org.springframework.data.domain.Pageable pageable);

	// Consulta para obter os produtos menos vendidos (bottom N)
	// Soma as quantidades de cada produto em todos os itens de pedido,
	// agrupa por produto e ordena de forma crescente.
	@Query("SELECT oi.product, SUM(oi.quantity) " +
			"FROM Order o JOIN o.items oi " +
			"GROUP BY oi.product " +
			"ORDER BY SUM(oi.quantity) ASC")
	List<Object[]> findLeastSellingProducts(org.springframework.data.domain.Pageable pageable);

	// Opcional: Produtos que nunca foram vendidos (se o Product não tiver OrderItem)
	@Query("SELECT p FROM Product p WHERE p.id NOT IN (SELECT oi.product.id FROM OrderItem oi)")
	List<Product> findProductsNeverSold();
}