package com.breadbuster.ewallet.classes.dataObjects;

public class InstitutionCategory_DataObject {
    private String institution_categoryID,institution_categoryName,institution_categoryInstitutionID,institution_categoryDateAndTimeAdded;

    public InstitutionCategory_DataObject(String institution_categoryID, String institution_categoryName, String institution_categoryInstitutionID, String institution_categoryDateAndTimeAdded) {
        this.institution_categoryID = institution_categoryID;
        this.institution_categoryName = institution_categoryName;
        this.institution_categoryInstitutionID = institution_categoryInstitutionID;
        this.institution_categoryDateAndTimeAdded = institution_categoryDateAndTimeAdded;
    }

    public String getInstitution_categoryID() {
        return institution_categoryID;
    }

    public String getInstitution_categoryName() {
        return institution_categoryName;
    }

    public String getInstitution_categoryInstitutionID() {
        return institution_categoryInstitutionID;
    }

    public String getInstitution_categoryDateAndTimeAdded() {
        return institution_categoryDateAndTimeAdded;
    }
}
