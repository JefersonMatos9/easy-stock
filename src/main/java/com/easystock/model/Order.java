package com.easystock.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.easystock.exception.productException.InsufficientQuantityException;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "orders")
public class Order {

	// Identificador único do pedido. Gerado automaticamente pelo banco de dados.
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// Contador estático para gerar números de pedido sequenciais.
	// Cuidado: Este contador não é persistente entre reinícios da aplicação
	// e não é seguro para ambientes multi-threaded sem sincronização.
	// Para produção, considere uma abordagem baseada em banco de dados ou UUID.
	private static int counterOrders = 0;
	// Número do pedido, formatado como string (ex: "ORD-1").
	private String orderNumber;
	// Data e hora em que o pedido foi realizado.
	private LocalDateTime dateTime;

	// Status do pedido. Utiliza o enum StatusRequest para representar os diferentes estados do pedido.
	@Enumerated(EnumType.STRING)
	private StatusRequest status;

	// Valor total do pedido.
	private double totalValue;

	// Lista de itens que compõem o pedido.
	// Relacionamento OneToMany com OrderItem.
	// CascadeType.ALL: se um pedido for persistido, atualizado ou removido, seus itens também serão.
	// orphanRemoval=true: se um item for removido da lista, ele será excluído do banco de dados.
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "order", orphanRemoval = true)
	private List<OrderItem> items = new ArrayList<>();

	// Construtor padrão. Inicializa o número do pedido, data/hora e status.
	public Order() {
		this.orderNumber = "ORD-" + (++counterOrders); // Formata como string
		this.dateTime = LocalDateTime.now(); // inicializa a data e hora
		this.status = StatusRequest.RECEBIDO;
		this.totalValue = 0;
	}

	// Métodos getter e setter para os atributos da classe.

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public static int getCounterOrders() {
		return counterOrders;
	}

	public static void setCounterOrders(int counterOrders) {
		Order.counterOrders = counterOrders;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public List<OrderItem> getItems() {
		return items;
	}

	// Define os itens do pedido e recalcula o valor total.
	public void setItems(List<OrderItem> items) {
		this.items = items;
		recalculateTotal();
	}

	public LocalDateTime getDateTime() {
		return dateTime;
	}

	public void setDateTime(LocalDateTime dateTime) {
		this.dateTime = dateTime;
	}

	public StatusRequest getStatus() {
		return status;
	}

	public void setStatus(StatusRequest status) {
		this.status = status;
	}

	public double getTotalValue() {
		return totalValue;
	}

	public void setTotalValue(double totalValue) {
		this.totalValue = totalValue;
	}

	// Adiciona um item ao pedido.
	// Se o item for nulo ou a quantidade for inválida, lança uma exceção.
	public void addItem(OrderItem item) {
		if (item != null && item.getQuantity() > 0) {
			items.add(item);
			recalculateTotal();
		} else {
			throw new InsufficientQuantityException("Item inválido ou quantidade deve ser maior que 0");
		}
	}

	// Remove um item do pedido pelo seu ID.
	// Retorna true se o item foi removido, false caso contrário.
	public void removeItem(Long itemId) {
		boolean removed = items.removeIf(item -> item.getId() != null && item.getId().equals(itemId));
		if (removed) {
			recalculateTotal();
		}
	}

	// Recalcula o valor total do pedido com base nos seus itens.
	public void recalculateTotal() {
		totalValue = items.stream().mapToDouble(item ->
				item.getSubTotal() != null ? item.getSubTotal().doubleValue() : 0.0).sum();
	}
}