package com.easystock.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.easystock.model.Category;
import com.easystock.model.Product;
import com.easystock.service.interfaces.CategoryService;
import com.easystock.service.interfaces.ProductService;

@Controller
public class ProductViewController {

    @Autowired
    private CategoryService categoryService;
    
    @Autowired
    private ProductService productService;

    @GetMapping("/produtos/novo")
    public String exibirFormularioCadastro(Model model) {
        List<Category> categorias = categoryService.listarCategorias();

        model.addAttribute("produto", new Product());
        model.addAttribute("categorias", categorias);
        return "cadastro-produto";
    }
    
    @PostMapping("/produtos/salvar")
    public String salvarProduto(@ModelAttribute Product produto, Model model) {
        try {
            if (produto.getCategory() != null && produto.getCategory().getId() != null) {
                // Usar o serviço para buscar a categoria completa
                Category category = categoryService.findById(produto.getCategory().getId());
                produto.setCategory(category);
            }
            
            productService.createProduct(produto);
            return "redirect:/produtos";
        } catch (Exception e) {
            model.addAttribute("erro", e.getMessage());
            model.addAttribute("produto", produto);
            model.addAttribute("categorias", categoryService.listarCategorias());
            return "cadastro-produto";
        }
    }
    
    @GetMapping("/produtos")
    public String listarProdutos(Model model) {
        List<Product> produtos = productService.findAll(); // Supondo que você tenha um método para listar todos os produtos no seu ProductService
        model.addAttribute("produtos", produtos);
        return "lista-produtos"; // Este é o nome do arquivo HTML que mostrará a lista de produtos
    }
}
