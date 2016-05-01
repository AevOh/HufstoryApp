package co.kr.hufstory.hubigo_fragment;

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

    }

    public void loadDetailNodes(){

    }
}
