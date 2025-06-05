package com.easystock.service.interfaces;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.easystock.model.Category;
import com.easystock.model.Product;

// Esta interface define os serviços relacionados à entidade Product.
// Ela declara os métodos que as classes de serviço que a implementam devem fornecer.
public interface ProductService {

	// Cria um novo produto.
	Product createProduct(Product product); // criar produto

	// Busca um produto pelo seu ID.
	Product read(Long id); // buscar por id

	// Atualiza um produto existente.
	Product update(Long id, Product product); // atualizar

	// Exclui um produto.
	Product delete(Long id); // deletar

	// Busca um produto pelo nome.
	Optional<Product> findByName(String name); // procurar nome

	// Busca um produto pelo ID.
	Optional<Product> findById(Long id);

	// Busca todos os produtos.
	List<Product> findAll();

	// Busca produtos cujo nome contém a string de pesquisa (case-insensitive)
	List<Product> findProductsByNameContaining(String name);

	// Busca produtos por categoria
	List<Product> findProductsByCategory(Category category);

	// Busca produtos com quantidade abaixo de um determinado limite (para dashboard)
	List<Product> findLowStockProducts(int threshold);

	// Agrupa produtos por categoria para exibição em listas
	Map<Category, List<Product>> groupProductsByCategory(List<Product> products);

	Map<Product, Long> findTopSellingProducts(int i);

	void addQuantity(Product product, int quantity);

	void reduceQuantity(Product product, int quantity);
}