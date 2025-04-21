package com.easystock.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.easystock.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

	public Optional<Category> findByName(String name);

	Optional<Category> findByNameIgnoreCase(String name);

	Optional<Category> findById(Integer id);
}
