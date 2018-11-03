package com.example.hyunwoo.wintercoding;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TabPagerAdapter adapter;
    public Calendar calendar;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == ScheduleActivity.RESULT_FINISH){
            switch (requestCode){
                case MonthFragment.MONTHLY_REQUEST_CODE:
                    ((CalendarFragment) adapter.getItem(0)).showCalendar();
                    ((CalendarFragment) adapter.getItem(1)).setRenewFlag(true);
                    ((CalendarFragment) adapter.getItem(2)).setRenewFlag(true);
                    break;
                case WeekFragment.WEEKLY_REQUEST_CODE:
                    ((CalendarFragment) adapter.getItem(1)).showCalendar();
                    ((CalendarFragment) adapter.getItem(0)).setRenewFlag(true);
                    ((CalendarFragment) adapter.getItem(2)).setRenewFlag(true);
                    break;
                case DayFragment.DAILY_REQUEST_CODE:
                    ((CalendarFragment) adapter.getItem(2)).showCalendar();
                    ((CalendarFragment) adapter.getItem(0)).setRenewFlag(true);
                    ((CalendarFragment) adapter.getItem(1)).setRenewFlag(true);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //프래그먼트 모두 공유하는 Calendar 객체
        calendar = Calendar.getInstance();

        //매핑
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        //탭 설정
        tabLayout.addTab(tabLayout.newTab().setText("Monthly"));
        tabLayout.addTab(tabLayout.newTab().setText("Weekly"));
        tabLayout.addTab(tabLayout.newTab().setText("Daily"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setSelectedTabIndicatorHeight(0);

        //탭 프래그먼트 할당
        adapter = new TabPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        //탭 리스너
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

                //이전 프래그먼트에서 calendar값이 변경되었으면 적용
                CalendarFragment fragment = (CalendarFragment)adapter.getItem(tab.getPosition());
                if(fragment.isRenewFlag()) {
                    fragment.showCalendar();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        Toast.makeText(this, "날짜를 클릭시 스케줄 보기로 이동합니다.", Toast.LENGTH_SHORT).show();
    }
    public MonthFragment getMonthFragment(){
        android.support.v4.app.Fragment fragment = adapter.getItem(0);
        if(fragment instanceof MonthFragment){
            return (MonthFragment) fragment;
        }
        return null;
    }
    public WeekFragment getWeekFragment(){
        android.support.v4.app.Fragment fragment = adapter.getItem(1);
        if(fragment instanceof WeekFragment){
            return (WeekFragment) fragment;
        }
        return null;
    }
    public DayFragment getDayFragment(){
        android.support.v4.app.Fragment fragment = adapter.getItem(2);
        if(fragment instanceof DayFragment){
            return (DayFragment) fragment;
        }
        return null;
    }
}
