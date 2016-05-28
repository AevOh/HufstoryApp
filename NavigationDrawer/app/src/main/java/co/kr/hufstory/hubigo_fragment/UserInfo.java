package co.kr.hufstory.hubigo_fragment;

/**
 * Created by Hyeong Wook on 2016-05-24.
 */
public class UserInfo {
    private String user_id;
    private String nick_name;
    private char is_admin;

    public String getUser_id(){
        return user_id;
    }

    public void setUser_id(String user_id){
        this.user_id = user_id;
    }

    public  String getNick_name(){
        return nick_name;
    }

    public void setNick_name(String nick_name){
        this.nick_name = nick_name;
    }

    public char is_admin(){
        return is_admin;
    }

    public void setIs_admin(char is_admin){
        this.is_admin = is_admin;
    }
}
