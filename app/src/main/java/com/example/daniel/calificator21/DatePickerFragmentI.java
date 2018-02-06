package com.example.daniel.calificator21;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
public class DatePickerFragmentI extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {
        public TextView FirstDate;
        Calendar calendarV;
        int yearV, monthV, dayV;

@Override
public Dialog onCreateDialog(Bundle savedInstanceState) {
// Use the current date as the default date in the picker

        //FUENTE
        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "DJB Chalk It Up.ttf");
        FirstDate  = (TextView) getActivity().findViewById(R.id.FI);

        //FUENTE TEXTVIEW
        FirstDate.setTypeface(font);

        //DECLARANDO OBJETOS CALENDAR
        calendarV = Calendar.getInstance();
        yearV = calendarV.get(Calendar.YEAR);
        monthV = calendarV.get(Calendar.MONTH);
        dayV = calendarV.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, yearV, monthV, dayV);
        }

public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user

        yearV = year;
        monthV = month+1;
        dayV = day;

        FirstDate.setText(yearV+"-"+monthV+"-"+dayV);
        }

}

