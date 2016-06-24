package co.kr.hufstory.main;

import android.util.Log;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

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
    private HufstoryAPI mHufstoryAPI;

    String userSession = "";

    public MainController(){
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("http://hufstory.co.kr:5000")
                .build();

        RestAdapter hufstoryAdapter = new RestAdapter.Builder()
                .setEndpoint("http://hufstory.co.kr:5500")
                .build();

        mUserAPI = restAdapter.create(UserAPI.class);
        mHufstoryAPI = hufstoryAdapter.create(HufstoryAPI.class);
    }

    public void attachView(MainActivity activity){
        mView = activity;
    }

    public void dettachView(){
        mView = null;
    }

    public void loadUserInfo(String cookie){
        final String session = CookieParser.parse(cookie, "PHPSESSID");
        if(session == null || session.equals(userSession))
            return;

        JsonObject sessionJson = new JsonObject();
        sessionJson.addProperty("session_key", session);

        mUserAPI.getUserInfo(sessionJson, new Callback<UserInfo>() {
            @Override
            public void success(UserInfo userInfo, Response response) {
                if (userInfo != null) {
                    userSession = session;
                    userInfo.setLogin(true);
                    mView.showUserInfo(userInfo);
                } else
                    mView.showUserInfo(getNonmemberInfo());
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("userInfo error", error.toString());
            }
        });
    }

    public void loadMenuCategory(){
        mHufstoryAPI.getMenuCategory(new Callback<List<JsonObject>>() {
            @Override
            public void success(List<JsonObject> jsonObjects, Response response) {
                HashMap<Integer, String> category = new HashMap<>();
                List<String> categoryList = new ArrayList<>();

                for (JsonObject obj : jsonObjects) {
                    int srl = obj.get("menu_item_srl").getAsInt();
                    String name = obj.get("name").getAsString();
                    category.put(srl, name);
                    categoryList.add(name);
                }

                loadMenuItems(category, categoryList);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("load category error", error.toString());
            }
        });
    }

    private void loadMenuItems(final HashMap<Integer, String> categoryMap, final List<String> categoryList){
        mHufstoryAPI.getMenuItems(new Callback<List<JsonObject>>() {
            @Override
            public void success(List<JsonObject> jsonObjects, Response response) {
                HashMap<String, List<String>> itemMap = new HashMap<>();
                HashMap<String, String> urlMap = new HashMap<>();

                for (String category : categoryMap.values())
                    itemMap.put(category, new ArrayList<String>());

                for (JsonObject obj : jsonObjects) {
                    int categorySrl = obj.get("parent_srl").getAsInt();
                    String categoryName = categoryMap.get(categorySrl);
                    String menuName = obj.get("name").getAsString();
                    String url = obj.get("url").getAsString();

                    if(categoryName != null) {
                        itemMap.get(categoryName).add(menuName);
                        urlMap.put(menuName, getUrl(url));
                    }
                }

                mView.showNavigationBar(categoryList, itemMap, urlMap);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("load menu item error", error.toString());
            }
        });
    }

    private String getUrl(String foot){
        if(foot.contains("http://"))
            return foot;
        else
            return "http://hufstory.co.kr/" + foot;
    }

    private UserInfo getNonmemberInfo(){
        UserInfo info = new UserInfo();
        info.setNick_name("비회원");
        info.setUser_id("로그인이 필요합니다");
        info.setLogin(false);

        return info;
    }
}
