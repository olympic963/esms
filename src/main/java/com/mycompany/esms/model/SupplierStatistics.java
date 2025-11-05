package com.mycompany.esms.model;

public class SupplierStatistics extends Supplier {

    private int totalImportQuantity;
    private float totalImportValue;
    private int importCount;

    public int getTotalImportQuantity() { return totalImportQuantity; }
    public void setTotalImportQuantity(int totalImportQuantity) { this.totalImportQuantity = totalImportQuantity; }

    public float getTotalImportValue() { return totalImportValue; }
    public void setTotalImportValue(float totalImportValue) { this.totalImportValue = totalImportValue; }

    public int getImportCount() { return importCount; }
    public void setImportCount(int importCount) { this.importCount = importCount; }

}
