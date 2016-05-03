package co.kr.hufstory.hubigo_fragment;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hyeong Wook on 2016-05-02.
 */
public class HubigoPresenter implements Presenter<HubigoView> {

    private HubigoView mHubigoView;

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

    private float divideFloat(float num, float den){
        return Float.valueOf(String.format("%.2f",num / den));
    }
}
