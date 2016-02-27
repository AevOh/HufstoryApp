package co.kr.hufstory.version_update;

import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

import co.kr.hufstory.MainActivity;

/**
 * Created by Aev Oh on 2016-02-27.
 * PlayStore에 올라온 어플 버젼 정보와 사용자 앱의 어플 정보를 비교해서 최신 버젼을 알림.
 * 참고 링크: http://gun0912.tistory.com/8
 * 사용 라이브러리: Jsoup
 */

public class MarketVersionChecker {
    public static final String PACKAGE_NAME = "co.kr.hufstory";

    public void doMarketVersionTask(){
        new MarketVersionTask().execute();
    }

    // AsyncTask<Params,Progress,Result>
    private class MarketVersionTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            try {
                Document doc = Jsoup.connect(
                        "https://play.google.com/store/apps/details?id="
                                + PACKAGE_NAME).get();
                Elements Version = doc.select(".content");
                System.out.println("doMarketVersionTask!!!");
                for (Element mElement : Version) {
                    if (mElement.attr("itemprop").equals("softwareVersion")) {
                        return mElement.text().trim();
                    }
                }

            } catch (IOException ex) {
                ex.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String market_version){

            //PlayStore에 있는 Hufstory 어플 버젼.
            String store_version = market_version;

            //사용자 디바이스에 설치되어 있는 Hufstory 어플 버젼.
            String device_version = MainActivity.getDeviceVersion();
            //System.out.println("store_version: " + store_version + ", device_version: " + device_version);
            if(store_version.compareTo(device_version) > 0){
                MainActivity.setUpdateCheckTrue();
            }
        }
    }
}



