package com.project.changzhzfinalproject;

public class DatabaseMgm {
    private String transactionID, itemID, customerID;
    int dealPrice;

    //Database Management constructor
    public DatabaseMgm(String transactionID, String itemID, String customerID, int dealPrice){
        this.transactionID=transactionID;
        this.itemID=itemID;
        this.customerID=customerID;
        this.dealPrice=dealPrice;
    }

    public String getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

    public String getItemID() {
        return itemID;
    }

    public void setItemID(String itemID) {
        this.itemID = itemID;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public int getDealPrice() {
        return dealPrice;
    }

    public void setDealPrice(int dealPrice) {
        this.dealPrice = dealPrice;
    }
}
