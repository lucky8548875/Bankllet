package com.breadbuster.ewallet.classes;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {
    private static SharedPrefManager mInstance;
    private static Context mContext;

    private static final String SHARED_PREF_NAME = "mySharedPref";
    private static final String KEY_IDNUMBER = "IDNumber";

    public SharedPrefManager(Context context) {
        mContext = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context){
        if(mInstance == null){
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    public void userLogin(String idNumber){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(KEY_IDNUMBER, idNumber);

        editor.apply();

    }

    public boolean isLoggedIn(){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        if(sharedPreferences.getString(KEY_IDNUMBER, null) != null){
            return true;
        }
        return false;
    }

    public boolean logout(){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        return true;
    }

    public String getIDNumber(){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_IDNUMBER,null);
    }
}
