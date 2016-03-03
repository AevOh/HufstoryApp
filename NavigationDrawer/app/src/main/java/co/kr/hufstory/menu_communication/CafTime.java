package co.kr.hufstory.menu_communication;

import java.util.HashMap;

/**
 * Created by Aev Oh on 2016-03-04.
 */
public class CafTime {
    public static final HashMap<Integer, String> CAF_TIME_MAP = new HashMap<Integer, String>();
    public CafTime(){
        CAF_TIME_MAP.put(11, "10:30 ~ 11:30");
        CAF_TIME_MAP.put(12, "11:30 ~ 18:30");
        CAF_TIME_MAP.put(13, "11:30 ~ 13:40");
        CAF_TIME_MAP.put(14, "11:30 ~ 13:40");
        CAF_TIME_MAP.put(15, "17:30 ~ 18:30");
        CAF_TIME_MAP.put(21, "10:30 ~ 16:30");
        CAF_TIME_MAP.put(31, "08:00 ~ 09:30");
        CAF_TIME_MAP.put(32, "12:00 ~ 14:00");
        CAF_TIME_MAP.put(33, "17:30 ~ 19:00");
    }
}
