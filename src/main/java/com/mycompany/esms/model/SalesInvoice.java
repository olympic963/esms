package com.mycompany.esms.model;

import java.time.LocalDate;

public class SalesInvoice {

    private Integer id;

    private LocalDate issueDate;
    private float tax;
    private float totalValue;

    public SalesInvoice() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public LocalDate getIssueDate() { return issueDate; }
    public void setIssueDate(LocalDate issueDate) { this.issueDate = issueDate; }

    public float getTax() { return tax; }
    public void setTax(float tax) { this.tax = tax; }

    public float getTotalValue() { return totalValue; }
    public void setTotalValue(float totalValue) { this.totalValue = totalValue; }
}
