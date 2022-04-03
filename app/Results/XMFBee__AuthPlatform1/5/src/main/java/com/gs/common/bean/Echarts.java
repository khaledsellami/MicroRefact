package com.gs.common.bean;
 import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
public class Echarts {


public int getWeek(String time){
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    Date date = null;
    try {
        date = sdf.parse(time);
    } catch (ParseException e) {
        e.printStackTrace();
    }
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    int weekOfMonth = calendar.get(Calendar.WEEK_OF_YEAR);
    return weekOfMonth;
}


}