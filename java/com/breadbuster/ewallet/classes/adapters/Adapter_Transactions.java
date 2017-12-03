package com.breadbuster.ewallet.classes.adapters;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.breadbuster.ewallet.R;
import com.breadbuster.ewallet.classes.URLConstants;
import com.breadbuster.ewallet.classes.dataObjects.Transactions_DataObject;
import com.breadbuster.ewallet.classes.requests.MySingleton;
import com.breadbuster.ewallet.classes.requests.MyStringRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Adapter_Transactions extends RecyclerView.Adapter<Adapter_Transactions.ViewHolder> {
    private List<Transactions_DataObject> transactions_dataObjectList;
    private Context context;
    private View view;

    private String LOG_TAG = "Adapter_Transactions";
    private URLConstants urlConstants = new URLConstants();

    public Adapter_Transactions(List<Transactions_DataObject> transactions_dataObjectList, Context context) {
        this.transactions_dataObjectList = transactions_dataObjectList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_transactions, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        try{
            final Transactions_DataObject transactions_dataObject = transactions_dataObjectList.get(position);

            holder.lblPurchasedBy.setText(transactions_dataObject.getProductPurchasedBy());
            holder.lblDatePurchased.setText(transactions_dataObject.getDateAndTimePurchased());
            holder.lblProductCategory.setText(transactions_dataObject.getProductCategory());
            holder.lblProductInstitution.setText(transactions_dataObject.getProductInstitution());
            holder.lblProductOrItemName.setText(transactions_dataObject.getProductOrItemName());
            holder.lblProductOrItemAmount.setText(transactions_dataObject.getProductAmountCurrency() + " " + transactions_dataObject.getProductOrItemAmount());

            if(transactions_dataObject.getProductStatus().equalsIgnoreCase("Unseen")){
                holder.btnOkayGotIt.setVisibility(View.VISIBLE);
            }else{
                holder.btnOkayGotIt.setVisibility(View.GONE);
            }


            holder.btnOkayGotIt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    updateProductStatus(transactions_dataObject.getProductID());
                }
            });
        }catch(Exception ex){
            Log.e(LOG_TAG,ex.toString());
        }
    }

    @Override
    public int getItemCount() {
        return transactions_dataObjectList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.lblPurchasedBy) TextView lblPurchasedBy;
        @BindView(R.id.lblDatePurchased) TextView lblDatePurchased;
        @BindView(R.id.lblProductCategory) TextView lblProductCategory;
        @BindView(R.id.lblProductInstitution) TextView lblProductInstitution;
        @BindView(R.id.lblProductOrItemName) TextView lblProductOrItemName;
        @BindView(R.id.lblProductOrItemAmount) TextView lblProductOrItemAmount;
        @BindView(R.id.btnOkayGotIt) AppCompatButton btnOkayGotIt;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    private void updateProductStatus(String id){
        Map<String, String> parameters = new HashMap<>();

        parameters.put("productStatus","Seen");

        MyStringRequest request = new MyStringRequest(parameters, Request.Method.POST, urlConstants.getUpdateProductStatusUrl() + id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(context, response, Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }
        );

        MySingleton.getInstance(context).addToRequestQueue(request);
    }

    public void setFilter(List<Transactions_DataObject> institutionsModel) {
        transactions_dataObjectList = new ArrayList<>();
        transactions_dataObjectList.addAll(institutionsModel);
        notifyDataSetChanged();
    }
}
