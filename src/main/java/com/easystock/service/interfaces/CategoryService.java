package com.easystock.service.interfaces;

import com.easystock.model.Category;

public interface CategoryService {

	Category createCategory(Category category);

	Category read(Long id); // buscar por id

	Category delete(Long id);

	Category update(Long id, Category category);

}
