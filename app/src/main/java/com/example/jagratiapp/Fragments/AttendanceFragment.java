package com.example.jagratiapp.Fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.jagratiapp.R;
import com.example.jagratiapp.model.Students;
import com.example.jagratiapp.ui.AttendenceRecyclerAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.ref.WeakReference;
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

public class AttendanceFragment extends Fragment implements AttendenceRecyclerAdapter.OnStudentListener {

    private Button submitButton;
    private String classid;
    private String groupid;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference documentReference;
    private List<Students> studentsList;
    private List<Students> newStudentsList;
    private RecyclerView attendenceRecyclerView;
    private AttendenceRecyclerAdapter attendenceAdapter;
    private boolean flag;
    private AttendenceRecyclerAdapter.OnStudentListener onStudentListener = this;
    private final Map<String,Boolean> recordedAttendance = new HashMap<>();
    private String formattedDate;
    private Spinner spinner;
    private Button syncButton;
    private Map<String, Boolean> attendence;
    private int count = 0;
    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefreshLayout;

    public AttendanceFragment() {
        // Required empty public constructor
    }

    public static AttendanceFragment newInstance(String classid,String groupid) {
        AttendanceFragment fragment = new AttendanceFragment();
        Bundle bundle = new Bundle();
        bundle.putString("classid", classid);
        bundle.putString("groupid",groupid);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        studentsList = new ArrayList<>();
        attendence = new HashMap<>();
        Bundle bundle=getArguments();
        if(bundle!=null){
            classid = bundle.getString("classid");
            groupid = bundle.getString("groupid");
        }
        documentReference = db.collection("Classes").document(classid)
                .collection("Groups").document(groupid);

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        formattedDate = df.format(c);

        // To show the student list in attendance segment
        updateStudentList();

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_attendance, container, false);
        submitButton = view.findViewById(R.id.submitAttendance);
        swipeRefreshLayout = view.findViewById(R.id.attendance_swipe);
        attendenceRecyclerView = view.findViewById(R.id.attendence_recycler_view);
        attendenceRecyclerView.setHasFixedSize(true);
        attendenceRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));





         submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Yes i m there", LENGTH_SHORT).show();
                if(!recordedAttendance.isEmpty()) {
                    Iterator it = recordedAttendance.entrySet().iterator();
                    while(it.hasNext()) {
                        Map.Entry obj = (Map.Entry)it.next();
                        //Toast.makeText(getContext(),obj.getKey().toString() + " " + obj.getValue(), LENGTH_SHORT).show();
                        documentReference.collection("Attendance").document(formattedDate).update(obj.getKey().toString(),obj.getValue());
                    }
                }
            }
        });


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                studentsList.clear();
                updateStudentList();
                updateAttendance();
                setAttendance();
                swipeRefreshLayout.setRefreshing(false);

            }
        });

        AsyncTaskFetching task = new AsyncTaskFetching(AttendanceFragment.this);
        task.execute();
//        updateAttendance();
//        setAttendance();

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        deleteIfNone();

    }

    private void updateStudentList( ) {
        documentReference.collection("Students").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                Students student = documentSnapshot.toObject(Students.class);
                                student.setUid(documentSnapshot.getId());
                                studentsList.add(student);
                            }
                        } else {
                            Toast.makeText(getContext(), "No students for attendance", LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void updateAttendance() {
        // Check weather attendance of particular day is present or not
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
                        //Initialize false for every student having student ID
                        if (!flag){
                            documentReference.collection("Students").get()
                                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                        @Override
                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                            if (!queryDocumentSnapshots.isEmpty()){
                                                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                                                    attendence.put(documentSnapshot.getId(),false);
                                                }
                                                documentReference.collection("Attendance").document(formattedDate).set(attendence);
                                                documentReference.collection("Attendance").document(formattedDate).update("timestamp", Timestamp.now());
                                            }
                                        }
                                    });
                        }

                    }
                });



    }

    public void setAttendance(){
        documentReference.collection("Attendance").document(formattedDate).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()){
                            for (int i=0;i<studentsList.size();i++) {
                                if (documentSnapshot.getBoolean(studentsList.get(i).getUid()) != null){
                                    recordedAttendance.put(studentsList.get(i).getUid(),documentSnapshot.getBoolean(studentsList.get(i).getUid()));
                                }
                                else {
                                    documentReference.collection("Attendance").document(formattedDate).update(studentsList.get(i).getUid(),false);
                                    recordedAttendance.put(studentsList.get(i).getUid(),false);
                                }
                            }
                        }
                        attendenceAdapter = new AttendenceRecyclerAdapter(getContext(),studentsList,recordedAttendance,onStudentListener,true);
                        attendenceRecyclerView.setAdapter(attendenceAdapter);
                        attendenceAdapter.notifyDataSetChanged();

                    }
                });


    }


    private void deleteIfNone() {
        documentReference.collection("Attendance").document(formattedDate).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            for (int i = 0; i < studentsList.size(); i++) {
                                if (!documentSnapshot.getBoolean(studentsList.get(i).getUid())) {
                                    count++;
                                }
                            }

                            if (count == studentsList.size()) {
                                documentReference.collection("Attendance").document(formattedDate).delete();
                            }
                        }

                    }
                });

    }
    @Override
    public void onStudentClick(int position,boolean state) {
        Students student = studentsList.get(position);
        recordedAttendance.put(student.getUid(),state);
       // Toast.makeText(getContext()," " + recordedAttendance.get(student.getUid()), Toast.LENGTH_SHORT).show();
    }



    private static class  AsyncTaskFetching extends AsyncTask<Void,Void,Void> {

        private WeakReference<AttendanceFragment> WeakReference;
        AsyncTaskFetching(AttendanceFragment activity) {
            WeakReference = new WeakReference<AttendanceFragment>(activity);
        }


        /*@Override
        protected void onPreExecute() {
            super.onPreExecute();
            AttendanceFragment activity = WeakReference.get();
            if (activity == null) {
                return;
            }
            //activity.progressBar.setVisibility(View.VISIBLE);
        }*/

        @Override
        protected Void doInBackground(Void... voids) {
            AttendanceFragment activity = WeakReference.get();
            if (activity == null) {
                return null;
            }
            activity.updateAttendance();
            return null;

        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            AttendanceFragment activity = WeakReference.get();
            if (activity == null ) {
                return;
            }
            activity.setAttendance();
           // activity.progressBar.setVisibility(View.INVISIBLE);
        }


    }

}
