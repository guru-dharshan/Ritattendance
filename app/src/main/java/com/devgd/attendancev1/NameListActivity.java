package com.devgd.attendancev1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NameListActivity extends AppCompatActivity {

    FirebaseFirestore firestore;
    RecyclerView recyclerView;
    String batch,dept,sec,year,date,sem,hr;
    List<AttendanceModelClass> nameList;
    List<Integer> prevAttendance;
    TextView dep,sect,hour,semester,datet;
    PutAttendanceAdapter adapter;
    List<Boolean> attendanceStatus;
    int firsttime=0;
    SharedPreferences sharedPreferences;
    FirebaseDatabase database;
    DatabaseReference reference;
    ProgressBar progressBar;
    CardView layout;
    FirebaseAuth auth;
    //int i1=0;
    List<Integer> done;
    int count=0,i,no_of_present,prevattendance;
    int temp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name_list);
        firestore=FirebaseFirestore.getInstance();
        recyclerView=findViewById(R.id.namelistrecyclerview);
        nameList=new ArrayList<>();
        attendanceStatus=new ArrayList<>();

        done=new ArrayList<>();

        i=0;

        prevAttendance=new ArrayList<>();

        dep=findViewById(R.id.namelistdepartment);
        sect=findViewById(R.id.namelistsec);
        hour=findViewById(R.id.namelisthour);
        datet=findViewById(R.id.namelistdate);
        semester=findViewById(R.id.namelistsem);
        database=FirebaseDatabase.getInstance();
        sharedPreferences=this.getPreferences(MODE_PRIVATE);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        progressBar=findViewById(R.id.progressBar);
        layout=findViewById(R.id.layout);
        Intent intent=getIntent();
        batch=intent.getStringExtra("batch");
        dept=intent.getStringExtra("dep");
        sec=intent.getStringExtra("sec");
        year=intent.getStringExtra("year");
        date=intent.getStringExtra("date");
        sem=intent.getStringExtra("sem");
        dep.setText("Department: "+dept);
        sect.setText("Section: "+sec);
        datet.setText("Date: "+date);
        auth=FirebaseAuth.getInstance();

        hr=intent.getStringExtra("hour");
        hour.setText("Hour: "+hr);
        semester.setText("Semester: "+sem);
        String prevPeriod;
        int las_per=Integer.parseInt(hr)-1;
        prevPeriod="h"+las_per;
        reference=database.getReference(batch);
        adapter=new PutAttendanceAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(NameListActivity.this));

    }

    @Override
    protected void onStart() {
        super.onStart();
//        firestore.collection(batch)
//                .whereEqualTo("dep",dept)
//                .whereEqualTo("sec",sec)
//                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//            @Override
//            public void onSuccess(QuerySnapshot querySnapshot) {
//                nameList=new ArrayList<>();
//                attendanceStatus=new ArrayList<>();
//                for(QueryDocumentSnapshot documentSnapshot:querySnapshot){
//                    AttendanceModelClass modelClass=documentSnapshot.toObject(AttendanceModelClass.class);
//                    modelClass.setRegNo(documentSnapshot.getId());
//                    nameList.add(modelClass);
//                    attendanceStatus.add(true);
//                }
//                adapter.setNameList(nameList);
//                recyclerView.setAdapter(adapter);
//                recyclerView.setLayoutManager(new LinearLayoutManager(NameListActivity.this));
//
//            }
//        });
        i=0;
        reference.orderByChild("dep").equalTo(dept)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        Log.i("start", "called");
                        nameList = new ArrayList<>();
                        prevAttendance=new ArrayList<>();
                        done=new ArrayList<>();
                        attendanceStatus = new ArrayList<>();
                        int i = 0;
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                            if (dataSnapshot.child("sec").getValue(String.class).equals(sec)) {

                                //Log.i("checking",dataSnapshot.child("name").getValue(String.class));
//                    AttendanceModelClass modelClass=new AttendanceModelClass(dataSnapshot.child("name").getValue(String.class),
//                            dataSnapshot.child("dep").getValue(String.class),
//                            dataSnapshot.child("sec").getValue(String.class),
//                            dataSnapshot.child("phno").getValue(String.class),
//                            dataSnapshot.child("regno").getValue(String.class)
//                            );
                                AttendanceModelClass modelClass = dataSnapshot.getValue(AttendanceModelClass.class);
                                attendanceStatus.add(true);
                                nameList.add(modelClass);
                            }

                        }
                        if(nameList.size()==0){
                            Toast.makeText(NameListActivity.this, "NO DATA AVAILABLE", Toast.LENGTH_LONG).show();
                        }
                        adapter.setNameList(nameList, attendanceStatus);
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(NameListActivity.this));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    public void upload(View view) {
        i=0;
        //int i1;
        firsttime=0;
        // if(sharedPreferences.getString("first",null)==null) {
        if(hr.equals("1")) {
            for (i = 0; i <nameList.size();i++){
                no_of_present=getAttendance(String.valueOf(attendanceStatus.get(i)));
                AttendanceModelClass modelClass=nameList.get(i);
                AttendanceModelClass attendance=new AttendanceModelClass(modelClass.getRegno(),modelClass.getPhno(),modelClass.getName(),
                        modelClass.getSec(),sem,dept,year,String.valueOf(attendanceStatus.get(i)),
                        " "," "," "," "," "," ",String.valueOf(no_of_present));
                firestore.collection(date).document(String.valueOf(modelClass.getRegno())).set(attendance).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        count++;
                        firsttime++;
                    }
                });
            }
            no_of_present=0;
            //Log.i("heyyy first time","ueeeee");


        }
        else {
            firestore.collection(date)
                    .whereEqualTo("dep", dept)
                    .whereEqualTo("year", year)
                    .whereEqualTo("sec", sec).
                    get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot querySnapshot) {
                    for (QueryDocumentSnapshot documentSnapshot : querySnapshot) {
                        int attcount = Integer.parseInt(documentSnapshot.getString("noofpresent"));
                        //Log.i("checkinggg", documentSnapshot.getString("noofpresent"));
                        prevAttendance.add(attcount);
                        Log.i("list prevatt", String.valueOf(prevAttendance.size()));
                    }

                    for (int i1 = 0; i1 <nameList.size();i1++) {
                        //int finalI1 = i11;
                        temp=i1;
                        //Log.i("value of iiii",""+temp+"  "+i1);

                        AttendanceModelClass modelClass = nameList.get(i1);

                        String h = "h" + hr;

                        int attper=prevAttendance.get(i1)+getAttendance(String.valueOf(attendanceStatus.get(i1)));

                        firestore.collection(date).document(String.valueOf(modelClass.getRegno())).update(h, String.valueOf(attendanceStatus.get(i1)),
                                "noofpresent", String.valueOf(attper)
                        ).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                firsttime++;
                            }
                        });
                    }
                }

            });


            //setData(h,modelClass,i1);

        }
        //i1=0;
        if(nameList.size()==0){
            Toast.makeText(NameListActivity.this, "no data available", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(NameListActivity.this, "Attendance uploaded", Toast.LENGTH_SHORT).show();
        }
    }
// menu top
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        if (id == R.id.log_out) {
//            auth.signOut();
//            Intent intent=new Intent(getApplicationContext(),Login.class);
//            SharedPreferences preferences = this.getSharedPreferences("preference", Context.MODE_PRIVATE);
//            SharedPreferences.Editor editor = preferences.edit();
//            editor.clear();
//            editor.apply();
//            startActivity(intent);
//            finish();
//            Toast.makeText(getApplicationContext(), "Logged Out Successfully!", Toast.LENGTH_LONG).show();
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    public int getAttendance(String s){
        if(s.equals("true")){
            Log.i("checkinggg","trueeeee");
            return 1;
        }
        Log.i("checkinggg","falseeeee");
        return 0;
    }

}