package com.devgd.attendancev1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Spinner dept,sec,hour;
    TextView date,curyear;
    EditText batch,acayear;
    RadioGroup radioGroup;
    String year,semind="";
    TextView oddind,evenind;
    RadioButton b1,b2;
    DatePickerDialog datePickerDialog;
    FirebaseAuth auth;
    ArrayAdapter deptadapter, secadapter, houradapter, updatedDepAdapter,updatedSecAdapter;

    Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        batch=findViewById(R.id.batch);
        date=findViewById(R.id.date);
        curyear=findViewById(R.id.year);
        dept=findViewById(R.id.dep);
        sec=findViewById(R.id.sec);
        hour=findViewById(R.id.hour);
        acayear=findViewById(R.id.acayear);
        radioGroup=findViewById(R.id.radioGroup);
        auth=FirebaseAuth.getInstance();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        b1=findViewById(R.id.oddSem);
        b2=findViewById(R.id.evenSem);
//        oddind=findViewById(R.id.oddSemInd);
//        evenind=findViewById(R.id.evenSemInd);

        String[] depts = { "CSE", "Mechanical", "ECE", "EEE"};
        String[] secs = { "A", "B", "C"};
        String[] hours = { "1", "2", "3","4","5","6","7"};
        String[] upDep = { "CSE", "Mechanical", "ECE", "EEE","CCE","B TECH AI & DS"};
        String[] upSec = { "A", "B", "C","D","E","F","G","H"};

       deptadapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,depts);
        secadapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,secs);
        houradapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,hours);
        updatedDepAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,upDep);
        updatedSecAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,upSec);

        deptadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        secadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        houradapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        hour.setAdapter(houradapter);
        dept.setAdapter(deptadapter);
        sec.setAdapter(secadapter);

        drawerLayout=findViewById(R.id.drawer);
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        navigationView=findViewById(R.id.navDrawer);
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawerLayout,
                toolbar,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(toggle);
        setTitle("");
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.AttendanceRange:
                            Intent intent1 = new Intent(getApplicationContext(), RangeActivity.class);
                            startActivity(intent1);
                        break;
                    case R.id.AttendanceRollno:
                        Intent intent2 = new Intent(getApplicationContext(),RangeActivity.class);
                        startActivity(intent2);
                        break;
                    case R.id.Myfile:
                        Uri uri = Uri.parse(Environment.getExternalStorageDirectory()+"/"+"RitAttendance/");
                        startActivity(new Intent(Intent.ACTION_GET_CONTENT)
                        .setDataAndType(uri,"*/*"));
                        break;
                    case R.id.Logout:
                        auth.signOut();
                        Intent intent=new Intent(getApplicationContext(),Login.class);
                        SharedPreferences preferences =getApplicationContext().getSharedPreferences("preference", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.clear();
                        editor.apply();
                        startActivity(intent);
                        finish();
                        Toast.makeText(getApplicationContext(), "Logged Out Successfully!", Toast.LENGTH_LONG).show();
                        break;

                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        batch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                setAdapter(batch.getText().toString());
                radioGroup.clearCheck();
                if(!batch.getText().toString().equals("") && !acayear.getText().toString().equals("")) {
                    if (batch.getText().toString().equals(acayear.getText().toString())) {
                        year = "1";
                        //oddind.setText("1");
                        //evenind.setText("2");
                        b1.setText("Invalid Sem ");
                        b2.setText("Invalid Sem ");
                    } else {
                        int y;
                        y = Integer.parseInt(acayear.getText().toString()) - Integer.parseInt(batch.getText().toString());
//                    oddind.setText(String.valueOf((y*2)-1));
//                    evenind.setText(String.valueOf(y*2));
//                        b1.setText("Odd Sem " + String.valueOf((y * 2) - 1));
//                        b2.setText("Even Sem " + String.valueOf(y * 2));

                        //changed
                        if((y * 2)>8 ||y<1) {
                            b1.setText("Invalid Sem ");
                        }
                        else {
                            b1.setText("Even Sem " + String.valueOf((y * 2)));
                        }

                        //b1.setText("Even Sem " + (y * 2));
                        if((y * 2)-1>8 || y<1) {
                            b2.setText("Invalid Sem ");
                        }
                        else {
                            b2.setText("Odd Sem " + String.valueOf((y * 2) - 1));
                        }

                        year = String.valueOf(y);
                        if(y>4 || y<1){
                            curyear.setText("year :");
                        }
                        else{
                            curyear.setText("year :"+year);
                        }
                    }
                }
            }
        });

        acayear.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                radioGroup.clearCheck();
                if(!batch.getText().toString().equals("") && !acayear.getText().toString().equals("")) {
                    if (batch.getText().toString().equals(acayear.getText().toString())) {
                        year = "1";
                        //oddind.setText("1");
                        //evenind.setText("2");
                        b1.setText("Invalid Sem ");
                        b2.setText("Invalid Sem ");
                    } else {
                        int y;
                        y = Integer.parseInt(acayear.getText().toString()) - Integer.parseInt(batch.getText().toString());
//                    oddind.setText(String.valueOf((y*2)-1));
//                    evenind.setText(String.valueOf(y*2));
//                        b1.setText("Odd Sem " + String.valueOf((y * 2) - 1));
//                        b2.setText("Even Sem " + String.valueOf(y * 2));

                        if((y * 2)>8 || y<1) {
                            b1.setText("Invalid Sem ");
                        }
                        else {
                            b1.setText("Even Sem " + String.valueOf((y * 2)));
                        }

                        //b1.setText("Even Sem " + (y * 2));
                        if((y * 2)-1>8 || y<1) {
                            b2.setText("Invalid Sem ");
                        }
                        else {
                            b2.setText("Odd Sem " + String.valueOf((y * 2) - 1));
                        }
                        year = String.valueOf(y);
                        if(y>4 || y<1){
                            curyear.setText("year :");
                        }
                        else{
                            curyear.setText("year :"+year);
                        }


                    }
                }
            }
        });


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.oddSem:
                        semind= String.valueOf(b1.getText().toString().charAt(b1.getText().toString().length()-1));
                        //semind=oddind.getText().toString();
                        Log.i("clicked","oddbtn");
                        break;
                    case R.id.evenSem:
                        semind= String.valueOf(b2.getText().toString().charAt(b2.getText().toString().length()-1));
                        //semind=evenind.getText().toString();
                        Log.i("clicked","oddbtn");
                        break;
                }
            }
        });

    }
    public boolean validate(){
        if(!batch.getText().toString().isEmpty() && !acayear.getText().toString().isEmpty() && !semind.isEmpty() && !date.getText().toString().equals("date")){
            return true;
        }
        return false;
    }

    public void fetch(View view) {
        if(validate()){
            Intent intent=new Intent(getApplicationContext(),NameListActivity.class);
            intent.putExtra("batch",batch.getText().toString());
            intent.putExtra("date",date.getText().toString());
            intent.putExtra("dep",dept.getSelectedItem().toString());
            intent.putExtra("sec",sec.getSelectedItem().toString());
            intent.putExtra("hour",hour.getSelectedItem().toString());
            intent.putExtra("year",year);
            intent.putExtra("sem",semind);
            startActivity(intent);
        }
        else {
            Toast.makeText(this, "fill the required data", Toast.LENGTH_SHORT).show();
        }


    }

    public void check(View view) {
        if(validate()) {
            Intent intent = new Intent(getApplicationContext(), ChecKAttendanceActivity.class);
            intent.putExtra("year", year);
            intent.putExtra("dep", dept.getSelectedItem().toString());
            intent.putExtra("sec", sec.getSelectedItem().toString());
            intent.putExtra("date", date.getText().toString().trim());
            startActivity(intent);
        }
        else {
            Toast.makeText(this, "fill the required data", Toast.LENGTH_SHORT).show();
        }



    }

    public void setDate(View view) {
        datePickerDialog= new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                Calendar calendar=Calendar.getInstance();
                calendar.set(Calendar.DAY_OF_MONTH,i2);
                calendar.set(Calendar.MONTH,i1);
                calendar.set(Calendar.YEAR,i);

                String dates= i2+"-"+(i1+1)+"-"+i;
                date.setText(dates);
                Log.i("hour",hour.getSelectedItem().toString());




            }
        },Calendar.getInstance().get(Calendar.YEAR),Calendar.getInstance().get(Calendar.MONDAY),Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    public void setAdapter(String Batch){
        if(Batch.compareTo("2019")>0){
            sec.setAdapter(updatedSecAdapter);
        }
        else {
            sec.setAdapter(secadapter);
        }
        if(Batch.compareTo("2019")>0){
            dept.setAdapter(updatedDepAdapter);
        }
        else {
            dept.setAdapter(deptadapter);
        }
    }

    //menu top
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
//        if(id==R.id.att_range){
//            Intent intent=new Intent(getApplicationContext(),RangeActivity.class);
//            startActivity(intent);
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }


}