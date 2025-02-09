package com.easystock.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.easystock.model.Product;
import com.easystock.service.interfaces.ProductService;

@Controller
@RequestMapping("/api/products")
public class ProductController {

	@Autowired
	private ProductService productService;
	
	@PostMapping
	public ResponseEntity<Product> create(@RequestBody Product product) {
		Product createdProduct = productService.createProduct(product);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Product> read(@PathVariable Long id) {
		Product product = productService.read(id);
				return ResponseEntity.ok(product);
	}
	
	@PutMapping("/{id}")
	public Product update (@PathVariable Long id, @RequestBody Product product){
		return productService.update(id, product);
	}
	
	@DeleteMapping("/{id}")
	public Product delete(@PathVariable Long id) {
		return productService.delete(id);
	}
}
