package com.example.jagratiapp.Fragments;

import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jagratiapp.R;
import com.example.jagratiapp.model.Students;
import com.example.jagratiapp.ui.AttendenceRecyclerAdapter;
import com.example.jagratiapp.ui.PastAttendanceRecyclerAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.widget.Toast.LENGTH_SHORT;

public class PastAttendanceFragment extends Fragment implements DatePickerDialog.OnDateSetListener{

    private String formattedDate;
    private String classUid;
    private String groupUid;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference documentReference;
    private List<String> recordedAttendance = new ArrayList<>();
    private RecyclerView attendenceRecyclerView;
    private PastAttendanceRecyclerAdapter pastAttendenceAdapter;
    private boolean flag;
    private final Map<String,Students> studentsMap = new HashMap<>();
    private Button date;


    public PastAttendanceFragment() {
        // Required empty public constructor
    }
    public static PastAttendanceFragment newInstance(String classid, String groupid) {
        PastAttendanceFragment fragment = new PastAttendanceFragment();
        Bundle bundle = new Bundle();
        bundle.putString("classid", classid);
        bundle.putString("groupid",groupid);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle=getArguments();
        if(bundle!=null){
            classUid = bundle.getString("classid");
            groupUid = bundle.getString("groupid");
        }

        documentReference = db.collection("Classes").document(classUid)
                              .collection("Groups").document(groupUid);

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        formattedDate = df.format(c);

        // To show the student list in attendance segment
        if(studentsMap.isEmpty()) {
            documentReference.collection("Students").get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            if (!queryDocumentSnapshots.isEmpty()) {
                                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                    Students student = documentSnapshot.toObject(Students.class);
                                    studentsMap.put(documentSnapshot.getId(), student);
                                }
                            }
                            else {
                                Toast.makeText(getContext(), "Kuch ni hain", LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_past_attendance, container, false);

        date = view.findViewById(R.id.date_past_attendance);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });

        attendenceRecyclerView = view.findViewById(R.id.past_attendence_recycler_view);
        attendenceRecyclerView.setHasFixedSize(true);
        attendenceRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        setRecyclerView();
        // Check weather attendance of particular day is present or not
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR,year);
        calendar.set(Calendar.MONTH,month);
        calendar.set(Calendar.DAY_OF_MONTH,day);

        Date date = calendar.getTime();
        formattedDate = new SimpleDateFormat("dd-MMM-yyyy").format(date);
        //Toast.makeText(getContext(),formattedDate +"", LENGTH_SHORT).show();
        setRecyclerView();
    }

    public void setRecyclerView(){
        //Toast.makeText(getContext(),formattedDate +"", LENGTH_SHORT).show();
        date.setText(formattedDate);
        flag = false;
        documentReference.collection("Attendance").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                            if (formattedDate.equals(documentSnapshot.getId())) {
                                flag = true;
                                break;
                            }
                        }
                        //Toast.makeText(getContext(),formattedDate + flag, LENGTH_SHORT).show();
                        if (!flag){
                            recordedAttendance.clear();
                            pastAttendenceAdapter = new PastAttendanceRecyclerAdapter(getContext(),recordedAttendance,studentsMap);
                            attendenceRecyclerView.setAdapter(pastAttendenceAdapter);
                            pastAttendenceAdapter.notifyDataSetChanged();
                            Toast.makeText(getContext(),"there is no attendance record", LENGTH_SHORT).show();
                        }
                        else {
                                recordedAttendance.clear();
                                documentReference.collection("Attendance").document(formattedDate).get()
                                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            if (documentSnapshot.exists()){
                                                if(!studentsMap.isEmpty()) {
                                                    Iterator it = studentsMap.entrySet().iterator();
                                                    while(it.hasNext()) {
                                                        Map.Entry obj = (Map.Entry)it.next();
                                                        if (documentSnapshot.getBoolean(obj.getKey().toString()) != null){
                                                            Toast.makeText(getContext(),obj.getKey().toString(), LENGTH_SHORT).show();
                                                            recordedAttendance.add(obj.getKey().toString());
                                                        }
                                                    }
                                                }
                                                pastAttendenceAdapter = new PastAttendanceRecyclerAdapter(getContext(),recordedAttendance,studentsMap);
                                                attendenceRecyclerView.setAdapter(pastAttendenceAdapter);
                                                pastAttendenceAdapter.notifyDataSetChanged();
                                            }
                                        }
                                    });
                        }
                    }
                });
    }



}