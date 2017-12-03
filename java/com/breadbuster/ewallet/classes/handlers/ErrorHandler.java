package com.breadbuster.ewallet.classes.handlers;

import android.content.Context;
import android.widget.Toast;

public class ErrorHandler{
    public static ErrorHandler mInstance;
    private static Context mContext;

    public ErrorHandler(Context context){
        mContext = context;
    }

    public static synchronized ErrorHandler getInstance(Context context){
        if(mInstance == null){
            mInstance = new ErrorHandler(context);
        }
        return mInstance;
    }

    public void viewToastErrorMessage(Context context, String message){
        try{
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        }catch(Exception ex){
            Toast.makeText(context, ex.toString(), Toast.LENGTH_LONG).show();
        }
    }
}
