package com.example.hyunwoo.wintercoding;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder> {
    private ArrayList<Schedule> schedules;
    private Context context;
    public ScheduleAdapter(ArrayList<Schedule> schedules, Context context){
        this.schedules = schedules;
        this.context = context;
    }

    public void setSchedules(ArrayList<Schedule> schedules){
        this.schedules = schedules;
    }

    public void addSchedule(Schedule schedule){
        schedules.add(schedule);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedule_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.tvContent.setText(schedules.get(position).getContent());
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBHandler handler = new DBHandler(context, null,null,1);
                if(handler.deleteSchedule(schedules.get(position).getYear(), schedules.get(position).getMonth(), schedules.get(position).getDate(), schedules.get(position).getContent())){
                    schedules.remove(position);
                    notifyDataSetChanged();
                }else{
                    Toast.makeText(context, "삭제 실패", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return schedules.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvContent;
        Button deleteButton;
        public ViewHolder(View itemView) {
            super(itemView);
            tvContent = itemView.findViewById(R.id.scheduleContent);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}
