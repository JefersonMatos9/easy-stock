package com.easystock.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.easystock.model.Product;



@Repository
public interface ProductRepository extends JpaRepository<Product, Long>{

	Optional<Product> findByName(String name);
	
	Optional<Product> findById(Long id);
	
}
