package com.easystock.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.easystock.exception.OrderNotFoundException;
import com.easystock.exception.errorInValue.ErrorInValue;
import com.easystock.exception.productException.InsufficientQuantityException;
import com.easystock.exception.productException.ProductInvalidException;
import com.easystock.model.Order;
import com.easystock.model.OrderItem;
import com.easystock.model.Product;
import com.easystock.repository.OrderRepository;
import com.easystock.service.exception.ItemInvalidException;
import com.easystock.service.interfaces.OrderService;
import com.easystock.service.interfaces.ProductService; // **IMPORTANTE: Importar ProductService**

@Service
public class OrderServiceIMPL implements OrderService {

	private static final Logger logger = LoggerFactory.getLogger(OrderServiceIMPL.class);

	private final OrderRepository orderRepository;
	private final ProductService productService; // **Agora injetamos a interface ProductService**

	@Autowired
	public OrderServiceIMPL(OrderRepository orderRepository, ProductService productService) { // **Adicionar ProductService ao construtor**
		this.orderRepository = orderRepository;
		this.productService = productService; // **Atribuir ProductService**
	}

	/**
	 * Cria um novo pedido.
	 *
	 * @param order O pedido a ser criado.
	 * @return O pedido criado.
	 * @throws ItemInvalidException Se o pedido não tiver itens ou o status for
	 * inválido.
	 * @throws ErrorInValue         Se o valor total do pedido for menor que
	 * zero.
	 */
	@Override
	@Transactional
	public Order create(Order order) {
		validateOrder(order);
		return createOrderWithStockUpdate(order);
	}

	/**
	 * Busca um pedido pelo ID.
	 *
	 * @param id O ID do pedido a ser buscado.
	 * @return O pedido encontrado.
	 * @throws OrderNotFoundException Se nenhum pedido for encontrado com o ID
	 * fornecido.
	 */
	@Override
	public Order read(Long id) {
		return orderRepository.findById(id)
				.orElseThrow(() -> new OrderNotFoundException("Pedido não encontrado com o id: " + id));
	}

	/**
	 * Exclui um pedido pelo ID e devolve os itens ao estoque.
	 *
	 * @param id O ID do pedido a ser excluído.
	 * @return O pedido excluído.
	 * @throws OrderNotFoundException Se nenhum pedido for encontrado com o ID
	 * fornecido.
	 */
	@Override
	@Transactional // Garante atomicidade
	public Order delete(Long id) { // Alterado para retornar Order, conforme sua assinatura original na interface
		Order orderToDelete = orderRepository.findById(id)
				.orElseThrow(() -> new OrderNotFoundException("Pedido não encontrado com ID: " + id));

		// Devolve os itens do pedido ao estoque
		for (OrderItem item : orderToDelete.getItems()) {
			// A linha 116 e 127 se referiam a 'productServiceImpl', que não existia.
			// Agora usamos 'productService' que foi injetado corretamente.
			productService.addQuantity(item.getProduct(), item.getQuantity());
		}

		orderRepository.delete(orderToDelete);
		return orderToDelete; // Retorna o pedido que foi excluído
	}

	/**
	 * Atualiza um pedido existente.
	 *
	 * @param id    O ID do pedido a ser atualizado.
	 * @param order O pedido com os novos dados.
	 * @return O pedido atualizado.
	 * @throws OrderNotFoundException Se nenhum pedido for encontrado com o ID
	 * fornecido.
	 */
	@Override
	@Transactional
	public Order update(Long id, Order updatedOrder) {
		Order existingOrder = orderRepository.findById(id)
				.orElseThrow(() -> new OrderNotFoundException("Pedido não encontrado com ID: " + id));

		// Mapa dos itens antigos para facilitar a comparação
		Map<Long, OrderItem> oldItemsMap = existingOrder.getItems().stream()
				.collect(Collectors.toMap(item -> item.getProduct().getId(), Function.identity()));

		// Criar uma nova lista para os itens atualizados do pedido, evitando
		// ConcurrentModificationException
		List<OrderItem> newItemsForExistingOrder = new ArrayList<>();

		// Processar os novos itens e comparar com os antigos
		for (OrderItem newItem : updatedOrder.getItems()) {
			// **Linha 130** (anteriormente)
			Product product = productService.read(newItem.getProduct().getId()); // Garante que o produto é gerenciado
			newItem.setProduct(product); // Garante que o OrderItem referência o Product gerenciado

			OrderItem oldItem = oldItemsMap.get(newItem.getProduct().getId());

			if (oldItem != null) {
				// Item já existia, verificar mudança de quantidade
				if (newItem.getQuantity() != oldItem.getQuantity()) {
					int quantityChange = newItem.getQuantity() - oldItem.getQuantity();
					if (quantityChange > 0) {
						// Aumentou a quantidade: reduzir do estoque
						// **Linha 136** (anteriormente)
						productService.reduceQuantity(product, quantityChange);
					} else {
						// Diminuiu a quantidade: aumentar no estoque
						// **Linha 139** (anteriormente)
						productService.addQuantity(product, -quantityChange); // Use -quantityChange para tornar positivo
					}
				}
				// Remove dos antigos para saber quais foram deletados
				oldItemsMap.remove(newItem.getProduct().getId());
			} else {
				// Item é novo: reduzir do estoque
				// **Linha 143** (anteriormente)
				productService.reduceQuantity(product, newItem.getQuantity());
			}
			newItem.setOrder(existingOrder); // Associa o novo item ao pedido existente
			newItemsForExistingOrder.add(newItem); // Adiciona à nova lista para o pedido existente
		}

		// Processar itens que foram removidos
		for (OrderItem removedItem : oldItemsMap.values()) {
			// **Linha 148** (anteriormente)
			productService.addQuantity(removedItem.getProduct(), removedItem.getQuantity());
		}

		// Atualizar o pedido com os novos itens e outras propriedades
		// Importante: limpe e adicione os itens da nova lista. O CascadeType.ALL e
		// orphanRemoval=true farão o resto.
		existingOrder.getItems().clear();
		existingOrder.getItems().addAll(newItemsForExistingOrder);

		existingOrder.setStatus(updatedOrder.getStatus());
		existingOrder.recalculateTotal(); // Recalcula o total do pedido com base nos novos itens

		return orderRepository.save(existingOrder); // Salva o pedido atualizado
	}

	@Override
	public List<Order> findAll() {
		return orderRepository.findAll();
	}

	/**
	 * Valida os dados de um pedido.
	 *
	 * @param order O pedido a ser validado.
	 * @throws ItemInvalidException Se o pedido não tiver itens ou o status for
	 * inválido.
	 * @throws ErrorInValue         Se o valor total do pedido for menor que
	 * zero.
	 */
	private void validateOrder(Order order) {
		if (order.getItems().isEmpty()) {
			throw new ItemInvalidException("Pedido deve conter no mínimo 1 item.");
		}
		if (order.getStatus() == null) {
			throw new ItemInvalidException("Status do pedido é obrigatório");
		}
		if (order.getTotalValue() < 0) {
			throw new ErrorInValue("Valor total do pedido deve ser maior que zero.");
		}
	}

	/**
	 * Cria um pedido e atualiza o estoque dos produtos. Este método é chamado pelo
	 * método create().
	 *
	 * @param order O pedido a ser criado.
	 * @return O pedido criado com os itens e o valor total calculados.
	 * @throws ProductInvalidException       Se um produto não for encontrado.
	 * @throws InsufficientQuantityException Se não houver estoque suficiente para
	 * um produto.
	 */
	private Order createOrderWithStockUpdate(Order order) {
		List<OrderItem> newItems = new ArrayList<>();
		double totalValue = 0.0;

		logger.info("Número de itens recebidos no pedido: {}", order.getItems().size());

		for (OrderItem item : order.getItems()) {
			// Verifica se o produto tem ID
			if (item.getProduct() == null || item.getProduct().getId() == null) {
				throw new ProductInvalidException("Produto não especificado para um dos itens do pedido.");
			}

			Long productId = item.getProduct().getId();
			logger.info("Buscando produto com ID: {}", productId);
			Optional<Product> productOpt = productService.findById(productId); // **AQUI: Usar productService.read ou findById**

			if (productOpt.isPresent()) {
				Product product = productOpt.get();
				logger.info("Produto encontrado: {}, Quantidade em estoque: {}", product.getName(),
						product.getQuantity());

				// Verifica se há estoque suficiente
				if (product.getQuantity() < item.getQuantity()) {
					throw new InsufficientQuantityException(
							"Estoque insuficiente para o produto '" + product.getName() + "'. Quantidade solicitada: "
									+ item.getQuantity() + ", quantidade disponível: " + product.getQuantity() + ".");
				}

				// Substitui o produto parcial pelo produto completo do banco de dados
				OrderItem newItem = new OrderItem(product, item.getQuantity(), item.getObservation());

				// Calcula o subtotal (a lógica já está no construtor de OrderItem, mas podemos
				// garantir aqui)
				newItem.calculateSubTotal(); // Chama o método para garantir o cálculo
				totalValue += newItem.getSubTotal().doubleValue();
				newItem.setOrder(order);
				newItems.add(newItem);

				// Atualiza o estoque
				logger.info("Diminuindo estoque do produto '{}' em {} unidades.", product.getName(),
						item.getQuantity());
				// **Linha 160** (anteriormente)
				productService.reduceQuantity(product, item.getQuantity());
				logger.info("Novo estoque do produto '{}': {}", product.getName(), product.getQuantity());
			} else {
				throw new ProductInvalidException("Produto com ID " + productId + " não existe.");
			}
		}

		order.setItems(newItems);
		order.setTotalValue(totalValue);

		logger.info("Salvando pedido com valor total: {}", order.getTotalValue());
		Order savedOrder = orderRepository.save(order);
		logger.info("Pedido salvo com ID: {}", savedOrder.getId());
		return savedOrder;
	}

	@Override
	public Map<Product, Long> findTopSellingProducts(int limit) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<Product, Long> findLeastSellingProducts(int limit) {
		// TODO Auto-generated method stub
		return null;
	}
}