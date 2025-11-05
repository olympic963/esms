package com.mycompany.esms.model;

public class ItemStatistics extends Item {

    private float revenue;
    private int unitsSold;
    private float avgSales;

    public float getRevenue() { return revenue; }
    public void setRevenue(float revenue) { this.revenue = revenue; }

    public int getUnitsSold() { return unitsSold; }
    public void setUnitsSold(int unitsSold) { this.unitsSold = unitsSold; }

    public float getAvgSales() { return avgSales; }
    public void setAvgSales(float avgSales) { this.avgSales = avgSales; }

}
