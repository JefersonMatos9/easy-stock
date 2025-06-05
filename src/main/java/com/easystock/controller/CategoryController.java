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
	public ResponseEntity<Category> create(@RequestBody Category category) {
		// Este endpoint está responsável por criar uma nova categoria no sistema.
		// Recebe um objeto Category no corpo da requisição e utiliza o CategoryService
		// para persistir essa nova categoria no banco de dados. Retorna a categoria criada
		// com status HTTP 201 (CREATED).
		Category createdCategory = categoryService.createCategory(category);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdCategory);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Category> read(@PathVariable Integer id) {
		// Através deste endpoint, eu consigo buscar uma categoria específica pelo seu ID.
		// O ID da categoria é passado na URL como um parâmetro de path. O CategoryService
		// é chamado para recuperar a categoria do banco de dados. Se a categoria for
		// encontrada, ela é retornada com status HTTP 200 (OK). Preciso tratar o caso
		// em que a categoria não é encontrada para retornar um status 404 (NOT FOUND) no futuro.
		Category category = categoryService.read(id);
		return ResponseEntity.ok(category);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Category> update(@PathVariable Integer id, @RequestBody Category category) {
		// Este endpoint permite atualizar os dados de uma categoria existente.
		// O ID da categoria a ser atualizada é passado na URL, e os novos dados
		// da categoria são enviados no corpo da requisição. O CategoryService
		// realiza a atualização e retorna a categoria atualizada com status HTTP 200 (OK).
		// Seria bom adicionar uma verificação para garantir que a categoria com o ID
		// fornecido realmente existe antes de tentar atualizar.
		Category updateCategory = categoryService.update(id, category);
		return ResponseEntity.ok(updateCategory);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		// Usei este endpoint para deletar uma categoria do sistema pelo seu ID.
		// O ID da categoria a ser removida é especificado na URL. O CategoryService
		// cuida da exclusão. Retorno um status HTTP 204 (NO_CONTENT) para indicar
		// que a operação foi bem-sucedida e não há conteúdo para retornar.
		categoryService.delete(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}