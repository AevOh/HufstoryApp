package co.kr.hufstory.hubigo_fragment;

import android.telecom.Call;

import com.google.gson.JsonObject;

import java.util.List;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by Hyeong Wook on 2016-05-23.
 */
public interface HubigoService {
    @GET("/recentComments")
    void getListMainNodes(Callback<List<JsonObject>> callback);

    @GET("/detailInfo")
    void getDetailNodeInfo(@Query("lecture_id") String id, Callback<List<JsonObject>> callback);

    @GET("/searchInfo")
    void getListSearchNodes(@Query("context") String keyword, Callback<List<JsonObject>> callback);

    @GET("/statisInfo")
    void getAmountStatus(@Query("start_year") int year, @Query("start_month") int month, @Query("start_date") int date,
                         @Query("period") int period, Callback<List<JsonObject>> callback);

    @GET("/topActiveMajor")
    void getCategoryRank(Callback<List<JsonObject>> callback);

    @GET("/incUserCount")
    void increaseUserCount(Callback<String> callback);

    @GET("/incWriteCount")
    void increaseWriteCount(Callback<String> callback);

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

    @POST("/writeEvaluation")
    void registerEvaluation(@Body JsonObject writeInfo, Callback<JsonObject> callback);

    @POST("/removeEvaluation")
    void removeEvaluation(@Body JsonObject evaluationInfo, Callback<String> callback);
}
