package co.kr.hufstory.login;

/**
 * Created by Aev Oh on 2016-03-12.
 */

import android.webkit.CookieManager;

import java.io.IOException;
import java.net.HttpCookie;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class LoginInfo {
    private static final String LOGIN_INFO_URL = "http://hufstory.co.kr/test.php";

    private List<Login> loginList;

    public void pullLogin(){
        //String strUrl = "http://hufstory.co.kr";
        //setSession(strUrl);
        System.out.println("Login Info URL: " + LOGIN_INFO_URL);

        RestAdapter adapter = new RestAdapter.Builder().setEndpoint(LOGIN_INFO_URL).build();
        LoginAPI loginAPI = adapter.create(LoginAPI.class);

        loginAPI.getLoginInfo(new Callback<List<Login>>() {
            @Override
            public void success(List<Login> login, Response response) {
                System.out.println("loginAPI.getLoginInfo Success!!");
                loginList = login;
                showLoginInfo();
            }

            @Override
            public void failure(RetrofitError error) {
                System.out.println("Failure!!");
                System.out.println("error :" + error);
            }
        });
    }

    private void showLoginInfo(){
        if(loginList == null)
            System.out.println("로그인 정보가 들어오질 못하였습니다.");
        else if(loginList.get(0).getId() == null || loginList.get(0).getId() == "" ){
            System.out.println("사용자가 로그인을 하지 않았습니다.");
        }
        else{
            System.out.println("ID: " + loginList.get(0).getId());
            System.out.println("NickName: " + loginList.get(0).getNickname());
        }
    }

    public void setSession(String strUrl) {
        URL url;

        HttpsURLConnection con = null;
        try {
            url = new URL(strUrl);
            con = (HttpsURLConnection) url.openConnection();
            con.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String COOKIES_HEADER = "Set-Cookie";


        Map<String, List<String>> headerFields = con.getHeaderFields();
        List<String> cookiesHeader = headerFields.get(COOKIES_HEADER);

        if(cookiesHeader != null) {
            for (String cookie : cookiesHeader) {
                String cookieName = HttpCookie.parse(cookie).get(0).getName();
                String cookieValue = HttpCookie.parse(cookie).get(0).getValue();

                String cookieString = cookieName + "=" + cookieValue;

                CookieManager.getInstance().setCookie(strUrl, cookieString);
            }
        }
    }
}
