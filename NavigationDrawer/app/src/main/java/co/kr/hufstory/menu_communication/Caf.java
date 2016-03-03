package co.kr.hufstory.menu_communication;

import java.util.HashMap;

/**
 * Created by Aev Oh on 2016-03-04.
 */

//건물 안에 있는 식당 구분.
public class Caf {
    public static final HashMap<Integer, String> CAF_MAP = new HashMap<Integer, String>();
    public Caf(){
        CAF_MAP.put(11, "조식");
        CAF_MAP.put(12, "컵밥");
        CAF_MAP.put(13, "한식");
        CAF_MAP.put(14, "일품");
        CAF_MAP.put(15, "석식");
        CAF_MAP.put(21, "일품");
        CAF_MAP.put(31, "조식(한식)");
        CAF_MAP.put(32, "중식");
        CAF_MAP.put(33, "석식(한식)");
    }
}
