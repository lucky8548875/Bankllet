package com.breadbuster.ewallet.fragment;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.breadbuster.ewallet.R;
import com.breadbuster.ewallet.activity.HomeActivity;
import com.breadbuster.ewallet.classes.Bean;
import com.breadbuster.ewallet.classes.URLConstants;
import com.breadbuster.ewallet.classes.adapters.Adapter_Institution;
import com.breadbuster.ewallet.classes.adapters.Adapter_Transactions;
import com.breadbuster.ewallet.classes.adapters.EmptyRecyclerViewAdapter;
import com.breadbuster.ewallet.classes.dataObjects.Institution_DataObject;
import com.breadbuster.ewallet.classes.dataObjects.Transactions_DataObject;
import com.breadbuster.ewallet.classes.requests.CacheRequest;
import com.breadbuster.ewallet.classes.requests.MySingleton;
import com.breadbuster.ewallet.classes.requests.MyStringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class CheckTransactionsFragment extends Fragment implements SearchView.OnQueryTextListener{
    @BindView(R.id.recyclerView_transactions) RecyclerView recyclerView_transactions;
    @BindView(R.id.swipeToRefresh_transactions) SwipeRefreshLayout swipeToRefresh_transactions;

    View view;
    URLConstants urlConstants = new URLConstants();
    Bean bean = new Bean();

    private List<Transactions_DataObject> transactions_dataObjectList;
    private Adapter_Transactions adapter_transactions;
    private RecyclerView.Adapter adapter_emptyRecyclerView;

    public CheckTransactionsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_check_transactions, container, false);
        ButterKnife.bind(this,view);

        recyclerView_transactions = view.findViewById(R.id.recyclerView_transactions);
        recyclerView_transactions.setHasFixedSize(true);
        recyclerView_transactions.setLayoutManager(new LinearLayoutManager(getActivity()));

        swipeToRefresh_transactions.setColorSchemeResources(R.color.colorPrimary);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setHasOptionsMenu(true);

        transactions_dataObjectList = new ArrayList<>();

        if(HomeActivity.bean.getUserType().equalsIgnoreCase("Administrator")){
            checkAllTransactions(urlConstants.getCheckAllTransactionsUrl() + HomeActivity.bean.getInstitutionName());
        }else if(HomeActivity.bean.getUserType().equalsIgnoreCase("Student")){
            checkAllTransactions(urlConstants.getCheckAllTransactionsOfLoggedInUserUrl() + HomeActivity.bean.getIdNumber());
        }

        swipeToRefresh_transactions.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(transactions_dataObjectList != null){
                    transactions_dataObjectList.clear();
                }

                if(HomeActivity.bean.getUserType().equalsIgnoreCase("Administrator")){
                    checkAllTransactions(urlConstants.getCheckAllTransactionsUrl() + HomeActivity.bean.getUserInstitution());
                }else if(HomeActivity.bean.getUserType().equalsIgnoreCase("Student")){
                    checkAllTransactions(urlConstants.getCheckAllTransactionsOfLoggedInUserUrl() + HomeActivity.bean.getIdNumber());
                }

                swipeToRefresh_transactions.setRefreshing(false);
            }
        });

        adapter_transactions = new Adapter_Transactions(transactions_dataObjectList,getActivity());
        recyclerView_transactions.setAdapter(adapter_transactions);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.home,menu);
        final MenuItem menuItem = menu.findItem(R.id.btnSearch);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setQueryHint("Search by Product or Item Name...");
        searchView.setOnQueryTextListener(this);

        MenuItemCompat.setOnActionExpandListener(menuItem,
                new MenuItemCompat.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem item) {
                        // Do something when collapsed
                        adapter_transactions.setFilter(transactions_dataObjectList);
                        return true; // Return true to collapse action view
                    }

                    @Override
                    public boolean onMenuItemActionExpand(MenuItem item) {
                        // Do something when expanded
                        return true; // Return true to expand action view
                    }
                });
    }

    @SuppressLint("LongLogTag")
    private void checkAllTransactions(String url){
        try{
            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Loading data...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            MyStringRequest request = new MyStringRequest(null, Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();

                            try {
                                if(response.equalsIgnoreCase("no_data")){
                                    adapter_emptyRecyclerView = new EmptyRecyclerViewAdapter();
                                    recyclerView_transactions.setAdapter(adapter_emptyRecyclerView);
                                }else {
                                    JSONObject jsonObject = new JSONObject(response);
                                    JSONArray transactions = jsonObject.getJSONArray("transactions");
                                    for (int i = 0; i < transactions.length(); i++) {
                                        JSONObject transaction = transactions.getJSONObject(i);

                                        bean.setProductID(transaction.getString("ID"));
                                        bean.setProductOrItemName(transaction.getString("ProductOrItemName"));
                                        bean.setProductOrItemAmount(transaction.getString("ProductOrItemAmount"));
                                        bean.setProductAmountCurrency(transaction.getString("ProductAmountCurrency"));
                                        bean.setProductCategory(transaction.getString("ProductCategory"));
                                        bean.setProductPurchasedBy(transaction.getString("ProductPurchasedBy"));
                                        bean.setInstitutionName(transaction.getString("ProductInstitution"));
                                        bean.setProductStatus(transaction.getString("ProductStatus"));
                                        bean.setDateAndTimePurchased(transaction.getString("DateAndTimePurchased"));

                                        Transactions_DataObject transactions_dataObject = new Transactions_DataObject(
                                                bean.getProductID(),
                                                bean.getProductOrItemName(),
                                                bean.getProductOrItemAmount(),
                                                bean.getProductAmountCurrency(),
                                                bean.getProductCategory(),
                                                bean.getProductPurchasedBy(),
                                                bean.getInstitutionName(),
                                                bean.getProductStatus(),
                                                bean.getDateAndTimePurchased()
                                        );

                                        transactions_dataObjectList.add(transactions_dataObject);
                                    }

                                    adapter_transactions = new Adapter_Transactions(transactions_dataObjectList,getActivity());

                                    recyclerView_transactions.setAdapter(adapter_transactions);
                                    adapter_transactions.notifyDataSetChanged();
                                }
                            } catch (JSONException e) {
                                Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
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

                            Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                        }
                    }
            );

            request.setRetryPolicy(new DefaultRetryPolicy(0, -1, 0));
            MySingleton.getInstance(getActivity()).addToRequestQueue(request);
        }catch(Exception ex){
            Log.e("CheckTransactionsFragment", ex.toString());
        }

    }

    private List<Transactions_DataObject> filter(List<Transactions_DataObject> models, String query) {
        query = query.toLowerCase();
        final List<Transactions_DataObject> filteredModelList = new ArrayList<>();
        for (Transactions_DataObject model : models) {
            final String productOrItemName = model.getProductOrItemName().toLowerCase();

            if (productOrItemName.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        final List<Transactions_DataObject> filteredModelList = filter(transactions_dataObjectList,newText);
        adapter_transactions.setFilter(filteredModelList);

        return true;
    }
}
