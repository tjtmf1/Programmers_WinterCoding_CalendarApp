package com.example.hyunwoo.wintercoding;

import java.io.Serializable;

public class Schedule extends ScheduleDate implements Serializable{
    private String content;
    public Schedule(int year, int month, int date, String content) {
        super(year, month, date);
        this.content = content;
    }

    public String getContent() {
        return content;
    }
}
