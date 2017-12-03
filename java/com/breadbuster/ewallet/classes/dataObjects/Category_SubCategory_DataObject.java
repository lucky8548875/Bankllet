package com.breadbuster.ewallet.classes.dataObjects;

public class Category_SubCategory_DataObject {
    private String category_subCategoryID,category_subCategoryName,categoryID,subCategory_dateAndTimeAdded;

    public Category_SubCategory_DataObject(String category_subCategoryID, String category_subCategoryName, String categoryID, String subCategory_dateAndTimeAdded) {
        this.category_subCategoryID = category_subCategoryID;
        this.category_subCategoryName = category_subCategoryName;
        this.categoryID = categoryID;
        this.subCategory_dateAndTimeAdded = subCategory_dateAndTimeAdded;
    }

    public String getCategory_subCategoryID() {
        return category_subCategoryID;
    }

    public String getCategory_subCategoryName() {
        return category_subCategoryName;
    }

    public String getCategoryID() {
        return categoryID;
    }

    public String getSubCategory_dateAndTimeAdded() {
        return subCategory_dateAndTimeAdded;
    }
}
