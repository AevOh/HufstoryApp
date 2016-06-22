package co.kr.hufstory.Util;

/**
 * Created by Hyeong Wook on 2016-06-22.
 */
abstract public class CookieParser {
    public static String parse(String cookies, String key){
        String[] cookie = cookies.split(";");
        //String key = "PHPSESSID";

        for(String arg : cookie){
            if(arg.contains(key)){
                String[] cookieValue = arg.split("=");
                return cookieValue[1];
            }
        }

        return null;
    }
}
