package com.mycompany.esms.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Order {

    private Integer id;
    private LocalDate orderDate;
    private String status;
    private String shippingAddress;
    private LocalDate deliveryDate;
    private String note;
    private Customer customer;
    private List<OrderDetails> orderItems = new ArrayList<>();

    public Order() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public LocalDate getOrderDate() { return orderDate; }
    public void setOrderDate(LocalDate orderDate) { this.orderDate = orderDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    // Tổng giá trị đơn hàng đã chuyển sang SalesInvoice

    public String getShippingAddress() { return shippingAddress; }
    public void setShippingAddress(String shippingAddress) { this.shippingAddress = shippingAddress; }

    public LocalDate getDeliveryDate() { return deliveryDate; }
    public void setDeliveryDate(LocalDate deliveryDate) { this.deliveryDate = deliveryDate; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }

    public List<OrderDetails> getOrderItems() { return orderItems; }
    public void setOrderItems(List<OrderDetails> orderItems) { this.orderItems = orderItems; }
}


