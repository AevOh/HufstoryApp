package co.kr.hufstory.main;

import android.util.Log;

import com.google.gson.JsonObject;

import co.kr.hufstory.Util.CookieParser;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Hyeong Wook on 2016-06-22.
 */
public class MainController {
    private MainActivity mView;
    private UserAPI mUserAPI;

    public MainController(){
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("http://hufstory.co.kr:5000")
                .build();

        mUserAPI = restAdapter.create(UserAPI.class);
    }

    public void attachView(MainActivity activity){
        mView = activity;
    }

    public void dettachView(){
        mView = null;
    }

    public void loadUserInfo(String cookie){
        JsonObject sessionJson = new JsonObject();
        sessionJson.addProperty("session_key", CookieParser.parse(cookie, "PHPSESSID"));

        mUserAPI.getUserInfo(sessionJson, new Callback<UserInfo>() {
            @Override
            public void success(UserInfo userInfo, Response response) {
                if(userInfo != null) {
                    userInfo.setLogin(true);
                    mView.showUserInfo(userInfo);
                }
                else
                    mView.showUserInfo(getNonmemberInfo());
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("userInfo error", error.toString());
            }
        });
    }

    private UserInfo getNonmemberInfo(){
        UserInfo info = new UserInfo();
        info.setNick_name("로그인이 필요합니다.");
        info.setUser_id(" ");
        info.setLogin(false);

        return info;
    }
}
