package co.kr.hufstory.hubigo_fragment;

import android.util.Log;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Hyeong Wook on 2016-05-02.
 */
public class HubigoPresenter implements Presenter<HubigoView> {
    private HubigoModel mHubigoModel;
    private HubigoView mHubigoView;

    private RestAdapter mRestAdapter;
    private HubigoService mHubigoService;

    public HubigoPresenter(){
        mHubigoModel = HubigoModel.getInstance();

        mRestAdapter = new RestAdapter.Builder()
                .setEndpoint("http://hufstory.co.kr:5000")
                .build();

        mHubigoService = mRestAdapter.create(HubigoService.class);
    }

    @Override
    public void attachView(HubigoView view) {
        mHubigoView = view;
    }

    @Override
    public void detachView() {
        mHubigoView = null;
    }

    public void loadSimpleNodes(){
        mHubigoService.getListMainNodes(new Callback<List<JsonObject>>() {
            @Override
            public void success(List<JsonObject> JsonObjects, Response response) {
                for (JsonObject node : JsonObjects)
                    mHubigoModel.addHubigoSimpleNode(JsonToHubigoSimpleNode(node));

                mHubigoView.showSimpleNodeList(mHubigoModel.getMainNodeList());
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("fail", error.toString());
            }
        });
    }

    public void loadDetailNode(int id){
        Log.i("id", String.valueOf(id));
        mHubigoView.showDetailNode();
    }

    public void userInfoChange(String cookies){
        String sessionID = getSessionID(cookies);

        if(sessionID != null){
            JsonObject session = new JsonObject();
            session.addProperty("session_key", sessionID);

            mHubigoService.getUserInfo(session, new Callback<UserInfo>() {
                @Override
                public void success(UserInfo userInfo, Response response) {
                    if(userInfo == null) {
                        mHubigoView.showErrorToast("로그인이 필요합니다.");
                        mHubigoView.close();
                        return;
                    }

                    mHubigoModel.setUserInfo(userInfo);
                    Log.i("userInfo", userInfo.getUser_id() +", " + userInfo.getNick_name() + ", " + String.valueOf(userInfo.is_admin() == 'Y'));
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.e("fail", error.toString());
                }
            });
        }
    }

    private float divideFloat(float num, float den){
        return Float.valueOf(String.format("%.2f",num / den));
    }

    private String StringLimitSizing(String str, int limitSize){
        if(str.length() > limitSize)
            return str.substring(0, limitSize - 1) + "...";

        return str;
    }

    private HubigoSimpleNode JsonToHubigoSimpleNode(JsonObject simpleNodeJson){
        JsonObject lectureJson = simpleNodeJson.getAsJsonObject("lecture");
        JsonObject professorJson = lectureJson.getAsJsonObject("professor");
        JsonObject majorJson = professorJson.getAsJsonObject("major");

        float evaluation_count = lectureJson.get("evaluation_count").getAsFloat();
        float score_count = lectureJson.get("score_count").getAsFloat();
        float content_count = lectureJson.get("content_count").getAsFloat();

        return new HubigoSimpleNode(
                simpleNodeJson.get("lecture_id").getAsInt(),
                lectureJson.get("name").getAsString(),
                professorJson.get("name").getAsString(),
                majorJson.get("name").getAsString(),
                StringLimitSizing(simpleNodeJson.get("comment").getAsString(), 100),
                divideFloat(score_count, evaluation_count),
                divideFloat(content_count, evaluation_count)
        );
    }

    private String getSessionID(String cookies){
        String[] cookie = cookies.split(";");
        String key = "PHPSESSID";

        for(String arg : cookie){
            if(arg.contains(key)){
                String[] cookieValue = arg.split("=");
                return cookieValue[1];
            }
        }

        return null;
    }
}
