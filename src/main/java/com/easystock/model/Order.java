package com.easystock.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Order {

	private Long id;
	private static int counterOrders = 0;
	private int orderNumber;
	private List<OrderItem> itens;
	private LocalDateTime dateTime;
	private StatusRequest status;
	private double totalValue;

	public Order() {
		this.orderNumber = ++counterOrders;
		this.itens = new ArrayList<>();
		this.status = StatusRequest.RECEBIDO;
		this.totalValue = 0;
	}

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

	public int getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(int orderNumber) {
		this.orderNumber = orderNumber;
	}

	public List<OrderItem> getItens() {
		return itens;
	}

	public void setItens(List<OrderItem> itens) {
		this.itens = itens;
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
}
