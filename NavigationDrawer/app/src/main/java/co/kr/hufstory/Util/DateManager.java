package co.kr.hufstory.Util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Hyeong Wook on 2016-06-27.
 */
public abstract class DateManager {
    static public int getLastSunDayDate(String format){
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
        cal.add(Calendar.DATE, -(cal.get(Calendar.DAY_OF_WEEK) + 6));
        Date weekAgo = cal.getTime();

        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        String result = dateFormat.format(weekAgo);

        return Integer.valueOf(result);
    }

    static public int getCurrentDate(String format){
        Calendar cal = Calendar.getInstance();
        Date current = cal.getTime();

        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        String result = dateFormat.format(current);

        return Integer.valueOf(result);
    }
}
