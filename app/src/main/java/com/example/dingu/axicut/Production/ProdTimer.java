package com.example.dingu.axicut.Production;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dingu.axicut.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProdTimer extends DialogFragment {


    public ProdTimer() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_prod_timer, container, false);
    }

}
