package com.easystock.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.easystock.exception.productException.CategoryRequiredException;
import com.easystock.model.Category;
import com.easystock.repository.CategoryRepository;
import com.easystock.service.interfaces.CategoryService;

@Service
public class CategoryServiceIMPL implements CategoryService {

	private final CategoryRepository categoryRepository;
	
	@Autowired
	public CategoryServiceIMPL(CategoryRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}
	
	@Override
	public Category createCategory(Category category) {
		
			validateCategory(category);
		
		return categoryRepository.save(category);
	}

	@Override
	public Category read(Long id) {
		return categoryRepository.findById(id)
				.orElseThrow(()->
				new CategoryRequiredException("Categoria não encontrada para o id: " + id));
	}

	@Override
	public Category delete(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Category update(Long id, Category category) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	private void validateCategory(Category category) {
	if(category.getName() == null || category.getName().trim().isEmpty()) {
		throw new CategoryRequiredException("Categoria não pode ser nula ou vazia");
	}
	String normalizedName = category.getName().trim();
	
	if (categoryRepository.findByNameIgnoreCase(normalizedName).isPresent()) {
		throw new CategoryRequiredException("Categoria ja registrada " + normalizedName);
		
		}	
	}

	
}
