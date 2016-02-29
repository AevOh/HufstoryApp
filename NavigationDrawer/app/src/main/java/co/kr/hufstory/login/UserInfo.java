package co.kr.hufstory.login;

import android.net.Uri;
import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;

import co.kr.hufstory.main.MainActivity;

/**
 * Created by Aev Oh on 2016-02-28.
 */
public class UserInfo {
    public static final String USERINFO_URL = "http://hufstory.co.kr/";
    public void doUserInfo(){
        System.out.println("UserInfo Created!!");
        new UserInfoTask().execute();
    }

    // AsyncTask<Params,Progress,Result>
    private class UserInfoTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            System.out.println("UserInfoTask doInBackground!!");
            try {
                System.out.println("Try Start!!");
                System.out.println(USERINFO_URL);
                Document doc = Jsoup.connect(USERINFO_URL).followRedirects(false).ignoreHttpErrors(true).get();
                System.out.println("Try Pass!!");
                Elements userInfo = doc.select("text");
                System.out.println("For Before!!");
                for (Element mElement : userInfo) {
                    System.out.println("userInfo: " + mElement);
                }
                System.out.println("For End!!");

            } catch (IOException ex) {
                ex.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String userInfo) {

        }
    }
}
