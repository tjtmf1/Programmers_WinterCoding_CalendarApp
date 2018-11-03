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
public class DayFragment extends CalendarFragment {
    /******************************************
     * DAILY_REQUEST_CODE : startActivityForResult 리퀘스트 코드
     *****************************************/
    private TextView tvDay;
    private TextView tvDate;
    private TextView tvSchedule;
    private TextView tvMore;
    private ScheduleDate scheduleDate;
    private ArrayList<Schedule> schedules;

    public static final int DAILY_REQUEST_CODE = 300;

    public DayFragment() {
        // Required empty public constructor
        super();

        super.tvDate = new TextView[1];
        super.tvMore = new TextView[1];
        super.tvSchedule = new TextView[1];
        super.schedules = new ArrayList[1];
        super.scheduleDate = new ScheduleDate[1];

        this.tvDate = super.tvDate[0];
        this.tvMore = super.tvMore[0];
        this.tvSchedule = super.tvSchedule[0];
        this.schedules = super.schedules[0];
        this.scheduleDate = super.scheduleDate[0];

        titleTextFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.KOREA);
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
        gridLayout.setRowCount(2);
        gridLayout.setColumnCount(1);
        View dayOfWeek = inflater.inflate(R.layout.day_layout, null, false);
        tvDay = dayOfWeek.findViewById(R.id.dayText);
        gridLayout.addView(dayOfWeek);
        View dateView = inflater.inflate(R.layout.single_data_with_schedule, null, false);
        dateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoSchedule();
            }
        });
        tvDate = dateView.findViewById(R.id.dateNumber);
        tvSchedule = dateView.findViewById(R.id.dateSchedule);
        tvMore = dateView.findViewById(R.id.dateMore);
        gridLayout.addView(dateView);
        showCalendar();

        //이전 버튼 클릭
        view.findViewById(R.id.previousButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.DATE, -1);
                showCalendar();

                //월별, 주별 프래그먼트 화면 갱신 필요
                Activity activity = getActivity();
                if(activity instanceof MainActivity){
                    ((MainActivity)activity).getMonthFragment().setRenewFlag(true);
                    ((MainActivity)activity).getWeekFragment().setRenewFlag(true);
                }
            }
        });

        //다음 버튼 클릭
        view.findViewById(R.id.nextButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.DATE, 1);
                showCalendar();

                //월별, 주별 프래그먼트 화면 갱신 필요
                Activity activity = getActivity();
                if(activity instanceof MainActivity){
                    ((MainActivity)activity).getMonthFragment().setRenewFlag(true);
                    ((MainActivity)activity).getWeekFragment().setRenewFlag(true);
                }
            }
        });

        isShowReady = true;

        return view;
    }

    @Override
    public void showCalendar(){
        setRenewFlag(false);

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int date = calendar.get(Calendar.DATE);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        String dailyText = titleTextFormat.format(calendar.getTime());
        tvTitle.setText(dailyText);
        String dateText = String.format(Locale.KOREAN, "%d", date);
        tvDate.setText(dateText);
        tvDay.setText(day[dayOfWeek]);
        if(dayOfWeek == 1) {               //일요일
            tvDay.setTextColor(Color.RED);
            tvDate.setTextColor(Color.RED);
        } else if(dayOfWeek == 7) {        //토요일
            tvDay.setTextColor(Color.BLUE);
            tvDate.setTextColor(Color.BLUE);
        } else {                           //그 외
            tvDay.setTextColor(Color.BLACK);
            tvDate.setTextColor(Color.BLACK);
        }
        scheduleDate = new ScheduleDate(year, month, date);
        DBHandler handler = new DBHandler(getContext(), null, null, 1);
        schedules = handler.findSchedule(year, month, date);
        if(schedules == null){                          //DB에서 스케줄 가져오는데 에러일경우
            final String errorText = "ERROR";
            tvSchedule.setTextColor(Color.RED);
            tvSchedule.setText(errorText);
        } else if(schedules.size() > 0){                //스케줄이 1개 이상일 경우 1개만 표시
            tvSchedule.setTextColor(Color.BLACK);
            tvSchedule.setText(schedules.get(0).getContent());
            tvSchedule.setVisibility(View.VISIBLE);
            tvMore.setVisibility(View.INVISIBLE);
            if(schedules.size() > 1){                   //스케줄이 2개 이상일 경우 + 표시
                tvMore.setVisibility(View.VISIBLE);
            }
        } else {                                        //스케줄이 없는 경우
            tvSchedule.setVisibility(View.INVISIBLE);
            tvMore.setVisibility(View.INVISIBLE);
        }
    }

    private void gotoSchedule(){
        Intent intent = new Intent(getActivity(), ScheduleActivity.class);
        intent.putExtra("YEAR", scheduleDate.getYear());
        intent.putExtra("MONTH", scheduleDate.getMonth());
        intent.putExtra("DATE", scheduleDate.getDate());
        intent.putExtra("SCHEDULE", schedules);
        try {
            getActivity().startActivityForResult(intent, DAILY_REQUEST_CODE);
        } catch(NullPointerException e){
            Toast.makeText(getContext(), "스케줄 보기 실패", Toast.LENGTH_SHORT).show();
        }
    }
}
