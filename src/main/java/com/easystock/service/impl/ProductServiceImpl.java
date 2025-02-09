package com.easystock.service.impl;

import java.util.Optional;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.easystock.exception.productException.CategoryRequiredException;
import com.easystock.exception.productException.InsufficientQuantityException;
import com.easystock.exception.productException.InvalidNameProductException;
import com.easystock.exception.productException.InvalidPriceException;
import com.easystock.exception.productException.ProductAlreadyRegisteredException;
import com.easystock.exception.productException.RegisteredProductException;
import com.easystock.model.Product;
import com.easystock.repository.ProductRepository;
import com.easystock.service.exception.ProductNotFound;
import com.easystock.service.interfaces.ProductService;

import ch.qos.logback.classic.Logger;

@Service
public class ProductServiceImpl implements ProductService {
	
	private static final Logger logger = (Logger) LoggerFactory.getLogger(ProductServiceImpl.class);
	
	private final ProductRepository productRepository;
	
	@Autowired 
	public ProductServiceImpl(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	@Override
	public Product createProduct(Product product) {
		logger.info("Iniciando a validação do produto");
		validateProduct(product);
		
		logger.info("Verificando se o produto ja existe");
		verifyProductAlreadyRegistered(product.getName()); // verifica se o nome do produto ja existe
		
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Product delete(Long id) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Optional<Product> findByName(String name) {
		return productRepository.findByName(name);
	}
	
	
	private void validateProductExistence(Product product) {
		if(!productRepository.findByName(product.getName()).isPresent()) {
			throw new RegisteredProductException("Produto não encontrado.");
		}
	}
	
	private void validateProduct(Product product) {
		if(product.getName() == null || product.getName().trim().isEmpty()) {
			throw new InvalidNameProductException("Nome do produto é obrigatório");
		}
		if(product.getPrice() < 0) {
			throw new InvalidPriceException("Preço do produto não pode ser negativo.");
		}
		if(product.getCategory() == null) {
			throw new CategoryRequiredException("É obrigatorio adicionar uma categoria ao produto.");
		}
		if(product.getQuantity() < 0) {
			throw new InsufficientQuantityException("Quantidade do produto não pode ser negativo");
		}
	}
	
	private void verifyProductAlreadyRegistered(String name) {
		logger.info("Verificando Produto");
		Optional<Product>existingProduct = productRepository.findByName(name);
		logger.info("Produto encontrado: ");
		if(existingProduct.isPresent()) {
			throw new ProductAlreadyRegisteredException("Produto já registrado com o nome: " + name);
		}
	}
}
