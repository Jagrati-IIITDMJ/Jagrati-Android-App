package com.example.jagratiapp.Fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.jagratiapp.R;

import java.util.Calendar;

public class PastAttendanceFragment extends Fragment implements DatePickerDialog.OnDateSetListener {

    public PastAttendanceFragment() {
        // Required empty public constructor
    }
    public static PastAttendanceFragment newInstance(String param1, String param2) {
        PastAttendanceFragment fragment = new PastAttendanceFragment();
        return fragment;
        }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_past_attendance, container, false);

        Button date = view.findViewById(R.id.date);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });
        return view;
    }

    public void showDatePickerDialog(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getContext(),
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        Toast.makeText(getContext(),"" + year +"/" + month +"/" + day +"/",Toast.LENGTH_SHORT).show();
    }
}