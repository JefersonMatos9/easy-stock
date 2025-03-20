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

import com.easystock.model.Order;
import com.easystock.service.interfaces.OrderService;

@Controller
@RequestMapping("/api/orders")
public class OrderController {

	@Autowired
	OrderService orderService;
	
	@PostMapping
	public ResponseEntity<Order> create(@RequestBody Order order) {
		Order createdOrder = orderService.create(order);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Order> read(@PathVariable Long id) {
		Order order = orderService.read(id);
		return ResponseEntity.ok(order);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Order> update(@PathVariable Long id, @RequestBody Order order) {
		Order updateOrder = orderService.update(id, order);
		return ResponseEntity.ok(updateOrder);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Order> delete(@PathVariable Long id) {
		Order deleteOrder = orderService.delete(id);
		return ResponseEntity.ok(deleteOrder);
	}
	
}