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
        List<HubigoSimpleNode> dataList = new ArrayList<>();
        dataList.add(new HubigoSimpleNode(0, "자료구조", "김희철", "컴퓨터공학과", "씐난다", divideFloat(8,13), divideFloat(6,13)));
        dataList.add(new HubigoSimpleNode(1, "알고리즘", "김희철", "컴퓨터공학과", "씐난다", divideFloat(5, 13), divideFloat(10, 13)));
        dataList.add(new HubigoSimpleNode(2, "알고리즘", "김희철", "컴퓨터공학과", "씐난다", divideFloat(5, 13), divideFloat(10, 13)));

        mHubigoView.showSimpleNodeList(dataList);
    }

    public void loadDetailNode(int id){
        Log.i("id", String.valueOf(id));
        mHubigoView.showDetailNode();
    }

    public void loadRecentLectures(){
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

    private float divideFloat(float num, float den){
        return Float.valueOf(String.format("%.2f",num / den));
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
                simpleNodeJson.get("comment").getAsString(),
                divideFloat(score_count, evaluation_count),
                divideFloat(content_count, evaluation_count)
        );
    }
}
