package com.devgd.attendancev1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


public class CheckAttendanceAdapter extends RecyclerView.Adapter<CheckAttendanceAdapter.ViewHolder>{
    private List<AttendanceModelClass> nameList=new ArrayList<>();
    Context context;
    public CheckAttendanceAdapter(Context context) {
        this.context = context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view= LayoutInflater.from(parent.getContext()).inflate(R.layout.check_attendance_layout,parent,false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        AttendanceModelClass modelClass=nameList.get(position);
        holder.name.setText(modelClass.getName());
        holder.dep.setText(modelClass.getDep());
        holder.year.setText(modelClass.getYear());
        holder.sec.setText(modelClass.getSec());
            if (!modelClass.getH1().equals(" ") && status(modelClass.getH1())) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    holder.h1.setCardBackgroundColor(context.getColor(R.color.present));
                }
            }
            else if(modelClass.getH1().equals(" ")){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    holder.h1.setCardBackgroundColor(context.getColor(R.color.defaultc));
                }
            }
            else{
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    holder.h1.setCardBackgroundColor(context.getColor(R.color.absent));
                }
            }

            if (!modelClass.getH2().equals(" ") && status(modelClass.getH2())) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    holder.h2.setCardBackgroundColor(context.getColor(R.color.present));
                }
            }
            else if(modelClass.getH2().equals(" ")) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    holder.h2.setCardBackgroundColor(context.getColor(R.color.defaultc));
                }
            }
            else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    holder.h2.setCardBackgroundColor(context.getColor(R.color.absent));
                }
            }
            if (!modelClass.getH3().equals(" ") && status(modelClass.getH3())) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    holder.h3.setCardBackgroundColor(context.getColor(R.color.present));
                }
            }
            else if(modelClass.getH3().equals(" ")) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    holder.h3.setCardBackgroundColor(context.getColor(R.color.defaultc));
                }
            }
            else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    holder.h3.setCardBackgroundColor(context.getColor(R.color.absent));
                }
            }
            if (!modelClass.getH4().equals(" ") && status(modelClass.getH4())) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    holder.h4.setCardBackgroundColor(context.getColor(R.color.present));
                }
            }
            else if(modelClass.getH4().equals(" ")){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    holder.h4.setCardBackgroundColor(context.getColor(R.color.defaultc));
                }
            }
            else{
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    holder.h4.setCardBackgroundColor(context.getColor(R.color.absent));
                }
            }
            if (!modelClass.getH5().equals(" ") && status(modelClass.getH5())) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    holder.h5.setCardBackgroundColor(context.getColor(R.color.present));
                }
            } else if(modelClass.getH5().equals(" ")) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    holder.h5.setCardBackgroundColor(context.getColor(R.color.defaultc));
                }
            }
            else{
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    holder.h5.setCardBackgroundColor(context.getColor(R.color.absent));
                }
            }
            if (!modelClass.getH6().equals(" ") && status(modelClass.getH6())) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    holder.h6.setCardBackgroundColor(context.getColor(R.color.present));
                }
            }
            else if(modelClass.getH6().equals(" ")) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    holder.h6.setCardBackgroundColor(context.getColor(R.color.defaultc));
                }
            }
            else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    holder.h6.setCardBackgroundColor(context.getColor(R.color.absent));
                }
            }

            if (!modelClass.getH7().equals(" ") && status(modelClass.getH7())) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    holder.h7.setCardBackgroundColor(context.getColor(R.color.present));
                }
            }
            else if(modelClass.getH7().equals(" ")) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    holder.h7.setCardBackgroundColor(context.getColor(R.color.defaultc));
                }
            }
            else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    holder.h7.setCardBackgroundColor(context.getColor(R.color.absent));
                }
            }

            holder.call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:"+modelClass.getPhno()));
                    context.startActivity(intent);
                }
            });





//        holder.h2.setText(modelClass.getH2());
//        holder.h3.setText(modelClass.getH3());
//        holder.h4.setText(modelClass.getH4());
//        holder.h5.setText(modelClass.getH5());
//        holder.h6.setText(modelClass.getH6());
//        holder.h7.setText(modelClass.getH7());
    }
    @Override
    public int getItemCount() {
        return nameList.size();
    }



    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView name,dep,year,sec;
        CardView h1,h2,h3,h4,h5,h6,h7,call;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.checkName);
            dep=itemView.findViewById(R.id.checkDep);
            year=itemView.findViewById(R.id.checkYear);
            sec=itemView.findViewById(R.id.checkSec);
            h1=itemView.findViewById(R.id.checkH1);
            h2=itemView.findViewById(R.id.checkH2);
            h3=itemView.findViewById(R.id.checkH3);
            h4=itemView.findViewById(R.id.checkH4);
            h5=itemView.findViewById(R.id.checkH5);
            h6=itemView.findViewById(R.id.checkH6);
            h7=itemView.findViewById(R.id.checkH7);
            call=itemView.findViewById(R.id.checkCardView);
        }
    }
    public void setNameList(List<AttendanceModelClass> nameList){
        this.nameList=nameList;
    }

    public boolean status(String attendance) {
        return attendance.equals("true");
    }


}

