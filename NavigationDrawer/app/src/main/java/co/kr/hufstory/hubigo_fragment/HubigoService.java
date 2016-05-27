package co.kr.hufstory.hubigo_fragment;

import com.google.gson.JsonObject;

import java.util.List;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;

/**
 * Created by Hyeong Wook on 2016-05-23.
 */
public interface HubigoService {
    @GET("/recentComments")
    void getListMainNodes(Callback<List<JsonObject>> callback);

    @POST("/hufstorySessions")
    void getUserInfo(@Body JsonObject keyJSON, Callback<UserInfo> callback);
}
