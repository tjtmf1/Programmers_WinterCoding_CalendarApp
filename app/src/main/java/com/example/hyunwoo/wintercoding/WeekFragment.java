package com.example.hyunwoo.wintercoding;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class WeekFragment extends CalendarFragment {
    /******************************************
     * WEEKLY_REQUEST_CODE : startActivityForResult 리퀘스트 코드
     *****************************************/
    public static final int WEEKLY_REQUEST_CODE = 200;

    public WeekFragment() {
        // Required empty public constructor
        super();

        tvDate = new TextView[7];
        tvSchedule = new TextView[7];
        tvMore = new TextView[7];
        scheduleDate = new ScheduleDate[7];
        schedules = new ArrayList[7];
        titleTextFormat = new SimpleDateFormat("yyyy/MM", Locale.KOREA);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        tvTitle = view.findViewById(R.id.TitleText);
        if(getActivity() instanceof MainActivity){
            calendar = ((MainActivity)getActivity()).calendar;
        }
        gridLayout = view.findViewById(R.id.calendarGridLayout);
        gridLayout.setColumnCount(7);
        gridLayout.setRowCount(2);
        for(int i=0; i<7; i++){
            View v = inflater.inflate(R.layout.day_layout, null, false);
            gridLayout.addView(v);
            TextView tv = v.findViewById(R.id.dayText);
            tv.setText(day[i + 1]);
            if(i % 7 == 0)
                tv.setTextColor(Color.RED);
            if(i % 7 == 6)
                tv.setTextColor(Color.BLUE);
        }
        for(int i=0; i<7; i++){
            View v = inflater.inflate(R.layout.single_data_with_schedule, null, false);
            final int index = i;
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    gotoSchedule(index);
                }
            });
            tvDate[i] = v.findViewById(R.id.dateNumber);
            tvSchedule[i] = v.findViewById(R.id.dateSchedule);
            tvMore[i] = v.findViewById(R.id.dateMore);
            gridLayout.addView(v);
        }
        showCalendar();

        //이전 버튼 클릭
        view.findViewById(R.id.previousButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.DATE, -7);
                showCalendar();

                //월별, 일별 프래그먼트 화면 갱신 필요
                Activity activity = getActivity();
                if(activity instanceof MainActivity){
                    ((MainActivity)activity).getMonthFragment().setRenewFlag(true);
                    ((MainActivity)activity).getDayFragment().setRenewFlag(true);
                }
            }
        });

        //다음 버튼 클릭
        view.findViewById(R.id.nextButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.DATE, 7);
                showCalendar();

                //월별, 일별 프래그먼트 화면 갱신 필요
                Activity activity = getActivity();
                if(activity instanceof MainActivity){
                    ((MainActivity)activity).getMonthFragment().setRenewFlag(true);
                    ((MainActivity)activity).getDayFragment().setRenewFlag(true);
                }
            }
        });

        isShowReady = true;

        return view;
    }

    @Override
    public void showCalendar(){
        setRenewFlag(false);

        int curMonth = calendar.get(Calendar.MONTH);
        String weeklyText = titleTextFormat.format(calendar.getTime());
        tvTitle.setText(weeklyText);
        //calendar 객체가 일요일을 가르키도록
        int addDate = 1 - (calendar.get(Calendar.DAY_OF_WEEK));
        calendar.add(Calendar.DATE, addDate);

        DBHandler handler = new DBHandler(getContext(), null, null, 1);
        for(int i=0; i<7; i++){
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1;
            int date = calendar.get(Calendar.DATE);

            String dateText = String.format(Locale.KOREAN, "%d", date);
            tvDate[i].setText(dateText);

            if(month - 1 != curMonth)
                tvDate[i].setTextColor(Color.rgb(200, 200, 200));
            else if(i % 7 == 0)
                tvDate[i].setTextColor(Color.RED);
            else if(i % 7 == 6)
                tvDate[i].setTextColor(Color.BLUE);
            else
                tvDate[i].setTextColor(Color.BLACK);

            scheduleDate[i] = new ScheduleDate(year, month, date);
            schedules[i] = handler.findSchedule(year, month, date);
            if(schedules[i] == null){                          //DB에서 스케줄 가져오는데 에러일경우
                final String errorText = "ERROR";
                tvSchedule[i].setTextColor(Color.RED);
                tvSchedule[i].setText(errorText);
            } else if(schedules[i].size() > 0){                //스케줄이 1개 이상일 경우 1개만 표시
                tvSchedule[i].setTextColor(Color.BLACK);
                tvSchedule[i].setText(schedules[i].get(0).getContent());
                tvSchedule[i].setVisibility(View.VISIBLE);
                tvMore[i].setVisibility(View.INVISIBLE);
                if(schedules[i].size() > 1){                   //스케줄이 2개 이상일 경우 + 표시
                    tvMore[i].setVisibility(View.VISIBLE);
                }
            } else {                                           //스케줄이 없는 경우
                tvSchedule[i].setVisibility(View.INVISIBLE);
                tvMore[i].setVisibility(View.INVISIBLE);
            }
            calendar.add(Calendar.DATE, 1);
        }
        //다시 원래 날짜을 가르키도록
        calendar.add(Calendar.DATE, -(7 + addDate));
    }

    private void gotoSchedule(int index){
        Intent intent = new Intent(getActivity(), ScheduleActivity.class);
        intent.putExtra("YEAR", scheduleDate[index].getYear());
        intent.putExtra("MONTH", scheduleDate[index].getMonth());
        intent.putExtra("DATE", scheduleDate[index].getDate());
        intent.putExtra("SCHEDULE", schedules[index]);
        try {
            getActivity().startActivityForResult(intent, WEEKLY_REQUEST_CODE);
        } catch(NullPointerException e){
            Toast.makeText(getContext(), "스케줄 보기 실패", Toast.LENGTH_SHORT).show();
        }
    }
}
