package com.example.hyunwoo.wintercoding;

import android.widget.GridLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public abstract class CalendarFragment extends android.support.v4.app.Fragment {
    /******************************************
     * gridLayout      : 그리드레이아웃 객체
     * tvTitle         : 버튼 사이의 타이틀 (2018/10)
     * tvDate          : 그리드레이아웃 각 칸의 날짜 텍스트뷰
     * tvSchedule      : 그리드레이아웃 각 칸의 스케줄 텍스트뷰 (1개만 표시)
     * tvMore          : 그리드레이아웃 각 칸의 스케줄 2개이상 여부 표시 텍스트뷰
     * calendar        : 캘린더객체
     * scheduleDate    : 그리드레이아웃 각 칸의 날짜 저장
     * schedules       : 각 날짜별 스케줄목록 저장
     * isShowReady     : 출력 준비 여부
     * titleTextFormat : 타이틀 스트링 포맷
     * renewFlag       : 화면갱신 필요여부
     * day             : 1~7에 해당하는 요일 상수 스트링 (0은 무시)
     *****************************************/
    protected GridLayout gridLayout;
    protected TextView tvTitle;
    protected TextView[] tvDate;
    protected TextView[] tvSchedule;
    protected TextView[] tvMore;
    protected Calendar calendar;
    protected ScheduleDate[] scheduleDate;
    protected ArrayList<Schedule>[] schedules;
    protected boolean isShowReady;
    protected SimpleDateFormat titleTextFormat;
    protected boolean renewFlag;
    protected final String day[] = {"", "일", "월", "화", "수", "목", "금", "토"};

    protected CalendarFragment(){
        isShowReady = false;
        renewFlag = false;
    }

    //화면에 캘린더를 표시해주는 함수
    //그리드레이아웃의 각 칸을 채운다.
    public abstract void showCalendar();

    public boolean isShowReady() {
        return isShowReady;
    }

    public boolean isRenewFlag() {
        return renewFlag;
    }

    public void setRenewFlag(boolean flag){
        renewFlag = flag;
    }


}
