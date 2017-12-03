package com.breadbuster.ewallet.classes;

public class URLConstants {
    private String address = "http://tristanrosales.x10.mx/E-Wallet/";

    //login
    private String loginUserUrl = address + "loginUser.php";

    //add or insert
    private String addUserUrl = address + "insert/addUser.php";
    private String addInstitutionUrl = address + "insert/addInstitution.php";
    private String purchaseAnItem = address + "insert/purchaseAnItem.php";


    //update or commit
    private String updateProductStatusUrl = address + "update/updateProductStatus.php?id=";



    //delete


    //show
    private String showInfoOfLoggedInUserUrl = address + "show/showInfoOfLoggedInUser.php?IDNumber=";
    private String showAllInstitutionsUrl = address + "show/showAllInstitutions.php";
    private String checkAllTransactionsUrl = address + "show/checkAllTransactions.php?productInstitution=";
    private String checkAllTransactionsOfLoggedInUserUrl = address + "show/checkAllTransactionsOfLoggedInUser.php?productPurchasedBy=";
    private String showAllInstitutionCategoryUrl = address + "show/showAllInstitutionCategory.php";
    private String showAllSubCategoryUrl = address + "show/showAllSubCategory.php";
    private String showAllCategoryBasedOnInstitutionUrl = address + "show/showAllCategoryBasedOnInstitution.php?categoryInstitutionID=";
    private String showAllSubCategoryBasedOnSelectedInstitutionUrl = address + "show/showAllSubCategoryBasedOnSelectedInstitution.php?selectedCategory=";




    //login
    public String getLoginUserUrl() {
        return loginUserUrl;
    }


    //add or insert
    public String getAddUserUrl() {
        return addUserUrl;
    }

    public String getAddInstitutionUrl() {
        return addInstitutionUrl;
    }

    public String getPurchaseAnItem() {
        return purchaseAnItem;
    }




    //update or commit
    public String getUpdateProductStatusUrl() {
        return updateProductStatusUrl;
    }






    //show
    public String getShowInfoOfLoggedInUserUrl() {
        return showInfoOfLoggedInUserUrl;
    }

    public String getShowAllInstitutionsUrl() {
        return showAllInstitutionsUrl;
    }

    public String getCheckAllTransactionsUrl() {
        return checkAllTransactionsUrl;
    }

    public String getCheckAllTransactionsOfLoggedInUserUrl() {
        return checkAllTransactionsOfLoggedInUserUrl;
    }

    public String getShowAllInstitutionCategoryUrl() {
        return showAllInstitutionCategoryUrl;
    }

    public String getShowAllSubCategoryUrl() {
        return showAllSubCategoryUrl;
    }

    public String getShowAllCategoryBasedOnInstitutionUrl() {
        return showAllCategoryBasedOnInstitutionUrl;
    }

    public String getShowAllSubCategoryBasedOnSelectedInstitutionUrl() {
        return showAllSubCategoryBasedOnSelectedInstitutionUrl;
    }
}
