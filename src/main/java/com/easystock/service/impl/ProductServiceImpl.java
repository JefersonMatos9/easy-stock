package com.easystock.service.impl;

import java.util.Optional;

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
import com.easystock.service.interfaces.ProductService;

@Service
public class ProductServiceImpl implements ProductService {
	
	private final ProductRepository productRepository;
	
	@Autowired 
	public ProductServiceImpl(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	@Override
	public Product create(Product product) {
		validateProduct(product);
		verifyProductAlreadyRegistered(product.getName()); // verifica se o nome do produto ja existe
		return productRepository.save(product);
	}

	@Override
	public Product read(Long id) {
		// TODO Auto-generated method stub
		return null;
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
		// TODO Auto-generated method stub
		return Optional.empty();
	}
	
	
	private void validateProductExistence(Product product) {
		if(productRepository.findByName(product.getName()) == null) {
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
		if(productRepository.findByName(name).isPresent()) {
			throw new ProductAlreadyRegisteredException("Produto já registrado com o nome: " + name);
		}
	}

	
}
