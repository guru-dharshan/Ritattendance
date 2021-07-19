package com.devgd.attendancev1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

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

import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;

public class RangeActivity extends AppCompatActivity {

    Spinner dept,sec;
    TextView startRDate,curyear,endRDate;
    EditText batch,acayear;
    RadioGroup radioGroup;
    String year,semind="",startDates,endDates;
    RadioButton b1,b2;
    DatePickerDialog StartDatePickerDialog;
    DatePickerDialog EndDatePickerDialog;
    FirebaseAuth auth;
    String FinalDate;
    int sDate,sMonth,sYear,eDate,eMonth,eYear;
    ArrayAdapter deptadapter, secadapter,updatedDepAdapter,updatedSecAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_range);

        batch=findViewById(R.id.range_batch);
        startRDate=findViewById(R.id.range_start_date);
        endRDate=findViewById(R.id.range_end_date);
        curyear=findViewById(R.id.range_year);
        dept=findViewById(R.id.range_dep);
        sec=findViewById(R.id.range_sec);
        acayear=findViewById(R.id.range_acayear);
        radioGroup=findViewById(R.id.radioGroup);
        auth=FirebaseAuth.getInstance();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        b1=findViewById(R.id.range_oddSem);
        b2=findViewById(R.id.range_evenSem);
//        oddind=findViewById(R.id.oddSemInd);
//        evenind=findViewById(R.id.evenSemInd);

        String[] depts = { "CSE", "Mechanical", "ECE", "EEE"};
        String[] secs = { "A", "B", "C"};

        String[] upDep = { "CSE", "Mechanical", "ECE", "EEE","CCE","B TECH AI & DS"};
        String[] upSec = { "A", "B", "C","D","E","F","G","H"};

        deptadapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,depts);
        secadapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,secs);

        updatedDepAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,upDep);
        updatedSecAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,upSec);

        deptadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        secadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

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
        return !batch.getText().toString().isEmpty() && !acayear.getText().toString().isEmpty() &&
                !semind.isEmpty() && !startRDate.getText().toString().equals("date")
                && !endRDate.getText().toString().equals("date");
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




    public void startDate(View view) {
        StartDatePickerDialog= new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                Calendar calendar=Calendar.getInstance();
                calendar.set(Calendar.DAY_OF_MONTH,i2);
                calendar.set(Calendar.MONTH,i1);
                calendar.set(Calendar.YEAR,i);

                startDates= i2+"-"+(i1+1)+"-"+i;
                sDate=i2;
                sMonth=i1+1;
                sYear=i;
                startRDate.setText(startDates);




            }
        },Calendar.getInstance().get(Calendar.YEAR),Calendar.getInstance().get(Calendar.MONDAY),Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        StartDatePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        StartDatePickerDialog.show();
    }

    public void endDate(View view) {
        EndDatePickerDialog= new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                Calendar calendar=Calendar.getInstance();
                calendar.set(Calendar.DAY_OF_MONTH,i2);
                calendar.set(Calendar.MONTH,i1);
                calendar.set(Calendar.YEAR,i);

                endDates= i2+"-"+(i1+1)+"-"+i;
                eDate=i2;
                eMonth=i1+1;
                eYear=i;
                endRDate.setText(endDates);




            }
        },Calendar.getInstance().get(Calendar.YEAR),Calendar.getInstance().get(Calendar.MONDAY),Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        EndDatePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        EndDatePickerDialog.show();
    }

    public void check(View view) {
        //if (validate()) {

        FinalDate = startDates + "--" + endDates;
        Intent intent = new Intent(getApplicationContext(), CheckAttendanceRange.class);

        intent.putExtra("startDate", sDate);
        intent.putExtra("endDate", eDate);
        intent.putExtra("startMonth", sMonth);
        intent.putExtra("endMonth", eMonth);
        intent.putExtra("ye", sYear);
        intent.putExtra("date", FinalDate);
        intent.putExtra("year", year);
        intent.putExtra("sec", sec.getSelectedItem().toString());
        intent.putExtra("dep", dept.getSelectedItem().toString());

        startActivity(intent);
        // }
    }


}