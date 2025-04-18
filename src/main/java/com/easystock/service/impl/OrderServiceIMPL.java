package com.easystock.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.easystock.exception.OrderNotFoundException;
import com.easystock.exception.errorInValue.ErrorInValue;
import com.easystock.exception.productException.ProductInvalidException;
import com.easystock.model.Order;
import com.easystock.model.OrderItem;
import com.easystock.model.Product;
import com.easystock.repository.OrderRepository;
import com.easystock.repository.ProductRepository;
import com.easystock.service.exception.ItemInvalidException;
import com.easystock.service.interfaces.OrderService;

@Service
public class OrderServiceIMPL implements OrderService {

	private final OrderRepository orderRepository;

	@Autowired
	ProductServiceImpl productServiceImpl;

	@Autowired
	ProductRepository productRepository;

	@Autowired
	public OrderServiceIMPL(OrderRepository orderRepository) {
		this.orderRepository = orderRepository;
	}

	@Override
	public Order create(Order order) {
		validateOrder(order);
		return createOrder(order);
	}

	@Override
	public Order read(Long id) {
		return orderRepository.findById(id)
				.orElseThrow(() -> new OrderNotFoundException("Pedido não encontrado com o id: " + id));
	}

	@Override
	public Order delete(Long id) {
		Order order = read(id);
		orderRepository.delete(order);
		return order;
	}

	@Override
	public Order update(Long id, Order order) {
		Order existingOrder = read(id);

		// Atualize os campos necessários
		existingOrder.setStatus(order.getStatus());
		// Outros campos...

		return orderRepository.save(existingOrder);
	}

	private void validateOrder(Order order) {

		if (order.getItems().isEmpty()) {
			throw new ItemInvalidException("Pedido deve conter no minimo 1 item.");
		}
		if (order.getStatus() == null) {
			throw new ItemInvalidException("Status do pedido é obrigatório");
		}
		if (order.getTotalValue() < 0) {
			throw new ErrorInValue("Valor total do pedido deve ser maior que zero.");
		}
	}
	
	@Override
	public List<Order>findAll(){
		return orderRepository.findAll();
	}

	private Order createOrder(Order order) {
		List<OrderItem> newItems = new ArrayList<>();
		double totalValue = 0.0;

		for (OrderItem item : order.getItems()) {
			Optional<Product> productOpt = productRepository.findById(item.getProduct().getId());

			if (productOpt.isPresent()) {
				Product product = productOpt.get();

				// Criamos um novo item com o produto encontrado e os valores corretos
				OrderItem newItem = new OrderItem(product, item.getQuantity(), item.getObservation());

				// Calculamos o subtotal usando BigDecimal para precisão
				BigDecimal subTotal = product.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));

				// O subTotal é calculado automaticamente pelo construtor e pelo setQuantity,
				// então não precisamos fazer setSubTotal aqui

				totalValue += subTotal.doubleValue();

				newItem.setOrder(order);

				newItems.add(newItem);
			} else {
				throw new ProductInvalidException("Produto com ID " + item.getProduct().getId() + " não existe.");
			}
		}
		// Definimos os itens corretos usando o método correto
		order.setItems(newItems);// Aqui mantemos o nome do método como está na classe Order

		// Definimos o ID do pedido (provavelmente gerado ou automático)
		// order.setId(???); // Precisamos de um id para o pedido
		order.setTotalValue(totalValue);
		orderRepository.save(order);

		return order;
	}
}
