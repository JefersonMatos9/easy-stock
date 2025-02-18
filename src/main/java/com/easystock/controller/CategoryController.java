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

import com.easystock.model.Category;
import com.easystock.service.interfaces.CategoryService;

@Controller
@RequestMapping("/api/category")
public class CategoryController {
	
	@Autowired
	CategoryService categoryService;
	
	@PostMapping
	public ResponseEntity<Category>create(@RequestBody Category category){
		Category createdCategory = categoryService.createCategory(category);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdCategory);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Category>read(@PathVariable Long id){
		Category category = categoryService.read(id);
		return ResponseEntity.ok(category);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Category>update(@PathVariable Long id, @RequestBody Category category){
		Category updateCategory = categoryService.update(id, category);
		return ResponseEntity.ok(updateCategory);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void>delete(@PathVariable Long id){
		categoryService.delete(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}
