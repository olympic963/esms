package com.mycompany.esms.model;

public class SupplierStatistic {
    private int supplierId;
    private String supplierName;
    private int totalQuantity;
    private float totalValue;
    private int importCount;

    public SupplierStatistic(int supplierId, String supplierName, int totalQuantity, float totalValue, int importCount) {
        this.supplierId = supplierId;
        this.supplierName = supplierName;
        this.totalQuantity = totalQuantity;
        this.totalValue = totalValue;
        this.importCount = importCount;
    }

    public int getSupplierId() { return supplierId; }
    public String getSupplierName() { return supplierName; }
    public int getTotalQuantity() { return totalQuantity; }
    public float getTotalValue() { return totalValue; }
    public int getImportCount() { return importCount; }
}


