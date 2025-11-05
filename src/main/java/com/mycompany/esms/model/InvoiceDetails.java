package com.mycompany.esms.model;

public class InvoiceDetails {

    private Integer id;
    private int quantity;
    private float unitPrice;
    private Item directItem;

    public InvoiceDetails() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public float getUnitPrice() { return unitPrice; }
    public void setUnitPrice(float unitPrice) { this.unitPrice = unitPrice; }

    public Item getDirectItem() { return directItem; }
    public void setDirectItem(Item directItem) { this.directItem = directItem; }
}
