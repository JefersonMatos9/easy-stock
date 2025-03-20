package com.easystock.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.easystock.exception.errorInValue.ErrorInValue;
import com.easystock.model.Order;
import com.easystock.service.interfaces.OrderService;

@Service
public class OrderServiceIMPL implements OrderService {

	private final List<Order> orderList;

	@Autowired
	public OrderServiceIMPL() {
		this.orderList = new ArrayList<>();
	}

	@Override
	public Order create(Order order) {
		validateOrder(order);
		orderList.add(order);
		return order;
	}

	@Override
	public Order read(Long id) {
		return null;
	}

	@Override
	public Order delete(Long id) {
		return null;
	}

	@Override
	public Order update(Long id, Order order) {
		// TODO Auto-generated method stub
		return null;
	}

	private void validateOrder(Order order) {

		if (order.getItens().isEmpty()) {
			throw new ItemInvalidException("Pedido deve conter no minimo 1 item.");
		}
		if (order.getStatus() == null) {
			throw new ItemInvalidException("Status do pedido é obrigatório"); // continuar esse metodo
		}
		if (order.getTotalValue() < 0) {
			throw new ErrorInValue("Valor total do pedido deve ser maior que zero.");
		}
	}
	
}
