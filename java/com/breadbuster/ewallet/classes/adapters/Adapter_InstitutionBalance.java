package com.breadbuster.ewallet.classes.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.breadbuster.ewallet.R;
import com.breadbuster.ewallet.classes.dataObjects.InstitutionBalance_DataObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Adapter_InstitutionBalance extends RecyclerView.Adapter<Adapter_InstitutionBalance.ViewHolder> {
    private List<InstitutionBalance_DataObject> institutionBalance_dataObjectList;
    private Context context;
    private View view;

    private String LOG_TAG = "Adapter_Category_SubCategory";

    public Adapter_InstitutionBalance(List<InstitutionBalance_DataObject> institutionBalance_dataObjectList, Context context) {
        this.institutionBalance_dataObjectList = institutionBalance_dataObjectList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_balances, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        try{
            final InstitutionBalance_DataObject institutionBalance_dataObject = institutionBalance_dataObjectList.get(position);

            holder.lblInstitution_subCategory.setText(institutionBalance_dataObject.getInstitution_subCategory());
            holder.lblInstitution_subCategory_total.setText(institutionBalance_dataObject.getInstitution_subCategoryTotal());
            holder.lblInstitution_category.setText(institutionBalance_dataObject.getInstitution_category());
            holder.lblInstitution_category_total.setText(institutionBalance_dataObject.getInstitution_categoryTotal());
        }catch(Exception ex){
            Log.e(LOG_TAG,ex.toString());
        }
    }

    @Override
    public int getItemCount() {
        return institutionBalance_dataObjectList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.lblInstitution_subCategory) TextView lblInstitution_subCategory;
        @BindView(R.id.lblInstitution_subCategory_total) TextView lblInstitution_subCategory_total;
        @BindView(R.id.lblInstitution_category) TextView lblInstitution_category;
        @BindView(R.id.lblInstitution_category_total) TextView lblInstitution_category_total;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
