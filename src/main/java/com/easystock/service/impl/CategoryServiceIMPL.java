package com.easystock.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.easystock.exception.categoryException.CategoryException;
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
		if(category.getName() == null || category.getName().trim().isEmpty()) {
			throw new CategoryException("Categoria não pode ser nula ou vazia");	
		}
		//converte o nome para minusculo para comparação
		String nomeNormalizado = category.getName().trim().toLowerCase();
		
		if(categoryRepository.findByName(category.getName()).isPresent()){
			throw new CategoryException("Categoria ja registrada " + category.getName());
		}
		//define o nome normalizado na categoria antes de salvar
		category.setName(nomeNormalizado);
		
		return categoryRepository.save(category);
	}

	@Override
	public Category read(Long id) {
		// TODO Auto-generated method stub
		return null;
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

	
}
