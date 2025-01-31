package com.easystock.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.easystock.model.Product;
import com.easystock.service.interfaces.ProductService;

@RestController
@RequestMapping("/products")
public class ProductController {

	@Autowired
	private ProductService productService;
	
	@PostMapping
	public Product create(@RequestBody Product product) {
		return productService.create(product);
	}
	
	@GetMapping("/{id}")
	public Product read(@PathVariable Long id) {
		return productService.read(id);
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
