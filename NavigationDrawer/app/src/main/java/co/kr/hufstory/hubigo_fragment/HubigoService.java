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

    @POST("/writtenInfo")
    void getListWrittenNodes(@Body JsonObject userSession, Callback<List<JsonObject>> callback);

    @POST("/favoriteInfo")
    void getListBookmarkNodes(@Body JsonObject userSession, Callback<List<JsonObject>> callback);

    @POST("/loginInfo")
    void getUserInfo(@Body JsonObject keyJSON, Callback<UserInfo> callback);

    @POST("/writerInfo")
    void getUserHubigoInfo(@Body JsonObject keyJSON, Callback<List<UserHubigoInfo>> callback);

    @POST("/favorite")
    void addBookmark(@Body JsonObject bookmarkInfo, Callback<String> callback);

    @POST("/removeFavorite")
    void removeBookmark(@Body JsonObject bookmarkInfo, Callback<String> callback);
}
