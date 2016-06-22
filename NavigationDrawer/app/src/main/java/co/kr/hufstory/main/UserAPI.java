package co.kr.hufstory.main;

import com.google.gson.JsonObject;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.POST;

/**
 * Created by Hyeong Wook on 2016-06-22.
 */
public interface UserAPI {
    @POST("/loginInfo")
    void getUserInfo(@Body JsonObject sessionJson, Callback<UserInfo> callback);
}
