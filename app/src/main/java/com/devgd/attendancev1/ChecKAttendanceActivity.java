package com.devgd.attendancev1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class ChecKAttendanceActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    AttendanceModelClass modelClass;
    FirebaseFirestore firestore;
    String date,year,sec,dep;
    CheckAttendanceAdapter adapter;
    List<AttendanceModelClass> nameList;
    FirebaseAuth auth;
    SharedPreferences sharedPreferences;
    private static final int PERMISSION_REQUEST_CODE = 200;
    int pageHeight = 2120;
    int pagewidth = 792;
    public static final String USERNAME="USERNAME";
    // creating a bitmap variable
    // for storing our images
    Bitmap bmp, scaledbmp;
    private File filePath;
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
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        nameList=new ArrayList<>();
        adapter=new CheckAttendanceAdapter(this);
        auth=FirebaseAuth.getInstance();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        sharedPreferences=this.getSharedPreferences("preference", Context.MODE_PRIVATE);

        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.rit);
        scaledbmp = Bitmap.createScaledBitmap(bmp, 200, 140, false);


        if (checkPermission()) {
            Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
        } else {
            requestPermission();
        }
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
                if(nameList.size()==0){
                    Toast.makeText(ChecKAttendanceActivity.this, "NO DATA AVAILABLE", Toast.LENGTH_LONG).show();
                }

                adapter.setNameList(nameList);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(ChecKAttendanceActivity.this));
            }
        });
    }

    private void generatePDF() {
        int disbtnstd=0;
        // creating an object variable
        // for our PDF document.
        PdfDocument pdfDocument = new PdfDocument();

        // two variables for paint "paint" is used
        // for drawing shapes and we will use "title"
        // for adding text in our PDF file.
        Paint paint = new Paint();
        Paint title = new Paint();

        // we are adding page info to our PDF file
        // in which we will be passing our pageWidth,
        // pageHeight and number of pages and after that
        // we are calling it to create our PDF.
        PdfDocument.PageInfo mypageInfo = new PdfDocument.PageInfo.Builder(pagewidth, pageHeight, 1).create();

        // below line is used for setting
        // start page for our PDF file.
        PdfDocument.Page myPage = pdfDocument.startPage(mypageInfo);

        // creating a variable for canvas
        // from our page of PDF.
        Canvas canvas = myPage.getCanvas();

        // below line is used to draw our image on our PDF file.
        // the first parameter of our drawbitmap method is
        // our bitmap
        // second parameter is position from left
        // third parameter is position from top and last
        // one is our variable for paint.
        canvas.drawBitmap(scaledbmp, 300, 50, paint);

        // below line is used for adding typeface for
        // our text which we will be adding in our PDF file.
        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));

        // below line is used for setting text size
        // which we will be displaying in our PDF file.
        title.setTextSize(15);

        // below line is sued for setting color
        // of our text inside our PDF file.
        title.setColor(ContextCompat.getColor(this, R.color.black));

        // below line is used to draw text in our PDF file.
        // the first parameter is our text, second parameter
        // is position from start, third parameter is position from top
        // and then we are passing our variable of paint which is title.
        disbtnstd=280;
        Display display = getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
//        int height = display.getHeight();
        canvas.drawText("YEAR :  "+year+
                        "  DEPARTMENT :  "+dep+
                        "  SECTION :  "+sec+
                        "  DATE :  "+date
                ,
                200, 220, title);
        canvas.drawText("NAME", 20, 250, title);
        canvas.drawText("H1", 260, 250, title);
        canvas.drawText("H2", 290, 250, title);
        canvas.drawText("H3", 320, 250, title);
        canvas.drawText("H4", 350, 250, title);
        canvas.drawText("H5", 380, 250, title);
        canvas.drawText("H6", 410, 250, title);
        canvas.drawText("H7", 440, 250, title);
        for (int i=0;i<nameList.size();i++) {
            canvas.drawText(nameList.get(i).getName(), 20, disbtnstd, title);

            if (getAttendance(nameList.get(i).getH1()).equals("P")) {
                title.setColor(ContextCompat.getColor(this, R.color.present));
            } else if (getAttendance(nameList.get(i).getH1()).equals("A")) {
                title.setColor(ContextCompat.getColor(this, R.color.absent));
            }
            canvas.drawText(getAttendance(nameList.get(i).getH1()), 260, disbtnstd, title);
            title.setColor(ContextCompat.getColor(this, R.color.black));

            if (getAttendance(nameList.get(i).getH2()).equals("P")) {
                title.setColor(ContextCompat.getColor(this, R.color.present));
            } else if (getAttendance(nameList.get(i).getH2()).equals("A")) {
                title.setColor(ContextCompat.getColor(this, R.color.absent));
            }
            canvas.drawText(getAttendance(nameList.get(i).getH2()), 290, disbtnstd, title);
            title.setColor(ContextCompat.getColor(this, R.color.black));

            if (getAttendance(nameList.get(i).getH3()).equals("P")) {
                title.setColor(ContextCompat.getColor(this, R.color.present));
            } else if (getAttendance(nameList.get(i).getH3()).equals("A")) {
                title.setColor(ContextCompat.getColor(this, R.color.absent));
            }
            canvas.drawText(getAttendance(nameList.get(i).getH3()), 320, disbtnstd, title);
            title.setColor(ContextCompat.getColor(this, R.color.black));

            if (getAttendance(nameList.get(i).getH4()).equals("P")) {
                title.setColor(ContextCompat.getColor(this, R.color.present));
            } else if (getAttendance(nameList.get(i).getH4()).equals("A")) {
                title.setColor(ContextCompat.getColor(this, R.color.absent));
            }
            canvas.drawText(getAttendance(nameList.get(i).getH4()), 350, disbtnstd, title);
            title.setColor(ContextCompat.getColor(this, R.color.black));

            if (getAttendance(nameList.get(i).getH5()).equals("P")) {
                title.setColor(ContextCompat.getColor(this, R.color.present));
            } else if (getAttendance(nameList.get(i).getH5()).equals("A")) {
                title.setColor(ContextCompat.getColor(this, R.color.absent));
            }
            canvas.drawText(getAttendance(nameList.get(i).getH5()), 380, disbtnstd, title);
            title.setColor(ContextCompat.getColor(this, R.color.black));

            if (getAttendance(nameList.get(i).getH6()).equals("P")) {
                title.setColor(ContextCompat.getColor(this, R.color.present));
            } else if (getAttendance(nameList.get(i).getH6()).equals("A")) {
                title.setColor(ContextCompat.getColor(this, R.color.absent));
            }
            canvas.drawText(getAttendance(nameList.get(i).getH6()), 410, disbtnstd, title);
            title.setColor(ContextCompat.getColor(this, R.color.black));

            if (getAttendance(nameList.get(i).getH7()).equals("P")) {
                title.setColor(ContextCompat.getColor(this, R.color.present));
            } else if (getAttendance(nameList.get(i).getH7()).equals("A")) {
                title.setColor(ContextCompat.getColor(this, R.color.absent));
            }
            canvas.drawText(getAttendance(nameList.get(i).getH7()), 440, disbtnstd, title);
            title.setColor(ContextCompat.getColor(this, R.color.black));
            disbtnstd += 30;
        }
        canvas.drawText("Generated by: "+sharedPreferences.getString(USERNAME,null), 20, disbtnstd, title);

        pdfDocument.finishPage(myPage);



        //==========================from here===================
//                PdfDocument.PageInfo mypageInfo1 = new PdfDocument.PageInfo.Builder(pagewidth, pageHeight, 2).create();
//
//                // below line is used for setting
//                // start page for our PDF file.
//                PdfDocument.Page myPage1 = pdfDocument.startPage(mypageInfo1);
//
//                // creating a variable for canvas
//                // from our page of PDF.
//                Canvas canvas1 = myPage1.getCanvas();
//
//                // below line is used to draw our image on our PDF file.
//                // the first parameter of our drawbitmap method is
//                // our bitmap
//                // second parameter is position from left
//                // third parameter is position from top and last
//                // one is our variable for paint.
//                //canvas.drawBitmap(scaledbmp, 56, 40, paint);
//
//                // below line is used for adding typeface for
//                // our text which we will be adding in our PDF file.
//                title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
//
//                // below line is used for setting text size
//                // which we will be displaying in our PDF file.
//                title.setTextSize(15);
//
//                // below line is sued for setting color
//                // of our text inside our PDF file.
//                title.setColor(ContextCompat.getColor(this, R.color.black));
//
//                // below line is used to draw text in our PDF file.
//                // the first parameter is our text, second parameter
//                // is position from start, third parameter is position from top
//                // and then we are passing our variable of paint which is title.
//                disbtnstd = 50;
//
//            for(int i=31;i<nameList.size();i++) {
//
//                //Display display1 = getWindowManager().getDefaultDisplay();
////        int width = display.getWidth();
////        int height = display.getHeight();
////        canvas1.drawText("YEAR :  "+year
////                        +"\n\n"+ "DEPARTMENT :  "+dep
////                        +"\n\n"+ "SECTION :  "+sec
////                        +"\n\n"+ "DATE :  "+date
////                ,
////                200, 50, title);
//////        canvas.drawText("DEPARTMENT :  "+dep, width/2, 90, title);
//////        canvas.drawText("SECTION :  "+sec, width/2, 140, title);
//////        canvas.drawText("DATE :  "+date, width/2, 180, title);
//                canvas1.drawText(nameList.get(i).getName(), 20, disbtnstd, title);
//
//                if (getAttendance(nameList.get(i).getH1()).equals("P")) {
//                    title.setColor(ContextCompat.getColor(this, R.color.present));
//                } else if (getAttendance(nameList.get(i).getH1()).equals("A")) {
//                    title.setColor(ContextCompat.getColor(this, R.color.absent));
//                }
//                canvas1.drawText(getAttendance(nameList.get(i).getH1()), 260, disbtnstd, title);
//                title.setColor(ContextCompat.getColor(this, R.color.black));
//
//                if (getAttendance(nameList.get(i).getH2()).equals("P")) {
//                    title.setColor(ContextCompat.getColor(this, R.color.present));
//                } else if (getAttendance(nameList.get(i).getH2()).equals("A")) {
//                    title.setColor(ContextCompat.getColor(this, R.color.absent));
//                }
//                canvas1.drawText(getAttendance(nameList.get(i).getH2()), 290, disbtnstd, title);
//                title.setColor(ContextCompat.getColor(this, R.color.black));
//
//                if (getAttendance(nameList.get(i).getH3()).equals("P")) {
//                    title.setColor(ContextCompat.getColor(this, R.color.present));
//                } else if (getAttendance(nameList.get(i).getH3()).equals("A")) {
//                    title.setColor(ContextCompat.getColor(this, R.color.absent));
//                }
//                canvas1.drawText(getAttendance(nameList.get(i).getH3()), 320, disbtnstd, title);
//                title.setColor(ContextCompat.getColor(this, R.color.black));
//
//                if (getAttendance(nameList.get(i).getH4()).equals("P")) {
//                    title.setColor(ContextCompat.getColor(this, R.color.present));
//                } else if (getAttendance(nameList.get(i).getH4()).equals("A")) {
//                    title.setColor(ContextCompat.getColor(this, R.color.absent));
//                }
//                canvas1.drawText(getAttendance(nameList.get(i).getH4()), 350, disbtnstd, title);
//                title.setColor(ContextCompat.getColor(this, R.color.black));
//
//                if (getAttendance(nameList.get(i).getH5()).equals("P")) {
//                    title.setColor(ContextCompat.getColor(this, R.color.present));
//                } else if (getAttendance(nameList.get(i).getH5()).equals("A")) {
//                    title.setColor(ContextCompat.getColor(this, R.color.absent));
//                }
//                canvas1.drawText(getAttendance(nameList.get(i).getH5()), 380, disbtnstd, title);
//                title.setColor(ContextCompat.getColor(this, R.color.black));
//
//                if (getAttendance(nameList.get(i).getH6()).equals("P")) {
//                    title.setColor(ContextCompat.getColor(this, R.color.present));
//                } else if (getAttendance(nameList.get(i).getH6()).equals("A")) {
//                    title.setColor(ContextCompat.getColor(this, R.color.absent));
//                }
//                canvas1.drawText(getAttendance(nameList.get(i).getH6()), 410, disbtnstd, title);
//                title.setColor(ContextCompat.getColor(this, R.color.black));
//
//                if (getAttendance(nameList.get(i).getH7()).equals("P")) {
//                    title.setColor(ContextCompat.getColor(this, R.color.present));
//                } else if (getAttendance(nameList.get(i).getH7()).equals("A")) {
//                    title.setColor(ContextCompat.getColor(this, R.color.absent));
//                }
//                canvas1.drawText(getAttendance(nameList.get(i).getH7()), 440, disbtnstd, title);
//                title.setColor(ContextCompat.getColor(this, R.color.black));
//                disbtnstd += 30;
//            }
//
//
//
//        // similarly we are creating another text and in this
//        // we are aligning this text to center of our PDF file.
//        title.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
//        title.setColor(ContextCompat.getColor(this, R.color.purple_200));
//        title.setTextSize(15);
//
//        // below line is used for setting
//        // our text to center of PDF.
//        title.setTextAlign(Paint.Align.CENTER);
//        //canvas.drawText("This is sample document which we have created.", 396, 560, title);
//
//        // after adding all attributes to our
//        // PDF file we will be finishing our page.
//        pdfDocument.finishPage(myPage1);
        //=======================================till here====================================


        //pdfDocument.finishPage(myPage);

        // below line is used to set the name of
        // our PDF file and its path.
        File file = new File(Environment.getExternalStorageDirectory(), year+"-"+"-"+sec+"-"+date+"_Attendance.pdf");

        try {
            // after creating a file name we will
            // write our PDF file to that location.
            pdfDocument.writeTo(new FileOutputStream(file));

            // below line is to print toast message
            // on completion of PDF generation.
            Toast.makeText(ChecKAttendanceActivity.this, "PDF file generated successfully.", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            // below line is used
            // to handle error
            e.printStackTrace();
        }
        // after storing our pdf to that
        // location we are closing our PDF file.
        pdfDocument.close();
    }

    private void secondPage() {

    }

    private boolean checkPermission() {
        // checking of permissions.
        int permission1 = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int permission2 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        return permission1 == PackageManager.PERMISSION_GRANTED && permission2 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        // requesting permissions if not provided.
        ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0) {

                // after requesting permissions we are showing
                // users a toast message of permission granted.
                boolean writeStorage = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean readStorage = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                if (writeStorage && readStorage) {
                    Toast.makeText(this, "Permission Granted..", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Permission Denined.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }
    }

    public String getAttendance(String s){
        if(s.equals("true")){
            return "P";
        }
        else if(s.equals("false")) {
            return "A";
        }
        return "-";
    }

    public String getAttendanceXSl(String s){
        if(s.equals("true")){
            return "Present";
        }
        else if(s.equals("false")) {
            return "Absent";
        }
        return "-";
    }

    public void pdf(View view) {
        generatePDF();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.log_out) {
            auth.signOut();
            Intent intent=new Intent(getApplicationContext(),Login.class);
            SharedPreferences preferences = this.getSharedPreferences("preference", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.apply();
            startActivity(intent);
            finish();
            Toast.makeText(getApplicationContext(), "Logged Out Successfully!", Toast.LENGTH_LONG).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void xcel(View view) {
        filePath = new File(Environment.getExternalStorageDirectory().toString() + "/"+year+"-"+"-"+sec+"-"+date+"attendance.xls");
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
        HSSFSheet hssfSheet = hssfWorkbook.createSheet("Sheet 1");
        HSSFRow nameRow;
        HSSFCell nameCell;


        for(int i=0;i<nameList.size();i++) {
            nameRow = hssfSheet.createRow(i);
            nameCell = nameRow.createCell(0);

            nameCell.setCellValue(nameList.get(i).getName());

            //h1

           nameCell = nameRow.createCell(1);

           nameCell.setCellValue(getAttendance(nameList.get(i).getH1()));

            //h2
            nameCell = nameRow.createCell(2);

            nameCell.setCellValue(getAttendance(nameList.get(i).getH2()));


            //h3
            nameCell = nameRow.createCell(3);

            nameCell.setCellValue(getAttendance(nameList.get(i).getH3()));


            //h4
            nameCell = nameRow.createCell(4);

            nameCell.setCellValue(getAttendance(nameList.get(i).getH4()));


            //h5
            nameCell = nameRow.createCell(5);

            nameCell.setCellValue(getAttendance(nameList.get(i).getH5()));


            //h6
            nameCell = nameRow.createCell(6);

            nameCell.setCellValue(getAttendance(nameList.get(i).getH6()));

            //h7
            nameCell = nameRow.createCell(7);

            nameCell.setCellValue(getAttendance(nameList.get(i).getH7()));



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

}