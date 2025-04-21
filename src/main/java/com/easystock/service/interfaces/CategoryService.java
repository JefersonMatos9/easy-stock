package com.easystock.service.interfaces;

import java.util.List;
import java.util.Optional;

import com.easystock.model.Category;

public interface CategoryService {

	Category createCategory(Category category);

	Category read(Integer id); // buscar por id

	Category delete(Integer id);

	Category update(Integer id, Category category);
	
	List<Category>listarCategorias();
	
	Category findById(Integer id);

}
