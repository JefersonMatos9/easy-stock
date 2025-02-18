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

import com.easystock.dto.ProductDTO;
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
	public ResponseEntity<Product> update (@PathVariable Long id, @RequestBody Product product){
		Product updateProduct = productService.update(id,product);
		return ResponseEntity.ok(updateProduct);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Product> delete(@PathVariable Long id) {
		Product deleteProduct = productService.delete(id);
		return ResponseEntity.ok(deleteProduct);
	}
}

