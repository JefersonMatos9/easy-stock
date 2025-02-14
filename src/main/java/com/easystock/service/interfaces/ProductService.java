package com.easystock.service.interfaces;

import java.util.Optional;

import com.easystock.model.Product;

public interface ProductService {

	Product createProduct(Product product); // criar produto
	Product read(Long id); // buscar por id
	Product update(Long id, Product product); //atualizar
	Product delete(Long id); //deletar
	
	Optional<Product> findByName(String name); // procurar nome
	Optional<Product> findById(Long id);
}
