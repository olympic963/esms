package com.mycompany.esms.model;

import java.util.ArrayList;
import java.util.List;

public class CounterInvoice extends SalesInvoice {

    private SalesEmployee salesEmp;
    private List<InvoiceDetails> invoiceDetails = new ArrayList<>();

    public SalesEmployee getSalesEmp() { return salesEmp; }
    public void setSalesEmp(SalesEmployee salesEmp) { this.salesEmp = salesEmp; }

    public List<InvoiceDetails> getInvoiceDetails() { return invoiceDetails; }
    public void setInvoiceDetails(List<InvoiceDetails> invoiceDetails) { this.invoiceDetails = invoiceDetails; }
}
