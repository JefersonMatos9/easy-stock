package com.easystock.model;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "order_items")
public class OrderItem {

	// Identificador único do item do pedido. Gerado automaticamente pelo banco de dados.
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// Produto associado a este item do pedido.
	// Relacionamento ManyToOne: um produto pode estar em vários itens de pedido.
	@ManyToOne
	@JoinColumn(name = "product_id")
	private Product product;

	// Pedido ao qual este item pertence.
	// Relacionamento ManyToOne: um pedido pode ter vários itens de pedido.
	// A anotação nullable=false garante que todo item de pedido deve estar associado a um pedido.
	@ManyToOne
	@JoinColumn(name = "order_id")
	private Order order;

	// Quantidade do produto neste item do pedido.
	private int quantity;

	// Observação ou detalhe adicional sobre este item do pedido.
	private String observation;

	// Subtotal deste item do pedido (preço do produto multiplicado pela quantidade).
	private BigDecimal subTotal;

	// Construtor padrão necessário para o JPA.
	public OrderItem() {
		// Construtor padrão necessário para JPA
	}

	// Construtor que recebe os dados do item do pedido.
	// Calcula o subtotal ao criar o item.
	public OrderItem(Product product, int quantity, String observation) {
		this.product = product;
		this.quantity = quantity;
		this.observation = observation;
		this.calculateSubTotal();
	}

	// Métodos getter e setter para os atributos da classe.

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Product getProduct() {
		return product;
	}

	// Define o produto e recalcula o subtotal.
	public void setProduct(Product product) {
		this.product = product;
		calculateSubTotal();
	}

	public int getQuantity() {
		return quantity;
	}

	// Define a quantidade e recalcula o subtotal.
	public void setQuantity(int quantity) {
		this.quantity = quantity;
		calculateSubTotal();
	}

	public String getObservation() {
		return observation;
	}

	public void setObservation(String observation) {
		this.observation = observation;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public BigDecimal getSubTotal() {
		return subTotal;
	}

	public void setSubTotal(BigDecimal subTotal) {
		this.subTotal = subTotal;
	}

	// Método privado para calcular o subtotal do item.
	// Multiplica o preço do produto pela quantidade.
	public void calculateSubTotal() {
		if (product != null && product.getPrice() != null) {
			this.subTotal = product.getPrice().multiply(BigDecimal.valueOf(quantity));
		} else {
			this.subTotal = BigDecimal.ZERO;
		}
	}
}