package com.mycompany.esms.model;

public class ImportDetails {

    private Integer id;

    private int quantity;
    private float unitPrice;
    private Item importItem;
    private ImportInvoice importInvoice;
    
    public ImportDetails() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public float getUnitPrice() { return unitPrice; }
    public void setUnitPrice(float unitPrice) { this.unitPrice = unitPrice; }

    public Item getImportItem() { return importItem; }
    public void setImportItem(Item importItem) { this.importItem = importItem; }

    public ImportInvoice getImportInvoice() { return importInvoice; }
    public void setImportInvoice(ImportInvoice importInvoice) { this.importInvoice = importInvoice; }
}
