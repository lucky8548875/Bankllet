package com.breadbuster.ewallet.classes.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.breadbuster.ewallet.R;
import com.breadbuster.ewallet.activity.InstitutionCategoryActivity;
import com.breadbuster.ewallet.classes.dataObjects.Institution_DataObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Adapter_Institution extends RecyclerView.Adapter<Adapter_Institution.ViewHolder> {
    private List<Institution_DataObject> institution_dataObjectList;
    private Context context;
    private View view;

    private String LOG_TAG = "Adapter_Institution";

    public Adapter_Institution(List<Institution_DataObject> institution_dataObjectList, Context context) {
        this.institution_dataObjectList = institution_dataObjectList;
        this.context = context;
    }

    @Override
    public Adapter_Institution.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_institution, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Adapter_Institution.ViewHolder holder, int position) {
        try {
            final Institution_DataObject institution_dataObject = institution_dataObjectList.get(position);

            holder.lblInstitutionName.setText(institution_dataObject.getInstitution_name());
            holder.lblInstitutionDescription.setText(institution_dataObject.getInstitution_description());

            holder.cardView_institutions.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    context.startActivity(new Intent(context, InstitutionCategoryActivity.class));
                }
            });
        }catch(Exception ex){
            Log.e(LOG_TAG,ex.toString());
        }
    }

    @Override
    public int getItemCount() {
        return institution_dataObjectList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.lblInstitutionName) TextView lblInstitutionName;
        @BindView(R.id.lblInstitutionDescription) TextView lblInstitutionDescription;
        @BindView(R.id.cardView_institutions) CardView cardView_institutions;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public void setFilter(List<Institution_DataObject> institutionsModel) {
        institution_dataObjectList = new ArrayList<>();
        institution_dataObjectList.addAll(institutionsModel);
        notifyDataSetChanged();
    }
}
