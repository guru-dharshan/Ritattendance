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


public class CheckAttendanceRangeAdapter extends RecyclerView.Adapter<CheckAttendanceRangeAdapter.ViewHolder>{
    private List<AttendanceModelClass> nameList=new ArrayList<>();
    List<Double> attPercentage=new ArrayList<>();
    List<String> noOfPresent=new ArrayList<>();
    List<String> noOfAbsent=new ArrayList<>();
    Context context;
    public CheckAttendanceRangeAdapter(Context context) {
        this.context = context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view= LayoutInflater.from(parent.getContext()).inflate(R.layout.check_attendance__range,parent,false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        AttendanceModelClass modelClass=nameList.get(position);
        holder.name.setText(modelClass.getName()+"\n"+modelClass.getRegno());
        holder.dep.setText(modelClass.getDep());
        holder.year.setText(modelClass.getYear());
        holder.sec.setText(modelClass.getSec());
        holder.attCount.setText("No. of Present :"+noOfPresent.get(position)+"\n"+"No. of Absent :"+noOfAbsent.get(position));
        holder.percent.setText(String.valueOf(attPercentage.get(position).shortValue())+"%");
        if (attPercentage.get(position)>75) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                holder.cardView.setCardBackgroundColor(context.getColor(R.color.good));
            }
        }
        else if (attPercentage.get(position)>50 && attPercentage.get(position)<75) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                holder.cardView.setCardBackgroundColor(context.getColor(R.color.average));
            }
        }
        else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                holder.cardView.setCardBackgroundColor(context.getColor(R.color.bad));
            }
        }

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+modelClass.getPhno()));
                context.startActivity(intent);
            }
        });

    }
    @Override
    public int getItemCount() {
        return nameList.size();
    }



    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView name,dep,year,sec,percent,attCount;
        CardView cardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView=itemView.findViewById(R.id.rangeCardView);
            name=itemView.findViewById(R.id.rangeName);
            dep=itemView.findViewById(R.id.rangeDep);
            sec=itemView.findViewById(R.id.rangeSec);
            percent=itemView.findViewById(R.id.rangeAttPercent);
            name=itemView.findViewById(R.id.rangeName);
            year=itemView.findViewById(R.id.rangeYear);
            attCount=itemView.findViewById(R.id.rangeAttCount);
        }
    }
    public void setNameList(List<AttendanceModelClass> nameList,List<Double> attPercentage){
        this.nameList=nameList;
        this.attPercentage=attPercentage;
    }

    public void setAtendanceCount(List<String> noOfPresent,List<String> noOfAbsent){
        this.noOfAbsent=noOfAbsent;
        this.noOfPresent=noOfPresent;
    }

    public boolean status(String attendance) {
        return attendance.equals("true");
    }


}
