package com.easystock.service.interfaces;

import java.util.List;
import java.util.Map;

import com.easystock.model.Order;
import com.easystock.model.Product;

// Esta interface define os serviços relacionados à entidade Order.
// Ela declara os métodos que as classes de serviço que a implementam devem fornecer.
public interface OrderService {

	// Cria um novo pedido.
	Order create(Order order);

	// Busca um pedido pelo seu ID.
	Order read(Long id); // buscar por id

	// Exclui um pedido.
	Order delete(Long id);

	// Atualiza um pedido existente.
	Order update(Long id, Order order);

	// Busca todos os pedidos
	List<Order>findAll();

	// Busca os N produtos mais vendidos
	Map<Product, Long> findTopSellingProducts(int limit);

	// Busca os N produtos menos vendidos
	Map<Product, Long> findLeastSellingProducts(int limit);
}