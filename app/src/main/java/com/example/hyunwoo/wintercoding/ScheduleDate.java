package com.example.hyunwoo.wintercoding;

import java.io.Serializable;

public class ScheduleDate implements Serializable{
    protected int year;
    protected int month;
    protected int date;

    public ScheduleDate(int year, int month, int date){
        this.year = year;
        this.month = month;
        this.date = date;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDate() {
        return date;
    }
}
