package com.example.daniel.calificator21;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by sanborns on 13/10/2016.
 */

public class FragCalAlum extends Fragment{
    public FragCalAlum() {
        // Required empty public constructor

    }
    Button cont;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.frag_cal_alum, container, false);
        return view;
    }
}