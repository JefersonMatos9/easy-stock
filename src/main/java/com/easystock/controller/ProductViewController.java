package com.easystock.controller;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.easystock.model.Category;
import com.easystock.model.Product;
import com.easystock.service.exception.ProductNotFound;
import com.easystock.service.interfaces.CategoryService;
import com.easystock.service.interfaces.ProductService;

import jakarta.validation.Valid;

@Controller
public class ProductViewController {

	private static final Logger logger = LoggerFactory.getLogger(ProductViewController.class);

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private ProductService productService;

	@GetMapping("/produtos/novo")
	public String exibirFormularioCadastro(Model model) {
		logger.info("Exibindo formulário de cadastro de novo produto.");
		List<Category> categorias = categoryService.listarCategorias();
		model.addAttribute("produto", new Product());
		model.addAttribute("categorias", categorias);
		return "cadastro-produto";
	}

	@PostMapping("/produtos/salvar")
	public String salvarProduto(
			@Valid @ModelAttribute("produto") Product produto,
			BindingResult result,
			@RequestParam(value = "category.id", required = false) Integer categoryId,
			@RequestParam("categoryOption") String categoryOption,
			@RequestParam(value = "newCategoryName", required = false) String newCategoryName,
			Model model,
			RedirectAttributes redirectAttributes) {

		// --- Início do tratamento de erros de validação do Spring (@Valid) ---
		if (result.hasErrors()) {
			model.addAttribute("categorias", categoryService.listarCategorias());
			// CORREÇÃO: Ajustar o objeto 'produto' diretamente para que o Thymeleaf possa fazer o binding
			if ("new".equals(categoryOption)) {
				// Cria uma categoria temporária com ID null e o nome digitado para manter o estado
				produto.setCategory(new Category(null, newCategoryName));
			} else if (categoryId != null) {
				// Tenta buscar a categoria existente para manter no dropdown
				try {
					produto.setCategory(categoryService.read(categoryId));
				} catch (ProductNotFound e) {
					// Caso a categoria não seja encontrada (improvável), define como null
					produto.setCategory(null);
				}
			}
			return "cadastro-produto"; // Retorna para o formulário com os erros exibidos
		}
		// --- Fim do tratamento de erros de validação do Spring ---

		try {
			// Lógica para definir a categoria
			if ("new".equals(categoryOption)) {
				if (newCategoryName == null || newCategoryName.trim().isEmpty()) {
					result.rejectValue("category", "category.new.empty", "Por favor, digite o nome da nova categoria.");
					model.addAttribute("categorias", categoryService.listarCategorias());
					// Se der erro, precisamos ajustar 'produto' para manter o estado da nova categoria
					produto.setCategory(new Category(null, newCategoryName));
					return "cadastro-produto";
				}
				Category newCategory = new Category();
				newCategory.setName(newCategoryName.trim());
				Category savedCategory = categoryService.createCategory(newCategory);
				produto.setCategory(savedCategory);
			} else if (categoryId != null && categoryId != -1) {
				Category existingCategory = categoryService.read(categoryId);
				produto.setCategory(existingCategory);
			} else {
				result.rejectValue("category", "category.empty", "Por favor, selecione uma categoria existente.");
				model.addAttribute("categorias", categoryService.listarCategorias());
				return "cadastro-produto";
			}

			productService.createProduct(produto);
			redirectAttributes.addFlashAttribute("mensagemSucesso", "Produto cadastrado com sucesso!");
			return "redirect:/produtos";

		} catch (Exception e) {
			logger.error("Erro ao salvar produto: " + e.getMessage(), e);
			if (e.getCause() instanceof com.easystock.exception.productException.InvalidPriceException) {
				result.rejectValue("price", "product.price.invalid", e.getCause().getMessage());
			} else {
				model.addAttribute("erroGeral", "Erro ao salvar produto: " + e.getMessage());
			}
			model.addAttribute("produto", produto); // Garante que o produto esteja no modelo para recarregar dados
			model.addAttribute("categorias", categoryService.listarCategorias());
			return "cadastro-produto";
		}
	}

	@GetMapping("/produtos")
	public String listarProdutos(
			Model model,
			@RequestParam(value = "searchName", required = false) String searchName,
			@RequestParam(value = "categoryId", required = false) Integer categoryId) {
		logger.info("Listando produtos. Pesquisa: '{}', Categoria ID: {}", searchName, categoryId);

		List<Product> produtos;
		if (searchName != null && !searchName.trim().isEmpty()) {
			produtos = productService.findProductsByNameContaining(searchName.trim());
		} else if (categoryId != null && categoryId != -1) {
			try {
				Category category = categoryService.read(categoryId);
				produtos = productService.findProductsByCategory(category);
			} catch (ProductNotFound e) {
				produtos = productService.findAll();
				model.addAttribute("mensagemErro", "Categoria selecionada não encontrada.");
			} catch (Exception e) {
				produtos = productService.findAll();
				model.addAttribute("mensagemErro", "Ocorreu um erro ao buscar produtos por categoria.");
			}
		} else {
			produtos = productService.findAll();
		}

		List<Category> categorias = categoryService.listarCategorias();
		logger.info("Número de produtos encontrados: {}", produtos.size());

		Map<Category, List<Product>> produtosPorCategoria = productService.groupProductsByCategory(produtos);

		model.addAttribute("produtosPorCategoria", produtosPorCategoria);
		model.addAttribute("categorias", categorias);
		model.addAttribute("searchName", searchName);
		model.addAttribute("selectedCategoryId", categoryId);
		return "lista-produtos";
	}

	@GetMapping("/produtos/editar/{id}")
	public String exibirFormularioEdicao(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
		try {
			logger.info("Exibindo formulário de edição para o produto ID: {}", id);
			Product produto = productService.read(id);
			List<Category> categorias = categoryService.listarCategorias();
			model.addAttribute("produto", produto);
			model.addAttribute("categorias", categorias);
			return "editar-produto";
		} catch (ProductNotFound e) {
			logger.error("Produto não encontrado para edição. ID: {}", id, e);
			redirectAttributes.addFlashAttribute("mensagemErro", "Produto não encontrado para edição.");
			return "redirect:/produtos";
		} catch (Exception e) {
			logger.error("Erro ao exibir formulário de edição para o produto ID: {}", id, e);
			redirectAttributes.addFlashAttribute("mensagemErro", "Ocorreu um erro ao carregar o produto para edição.");
			return "redirect:/produtos";
		}
	}

	@PostMapping("/produtos/editar/{id}")
	public String atualizarProduto(
			@PathVariable Long id,
			@Valid @ModelAttribute("produto") Product produto,
			BindingResult result,
			@RequestParam(value = "category.id", required = false) Integer categoryId,
			@RequestParam("categoryOption") String categoryOption,
			@RequestParam(value = "newCategoryName", required = false) String newCategoryName,
			Model model,
			RedirectAttributes redirectAttributes) {

		// --- Início do tratamento de erros de validação do Spring (@Valid) ---
		if (result.hasErrors()) {
			model.addAttribute("categorias", categoryService.listarCategorias());
			// CORREÇÃO: Ajustar o objeto 'produto' diretamente para que o Thymeleaf possa fazer o binding
			if ("new".equals(categoryOption)) {
				// Cria uma categoria temporária com ID null e o nome digitado para manter o estado
				produto.setCategory(new Category(null, newCategoryName));
			} else if (categoryId != null) {
				// Tenta buscar a categoria existente para manter no dropdown
				try {
					produto.setCategory(categoryService.read(categoryId));
				} catch (ProductNotFound e) {
					produto.setCategory(null);
				}
			}
			return "editar-produto"; // Retorna para o formulário com os erros exibidos
		}
		// --- Fim do tratamento de erros de validação do Spring ---

		try {
			logger.info("Atualizando produto ID: {}", id);
			produto.setId(id);

			// Lógica para definir a categoria, similar ao salvar
			if ("new".equals(categoryOption)) {
				if (newCategoryName == null || newCategoryName.trim().isEmpty()) {
					result.rejectValue("category", "category.new.empty", "Por favor, digite o nome da nova categoria.");
					model.addAttribute("categorias", categoryService.listarCategorias());
					// Se der erro, precisamos ajustar 'produto' para manter o estado da nova categoria
					produto.setCategory(new Category(null, newCategoryName));
					return "editar-produto";
				}
				Category newCategory = new Category();
				newCategory.setName(newCategoryName.trim());
				Category savedCategory = categoryService.createCategory(newCategory);
				produto.setCategory(savedCategory);
			} else if (categoryId != null && categoryId != -1) {
				Category existingCategory = categoryService.read(categoryId);
				produto.setCategory(existingCategory);
			} else {
				result.rejectValue("category", "category.empty", "Por favor, selecione uma categoria existente.");
				model.addAttribute("categorias", categoryService.listarCategorias());
				return "editar-produto";
			}

			productService.update(id, produto);
			redirectAttributes.addFlashAttribute("mensagemSucesso", "Produto atualizado com sucesso!");
			return "redirect:/produtos";
		} catch (Exception e) {
			logger.error("Erro ao atualizar o produto ID: {}: {}", id, e.getMessage(), e);
			if (e.getCause() instanceof com.easystock.exception.productException.InvalidPriceException) {
				result.rejectValue("price", "product.price.invalid", e.getCause().getMessage());
			} else {
				model.addAttribute("erroGeral", "Erro ao atualizar o produto: " + e.getMessage());
			}
			model.addAttribute("produto", produto);
			model.addAttribute("categorias", categoryService.listarCategorias());
			return "editar-produto";
		}
	}

	@GetMapping("/produtos/excluir/{id}")
	public String excluirProduto(@PathVariable Long id, RedirectAttributes redirectAttributes) {
		try {
			logger.info("Excluindo produto ID: {}", id);
			productService.delete(id);
			redirectAttributes.addFlashAttribute("mensagemSucesso", "Produto excluído com sucesso!");
		} catch (ProductNotFound e) {
			logger.error("Erro ao excluir produto ID: {}: {}", id, e.getMessage(), e);
			redirectAttributes.addFlashAttribute("mensagemErro", "Produto não encontrado para exclusão.");
		} catch (Exception e) {
			logger.error("Erro inesperado ao excluir produto ID: {}: {}", id, e.getMessage(), e);
			redirectAttributes.addFlashAttribute("mensagemErro", "Ocorreu um erro ao excluir o produto.");
		}
		return "redirect:/produtos";
	}
}