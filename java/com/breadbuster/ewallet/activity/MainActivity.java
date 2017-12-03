package com.breadbuster.ewallet.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.breadbuster.ewallet.R;
import com.breadbuster.ewallet.classes.Bean;
import com.breadbuster.ewallet.classes.SharedPrefManager;
import com.breadbuster.ewallet.classes.URLConstants;
import com.breadbuster.ewallet.classes.requests.MySingleton;
import com.breadbuster.ewallet.classes.requests.MyStringRequest;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.txtIDNumber) MaterialEditText txtIDNumber;
    @BindView(R.id.txtPassword) MaterialEditText txtPassword;
    @BindView(R.id.btnLogin) AppCompatButton btnLogin;

    ProgressDialog progressDialog;

    Bean bean = new Bean();
    URLConstants urlConstants = new URLConstants();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if(SharedPrefManager.getInstance(this).isLoggedIn()){
            finish();
            startActivity(new Intent(this, HomeActivity.class));
            return;
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();
            }
        });
    }

    private void init(){
        bean.setIdNumber(txtIDNumber.getText().toString());
        bean.setPassword(txtPassword.getText().toString());
    }

    private  void validate(){
        init();

        if(TextUtils.isEmpty(bean.getIdNumber())){
            txtIDNumber.setError("Please enter your ID Number!");
            txtIDNumber.requestFocus();
        }else if(TextUtils.isEmpty(bean.getPassword())){
            txtPassword.setError("Please enter your password!");
            txtPassword.requestFocus();
        }else{
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Authenticating...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            loginUser();
                        }
                    }, 3000);
        }
    }

    private void loginUser(){
        final Map<String, String> parameters = new HashMap<>();

        parameters.put("IDNumber",bean.getIdNumber());
        parameters.put("userPassword",bean.getPassword());

        MyStringRequest request = new MyStringRequest(parameters, Request.Method.POST, urlConstants.getLoginUserUrl(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        if(response.equalsIgnoreCase("invalid")){
                            Toast.makeText(MainActivity.this, "Invalid ID Number and password! Please try again!", Toast.LENGTH_LONG).show();
                        }else{
                            SharedPrefManager.getInstance(getApplicationContext())
                                    .userLogin(bean.getIdNumber());

                            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                            finish();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        String message = null;
                        if (error instanceof NetworkError) {
                            message = "Cannot connect to Internet...Please check your connection!";
                        } else if (error instanceof ServerError) {
                            message = "The server could not be found. Please try again after some time!!";
                        } else if (error instanceof AuthFailureError) {
                            message = "Cannot connect to Internet...Please check your connection!";
                        } else if (error instanceof ParseError) {
                            message = "Parsing error! Please try again after some time!!";
                        } else if (error instanceof TimeoutError) {
                            message = "Connection TimeOut! Please check your internet connection.";
                        }

                        Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
                    }
                }
        );

        MySingleton.getInstance(MainActivity.this).addToRequestQueue(request);
    }
}
