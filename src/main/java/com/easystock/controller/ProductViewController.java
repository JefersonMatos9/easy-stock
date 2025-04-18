package com.easystock.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.easystock.model.Category;
import com.easystock.model.Product;
import com.easystock.service.interfaces.CategoryService;

@Controller
public class ProductViewController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/produtos/novo")
    public String exibirFormularioCadastro(Model model) {
        List<Category> categorias = categoryService.listarCategorias();

        model.addAttribute("produto", new Product());
        model.addAttribute("categorias", categorias);
        return "cadastro-produto";
    }
}
