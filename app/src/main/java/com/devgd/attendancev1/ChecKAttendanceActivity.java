package com.devgd.attendancev1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ChecKAttendanceActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    AttendanceModelClass modelClass;
    FirebaseFirestore firestore;
    String date,year,sec,dep;
    CheckAttendanceAdapter adapter;
    List<AttendanceModelClass> nameList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chec_k_attendance);
        recyclerView=findViewById(R.id.checkAttendanceRecyclerview);
        firestore=FirebaseFirestore.getInstance();
        Intent intent=getIntent();
        date=intent.getStringExtra("date");
        year=intent.getStringExtra("year");
        sec=intent.getStringExtra("sec");
        dep=intent.getStringExtra("dep");
        nameList=new ArrayList<>();
        adapter=new CheckAttendanceAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();
        firestore.collection(date)
                .whereEqualTo("dep",dep)
                .whereEqualTo("year",year)
                .whereEqualTo("sec",sec)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot querySnapshot) {
                nameList=new ArrayList<>();
                for(QueryDocumentSnapshot documentSnapshot:querySnapshot){
//                    if(dep.equals(documentSnapshot.getString("dep")) &&
//                        year.equals(documentSnapshot.getString("year")) &&
//                        sec.equals(documentSnapshot.getString("sec"))
//                    ){
                        modelClass=documentSnapshot.toObject(AttendanceModelClass.class);
                        nameList.add(modelClass);
                    //}
                }
                adapter.setNameList(nameList);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(ChecKAttendanceActivity.this));
            }
        });
    }
}