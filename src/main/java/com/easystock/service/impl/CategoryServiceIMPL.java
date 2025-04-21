package com.easystock.service.impl;

import java.util.List;

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
	public Category read(Integer id) {
		return categoryRepository.findById(id)
				.orElseThrow(() -> new CategoryRequiredException("Categoria não encontrada para o id: " + id));
	}

	@Override
	public Category update(Integer id, Category category) {
		Category categoryUpdate = findById(id);
		updateCategoryFields(categoryUpdate, category);
		return categoryRepository.save(categoryUpdate);
	}

	@Override
	public Category delete(Integer id) {
		Category deleteCategory = findById(id);
		if (deleteCategory.getProducts() != null && !deleteCategory.getProducts().isEmpty()) {
			throw new CategoryRequiredException(
					"Não é possivel excluir a categoria. Existem produtos vinculados a ela.");
		}
		categoryRepository.delete(deleteCategory);
		return deleteCategory;
	}

	
	@Override
	   public List<Category> listarCategorias() {
        return categoryRepository.findAll();
    }
	
	
	
	private void updateCategoryFields(Category categoryUpdate, Category category) {
		if (category.getName() == null || category.getName().trim().isEmpty()) { // .trim()remove espaços antes e depois
																					// do nome}
			throw new CategoryRequiredException("O nome da categoria não pode ser nulo ou vazio");
		}
		categoryUpdate.setName(category.getName().trim());
	}

	public Category findById(Integer id) {
		return categoryRepository.findById(id)
				.orElseThrow(() -> new CategoryRequiredException("Categoria não encontrada para o id: " + id));
	}

	private void validateCategory(Category category) {
		if (category.getName() == null || category.getName().trim().isEmpty()) {
			throw new CategoryRequiredException("Categoria não pode ser nula ou vazia");
		}
		String normalizedName = category.getName().trim();

		if (categoryRepository.findByNameIgnoreCase(normalizedName).isPresent()) {
			throw new CategoryRequiredException("Categoria ja registrada " + normalizedName);

		}
	}

}
