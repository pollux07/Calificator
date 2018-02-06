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

public class FragHorAlum extends Fragment{
    public FragHorAlum() {
        // Required empty public constructor

    }
    Button cont;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.frag_horario_alum, container, false);
        return view;
    }
}