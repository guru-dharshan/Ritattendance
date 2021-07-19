package com.devgd.attendancev1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class CheckAttendanceRange extends AppCompatActivity {

    TextView range;
    RecyclerView recyclerView;
    CheckAttendanceRangeAdapter adapter;
    FirebaseFirestore firestore;
    String year,sec,dep,date;
    List<AttendanceModelClass> nameList,filteredNameList;
    int sdate,edate,smonth,emonth,snoofdays,enoofdays,ye;
    AttendanceModelClass modelClass;
    int curi=0;
    List<String> noOfPresent,noOfAbsent;
    List<String> dates;
    List<Integer> percent;
    List<Double> attper;
    List<Double> finalPercentage;
    int TotalHours=0;

    List<Double> filteredPercentage;
    ImageView filterImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_attendance_range);

        range=findViewById(R.id.ranceDate);
        recyclerView=findViewById(R.id.rangeRecyclerView);

        noOfAbsent=new ArrayList<>();
        noOfPresent=new ArrayList<>();

        dates=new ArrayList<>();

        attper=new ArrayList<>();

        finalPercentage=new ArrayList<>();

        filterImage=findViewById(R.id.filterImage);

        firestore=FirebaseFirestore.getInstance();
        Intent intent=getIntent();
        sdate=intent.getIntExtra("startDate",0);
        edate=intent.getIntExtra("endDate",0);
        smonth=intent.getIntExtra("startMonth",0);
        emonth=intent.getIntExtra("endMonth",0);
        ye=intent.getIntExtra("ye",0);
        date=intent.getStringExtra("date");
        year=intent.getStringExtra("year");
        sec=intent.getStringExtra("sec");
        dep=intent.getStringExtra("dep");

        filteredNameList=new ArrayList<>();
        filteredPercentage=new ArrayList<>();

        percent=new ArrayList<>();

        percent.add(0);
        percent.add(0);
        percent.add(0);
        percent.add(0);
        percent.add(0);
        percent.add(0);
        percent.add(0);

        range.setText(date);

//        year="3";
//        sec="A";
//        dep="CSE";
//
//        sdate=12;
//        edate=14;
//        smonth=7;
//        emonth=7;
//        ye=2021;


        snoofdays=countNoOfDays(smonth);
        enoofdays=countNoOfDays(emonth);

        nameList=new ArrayList<>();

        range.setText(date);

        adapter=new CheckAttendanceRangeAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        if(smonth!=emonth) {

            for (int i = sdate; i < snoofdays+1; i++) {
                String d=i+"-"+smonth+"-"+ye;
                dates.add(d);
            }

            for (int i = 1; i < edate+1; i++) {
                String d=i+"-"+emonth+"-"+ye;
                dates.add(d);
            }
        }
        else{
            Log.i("comming","here");
            for (int i = sdate; i < edate+1; i++) {
                //Log.i("comming","here");
                String d=i+"-"+smonth+"-"+ye;
                //Log.i("date",d);
                dates.add(d);
            }
        }
//        for (int i = sdate; i < dates.size(); i++) {
//            Log.i("Dates",dates.get(i));
//        }
        TotalHours=dates.size()*7;
    }

    @Override
    protected void onStart() {
        super.onStart();
        //percent=new ArrayList<>();
        for (int i = 0; i < dates.size(); i++) {
            Log.i("Dates", dates.get(i));
            firestore.collection(dates.get(i))
                    .whereEqualTo("dep", dep)
                    .whereEqualTo("year", year)
                    .whereEqualTo("sec", sec)
                    .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot querySnapshot) {
                    nameList = new ArrayList<>();
                    for (QueryDocumentSnapshot documentSnapshot : querySnapshot) {
//                    if(dep.equals(documentSnapshot.getString("dep")) &&
//                        year.equals(documentSnapshot.getString("year")) &&
//                        sec.equals(documentSnapshot.getString("sec"))
//                    ){
                        modelClass = documentSnapshot.toObject(AttendanceModelClass.class);
                        nameList.add(modelClass);
                        //call the percentage


                        //}
                    }
                    findPercent();
                    if (nameList.size() == 0) {
                        Toast.makeText(CheckAttendanceRange.this, "NO DATA AVAILABLE", Toast.LENGTH_LONG).show();
                    }



//                    adapter.setNameList(nameList);
//                    recyclerView.setAdapter(adapter);
//                    recyclerView.setLayoutManager(new LinearLayoutManager(CheckAttendanceRange.this));
                }
            });
            setRecyclerView();
        }


//        adapter.setNameList(nameList,finalPercentage);
//        recyclerView.setAdapter(adapter);
//        recyclerView.setLayoutManager(new LinearLayoutManager(CheckAttendanceRange.this));
    }

    public void setRecyclerView(){
        //if(i==dates.size()-1) {
        for(int ind=0;ind<nameList.size();ind++){
            double val=(attper.get(ind)/TotalHours)*100;
            Log.i("finaaaaal val"," "+val);
            finalPercentage.add(val);
            noOfPresent.add(String.valueOf(attper.get(ind).shortValue()));
            noOfAbsent.add(String.valueOf(TotalHours-attper.get(ind).shortValue()));
            // }
            Log.i("finaaaaal sizeee"," "+finalPercentage.size());


        }
        adapter.setNameList(nameList,finalPercentage);
        adapter.setAtendanceCount(noOfPresent,noOfAbsent);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(CheckAttendanceRange.this));
    }

    public boolean checkLeapyear(int year){
        return ((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0);
    }

    public int countNoOfDays(int day){
        if (day!=2 && day<8){
            if (day%2==0)
                return 30;
            return 31;
        }
        else if(day==2){
            if(checkLeapyear(ye))
                return 28;
            return 27;
        }

        else if(day==8){
            return 31;
        }

        else if(day>8){
            if (day%2==0)
                return 31;
            return 31;
        }
        return 0;
    }

    public void findPercent(){
        for(int i=0;i<nameList.size();i++) {
            AttendanceModelClass mc = nameList.get(i);
//                int per1 = (percent.get(0) + getAttendance(mc.getH1()));
//                Log.i("Percentage value 1", String.valueOf(per1));
//            percent.set(0, per1);
//
//            int per2 = (percent.get(1) + getAttendance(mc.getH2()));
//            Log.i("Percentage value 2", String.valueOf(per2));
//            percent.set(1, per2);
//
//            int per3 = (percent.get(2) + getAttendance(mc.getH3()));
//            Log.i("Percentage value 3", String.valueOf(per3));
//            percent.set(2, per3);
//
//            int per4 = (percent.get(3) + getAttendance(mc.getH4()));
//            Log.i("Percentage value 4", String.valueOf(per4));
//            percent.set(3, per4);
//
//            int per5 = (percent.get(4) + getAttendance(mc.getH5()));
//            Log.i("Percentage value 5", String.valueOf(per5));
//            percent.set(4, per5);
//
//            int per6 = (percent.get(5) + getAttendance(mc.getH6()));
//            Log.i("Percentage value 6", String.valueOf(per6));
//            percent.set(5, per6);
//
//            int per7 = (percent.get(6) + getAttendance(mc.getH7()));
//            Log.i("Percentage value 7", String.valueOf(per7));
//                percent.set(6, per7);
            Log.i("cuuuurrr iiii",""+curi);
            if(curi==0){
                attper.add(Double.parseDouble(mc.getNoofpresent()));
                //Log.i("heyy its check for",mc.getNoofpresent());
            }
            else{
                //Log.i("percentageeee",attper.get(i)+"  "+mc.getNoofpresent()+" "+TotalHours);
                //double Percent=((attper.get(i)+Integer.parseInt(mc.getNoofpresent()))/TotalHours)*100;
                double cc=attper.get(i)+Integer.parseInt(mc.getNoofpresent());
                attper.set(i, cc);
//                Percent=(Percent+finalPercentage.get(i))/(curi+1);
//                finalPercentage.set(i,Percent);
                Log.i("percentageeee",i+"  "+cc);

            }

        }
        if(curi==dates.size()-1){
            setRecyclerView();
        }
        curi+=1;

    }

    public void Filter(View view) {
        PopupMenu pm = new PopupMenu(CheckAttendanceRange.this,filterImage);
        pm.getMenuInflater().inflate(R.menu.popupmenu, pm.getMenu());
        pm.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){

                    case R.id.all:
                        filterPercentage(0,100);
                        return true;

                    case R.id.good:
                        filterPercentage(75,100);
                        return true;

                    case R.id.average:
                        filterPercentage(50,75);
                        return true;

                    case R.id.bad:
                        filterPercentage(0,50);
                        return true;
                }
                return true;
            }
        });
        pm.show();
    }

    private void filterPercentage(int val,int val2) {
        filteredNameList=new ArrayList<>();
        filteredPercentage=new ArrayList<>();
        noOfAbsent=new ArrayList<>();
        noOfPresent=new ArrayList<>();
        for(int fil=0;fil<finalPercentage.size();fil++){
            if(finalPercentage.get(fil)>=val && finalPercentage.get(fil)<=val2){
                filteredPercentage.add(finalPercentage.get(fil));
                filteredNameList.add(nameList.get(fil));
                noOfPresent.add(nameList.get(fil).getNoofpresent());
                noOfAbsent.add(String.valueOf(TotalHours-Integer.parseInt(nameList.get(fil).getNoofpresent())));
            }
        }
        adapter.setNameList(filteredNameList,filteredPercentage);
        adapter.setAtendanceCount(noOfPresent,noOfAbsent);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(CheckAttendanceRange.this));


    }

    public void generateExcel(View view) {
        File filePath = new File(Environment.getExternalStorageDirectory().toString() + "/"+"RitAttendance"+"/"+year+"-"+"-"+sec+"-"+date+"attendance.xls");
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
        HSSFSheet hssfSheet = hssfWorkbook.createSheet("Sheet 1");
        HSSFRow nameRow;
        HSSFCell nameCell;


        for(int index=0;index<nameList.size();index++) {
            //name
            nameRow = hssfSheet.createRow(index);
            nameCell = nameRow.createCell(0);

            nameCell.setCellValue(nameList.get(index).getName());

            //no.of.present

            nameCell = nameRow.createCell(1);

            nameCell.setCellValue(noOfPresent.get(index));

            //no.of.absent
            nameCell = nameRow.createCell(2);

            nameCell.setCellValue(noOfAbsent.get(index));


            //percentage
            nameCell = nameRow.createCell(3);

            nameCell.setCellValue(finalPercentage.get(index));




        }
        //creatin dir in internal storage
        if(ActivityCompat.checkSelfPermission(CheckAttendanceRange.this, WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
            createFolder();
        }
        else {
            ActivityCompat.requestPermissions(CheckAttendanceRange.this,new String[]{READ_EXTERNAL_STORAGE},100);
        }




        try {
            if (!filePath.exists()){
                filePath.createNewFile();
            }

            FileOutputStream fileOutputStream= new FileOutputStream(filePath);
            hssfWorkbook.write(fileOutputStream);
            Toast.makeText(this, "Excel Generated Successfully", Toast.LENGTH_SHORT).show();

            if (fileOutputStream!=null){
                fileOutputStream.flush();
                fileOutputStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to Generate Excel", Toast.LENGTH_SHORT).show();
        }
    }

    public void createFolder(){
        File file = new File(Environment.getExternalStorageDirectory(),"RitAttendance");

        if(file.exists()){
            //put the data
            Toast.makeText(this, "file already there", Toast.LENGTH_SHORT).show();
        }
        else{
            file.mkdirs();
            if(file.isDirectory()){
                Toast.makeText(this, "file created", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(this, "problem while creating", Toast.LENGTH_SHORT).show();

            }
        }
    }
}