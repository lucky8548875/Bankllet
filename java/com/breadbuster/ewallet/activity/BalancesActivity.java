package com.breadbuster.ewallet.activity;

import android.app.ProgressDialog;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.breadbuster.ewallet.R;
import com.breadbuster.ewallet.classes.Bean;
import com.breadbuster.ewallet.classes.URLConstants;
import com.breadbuster.ewallet.classes.adapters.Adapter_InstitutionBalance;
import com.breadbuster.ewallet.classes.adapters.EmptyRecyclerViewAdapter;
import com.breadbuster.ewallet.classes.dataObjects.InstitutionBalance_DataObject;
import com.breadbuster.ewallet.classes.requests.MySingleton;
import com.breadbuster.ewallet.classes.requests.MyStringRequest;
import com.breadbuster.ewallet.fragment.InstitutionListFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BalancesActivity extends AppCompatActivity {
    @BindView(R.id.recyclerView_balances) RecyclerView recyclerView_balances;
    @BindView(R.id.swipeToRefresh_balances) SwipeRefreshLayout swipeToRefresh_balances;

    URLConstants urlConstants = new URLConstants();
    Bean bean = new Bean();

    private String LOG_TAG = "InstitutionCategoryActivity";

    private List<InstitutionBalance_DataObject> institutionBalance_dataObjectList;
    private RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balances);
        ButterKnife.bind(this);

        setTitle(InstitutionListFragment.bean.getInstitutionName());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView_balances.setHasFixedSize(true);
        recyclerView_balances.setLayoutManager(new LinearLayoutManager(BalancesActivity.this));

        institutionBalance_dataObjectList = new ArrayList<>();

        showAllBalances();

        swipeToRefresh_balances.setColorSchemeResources(R.color.colorPrimary);

        swipeToRefresh_balances.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(institutionBalance_dataObjectList != null){
                    institutionBalance_dataObjectList.clear();
                }

                showAllBalances();
                swipeToRefresh_balances.setRefreshing(false);
            }
        });
    }

    private void showAllBalances(){
        try{
            final ProgressDialog progressDialog = new ProgressDialog(BalancesActivity.this);
            progressDialog.setMessage("Loading data...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            MyStringRequest request = new MyStringRequest(null, Request.Method.POST, urlConstants.getShowAllInstitutionCategoryUrl(),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();

                            if(response.equalsIgnoreCase("no_data")){
                                adapter = new EmptyRecyclerViewAdapter();
                                recyclerView_balances.setAdapter(adapter);
                            }else{
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    JSONArray balances_array = jsonObject.getJSONArray("institution_category");
                                    for (int i = 0; i < balances_array.length(); i++) {
                                        JSONObject balances = balances_array.getJSONObject(i);



                                        //institutionBalance_dataObjectList.add(institutionCategory_dataObject);
                                    }

                                    adapter = new Adapter_InstitutionBalance(institutionBalance_dataObjectList, BalancesActivity.this);
                                    recyclerView_balances.setAdapter(adapter);
                                } catch (JSONException e) {
                                    Log.e(LOG_TAG, e.toString());
                                }
                            }
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

            MySingleton.getInstance(BalancesActivity.this).addToRequestQueue(request);
        }catch(Exception ex){
            Log.e(LOG_TAG, ex.toString());
        }
    }


}
