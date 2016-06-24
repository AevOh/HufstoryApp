package co.kr.hufstory.main;

import com.google.gson.JsonObject;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;

/**
 * Created by Hyeong Wook on 2016-06-24.
 */
public interface HufstoryAPI {
    @GET("/menuCategory")
    void getMenuCategory(Callback<List<JsonObject>> callback);

    @GET("/menuItem")
    void getMenuItems(Callback<List<JsonObject>> callback);
}
