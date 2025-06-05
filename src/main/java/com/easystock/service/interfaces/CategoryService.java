package com.easystock.service.interfaces;

import java.util.List;
import java.util.Optional;

import com.easystock.model.Category;

// Esta interface define os serviços relacionados à entidade Category.
// Ela declara os métodos que as classes de serviço que a implementam devem fornecer.
public interface CategoryService {

	// Cria uma nova categoria.
	Category createCategory(Category category);

	// Busca uma categoria pelo seu ID.
	Category read(Integer id); // buscar por id

	// Exclui uma categoria.
	Category delete(Integer id);

	// Atualiza uma categoria existente.
	Category update(Integer id, Category category);

	// Lista todas as categorias.
	List<Category> listarCategorias();

	// Busca uma categoria pelo ID.
	Category findById(Integer id);

	Optional<Category> findByNameIgnoreCase(String name);

	Object findAll();
}
