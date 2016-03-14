package co.kr.hufstory.login;

/**
 * Created by Aev Oh on 2016-03-12.
 */

import java.util.List;
import retrofit.Callback;
import retrofit.http.GET;

public interface LoginAPI {
    @GET("/")
    public void getLoginInfo(Callback<List<Login>> response);
}
