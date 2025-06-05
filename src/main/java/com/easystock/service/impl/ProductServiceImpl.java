package com.easystock.service.impl;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Importe Transactional

import com.easystock.exception.productException.CategoryRequiredException;
import com.easystock.exception.productException.InsufficientQuantityException; // Importe a exceção
import com.easystock.exception.productException.ProductAlreadyRegisteredException;
import com.easystock.model.Category;
import com.easystock.model.Product;
import com.easystock.repository.CategoryRepository;
import com.easystock.repository.ProductRepository;
import com.easystock.service.exception.ProductNotFound;
import com.easystock.service.interfaces.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

	private final ProductRepository productRepository;
	private final CategoryRepository categoryRepository;

	@Autowired
	public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository) {
		this.productRepository = productRepository;
		this.categoryRepository = categoryRepository;
	}

	@Override
	@Transactional 
	public Product createProduct(Product product) {
		String normalizedName = product.getName().trim();

		if (productRepository.findByNameIgnoreCase(normalizedName).isPresent()) {
			throw new ProductAlreadyRegisteredException("Produto já registrado com o nome: " + normalizedName);
		}

		Category category = ensureCategoryExists(product.getCategory().getName());

		product.setName(normalizedName);
		product.setCategory(category);

		return productRepository.save(product);
	}

	@Override
	public Product read(Long id) {
		
		return productRepository.findById(id)
				.orElseThrow(() -> new ProductNotFound("Produto não encontrado para o id: " + id));
	}

	@Override
	@Transactional // Adicione @Transactional
	public Product update(Long id, Product product) {
		Product productUpdate = findProductById(id);

		if (product.getName() != null && !product.getName().trim().isEmpty()) {
			String normalizedName = product.getName().trim();
			Optional<Product> existingProduct = productRepository.findByNameIgnoreCase(normalizedName);
			if (existingProduct.isPresent() && !existingProduct.get().getId().equals(id)) {
				throw new ProductAlreadyRegisteredException("Já existe outro produto com o nome: " + normalizedName);
			}
			productUpdate.setName(normalizedName);
		}

		if (product.getPrice() != null) {
			productUpdate.setPrice(product.getPrice());
		}

		// A quantidade será atualizada diretamente aqui, sem validações adicionais.
		// As validações de estoque (suficiência) são feitas no OrderService.
		if (product.getQuantity() != null) {
			productUpdate.setQuantity(product.getQuantity());
		}

		if (product.getCategory() != null && product.getCategory().getName() != null && !product.getCategory().getName().trim().isEmpty()) {
			Category category = ensureCategoryExists(product.getCategory().getName());
			productUpdate.setCategory(category);
		} else if (product.getCategory() == null || product.getCategory().getId() == null) {
			throw new CategoryRequiredException("A categoria do produto não pode ser nula ou vazia.");
		}

		return productRepository.save(productUpdate);
	}

	@Override
	@Transactional 
	public Product delete(Long id) {
		Product deleteProduct = findProductById(id);
		productRepository.delete(deleteProduct);
		return deleteProduct;
	}

	@Override
	public Optional<Product> findByName(String name) {
		return productRepository.findByName(name);
	}

	@Override
	public List<Product> findAll() {
		return productRepository.findAll();
	}

	private Product findProductById(Long id) {
	
		return productRepository.findById(id)
				.orElseThrow(() -> new ProductNotFound("Produto não encontrado para o id: " + id));
	}

	private Category ensureCategoryExists(String categoryName) {
		String normalizedName = categoryName.trim();
		Optional<Category> existingCategory = categoryRepository.findByNameIgnoreCase(normalizedName);

		if (existingCategory.isPresent()) {
			return existingCategory.get();
		}
		Category newCategory = new Category();
		newCategory.setName(normalizedName);
		return categoryRepository.save(newCategory);
	}

	@Override
	public Optional<Product> findById(Long id) {
		return productRepository.findById(id);
	}

	@Override
	public List<Product> findProductsByNameContaining(String name) {
		return productRepository.findByNameContainingIgnoreCase(name);
	}

	@Override
	public List<Product> findProductsByCategory(Category category) {
		return productRepository.findByCategory(category);
	}

	@Override
	public List<Product> findLowStockProducts(int threshold) {
		return productRepository.findByQuantityLessThan(threshold);
	}

	@Override
	public Map<Category, List<Product>> groupProductsByCategory(List<Product> products) {
		return products.stream()
				.collect(Collectors.groupingBy(
						Product::getCategory,
						LinkedHashMap::new,
						Collectors.toList()
				));
	}

	// --- IMPLEMENTAÇÃO DOS NOVOS MÉTODOS DE ESTOQUE ---
	@Override
	@Transactional // Garante que a operação de estoque seja atômica
	public void reduceQuantity(Product product, int quantity) {
		Product productToUpdate = productRepository.findById(product.getId())
				.orElseThrow(() -> new ProductNotFound("Produto não encontrado com ID: " + product.getId()));

		if (productToUpdate.getQuantity() < quantity) {
			throw new InsufficientQuantityException("Quantidade insuficiente em estoque para o produto: " + productToUpdate.getName() +
					". Quantidade solicitada: " + quantity + ", disponível: " + productToUpdate.getQuantity());
		}
		productToUpdate.setQuantity(productToUpdate.getQuantity() - quantity);
		productRepository.save(productToUpdate);
		logger.info("Estoque do produto '{}' reduzido em {} unidades. Novo estoque: {}", productToUpdate.getName(), quantity, productToUpdate.getQuantity());
	}

	@Override
	@Transactional // Garante que a operação de estoque seja atômica
	public void addQuantity(Product product, int quantity) {
		Product productToUpdate = productRepository.findById(product.getId())
				.orElseThrow(() -> new ProductNotFound("Produto não encontrado com ID: " + product.getId()));

		productToUpdate.setQuantity(productToUpdate.getQuantity() + quantity);
		productRepository.save(productToUpdate);
		logger.info("Estoque do produto '{}' adicionado em {} unidades. Novo estoque: {}", productToUpdate.getName(), quantity, productToUpdate.getQuantity());
	}
	// --- FIM DA IMPLEMENTAÇÃO DOS NOVOS MÉTODOS ---

	@Override
	public Map<Product, Long> findTopSellingProducts(int i) {
		// TODO Auto-generated method stub
		return null;
	}
}