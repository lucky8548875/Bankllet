package com.breadbuster.ewallet.classes.dataObjects;

public class Transactions_DataObject {
    private String productID,productOrItemName,productOrItemAmount,productAmountCurrency,productCategory,productPurchasedBy,
            productInstitution,productStatus,dateAndTimePurchased;

    public Transactions_DataObject(String productID, String productOrItemName, String productOrItemAmount, String productAmountCurrency, String productCategory, String productPurchasedBy, String productInstitution, String productStatus, String dateAndTimePurchased) {
        this.productID = productID;
        this.productOrItemName = productOrItemName;
        this.productOrItemAmount = productOrItemAmount;
        this.productAmountCurrency = productAmountCurrency;
        this.productCategory = productCategory;
        this.productPurchasedBy = productPurchasedBy;
        this.productInstitution = productInstitution;
        this.productStatus = productStatus;
        this.dateAndTimePurchased = dateAndTimePurchased;
    }

    public String getProductID() {
        return productID;
    }

    public String getProductOrItemName() {
        return productOrItemName;
    }

    public String getProductOrItemAmount() {
        return productOrItemAmount;
    }

    public String getProductAmountCurrency() {
        return productAmountCurrency;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public String getProductPurchasedBy() {
        return productPurchasedBy;
    }

    public String getProductInstitution() {
        return productInstitution;
    }

    public String getProductStatus() {
        return productStatus;
    }

    public String getDateAndTimePurchased() {
        return dateAndTimePurchased;
    }
}
