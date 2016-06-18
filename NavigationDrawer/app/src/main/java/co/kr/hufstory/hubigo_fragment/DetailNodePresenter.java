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
                Log.i("success", "loadDetail");
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("load detail node fail", error.toString());
            }
        });
    }

    public void registWriting(boolean gradeSatis, boolean contentSatis, String comment){
        List<EvaluationInfo> list = new ArrayList<>();
        list.add(new EvaluationInfo("훕스토리", "2015-01-01", true, false,
                "정말정말 머단했어요. 아주 신나고 유익한 수업이었죠. 정말정말 머단했어요. 아주 신나고 유익한 수업이었죠. 정말정말 머단했어요. 아주 신나고 유익한 수업이었죠."));
        list.add(new EvaluationInfo("훕스토리", "2015-01-01", false, false,
                "정말정말 머단했어요. 아주 신나고 유익한 수업이었죠. 정말정말 머단했어요. 아주 신나고 유익한 수업이었죠. 정말정말 머단했어요. 아주 신나고 유익한 수업이었죠."));
        list.add(new EvaluationInfo("훕스토리", "2015-01-01", false, true,
                "정말정말 머단했어요. 아주 신나고 유익한 수업이었죠. 정말정말 머단했어요. 아주 신나고 유익한 수업이었죠. 정말정말 머단했어요. 아주 신나고 유익한 수업이었죠."));
        list.add(new EvaluationInfo("훕스토리", "2015-01-01", true, true,
                "정말정말 머단했어요. 아주 신나고 유익한 수업이었죠. 정말정말 머단했어요. 아주 신나고 유익한 수업이었죠. 정말정말 머단했어요. 아주 신나고 유익한 수업이었죠."));

        HubigoDetailInfo info = new HubigoDetailInfo(
                "정보처리산업기사",
                "김찬규",
                "컴퓨터공학과",
                3, 2, 3,
                (float)0.75,
                (float)1,
                10,
                list
        );

        mView.show(info);
    }

}
