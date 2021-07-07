package com.devgd.attendancev1;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
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
    TextView date;
    EditText batch,acayear;
    RadioGroup radioGroup;
    String year,semind;
    TextView oddind,evenind;
    RadioButton b1,b2;
    DatePickerDialog datePickerDialog;
    ArrayAdapter deptadapter, secadapter, houradapter, updatedDepAdapter,updatedSecAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        batch=findViewById(R.id.batch);
        date=findViewById(R.id.date);
        dept=findViewById(R.id.dep);
        sec=findViewById(R.id.sec);
        hour=findViewById(R.id.hour);
        acayear=findViewById(R.id.acayear);
        radioGroup=findViewById(R.id.radioGroup);
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
                if(!batch.getText().toString().equals("") && !acayear.getText().toString().equals("")) {
                    int y;
                    y = Integer.parseInt(acayear.getText().toString()) - Integer.parseInt(batch.getText().toString());
//                    oddind.setText(String.valueOf((y*2)-1));
//                    evenind.setText(String.valueOf(y*2));
                    b1.setText("Odd Sem " + String.valueOf((y * 2) - 1));
                    b2.setText("Even Sem " + String.valueOf(y * 2));
                    year = String.valueOf(y);
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
                if(!batch.getText().toString().equals("") && !acayear.getText().toString().equals("")) {
                    if (batch.getText().toString().equals(acayear.getText().toString())) {
                        year = "1";
                        oddind.setText("1");
                        evenind.setText("2");
                    } else {
                        int y;
                        y = Integer.parseInt(acayear.getText().toString()) - Integer.parseInt(batch.getText().toString());
//                    oddind.setText(String.valueOf((y*2)-1));
//                    evenind.setText(String.valueOf(y*2));
                        b1.setText("Odd Sem " + String.valueOf((y * 2) - 1));
                        b2.setText("Even Sem " + String.valueOf(y * 2));
                        year = String.valueOf(y);
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

    public void fetch(View view) {
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

    public void check(View view) {
        Intent intent=new Intent(getApplicationContext(),ChecKAttendanceActivity.class);
        intent.putExtra("year",year);
        intent.putExtra("dep",dept.getSelectedItem().toString());
        intent.putExtra("sec",sec.getSelectedItem().toString());
        intent.putExtra("date",date.getText().toString().trim());
        startActivity(intent);


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
}