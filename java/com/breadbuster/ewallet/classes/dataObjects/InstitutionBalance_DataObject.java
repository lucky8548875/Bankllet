package com.breadbuster.ewallet.classes.dataObjects;

public class InstitutionBalance_DataObject {
    private String institution_subCategory,institution_subCategoryTotal,institution_category,institution_categoryTotal;

    public InstitutionBalance_DataObject(String institution_subCategory, String institution_subCategoryTotal, String institution_category, String institution_categoryTotal) {
        this.institution_subCategory = institution_subCategory;
        this.institution_subCategoryTotal = institution_subCategoryTotal;
        this.institution_category = institution_category;
        this.institution_categoryTotal = institution_categoryTotal;
    }

    public String getInstitution_subCategory() {
        return institution_subCategory;
    }

    public String getInstitution_subCategoryTotal() {
        return institution_subCategoryTotal;
    }

    public String getInstitution_category() {
        return institution_category;
    }

    public String getInstitution_categoryTotal() {
        return institution_categoryTotal;
    }
}
