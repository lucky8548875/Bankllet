package com.breadbuster.ewallet.fragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.breadbuster.ewallet.R;
import com.breadbuster.ewallet.activity.HomeActivity;
import com.breadbuster.ewallet.classes.Bean;
import com.breadbuster.ewallet.classes.URLConstants;
import com.breadbuster.ewallet.classes.requests.MySingleton;
import com.breadbuster.ewallet.classes.requests.MyStringRequest;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddInstitutionFragment extends Fragment {
    @BindView(R.id.txtInstitutionName) MaterialEditText txtInstitutionName;
    @BindView(R.id.txtInstitutionDescription) MaterialEditText txtInstitutionDescription;
    @BindView(R.id.txtBankAccountNumber) MaterialEditText txtBankAccountNumber;
    @BindView(R.id.btnSave) AppCompatButton btnSave;

    View view;
    Bean bean = new Bean();
    URLConstants urlConstants = new URLConstants();

    public AddInstitutionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_add_institution, container, false);
        ButterKnife.bind(this, view);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addInstitution();
            }
        });

        return view;
    }

    private void init(){
        bean.setInstitutionName(txtInstitutionName.getText().toString());
        bean.setInstitutionDescription(txtInstitutionDescription.getText().toString());
        bean.setBankAccountNumber(txtBankAccountNumber.getText().toString());

        SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy at hh:mm a");
        Calendar dateAndTimeRegistered_calendar = Calendar.getInstance();

        bean.setDateAndTimeRegistered(sdf.format(dateAndTimeRegistered_calendar.getTime()));
    }

    private void addInstitution(){
        init();

        Map<String, String> parameters = new HashMap<>();

        parameters.put("institutionName",bean.getInstitutionName());
        parameters.put("institutionDescription",bean.getInstitutionDescription());
        parameters.put("institutionBankAccountNumber",bean.getBankAccountNumber());
        parameters.put("dateAndTimeRegistered",bean.getDateAndTimeRegistered());

        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Adding institution.. Please wait!");
        progressDialog.setCancelable(false);
        progressDialog.show();

        MyStringRequest request = new MyStringRequest(parameters, Request.Method.POST, urlConstants.getAddInstitutionUrl(),
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
}
