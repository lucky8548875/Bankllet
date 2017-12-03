package com.breadbuster.ewallet.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
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
import com.breadbuster.ewallet.classes.SharedPrefManager;
import com.breadbuster.ewallet.classes.URLConstants;
import com.breadbuster.ewallet.classes.requests.CacheRequest;
import com.breadbuster.ewallet.classes.requests.MySingleton;
import com.breadbuster.ewallet.fragment.AddDeveloperFragment;
import com.breadbuster.ewallet.fragment.AddFundsFragment;
import com.breadbuster.ewallet.fragment.AddInstitutionFragment;
import com.breadbuster.ewallet.fragment.CheckTransactionsFragment;
import com.breadbuster.ewallet.fragment.HomeCoverFragment;
import com.breadbuster.ewallet.fragment.InstitutionListFragment;
import com.breadbuster.ewallet.fragment.PurchaseAnItemFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{
    Menu menu;
    NavigationView navigationView;
    View header;

    TextView lblIDNumber,lblUserType;

    public static Bean bean = new Bean();
    URLConstants urlConstants = new URLConstants();

    MenuItem[] menuItemsToHide_student,menuItemsToHide_administrator,menuItemsToHide_developer,menuItemsToHide_all;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);

        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        header = navigationView.getHeaderView(0);

        lblIDNumber = header.findViewById(R.id.lblIDNumber);
        lblUserType = header.findViewById(R.id.lblUserType);

        // Set the home as default
        Fragment fragment = new HomeCoverFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frameLayout_content, fragment)
                .commit();

        showInfoOfLoggedInUser();

        menu = navigationView.getMenu();

        menuItemsToHide_student = new MenuItem[]{
                menu.findItem(R.id.nav_manageInstitutions),
                menu.findItem(R.id.nav_checkFeedbacks),
                menu.findItem(R.id.nav_addInstitution),
                menu.findItem(R.id.nav_addDeveloper)
        };

        menuItemsToHide_administrator = new MenuItem[]{
                menu.findItem(R.id.nav_manageInstitutions),
                menu.findItem(R.id.nav_addFunds),
                menu.findItem(R.id.nav_checkFeedbacks),
                menu.findItem(R.id.nav_addInstitution),
                menu.findItem(R.id.nav_addDeveloper)
        };

        menuItemsToHide_developer = new MenuItem[]{
                menu.findItem(R.id.nav_purchaseAnItem)
        };

        menuItemsToHide_all = new MenuItem[]{
                menu.findItem(R.id.nav_home),
                menu.findItem(R.id.nav_myAccount),
                menu.findItem(R.id.nav_purchaseAnItem),
                menu.findItem(R.id.nav_checkTransactions),
                menu.findItem(R.id.nav_manageInstitutions),
                menu.findItem(R.id.nav_addFunds),
                menu.findItem(R.id.nav_checkFeedbacks),
                menu.findItem(R.id.nav_addInstitution),
                menu.findItem(R.id.nav_addDeveloper),
                menu.findItem(R.id.nav_FAQs),
                menu.findItem(R.id.nav_aboutTheDeveloper),
                menu.findItem(R.id.nav_helpAndFeedback)
        };
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        // Handle navigation view item clicks here.
        int id = menuItem.getItemId();

        Fragment fragment = null;
        Class fragmentClass = null;

        switch (id){
            case R.id.nav_home:
                startActivity(new Intent(HomeActivity.this,HomeActivity.class));
                break;
            case R.id.nav_myAccount:
                Toast.makeText(this, "Under development. Stay tuned!", Toast.LENGTH_LONG).show();
                break;
            case R.id.nav_purchaseAnItem:
                fragmentClass = PurchaseAnItemFragment.class;
                replaceFragment(fragmentClass,fragment);
                break;
            case R.id.nav_checkTransactions:
                fragmentClass = CheckTransactionsFragment.class;
                replaceFragment(fragmentClass,fragment);
                break;
            case R.id.nav_manageInstitutions:
                fragmentClass = InstitutionListFragment.class;
                replaceFragment(fragmentClass,fragment);
                break;
            case R.id.nav_addFunds:
                fragmentClass = AddFundsFragment.class;
                replaceFragment(fragmentClass,fragment);
                break;
            case R.id.nav_checkFeedbacks:
                Toast.makeText(this, "Under development. Stay tuned!", Toast.LENGTH_LONG).show();
                break;
            case R.id.nav_logout:
                showLogoutPrompt("Are you sure you want to logout?", "You cannot undo this action.");
                break;
            case R.id.nav_addInstitution:
                fragmentClass = AddInstitutionFragment.class;
                replaceFragment(fragmentClass,fragment);
                break;
            case R.id.nav_addDeveloper:
                fragmentClass = AddDeveloperFragment.class;
                replaceFragment(fragmentClass,fragment);
                break;
            case R.id.nav_FAQs:
                Toast.makeText(this, "Under development. Stay tuned!", Toast.LENGTH_LONG).show();
                break;
            case R.id.nav_aboutTheApp:
                Toast.makeText(this, "Under development. Stay tuned!", Toast.LENGTH_LONG).show();
                break;
            case R.id.nav_aboutTheDeveloper:
                Toast.makeText(this, "Under development. Stay tuned!", Toast.LENGTH_LONG).show();
                break;
            case R.id.nav_helpAndFeedback:
                Toast.makeText(this, "Under development. Stay tuned!", Toast.LENGTH_LONG).show();
                break;
        }



        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void replaceFragment(Class fragmentClass,Fragment fragment){
        try {
            if (fragmentClass != null) {
                fragment = (Fragment) fragmentClass.newInstance();
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frameLayout_content,fragment).commit();
    }

    public void showLogoutPrompt(String title, String message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this, R.style.MyDialogTheme);
        alertDialogBuilder.setTitle(title);

        alertDialogBuilder
                .setMessage(message)
                .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        SharedPrefManager.getInstance(getApplicationContext()).logout();
                        finish();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    }
                })
                .setNegativeButton("No",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();

        // Show Alert Message
        alertDialog.show();
    }

    public void showInfoOfLoggedInUser(){
        try{
            final ProgressDialog progressDialog = new ProgressDialog(HomeActivity.this);
            progressDialog.setMessage("Loading data...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            CacheRequest request = new CacheRequest(null, Request.Method.POST, urlConstants.getShowInfoOfLoggedInUserUrl() + SharedPrefManager.getInstance(HomeActivity.this).getIDNumber(),
                    new Response.Listener<NetworkResponse>() {
                        @Override
                        public void onResponse(NetworkResponse response) {
                            progressDialog.dismiss();

                            try {
                                final String jsonString = new String(response.data,
                                        HttpHeaderParser.parseCharset(response.headers));

                                JSONObject jsonObject = new JSONObject(jsonString);
                                JSONArray users = jsonObject.getJSONArray("userInfo");

                                for(int i = 0; i < users.length(); i++){
                                    JSONObject user = users.getJSONObject(i);

                                    bean.setUserID(user.getString("UserID"));
                                    bean.setIdNumber(user.getString("IDNumber"));
                                    bean.setUserType(user.getString("UserType"));
                                    bean.setUserInstitution(user.getString("UserInstitution"));

                                    lblIDNumber.setText(bean.getIdNumber());
                                    lblUserType.setText(bean.getUserType());

                                    if(bean.getUserType().equalsIgnoreCase("Student")){
                                        hideMenuItem(menuItemsToHide_student);
                                    }else if(bean.getUserType().equalsIgnoreCase("Administrator")){
                                        hideMenuItem(menuItemsToHide_administrator);
                                    }else if(bean.getUserType().equalsIgnoreCase("Developer")){
                                        hideMenuItem(menuItemsToHide_developer);
                                    }else{
                                        hideMenuItem(menuItemsToHide_all);
                                    }
                                }

                            } catch (JSONException e) {
                                Log.e("Error Message", e.toString());
                            } catch (UnsupportedEncodingException e) {
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

                            hideMenuItem(menuItemsToHide_all);
                            Toast.makeText(HomeActivity.this, message, Toast.LENGTH_LONG).show();
                        }
                    }
            );

            request.setRetryPolicy(new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            MySingleton.getInstance(HomeActivity.this).addToRequestQueue(request);
        }catch(Exception ex){
            Log.e("Error Message", ex.toString());
        }
    }

    private void hideMenuItem(MenuItem[] items){
        for(int i=0; i<items.length; i++){
            MenuItem item = items[i];
            item.setVisible(false);
        }
    }
}
