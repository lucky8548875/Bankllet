package com.breadbuster.ewallet.fragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatSpinner;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.breadbuster.ewallet.R;
import com.breadbuster.ewallet.activity.HomeActivity;
import com.breadbuster.ewallet.classes.Bean;
import com.breadbuster.ewallet.classes.JSON;
import com.breadbuster.ewallet.classes.URLConstants;
import com.breadbuster.ewallet.classes.handlers.NumberTextWatcher;
import com.breadbuster.ewallet.classes.requests.MyJSONRequest;
import com.breadbuster.ewallet.classes.requests.MySingleton;
import com.breadbuster.ewallet.classes.requests.MyStringRequest;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class PurchaseAnItemFragment extends Fragment {
    @BindView(R.id.cmbCategory) AppCompatSpinner cmbCategory;
    @BindView(R.id.cmbSubCategory) AppCompatSpinner cmbSubCategory;
    @BindView(R.id.txtProductOrItemAmount) MaterialEditText txtProductOrItemAmount;
    @BindView(R.id.btnPurchase) AppCompatButton btnPurchase;

    View view;
    Bean bean = new Bean();
    URLConstants urlConstants = new URLConstants();

    private String LOG_TAG = "PurchaseAnItemFragment";

    private ArrayList<String> category,subCategory;

    public PurchaseAnItemFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_purchase_an_item, container, false);
        ButterKnife.bind(this,view);

        category = new ArrayList<>();
        subCategory = new ArrayList<>();

        getAllCategoryBasedOnInstitution();
        getAllSubCategoryBasedOnSelectedInstitution();

        txtProductOrItemAmount.addTextChangedListener(new NumberTextWatcher(txtProductOrItemAmount,"#,###"));

        btnPurchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!validateDropdown(cmbCategory,"CHOOSE YOUR CATEGORY","Please choose your category!")) {
                    if(!validateDropdown(cmbSubCategory,"CHOOSE YOUR SUB-CATEGORY","Please choose your sub-category!")){
                        purchaseAnItem();
                    }
                }

            }
        });

        return view;
    }

    private void init(){
        bean.setProductOrItemAmount(txtProductOrItemAmount.getText().toString());
        bean.setProductAmountCurrency("PHP");
        bean.setProductCategory(cmbCategory.getSelectedItem().toString());
        bean.setProductSubCategory(cmbSubCategory.getSelectedItem().toString());
        bean.setProductPurchasedBy(HomeActivity.bean.getIdNumber());
        bean.setProductInstitution(HomeActivity.bean.getUserInstitution());
        bean.setProductStatus("Unseen");

        SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy at hh:mm a");
        Calendar dateAndTimePurchased_calendar = Calendar.getInstance();

        bean.setDateAndTimePurchased(sdf.format(dateAndTimePurchased_calendar.getTime()));
    }

    private boolean validateDropdown(AppCompatSpinner spinner, String selectedText, String errorMessage){
        boolean isEmpty = false;

        if(spinner.getSelectedItem().toString() == selectedText){
            Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
            spinner.requestFocus();
            isEmpty = true;
        }

        return isEmpty;
    }

    private void purchaseAnItem(){
        init();

        Map<String, String> parameters = new HashMap<>();

        parameters.put("productOrItemAmount",bean.getProductOrItemAmount());
        parameters.put("productAmountCurrency",bean.getProductAmountCurrency());
        parameters.put("productCategory",bean.getProductCategory());
        parameters.put("productSubCategory",bean.getProductSubCategory());
        parameters.put("productPurchasedBy",bean.getProductPurchasedBy());
        parameters.put("productInstitution",bean.getProductInstitution());
        parameters.put("productStatus",bean.getProductStatus());
        parameters.put("dateAndTimePurchased",bean.getDateAndTimePurchased());


        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Purchasing an item.. Please wait!");
        progressDialog.setCancelable(false);
        progressDialog.show();

        MyStringRequest request = new MyStringRequest(parameters, Request.Method.POST, urlConstants.getPurchaseAnItem(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        Toast.makeText(getActivity(), response, Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getActivity(), HomeActivity.class));
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();

                        Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                    }
                }
        );

        MySingleton.getInstance(getActivity()).addToRequestQueue(request);
    }

    private void initializeArrayAdapter(ArrayList<String> arrayList, AppCompatSpinner spinner){
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                getActivity(),R.layout.spinner_item,arrayList){
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                }
                else
                {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };

        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(spinnerArrayAdapter);
    }

    private void spinnerOnItemSelected(AppCompatSpinner spinner){
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText = (String) parent.getItemAtPosition(position);
                // If user change the default selection
                // First item is disable and it is used for hint
                if (position > 0) {
                    // Notify the selected item text
                    Toast.makeText
                            (getContext(), "Selected : " + selectedItemText, Toast.LENGTH_SHORT)
                            .show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void getAllCategoryBasedOnInstitution(){
        try{
            final ProgressDialog progressDialog = new ProgressDialog(getContext());
            progressDialog.setMessage("Loading data...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            MyJSONRequest request = new MyJSONRequest(null, Request.Method.POST, urlConstants.getShowAllCategoryBasedOnInstitutionUrl() + HomeActivity.bean.getUserInstitution(),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            progressDialog.dismiss();

                            category.clear();
                            category.add("CHOOSE YOUR CATEGORY");

                            JSON.getJSONData("category","CategoryName",category,response);

                            // Initializing an ArrayAdapter
                            initializeArrayAdapter(category,cmbCategory);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                            Log.e(LOG_TAG, error.toString());
                        }
                    }
            );

            MySingleton.getInstance(getActivity()).addToRequestQueue(request);
        }catch(Exception ex){
            Log.e(LOG_TAG, ex.toString());
        }
    }

    public void getAllSubCategoryBasedOnSelectedInstitution(){
        try{
            MyJSONRequest request = new MyJSONRequest(null, Request.Method.POST, urlConstants.getShowAllSubCategoryBasedOnSelectedInstitutionUrl() + cmbCategory.getSelectedItem().toString(),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            subCategory.clear();
                            subCategory.add("CHOOSE YOUR SUB-CATEGORY");

                            JSON.getJSONData("sub_category","SubCategoryName",subCategory,response);

                            // Initializing an ArrayAdapter
                            initializeArrayAdapter(subCategory,cmbSubCategory);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e(LOG_TAG, error.toString());
                        }
                    }
            );

            MySingleton.getInstance(getActivity()).addToRequestQueue(request);
        }catch(Exception ex){
            Log.e(LOG_TAG, ex.toString());
        }
    }

}
