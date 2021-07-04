package com.devgd.attendancev1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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
    TextView dep,sect,hour,semester;
    PutAttendanceAdapter adapter;
    List<Boolean> attendanceStatus;
    int firsttime=0;
    SharedPreferences sharedPreferences;
    FirebaseDatabase database;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name_list);
        firestore=FirebaseFirestore.getInstance();
        recyclerView=findViewById(R.id.namelistrecyclerview);
        nameList=new ArrayList<>();
        attendanceStatus=new ArrayList<>();
        dep=findViewById(R.id.namelistdepartment);
        sect=findViewById(R.id.namelistsec);
        hour=findViewById(R.id.namelisthour);
        semester=findViewById(R.id.namelistsem);
        database=FirebaseDatabase.getInstance();
        sharedPreferences=this.getPreferences(MODE_PRIVATE);
        Intent intent=getIntent();
        batch=intent.getStringExtra("batch");
        dept=intent.getStringExtra("dep");
        sec=intent.getStringExtra("sec");
        year=intent.getStringExtra("year");
        date=intent.getStringExtra("date");
        sem=intent.getStringExtra("sem");
        dep.setText("Department: "+dept);
        sect.setText("Section: "+sec);
        hr=intent.getStringExtra("hour");
        hour.setText("Hour: "+hr);
        semester.setText("Semester: "+sem);
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
        reference.orderByChild("dep").equalTo(dept)
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                    Log.i("start", "called");
                    nameList = new ArrayList<>();
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
                        Log.i("heyyy long value", String.valueOf(dataSnapshot.child("phno").getValue()));
                        Log.i("n000", String.valueOf(i++));
                        attendanceStatus.add(true);
                        nameList.add(modelClass);
                    }
                    adapter.setNameList(nameList, attendanceStatus);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(NameListActivity.this));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void upload(View view) {
        if(sharedPreferences.getString("first",null)==null) {
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.putString("first","1");
            editor.apply();
            firsttime++;
            for (int i = 0; i <nameList.size();i++){
                AttendanceModelClass modelClass=nameList.get(i);
               AttendanceModelClass attendance=new AttendanceModelClass(modelClass.getRegno(),modelClass.getPhno(),modelClass.getName(),
                       modelClass.getSec(),sem,dept,year,String.valueOf(attendanceStatus.get(i)),
                       " "," "," "," "," "," ");
               firestore.collection(date).document(String.valueOf(modelClass.getRegno())).set(attendance);
               Log.i("heyyy first time","ueeeee");
                Toast.makeText(this, "Attendance Uploaded", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            for (int i = 0; i <nameList.size();i++) {
                AttendanceModelClass modelClass = nameList.get(i);
                String h = "h" + hr;
                firestore.collection(date).document(String.valueOf(modelClass.getRegno())).update(h, String.valueOf(attendanceStatus.get(i)));
                Toast.makeText(this, "Attendance Updated", Toast.LENGTH_SHORT).show();
            }
        }
    }
}