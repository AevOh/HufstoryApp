package co.kr.hufstory.login;

import android.os.AsyncTask;

import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Source;
import net.htmlparser.jericho.TextExtractor;

import org.jsoup.nodes.Element;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aev Oh on 2016-02-28.
 */
public class UserInfo {
    //http://hufstory.co.kr
    public String USERINFO_URL = "http://hufstory.co.kr/test.php";
    public void doUserInfo(){
        System.out.println("UserInfo Created!!");
        new UserInfoTask().execute();
    }

    // AsyncTask<Params,Progress,Result>
    private class UserInfoTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            System.out.println("UserInfoTask doInBackground!!");
            Source source = null;
            String get_data = "";
            ArrayList array = new ArrayList();

            URL url = null;
            try {
                url = new URL(USERINFO_URL);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            try {
                System.out.println("URL Try: " + url.toString());
                source = new Source(url);  // 쓰레드를 사용 안하면 여기에서 예외 발생함 그 이유는 아래에서 설명
                System.out.println("source new!!");
            } catch (IOException e) {
                System.out.println("e!!!!!!!!!!!");
                e.printStackTrace();
            }

            net.htmlparser.jericho.Element element = null;
            List<net.htmlparser.jericho.Element> list = source.getAllElements(HTMLElementName.DT); // title 태그의 엘리먼트 가져옴

            for(int i = 0; i < list.size(); i++){
                element = list.get(i);
                String attributevalue = element.getAttributeValue("type");  // title 태그의 속성값이 type을 찾는다
                if(attributevalue != null){
                    if(attributevalue.equalsIgnoreCase("text")){  // type의 값이 text 이면
                        TextExtractor textExtractor = element.getTextExtractor();  // 해당 문자값을 가져온다
                        get_data = textExtractor.toString();  // 가져온 값을 스트링으로 변환후
                        array.add(get_data);  // ArrayList에 추가한다
                    }
                }
            }

            for(Object one: array){
                System.out.println("Test: "+ one);
            }


            return null;
        }

        @Override
        protected void onPostExecute(String userInfo) {

        }
    }
}
