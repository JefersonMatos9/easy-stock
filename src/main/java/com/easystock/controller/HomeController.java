package com.easystock.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.easystock.model.Product;
import com.easystock.service.interfaces.ProductService;

@Controller
public class HomeController {

    @Autowired
    private ProductService productService;

    @GetMapping("/")
    public String home(Model model) {
        return "index"; // Assumindo que o nome do seu ficheiro index.html é "index"
    }

    // Endpoint para a página do Dashboard
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        // Produtos com baixo estoque (ex: abaixo de 5 unidades)
        List<Product> lowStockProducts = productService.findLowStockProducts(5);
        model.addAttribute("lowStockProducts", lowStockProducts);

        // Produtos mais vendidos (top 5)
        Map<Product, Long> topSellingProducts = productService.findTopSellingProducts(5);
        model.addAttribute("topSellingProducts", topSellingProducts);

        // Produtos menos vendidos (bottom 5, pode ser os menos vendidos ou os que não venderam)
        Map<Product, Long> leastSellingProducts = (Map<Product, Long>) productService.findLowStockProducts(5);
        model.addAttribute("leastSellingProducts", leastSellingProducts);

        return "dashboard"; // Retorna o nome do template HTML: dashboard.html
    }

    // Endpoint para a página de Configurações
    @GetMapping("/configuracoes")
    public String configuracoes() {
        return "configuracoes"; // Retorna o nome do template HTML: configuracoes.html
    }
}