package co.kr.hufstory.Util;

/**
 * Created by Hyeong Wook on 2016-06-22.
 */
abstract public class DataFormat {
    static public String formatString(String value, int length, int ellipsis){
        String ellipsisStr = "";

        for(int i = 0; i < ellipsis; i++)
            ellipsisStr += ".";

        if(value.length() > length)
            return value.substring(0, length) + ellipsisStr;
        else
            return value;
    }

    static public String formatString(String value, int length){
        return formatString(value, length, 0);
    }
}
