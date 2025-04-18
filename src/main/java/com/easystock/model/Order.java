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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private static int counterOrders = 0;
    private String orderNumber; // Alterado para String para compatibilidade com JSON
    private LocalDateTime dateTime;
    
    @Enumerated(EnumType.STRING)
    private StatusRequest status;
    
    private double totalValue;
    
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "order_id")
    private List<OrderItem> items = new ArrayList<>();
    
    public Order() {
        this.orderNumber = "ORD-" + (++counterOrders); // Formata como string
        this.dateTime = LocalDateTime.now(); // inicializa a data e hora
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
    
    public String getOrderNumber() {
        return orderNumber;
    }
    
    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }
    
    public List<OrderItem> getItems() {
        return items;
    }
    
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
    
    public void addItem(OrderItem item) {
        if (item != null && item.getQuantity() > 0) {
            items.add(item);
            recalculateTotal();
        } else {
            throw new InsufficientQuantityException("Item inválido ou quantidade deve ser maior que 0");
        }
    }
    
    public void removeItem(Long itemId) {
        boolean removed = items.removeIf(item -> item.getId() != null && item.getId().equals(itemId));
        if (removed) {
            recalculateTotal();
        }
    }
    
    private void recalculateTotal() {
        totalValue = items.stream().mapToDouble(item -> 
            item.getSubTotal() != null ? item.getSubTotal().doubleValue() : 0.0).sum();
    }
}