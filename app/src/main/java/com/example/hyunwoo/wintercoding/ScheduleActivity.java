package com.example.hyunwoo.wintercoding;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ScheduleActivity extends AppCompatActivity {
    private int year, month, date;
    private ArrayList<Schedule> schedules;
    private ScheduleAdapter adapter;

    public static final int RESULT_FINISH = 500;

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        setResult(RESULT_FINISH, intent);
        finish();
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        try {
            Intent intent = getIntent();
            year = intent.getIntExtra("YEAR", -1);
            month = intent.getIntExtra("MONTH", -1);
            date = intent.getIntExtra("DATE", -1);
            schedules = (ArrayList<Schedule>) intent.getSerializableExtra("SCHEDULE");

            if(year == -1 || month == -1 || date == -1 || schedules == null){
                throw new Exception();
            }

            TextView tv = findViewById(R.id.scheduleDate);
            String str = year + "/" + month + "/" + date;
            tv.setText(str);

            RecyclerView recyclerView = findViewById(R.id.scheduleRecyclerView);
            adapter = new ScheduleAdapter(schedules, getApplicationContext());
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);

        }catch (Exception e){
            Toast.makeText(this, "에러발생", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    public void onAddSchedule(View view) {
        EditText editText = findViewById(R.id.scheduleInput);
        String content = editText.getText().toString();
        DBHandler dbHandler = new DBHandler(this, null,null, 1);
        if(dbHandler.addSchedule(year, month, date, content)){
            Schedule s = new Schedule(year, month, date, content);
            adapter.addSchedule(s);
        } else{
            Toast.makeText(this, "스케줄 등록 실패", Toast.LENGTH_SHORT).show();
        }
    }
}
