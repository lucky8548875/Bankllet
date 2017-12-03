package com.breadbuster.ewallet.fragment;


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
import com.breadbuster.ewallet.classes.Bean;
import com.breadbuster.ewallet.classes.URLConstants;
import com.breadbuster.ewallet.classes.adapters.Adapter_Institution;
import com.breadbuster.ewallet.classes.adapters.EmptyRecyclerViewAdapter;
import com.breadbuster.ewallet.classes.dataObjects.Institution_DataObject;
import com.breadbuster.ewallet.classes.requests.CacheRequest;
import com.breadbuster.ewallet.classes.requests.MySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class InstitutionListFragment extends Fragment implements SearchView.OnQueryTextListener {
    @BindView(R.id.recyclerView_institutions) RecyclerView recyclerView_institutions;
    @BindView(R.id.swipeToRefresh_institutions) SwipeRefreshLayout swipeToRefresh_institutions;

    View view;
    URLConstants urlConstants = new URLConstants();
    public static Bean bean = new Bean();

    private List<Institution_DataObject> institution_dataObjectList;
    private Adapter_Institution adapter_institution;
    private RecyclerView.Adapter adapter_emptyRecyclerView;

    public InstitutionListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_institution_list, container, false);
        ButterKnife.bind(this,view);

        recyclerView_institutions = view.findViewById(R.id.recyclerView_institutions);
        recyclerView_institutions.setHasFixedSize(true);
        recyclerView_institutions.setLayoutManager(new LinearLayoutManager(getActivity()));

        swipeToRefresh_institutions.setColorSchemeResources(R.color.colorPrimary);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setHasOptionsMenu(true);

        institution_dataObjectList = new ArrayList<>();

        showAllInstitutions();

        swipeToRefresh_institutions.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(institution_dataObjectList != null){
                    institution_dataObjectList.clear();
                }

                showAllInstitutions();
                swipeToRefresh_institutions.setRefreshing(false);
            }
        });

        adapter_institution = new Adapter_Institution(institution_dataObjectList,getContext());
        recyclerView_institutions.setAdapter(adapter_institution);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.home,menu);
        final MenuItem menuItem = menu.findItem(R.id.btnSearch);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setQueryHint("Search by Institution Name...");
        searchView.setOnQueryTextListener(this);

        MenuItemCompat.setOnActionExpandListener(menuItem,
                new MenuItemCompat.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem item) {
                        // Do something when collapsed
                        adapter_institution.setFilter(institution_dataObjectList);
                        return true; // Return true to collapse action view
                    }

                    @Override
                    public boolean onMenuItemActionExpand(MenuItem item) {
                        // Do something when expanded
                        return true; // Return true to expand action view
                    }
                });
    }

    private void showAllInstitutions(){
        try{
            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Loading data...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            CacheRequest request = new CacheRequest(null, Request.Method.POST, urlConstants.getShowAllInstitutionsUrl(),
                    new Response.Listener<NetworkResponse>() {
                        @Override
                        public void onResponse(NetworkResponse response) {
                            progressDialog.dismiss();

                            try {
                                final String jsonString = new String(response.data,
                                        HttpHeaderParser.parseCharset(response.headers));

                                if(jsonString.equalsIgnoreCase("no_data")){
                                    adapter_emptyRecyclerView = new EmptyRecyclerViewAdapter();
                                    recyclerView_institutions.setAdapter(adapter_emptyRecyclerView);
                                }else {
                                    JSONObject jsonObject = new JSONObject(jsonString);
                                    JSONArray institutions = jsonObject.getJSONArray("institutions");
                                    for (int i = 0; i < institutions.length(); i++) {
                                        JSONObject institution = institutions.getJSONObject(i);

                                        bean.setInstitutionID(institution.getString("InstitutionID"));
                                        bean.setInstitutionName(institution.getString("InstitutionName"));
                                        bean.setInstitutionDescription(institution.getString("InstitutionDescription"));
                                        bean.setBankAccountNumber(institution.getString("InstitutionBankAccountNumber"));
                                        bean.setDateAndTimeRegistered(institution.getString("DateAndTimeRegistered"));

                                        Institution_DataObject institution_dataObject = new Institution_DataObject(
                                                bean.getInstitutionID(),
                                                bean.getInstitutionName(),
                                                bean.getInstitutionDescription(),
                                                bean.getBankAccountNumber(),
                                                bean.getDateAndTimeRegistered()
                                        );

                                        institution_dataObjectList.add(institution_dataObject);
                                    }

                                    adapter_institution = new Adapter_Institution(institution_dataObjectList,getContext());

                                    recyclerView_institutions.setAdapter(adapter_institution);
                                    adapter_institution.notifyDataSetChanged();
                                }
                            } catch (UnsupportedEncodingException | JSONException e) {
                                e.printStackTrace();
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
            Log.e("UsersFragment", ex.toString());
        }
    }

    private List<Institution_DataObject> filter(List<Institution_DataObject> models, String query) {
        query = query.toLowerCase();
        final List<Institution_DataObject> filteredModelList = new ArrayList<>();
        for (Institution_DataObject model : models) {
            final String institutionName = model.getInstitution_name().toLowerCase();

            if (institutionName.contains(query)) {
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
        final List<Institution_DataObject> filteredModelList = filter(institution_dataObjectList,newText);
        adapter_institution.setFilter(filteredModelList);

        return true;
    }
}
