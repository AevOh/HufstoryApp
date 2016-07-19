package co.kr.hufstory.hubigo_fragment;

import android.util.Log;
import android.view.View;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.List;

import co.kr.hufstory.Util.CookieParser;
import co.kr.hufstory.main.MainActivity;
import co.kr.hufstory.main.UserInfo;
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
                mHubigoModel.setNodeOn("main");
                mHubigoModel.getMainNodeList().clear();

                for (JsonObject node : JsonObjects)
                    mHubigoModel.addHubigoSimpleNode(jsonToHubigoSimpleNode(node));

                mHubigoView.showSimpleNodeList(mHubigoModel.getMainNodeList());
                mHubigoView.clearSearchBar();
                mHubigoView.scroll(0);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("load main fail", error.toString());
            }
        });
    }

    public void loadWrittenSimpleNodes(){
        JsonObject session = new JsonObject();
        session.addProperty("session_key", mHubigoModel.getUserSession());

        mHubigoService.getListWrittenNodes(session, new Callback<List<JsonObject>>() {
            @Override
            public void success(List<JsonObject> jsonObjects, Response response) {
                mHubigoModel.setNodeOn("write");
                mHubigoModel.getMainNodeList().clear();

                for (JsonObject node : jsonObjects) {
                    JsonElement nodeElement = node.get("writtenEvaluation");

                    if (!nodeElement.isJsonNull())
                        mHubigoModel.addHubigoSimpleNode(jsonToHubigoSimpleNode(nodeElement.getAsJsonObject()));
                }

                mHubigoView.showSimpleNodeList(mHubigoModel.getMainNodeList());
                mHubigoView.clearSearchBar();
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
                mHubigoModel.setNodeOn("bookmark");
                mHubigoModel.getMainNodeList().clear();

                for (JsonObject node : jsonObjects) {
                    JsonElement nodeElement = node.get("favoriteLecture");

                    if (!nodeElement.isJsonNull())
                        mHubigoModel.addHubigoSimpleNode(specialJsonToHubigoSimpleNode(nodeElement.getAsJsonObject()));
                }

                mHubigoView.showSimpleNodeList(mHubigoModel.getMainNodeList());
                mHubigoView.clearSearchBar();
                mHubigoView.scroll(0);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("load bookmarkNode error", error.toString());
            }
        });
    }

    public void loadSearchSimpleNodes(final String keyword){
        if(keyword.length() < 2)
            mHubigoView.showErrorToast("2글자 미만으로 검색할 수 없습니다.");

        mHubigoService.getListSearchNodes(keyword, new Callback<List<JsonObject>>() {
            @Override
            public void success(List<JsonObject> jsonObjects, Response response) {
                mHubigoModel.setNodeOn("search");
                mHubigoModel.getMainNodeList().clear();

                for (JsonObject node : jsonObjects)
                    mHubigoModel.addHubigoSimpleNode(specialJsonToHubigoSimpleNode(node));

                mHubigoModel.setCurrentKeyword(keyword);
                mHubigoView.showSimpleNodeList(mHubigoModel.getMainNodeList());
                mHubigoView.scroll(0);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("load searchNode error", error.toString());
            }
        });
    }

    public void loadLastMainNodes(){
        if(mHubigoModel.getMainNodeList().isEmpty())
            return;
        else if(!mHubigoModel.isAct())
            mHubigoView.showSimpleNodeList(mHubigoModel.getMainNodeList());
        else{
            if(mHubigoModel.nodeOn("main")) loadMainSimpleNodes();
            else if(mHubigoModel.nodeOn("bookmark")) loadBookmarkSimpleNodes();
            else if(mHubigoModel.nodeOn("write")) loadWrittenSimpleNodes();
            else if(mHubigoModel.nodeOn("search")) loadSearchSimpleNodes(mHubigoModel.getCurrentKeyword());
        }
        mHubigoModel.setAct(false);
    }

    public void loadDetailNode(int id){
        mHubigoModel.setSelectLectureID(id);
        mHubigoView.showDetailNode();
    }

    public void userInfoChange(String cookies){
        String sessionID = CookieParser.parse(cookies, MainActivity.COOKIE_SESSION);

        if(sessionID != null){
            mHubigoModel.setUserSession(sessionID);

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
                mHubigoModel.isBookmarked(lectureID),
                mHubigoModel.nodeOn("write")
        );
    }

    private HubigoSimpleNode specialJsonToHubigoSimpleNode(JsonObject nodeJson){
        JsonElement evaluationElement = nodeJson.get("lectureEvaluation");

        JsonObject professorJson = nodeJson.getAsJsonObject("professor");
        JsonObject majorJson = professorJson.getAsJsonObject("major");

        int lectureID = nodeJson.get("lecture_id").getAsInt();
        float evaluation_count = nodeJson.get("evaluation_count").getAsFloat();
        float score_count = nodeJson.get("score_count").getAsFloat();
        float content_count = nodeJson.get("content_count").getAsFloat();
        String comment = "";

        if(!evaluationElement.isJsonNull())
            comment = evaluationElement.getAsJsonObject()
                    .get("comment").getAsString();

        return new HubigoSimpleNode(
                lectureID,
                nodeJson.get("name").getAsString(),
                professorJson.get("name").getAsString(),
                majorJson.get("name").getAsString(),
                getLimitSizedString(comment, 100),
                divideFloat(score_count, evaluation_count),
                divideFloat(content_count, evaluation_count),
                mHubigoModel.isBookmarked(lectureID),
                mHubigoModel.nodeOn("write")
        );
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
                //mHubigoView.showToolbarButtons();

                if(userInfo.is_admin() == 'Y') {
                    mHubigoModel.setAdmin(true);
                    mHubigoView.showAdminButtons();
                }
                else {
                    mHubigoModel.setAdmin(false);
                    mHubigoView.hideAdminButtons();
                }

                loadUserHubigoInfo(session);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("loadBaseInfoError", error.toString());
                mHubigoView.showErrorToast("서버와의 통신이 원활하지 않습니다.");
                mHubigoView.close();
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
