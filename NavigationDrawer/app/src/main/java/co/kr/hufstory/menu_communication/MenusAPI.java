package co.kr.hufstory.menu_communication;

/**
 * Created by Aev Oh on 2016-02-25.
 */

import java.util.List;
import retrofit.Callback;
import retrofit.http.GET;

//2015.02.25, Aev Oh, 식단표 정보를 받아오는 Retrofit API
public interface MenusAPI {
    //비동기적 방식
    @GET("/findAll/a")
    public void getMenus(Callback<List<User>> response);

    //동기적 방식
    @GET("/findAll/a")
    List<User> getMenus();
}
