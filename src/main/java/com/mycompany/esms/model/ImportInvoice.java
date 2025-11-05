package com.mycompany.esms.model;

import java.time.LocalDate;

public class ImportInvoice {

    private Integer id;
    private LocalDate importDate;
    private float totalValue;
    private int totalQuantity;
    private float tax;
    private Supplier supplier;
    private WarehouseEmployee warehouseEmp;

    public ImportInvoice() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public LocalDate getImportDate() { return importDate; }
    public void setImportDate(LocalDate importDate) { this.importDate = importDate; }

    public float getTotalValue() { return totalValue; }
    public void setTotalValue(float totalValue) { this.totalValue = totalValue; }

    public int getTotalQuantity() { return totalQuantity; }
    public void setTotalQuantity(int totalQuantity) { this.totalQuantity = totalQuantity; }

    public float getTax() { return tax; }
    public void setTax(float tax) { this.tax = tax; }

    public Supplier getSupplier() { return supplier; }
    public void setSupplier(Supplier supplier) { this.supplier = supplier; }

    public WarehouseEmployee getWarehouseEmp() { return warehouseEmp; }
    public void setWarehouseEmp(WarehouseEmployee warehouseEmp) { this.warehouseEmp = warehouseEmp; }
}
