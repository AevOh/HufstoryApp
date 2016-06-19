package co.kr.hufstory.hubigo_fragment;

import android.util.Log;
import android.view.View;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import co.kr.hufstory.R;
import co.kr.hufstory.main.MainActivity;
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

        mHubigoModel.addToggle("main");
        mHubigoModel.addToggle("bookmark");
        mHubigoModel.addToggle("write");
        mHubigoModel.addToggle("search");
    }

    @Override
    public void attachView(HubigoView view) {
        mHubigoView = view;
    }

    @Override
    public void detachView() {
        mHubigoView = null;
    }

    public void loadMainSimpleNodes(){
        mHubigoService.getListMainNodes(new Callback<List<JsonObject>>() {
            @Override
            public void success(List<JsonObject> JsonObjects, Response response) {
                mHubigoModel.getMainNodeList().clear();

                for (JsonObject node : JsonObjects)
                    mHubigoModel.addHubigoSimpleNode(jsonToHubigoSimpleNode(node));

                mHubigoModel.setNodeOn("main");
                mHubigoView.showSimpleNodeList(mHubigoModel.getMainNodeList());
                mHubigoView.scroll(0);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("fail", error.toString());
            }
        });
    }

    public void loadWrittenSimpleNodes(){
        JsonObject session = new JsonObject();
        session.addProperty("session_key", mHubigoModel.getUserSession());

        mHubigoService.getListWrittenNodes(session, new Callback<List<JsonObject>>() {
            @Override
            public void success(List<JsonObject> jsonObjects, Response response) {
                mHubigoModel.getMainNodeList().clear();

                for (JsonObject node : jsonObjects) {
                    JsonElement nodeElement = node.get("writtenEvaluation");

                    if (!nodeElement.isJsonNull())
                        mHubigoModel.addHubigoSimpleNode(jsonToHubigoSimpleNode(nodeElement.getAsJsonObject()));
                }

                mHubigoModel.setNodeOn("write");
                mHubigoView.showSimpleNodeList(mHubigoModel.getMainNodeList());
                mHubigoView.scroll(0);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("writtenSimpleNode error", error.toString());
            }
        });
    }

    public void loadBookmarkSimpleNodes(){
        JsonObject session = new JsonObject();
        session.addProperty("session_key", mHubigoModel.getUserSession());

        mHubigoService.getListBookmarkNodes(session, new Callback<List<JsonObject>>() {
            @Override
            public void success(List<JsonObject> jsonObjects, Response response) {
                mHubigoModel.getMainNodeList().clear();

                for (JsonObject node : jsonObjects) {
                    JsonElement nodeElement = node.get("favoriteLecture");

                    if (!nodeElement.isJsonNull())
                        mHubigoModel.addHubigoSimpleNode(jsonToBookmarkSimpleNode(nodeElement.getAsJsonObject()));
                }

                mHubigoModel.setNodeOn("bookmark");
                mHubigoView.showSimpleNodeList(mHubigoModel.getMainNodeList());
                mHubigoView.scroll(0);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("load bookmarkNode error", error.toString());
            }
        });
    }

    public void loadSearchInfo(String keyword){
        if(keyword.length() < 2)
            mHubigoView.showErrorToast("2글자 미만으로 검색할 수 없습니다.");


    }

    public void loadLastMainNodes(){
        if(mHubigoModel.getMainNodeList().isEmpty())
            loadMainSimpleNodes();
        else if(!mHubigoModel.isAct())
            mHubigoView.showSimpleNodeList(mHubigoModel.getMainNodeList());
        else{
            if(mHubigoModel.nodeOn("main")) loadMainSimpleNodes();
            else if(mHubigoModel.nodeOn("bookmark")) loadBookmarkSimpleNodes();
            else if(mHubigoModel.nodeOn("write")) loadWrittenSimpleNodes();
        }
        mHubigoModel.setAct(false);
    }

    public void loadDetailNode(int id){
        mHubigoModel.setSelectLectureID(id);
        mHubigoView.showDetailNode();
    }

    public void userInfoChange(String cookies){
        String sessionID = getSessionID(cookies);

        if(sessionID != null){
            JsonObject session = new JsonObject();
            session.addProperty("session_key", sessionID);

            loadUserInfo(session);
        }
    }

    public void changeBookmark(View bookmarkView, boolean isSelect, int position){
        int lectureID = mHubigoModel.getMainNodeList().get(position).getId();

        JsonObject bookmarkInfo = new JsonObject();
        bookmarkInfo.addProperty("session_key", mHubigoModel.getUserSession());
        bookmarkInfo.addProperty("lecture_id", lectureID);

        if(isSelect)
            addBookmark(bookmarkInfo, position, lectureID);
        else
            removeBookmark(bookmarkInfo, position, lectureID);

        bookmarkView.setSelected(isSelect);
    }

    public boolean closeAction(MainActivity activity){
        if(!mHubigoModel.nodeOn("main")) {
            loadMainSimpleNodes();
            return false;
        }

        activity.getWebViewManager().returnLastWebView();
        return true;
    }

    private float divideFloat(float num, float den){
        if(den == 0)
            return -1;

        return Float.valueOf(String.format("%.2f",num / den));
    }

    private String getLimitSizedString(String str, int limitSize){
        if(str.length() > limitSize)
            return str.substring(0, limitSize - 1) + "...";

        return str;
    }

    private HubigoSimpleNode jsonToHubigoSimpleNode(JsonObject simpleNodeJson){
        JsonObject lectureJson = simpleNodeJson.getAsJsonObject("lecture");
        JsonObject professorJson = lectureJson.getAsJsonObject("professor");
        JsonObject majorJson = professorJson.getAsJsonObject("major");

        int lectureID = simpleNodeJson.get("lecture_id").getAsInt();
        float evaluation_count = lectureJson.get("evaluation_count").getAsFloat();
        float score_count = lectureJson.get("score_count").getAsFloat();
        float content_count = lectureJson.get("content_count").getAsFloat();

        return new HubigoSimpleNode(
                lectureID,
                lectureJson.get("name").getAsString(),
                professorJson.get("name").getAsString(),
                majorJson.get("name").getAsString(),
                getLimitSizedString(simpleNodeJson.get("comment").getAsString(), 100),
                divideFloat(score_count, evaluation_count),
                divideFloat(content_count, evaluation_count),
                mHubigoModel.isBookmarked(lectureID)
        );
    }

    private HubigoSimpleNode jsonToBookmarkSimpleNode(JsonObject bookmarkNodeJson){
        JsonElement evaluationElement = bookmarkNodeJson.get("lectureEvaluation");

        JsonObject professorJson = bookmarkNodeJson.getAsJsonObject("professor");
        JsonObject majorJson = professorJson.getAsJsonObject("major");

        int lectureID = bookmarkNodeJson.get("lecture_id").getAsInt();
        float evaluation_count = bookmarkNodeJson.get("evaluation_count").getAsFloat();
        float score_count = bookmarkNodeJson.get("score_count").getAsFloat();
        float content_count = bookmarkNodeJson.get("content_count").getAsFloat();
        String comment = "";

        if(!evaluationElement.isJsonNull())
            comment = evaluationElement.getAsJsonObject()
                    .get("comment").getAsString();

        return new HubigoSimpleNode(
                lectureID,
                bookmarkNodeJson.get("name").getAsString(),
                professorJson.get("name").getAsString(),
                majorJson.get("name").getAsString(),
                getLimitSizedString(comment, 100),
                divideFloat(score_count, evaluation_count),
                divideFloat(content_count, evaluation_count),
                mHubigoModel.isBookmarked(lectureID)
        );
    }

    private String getSessionID(String cookies){
        String[] cookie = cookies.split(";");
        String key = "PHPSESSID";

        for(String arg : cookie){
            if(arg.contains(key)){
                String[] cookieValue = arg.split("=");
                mHubigoModel.setUserSession(cookieValue[1]);
                return cookieValue[1];
            }
        }

        return null;
    }

    private void loadUserInfo(final JsonObject session){
        mHubigoService.getUserInfo(session, new Callback<UserInfo>() {
            @Override
            public void success(UserInfo userInfo, Response response) {
                if (userInfo == null) {
                    mHubigoView.showErrorToast("로그인이 필요합니다.");
                    mHubigoView.close();
                    return;
                }

                mHubigoModel.clear();
                mHubigoModel.setUserInfo(userInfo);
                mHubigoView.showToolbarButtons();

                if(userInfo.is_admin() == 'Y')
                    mHubigoView.showAdminButtons();
                else
                    mHubigoView.hideAdminButtons();

                loadUserHubigoInfo(session);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("loadBaseInfoError", error.toString());
            }
        });
    }

    private void loadUserHubigoInfo(JsonObject session){
        mHubigoService.getUserHubigoInfo(session, new Callback<List<UserHubigoInfo>>() {
            @Override
            public void success(List<UserHubigoInfo> userHubigoInfos, Response response) {
                for (UserHubigoInfo hubigoInfo : userHubigoInfos) {
                    if (hubigoInfo.getWritten_evaluation() != null)
                        mHubigoModel.addWrittenEvaluation(hubigoInfo.getWritten_evaluation());

                    if (hubigoInfo.getFavorite_lecture() != null) {
                        mHubigoModel.addBookmarkLecture(hubigoInfo.getFavorite_lecture());
                    }
                }

                increaseUserCount();
                loadMainSimpleNodes();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("loadHubigoInfo error", error.toString());
            }
        });
    }

    private void addBookmark(JsonObject bookmarkInfo, final int nodePos, final int lectureID){
        mHubigoService.addBookmark(bookmarkInfo, new Callback<String>() {
            @Override
            public void success(String s, Response response) {
                if (s.equals("success")) {
                    Log.i("success", "add");
                    mHubigoModel.getMainNodeList().get(nodePos).setBookmarked(true);
                    mHubigoModel.addBookmarkLecture(lectureID);
                    mHubigoView.showSimpleNodeList(mHubigoModel.getMainNodeList());
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("addBookmark error", error.toString());
            }
        });
    }

    private void removeBookmark(JsonObject bookmarkInfo, final int nodePos, final int lectureID){
        mHubigoService.removeBookmark(bookmarkInfo, new Callback<String>() {
            @Override
            public void success(String s, Response response) {
                if (s.equals("success")) {
                    Log.i("success", "remove");
                    mHubigoModel.getMainNodeList().get(nodePos).setBookmarked(false);
                    mHubigoModel.deleteBookmarkLecture(lectureID);
                    mHubigoView.showSimpleNodeList(mHubigoModel.getMainNodeList());
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("removeBookmarkError", error.toString());
            }
        });
    }

    private void increaseUserCount(){
        mHubigoService.increaseUserCount(new Callback<String>() {
            @Override
            public void success(String s, Response response) {
                if(s.equals("success"))
                    Log.i("user count", "방문자 수 증가");
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("user count error", error.toString());
            }
        });
    }
}
