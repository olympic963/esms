package com.mycompany.esms.model;

public class OrderDetails {

    private Integer id;
    private int quantity;
    private float unitPrice;
    private Item orderItem;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public float getUnitPrice() { return unitPrice; }
    public void setUnitPrice(float unitPrice) { this.unitPrice = unitPrice; }

    public Item getOrderItem() { return orderItem; }
    public void setOrderItem(Item orderItem) { this.orderItem = orderItem; }
}
