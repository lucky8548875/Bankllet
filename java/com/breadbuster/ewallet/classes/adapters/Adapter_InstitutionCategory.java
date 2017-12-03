package com.breadbuster.ewallet.classes.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.breadbuster.ewallet.R;
import com.breadbuster.ewallet.activity.HomeActivity;
import com.breadbuster.ewallet.activity.InstitutionCategory_SubCategoryActivity;
import com.breadbuster.ewallet.classes.URLConstants;
import com.breadbuster.ewallet.classes.dataObjects.InstitutionCategory_DataObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Adapter_InstitutionCategory extends RecyclerView.Adapter<Adapter_InstitutionCategory.ViewHolder> {
    private List<InstitutionCategory_DataObject> institutionCategory_dataObjectList;
    private Context context;
    private View view;

    private String LOG_TAG = "Adapter_InstitutionCategory";
    private URLConstants urlConstants = new URLConstants();

    public Adapter_InstitutionCategory(List<InstitutionCategory_DataObject> institutionCategory_dataObjectList, Context context) {
        this.institutionCategory_dataObjectList = institutionCategory_dataObjectList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_institution_category, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        try{
            final InstitutionCategory_DataObject institutionCategory_dataObject = institutionCategory_dataObjectList.get(position);

            holder.lblInstitutionCategoryName.setText(institutionCategory_dataObject.getInstitution_categoryName());
            holder.lblInstitutionCategory_dateAndTimeAdded.setText(institutionCategory_dataObject.getInstitution_categoryDateAndTimeAdded());

            holder.cardView_institutionCategory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(HomeActivity.bean.getUserType().equalsIgnoreCase("Administrator")){
                        context.startActivity(new Intent(context, InstitutionCategory_SubCategoryActivity.class));
                    }
                }
            });

            if(HomeActivity.bean.getUserType().equalsIgnoreCase("Student")){
                holder.btnAddCategory.setVisibility(View.GONE);
            }else{
                holder.btnAddCategory.setVisibility(View.VISIBLE);
            }

            holder.btnAddCategory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }catch(Exception ex){
            Log.e(LOG_TAG,ex.toString());
        }
    }

    @Override
    public int getItemCount() {
        return institutionCategory_dataObjectList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.cardView_institutionCategory) CardView cardView_institutionCategory;
        @BindView(R.id.lblInstitutionCategoryName) TextView lblInstitutionCategoryName;
        @BindView(R.id.lblInstitutionCategory_dateAndTimeAdded) TextView lblInstitutionCategory_dateAndTimeAdded;
        @BindView(R.id.btnAddCategory) AppCompatButton btnAddCategory;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public void setFilter(List<InstitutionCategory_DataObject> institutionCategoryModel) {
        institutionCategory_dataObjectList = new ArrayList<>();
        institutionCategory_dataObjectList.addAll(institutionCategoryModel);
        notifyDataSetChanged();
    }
}
