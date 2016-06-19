package co.kr.hufstory.hubigo_fragment;

import android.util.Log;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import co.kr.hufstory.R;
import co.kr.hufstory.main.MainActivity;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Hyeong Wook on 2016-06-19.
 */
public class DetailNodePresenter implements Presenter<IDetailNodeView> {
    private IDetailNodeView mView;
    private HubigoModel mModel;

    private RestAdapter mRestAdapter;
    private HubigoService mHubigoService;

    public DetailNodePresenter(){
        mModel = HubigoModel.getInstance();

        mRestAdapter = new RestAdapter.Builder()
                .setEndpoint("http://hufstory.co.kr:5000")
                .build();

        mHubigoService = mRestAdapter.create(HubigoService.class);
    }

    @Override
    public void attachView(IDetailNodeView view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
    }

    public void loadDetailInfo(){
        mHubigoService.getDetailNodeInfo(String.valueOf(mModel.getSelectLectureID()), new Callback<List<JsonObject>>() {
            @Override
            public void success(List<JsonObject> jsonObjects, Response response) {
                mView.show(jsonToDetailNodeInfo(jsonObjects));
                mView.scroll(0);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("load detail node fail", error.toString());
            }
        });
    }

    public void registerEvaluation(boolean gradeSatis, boolean contentSatis, String comment){
        if(comment.length() < 8) {
            mView.showErrorToast("8글자 미만으로는 작성할 수 없습니다.");
            return;
        }

        JsonObject writeInfo = new JsonObject();
        writeInfo.addProperty("session_key", mModel.getUserSession());
        writeInfo.addProperty("lecture_id", mModel.getSelectLectureID());
        writeInfo.addProperty("score_recommend", gradeSatis? 1 : 0);
        writeInfo.addProperty("contents_recommend", contentSatis? 1 : 0);
        writeInfo.addProperty("comment", comment);

        mHubigoService.registerEvaluation(writeInfo, new Callback<String>() {
            @Override
            public void success(String s, Response response) {
                if(s.equals("success"))
                    loadDetailInfo();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("register error", error.toString());
            }
        });
    }

    public void loadMain(MainActivity activity){
        activity.getSupportActionBar().show();
        activity.contentFragmentTransaction(R.id.content_frame, activity.getHubigoFragment(), R.anim.fade_in, R.anim.no_animation);
    }

    private HubigoDetailInfo jsonToDetailNodeInfo(List<JsonObject> jsonObjects){
        JsonObject lecture = jsonObjects.get(0);
        JsonElement evaluation = lecture.get("lectureEvaluation");
        List<EvaluationInfo> evaluationInfos = new ArrayList<>();

        JsonObject professor = lecture.getAsJsonObject("professor");
        JsonObject major = professor.getAsJsonObject("major");

        if(evaluation != null){
            for(JsonObject node : jsonObjects)
                evaluationInfos.add(
                        jsonToEvaluationInfo(node.getAsJsonObject("lectureEvaluation")));
        }

        return new HubigoDetailInfo(
                lecture.get("name").getAsString(),
                professor.get("name").getAsString(),
                major.get("name").getAsString(),
                lecture.get("credit").getAsInt(),
                lecture.get("grade").getAsInt(),
                lecture.get("time").getAsInt(),
                divideFloat(lecture.get("score_count").getAsInt(), lecture.get("evaluation_count").getAsInt()),
                divideFloat(lecture.get("content_count").getAsInt(), lecture.get("evaluation_count").getAsInt()),
                lecture.get("evaluation_count").getAsInt(),
                evaluationInfos
        );
    }

    private EvaluationInfo jsonToEvaluationInfo(JsonObject lectureEvaluation){
        JsonObject evaluationWriter = lectureEvaluation.getAsJsonObject("evaluationWriter");

        return new EvaluationInfo(
                evaluationWriter.get("nick_name").getAsString(),
                lectureEvaluation.get("updatedAt").getAsString().substring(0, 10),
                lectureEvaluation.get("score_recommend").getAsInt() != 0,
                lectureEvaluation.get("contents_recommend").getAsInt() != 0,
                lectureEvaluation.get("comment").getAsString()
        );
    }

    private float divideFloat(float num, float den){
        if(den == 0)
            return -1;

        return Float.valueOf(String.format("%.2f",num / den));
    }

}
