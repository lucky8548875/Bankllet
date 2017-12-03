package com.breadbuster.ewallet.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.breadbuster.ewallet.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddFundsFragment extends Fragment {


    public AddFundsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_funds, container, false);
    }

}
