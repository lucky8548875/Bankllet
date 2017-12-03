package com.breadbuster.ewallet.classes.adapters;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.breadbuster.ewallet.R;
import com.breadbuster.ewallet.classes.dataObjects.Category_SubCategory_DataObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Adapter_Category_SubCategory extends RecyclerView.Adapter<Adapter_Category_SubCategory.ViewHolder>{
    private List<Category_SubCategory_DataObject> category_subCategory_dataObjectList;
    private Context context;
    private View view;

    private String LOG_TAG = "Adapter_Category_SubCategory";

    public Adapter_Category_SubCategory(List<Category_SubCategory_DataObject> category_subCategory_dataObjectList, Context context) {
        this.category_subCategory_dataObjectList = category_subCategory_dataObjectList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_category_subcategory, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        try{
            final Category_SubCategory_DataObject category_subCategory_dataObject = category_subCategory_dataObjectList.get(position);

            holder.lblSubCategoryName.setText(category_subCategory_dataObject.getCategory_subCategoryName());
            holder.lblSubCategory_dateAndTimeAdded.setText(category_subCategory_dataObject.getSubCategory_dateAndTimeAdded());

            holder.btnAddCategory_subCategory.setOnClickListener(new View.OnClickListener() {
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
        return category_subCategory_dataObjectList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.lblSubCategoryName) TextView lblSubCategoryName;
        @BindView(R.id.lblSubCategory_dateAndTimeAdded) TextView lblSubCategory_dateAndTimeAdded;
        @BindView(R.id.btnAddCategory_subCategory) AppCompatButton btnAddCategory_subCategory;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
