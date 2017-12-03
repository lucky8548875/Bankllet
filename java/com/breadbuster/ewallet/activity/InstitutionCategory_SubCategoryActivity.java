package com.breadbuster.ewallet.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
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
import com.breadbuster.ewallet.classes.adapters.Adapter_Category_SubCategory;
import com.breadbuster.ewallet.classes.adapters.EmptyRecyclerViewAdapter;
import com.breadbuster.ewallet.classes.dataObjects.Category_SubCategory_DataObject;
import com.breadbuster.ewallet.classes.requests.MySingleton;
import com.breadbuster.ewallet.classes.requests.MyStringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InstitutionCategory_SubCategoryActivity extends AppCompatActivity {
    @BindView(R.id.recyclerView_category_subCategory) RecyclerView recyclerView_category_subCategory;
    @BindView(R.id.swipeToRefresh_category_subCategory) SwipeRefreshLayout swipeToRefresh_category_subCategory;

    URLConstants urlConstants = new URLConstants();
    Bean bean = new Bean();

    private String LOG_TAG = "InstitutionCategory_SubCategoryActivity";

    private List<Category_SubCategory_DataObject> category_subCategory_dataObjectList;
    private RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_institution_category__sub_category);
        ButterKnife.bind(this);

        setTitle(InstitutionCategoryActivity.bean.getInstitution_categoryName());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView_category_subCategory.setHasFixedSize(true);
        recyclerView_category_subCategory.setLayoutManager(new LinearLayoutManager(InstitutionCategory_SubCategoryActivity.this));

        category_subCategory_dataObjectList = new ArrayList<>();

        showAllSubCategory();

        swipeToRefresh_category_subCategory.setColorSchemeResources(R.color.colorPrimary);

        swipeToRefresh_category_subCategory.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(category_subCategory_dataObjectList != null){
                    category_subCategory_dataObjectList.clear();
                }

                showAllSubCategory();
                swipeToRefresh_category_subCategory.setRefreshing(false);
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

    private void showAllSubCategory(){
        try{
            final ProgressDialog progressDialog = new ProgressDialog(InstitutionCategory_SubCategoryActivity.this);
            progressDialog.setMessage("Loading data...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            MyStringRequest request = new MyStringRequest(null, Request.Method.POST, urlConstants.getShowAllSubCategoryUrl(),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();

                            if(response.equalsIgnoreCase("no_data")){
                                adapter = new EmptyRecyclerViewAdapter();
                                recyclerView_category_subCategory.setAdapter(adapter);
                            }else{
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    JSONArray category_subCategory_array = jsonObject.getJSONArray("sub_category");
                                    for (int i = 0; i < category_subCategory_array.length(); i++) {
                                        JSONObject sub_category = category_subCategory_array.getJSONObject(i);

                                        bean.setCategory_subCategoryID(sub_category.getString("SubCategoryID"));
                                        bean.setCategory_subCategoryName(sub_category.getString("SubCategoryName"));
                                        bean.setCategoryID(sub_category.getString("CategoryID"));
                                        bean.setSubCategory_dateAndTimeAdded(sub_category.getString("DateAndTimeAdded"));

                                        Category_SubCategory_DataObject category_subCategory_dataObject = new Category_SubCategory_DataObject(
                                                bean.getCategory_subCategoryID(),
                                                bean.getCategory_subCategoryName(),
                                                bean.getCategoryID(),
                                                bean.getSubCategory_dateAndTimeAdded()
                                        );

                                        category_subCategory_dataObjectList.add(category_subCategory_dataObject);
                                    }

                                    adapter = new Adapter_Category_SubCategory(category_subCategory_dataObjectList, InstitutionCategory_SubCategoryActivity.this);
                                    recyclerView_category_subCategory.setAdapter(adapter);
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

            MySingleton.getInstance(InstitutionCategory_SubCategoryActivity.this).addToRequestQueue(request);
        }catch(Exception ex){
            Log.e(LOG_TAG, ex.toString());
        }
    }
}
