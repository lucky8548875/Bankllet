package com.breadbuster.ewallet.classes.dataObjects;

public class Institution_DataObject {
    private String institution_ID,institution_name,institution_description,institution_bankAccountNumber,institution_dateAndTimeRegistered;

    public Institution_DataObject(String institution_ID, String institution_name, String institution_description, String institution_bankAccountNumber, String institution_dateAndTimeRegistered) {
        this.institution_ID = institution_ID;
        this.institution_name = institution_name;
        this.institution_description = institution_description;
        this.institution_bankAccountNumber = institution_bankAccountNumber;
        this.institution_dateAndTimeRegistered = institution_dateAndTimeRegistered;
    }

    public String getInstitution_ID() {
        return institution_ID;
    }

    public String getInstitution_name() {
        return institution_name;
    }

    public String getInstitution_description() {
        return institution_description;
    }

    public String getInstitution_bankAccountNumber() {
        return institution_bankAccountNumber;
    }

    public String getInstitution_dateAndTimeRegistered() {
        return institution_dateAndTimeRegistered;
    }
}
