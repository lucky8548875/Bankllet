package com.breadbuster.ewallet.activity;

import android.app.ProgressDialog;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.breadbuster.ewallet.R;
import com.breadbuster.ewallet.classes.Bean;
import com.breadbuster.ewallet.classes.URLConstants;
import com.breadbuster.ewallet.classes.adapters.Adapter_InstitutionCategory;
import com.breadbuster.ewallet.classes.adapters.EmptyRecyclerViewAdapter;
import com.breadbuster.ewallet.classes.dataObjects.InstitutionCategory_DataObject;
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

public class InstitutionCategoryActivity extends AppCompatActivity {
    @BindView(R.id.recyclerView_institutionCategory) RecyclerView recyclerView_institutionCategory;
    @BindView(R.id.swipeToRefresh_institutionCategory) SwipeRefreshLayout swipeToRefresh_institutionCategory;

    URLConstants urlConstants = new URLConstants();
    public static Bean bean = new Bean();

    private String LOG_TAG = "InstitutionCategoryActivity";

    private List<InstitutionCategory_DataObject> institutionCategory_dataObjectList;
    private RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_institution_category);
        ButterKnife.bind(this);

        setTitle(InstitutionListFragment.bean.getInstitutionName());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView_institutionCategory.setHasFixedSize(true);
        recyclerView_institutionCategory.setLayoutManager(new LinearLayoutManager(InstitutionCategoryActivity.this));

        institutionCategory_dataObjectList = new ArrayList<>();

        showAllInstitution();

        swipeToRefresh_institutionCategory.setColorSchemeResources(R.color.colorPrimary);

        swipeToRefresh_institutionCategory.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(institutionCategory_dataObjectList != null){
                    institutionCategory_dataObjectList.clear();
                }

                showAllInstitution();
                swipeToRefresh_institutionCategory.setRefreshing(false);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem){
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    private void showAllInstitution(){
        try{
            final ProgressDialog progressDialog = new ProgressDialog(InstitutionCategoryActivity.this);
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
                                recyclerView_institutionCategory.setAdapter(adapter);
                            }else{
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    JSONArray institution_category_array = jsonObject.getJSONArray("institution_category");
                                    for (int i = 0; i < institution_category_array.length(); i++) {
                                        JSONObject institution_category = institution_category_array.getJSONObject(i);

                                        bean.setInstitution_categoryID(institution_category.getString("CategoryID"));
                                        bean.setInstitution_categoryName(institution_category.getString("CategoryName"));
                                        bean.setInstitution_categoryInstitutionID(institution_category.getString("CategoryInstitutionID"));
                                        bean.setInstitution_categoryDateAndTimeAdded(institution_category.getString("DateAndTimeAdded"));

                                        InstitutionCategory_DataObject institutionCategory_dataObject = new InstitutionCategory_DataObject(
                                                bean.getInstitution_categoryID(),
                                                bean.getInstitution_categoryName(),
                                                bean.getInstitution_categoryInstitutionID(),
                                                bean.getInstitution_categoryDateAndTimeAdded()
                                        );

                                        institutionCategory_dataObjectList.add(institutionCategory_dataObject);
                                    }

                                    adapter = new Adapter_InstitutionCategory(institutionCategory_dataObjectList, InstitutionCategoryActivity.this);
                                    recyclerView_institutionCategory.setAdapter(adapter);
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

            MySingleton.getInstance(InstitutionCategoryActivity.this).addToRequestQueue(request);
        }catch(Exception ex){
            Log.e(LOG_TAG, ex.toString());
        }
    }
}
