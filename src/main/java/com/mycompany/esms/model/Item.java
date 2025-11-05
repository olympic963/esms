package com.mycompany.esms.model;

public class Item {

    private Integer id;
    private String name;
    private String description;
    private float salePrice;
    private int stockQuantity;
    private int warranty;
    private boolean active;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public float getSalePrice() { return salePrice; }
    public void setSalePrice(float salePrice) { this.salePrice = salePrice; }

    public int getStockQuantity() { return stockQuantity; }
    public void setStockQuantity(int stockQuantity) { this.stockQuantity = stockQuantity; }

    public int getWarranty() { return warranty; }
    public void setWarranty(int warranty) { this.warranty = warranty; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    
}
