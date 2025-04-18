package com.easystock.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model; // ✅ CERTO
import org.springframework.web.bind.annotation.GetMapping;

import com.easystock.model.Order;
import com.easystock.service.interfaces.OrderService;


@Controller
public class OrderWebController {

	@Autowired
    private OrderService orderService;

	@GetMapping("/pedidos")
	public String mostrarPedidos(Model model) {
	    List<Order> listaPedidos = orderService.findAll(); // esse método precisa existir
	    model.addAttribute("pedidos", listaPedidos);
	    return "lista-pedidos"; // procura por lista-pedidos.html na pasta templates
	}

}
