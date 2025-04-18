package com.easystock.service.interfaces;

import java.util.List;

import com.easystock.model.Order;

public interface OrderService {

	Order create(Order order);

	Order read(Long id); // buscar por id

	Order delete(Long id);

	Order update(Long id, Order order);
	
	List<Order>findAll();

}
