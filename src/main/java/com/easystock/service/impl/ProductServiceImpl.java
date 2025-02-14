package com.easystock.service.impl;

import java.math.BigDecimal;
import java.util.Optional;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.easystock.exception.productException.CategoryRequiredException;
import com.easystock.exception.productException.InsufficientQuantityException;
import com.easystock.exception.productException.InvalidNameProductException;
import com.easystock.exception.productException.InvalidPriceException;
import com.easystock.exception.productException.ProductAlreadyRegisteredException;
import com.easystock.exception.productException.ProductInvalidException;
import com.easystock.exception.productException.RegisteredProductException;
import com.easystock.model.Category;
import com.easystock.model.Product;
import com.easystock.repository.CategoryRepository;
import com.easystock.repository.ProductRepository;
import com.easystock.service.exception.ProductNotFound;
import com.easystock.service.interfaces.ProductService;

import ch.qos.logback.classic.Logger;

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
	public Product createProduct(Product product) {
		logger.info("Iniciando a validação do produto");
		validateProduct(product);
		
		logger.info("Verificando se o produto ja existe");
		verifyProductAlreadyRegistered(product.getName()); // verifica se o nome do produto ja existe
		
		 logger.info("Verificando e criando categoria, se necessário");
		Category category = ensureCategoryExists(product.getCategory().getName());
		product.setCategory(category);
		
		logger.info("Salvando o produto no banco de dados");
		return productRepository.save(product);
	}

	@Override
	public Product read(Long id) {
		return productRepository.findById(id)
		.orElseThrow(()->
			 new ProductNotFound("Produto não encontrado para o id: " + id));
		}
		

	@Override
	public Product update(Long id, Product product) {
		Product productUpdate = findProductById(id);
		updateProductFields(productUpdate, product);
		
		return productRepository.save(productUpdate);
		
	}

	@Override
	public Product delete(Long id) {
		Product deleteProduct = findProductById(id);
		validateProductExistence(deleteProduct); //Passando o produto como parâmetro
		productRepository.delete(deleteProduct); //O método delete do repository retorna void
		return deleteProduct;
	}
	
	@Override
	public Optional<Product> findByName(String name) {
		return productRepository.findByName(name);
	}

	
	private Product findProductById(Long id){
		return productRepository.findById(id).
				orElseThrow(()-> new ProductNotFound("Produto não encontrado para o id: " + id));
	}
	
	
	private void validateProductExistence(Product product) {
		if(productRepository.findByName(product.getName()).isEmpty()) {
			throw new RegisteredProductException("Produto não encontrado.");
		}
	}
	
	private void validateProduct(Product product) {
		validateName(product.getName());
		validatePrice(product.getPrice());
		validateCategory(product.getCategory());
		validateQuantity(product.getQuantity());
	
	}
	
	private void verifyProductAlreadyRegistered(String name) {
		logger.info("Verificando Produto");
		Optional<Product>existingProduct = productRepository.findByName(name);
		logger.info("Produto encontrado: ");
		if(existingProduct.isPresent()) {
			throw new ProductAlreadyRegisteredException("Produto já registrado com o nome: " + name);
		}
	}
	
	private void validateName(String name) {
		if(name == null || name.trim().isEmpty()) {
			throw new InvalidNameProductException("Nome do produto é obrigatório");
		}	
	}
	
	private void validatePrice(BigDecimal price) {
		if(price == null) {
			throw new InvalidPriceException("O preço não pode ser nulo.");
		}
	    if(price.compareTo(BigDecimal.ZERO) <= 0) {
	    	throw new InvalidPriceException("O preço não pode ser negativo ou zero.");
	    }
	}
	
	private void validateQuantity(Integer quantity) {
		if(quantity == null ) {
			throw new InsufficientQuantityException("Quantidade do produto não pode ser nula");
		}
		if(quantity < 0) {
			throw new InsufficientQuantityException("Quantidade do produto não pode ser negativo");
		}
	}
	
	private void validateCategory(Category category) {
		if(category == null || category.getName() == null || category.getName().trim().isEmpty()) {
			throw new CategoryRequiredException("É obrigatorio adicionar uma categoria ao produto.");
		}
	}
	
	private Category ensureCategoryExists(String categoryName) {
		 return categoryRepository.findByName(categoryName)
	                .orElseGet(() -> {
	                    Category newCategory = new Category();
	                    newCategory.setName(categoryName);
	                    return categoryRepository.save(newCategory);
	                });
	}
	
	private void updateProductFields(Product productUpdate , Product product) {
	if(product.getName() != null && !product.getName().trim().isEmpty()) {   //.trim()remove espaços antes e depois do nome
		productUpdate.setName(product.getName());
	}
	
	if(product.getPrice() != null) {
		validatePrice(product.getPrice());
		productUpdate.setPrice(product.getPrice());
	}
	
	if(product.getQuantity() != null) {
		validateQuantity(product.getQuantity());
		productUpdate.setQuantity(product.getQuantity());
	}
	
	if(product.getCategory() != null) {
		Category category = ensureCategoryExists(product.getCategory().getName());
		productUpdate.setCategory(category);
	}
	}

	public Optional<Product> findById(Long id) {
		// TODO Auto-generated method stub
		return Optional.empty();
	}
	}


