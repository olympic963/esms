package com.mycompany.esms.model;

public class OnlineInvoice extends SalesInvoice {

    private Order order;
    private WarehouseEmployee warehouseEmp;
    private DeliveryEmployee deliveryEmp;

    public OnlineInvoice() {}

    public Order getOrder() { return order; }
    public void setOrder(Order order) { this.order = order; }

    public WarehouseEmployee getWarehouseEmp() { return warehouseEmp; }
    public void setWarehouseEmp(WarehouseEmployee warehouseEmp) { this.warehouseEmp = warehouseEmp; }

    public DeliveryEmployee getDeliveryEmp() { return deliveryEmp; }
    public void setDeliveryEmp(DeliveryEmployee deliveryEmp) { this.deliveryEmp = deliveryEmp; }
}
