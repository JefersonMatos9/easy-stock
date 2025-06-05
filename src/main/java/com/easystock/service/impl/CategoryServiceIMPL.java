package com.easystock.service.impl;

import java.util.List;
import java.util.Optional;

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
                .orElseThrow(() -> new CategoryRequiredException("Category not found for id: " + id));
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
                    "Cannot delete category. There are products associated with it.");
        }
        categoryRepository.delete(deleteCategory);
        return deleteCategory;
    }

    @Override
    public List<Category> listarCategorias() {
        return categoryRepository.findAll();
    }

    private void updateCategoryFields(Category categoryUpdate, Category category) {
        if (category.getName() == null || category.getName().trim().isEmpty()) {
            throw new CategoryRequiredException("Category name cannot be null or empty.");
        }
        categoryUpdate.setName(category.getName().trim());
    }

    public Category findById(Integer id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryRequiredException("Category not found for id: " + id));
    }

    private void validateCategory(Category category) {
        if (category.getName() == null || category.getName().trim().isEmpty()) {
            throw new CategoryRequiredException("Category cannot be null or empty.");
        }
        String normalizedName = category.getName().trim().toLowerCase();

        Optional<Category> existingCategory = categoryRepository.findByNameIgnoreCase(normalizedName);

        if (existingCategory.isPresent() && (category.getId() == null || !existingCategory.get().getId().equals(category.getId()))) {
            throw new CategoryRequiredException("Category already registered: " + normalizedName);
        }
    }

    @Override
    public Optional<Category> findByNameIgnoreCase(String name) {
        return categoryRepository.findByNameIgnoreCase(name);
    }

	@Override
	public Object findAll() {
		// TODO Auto-generated method stub
		return null;
	}
}