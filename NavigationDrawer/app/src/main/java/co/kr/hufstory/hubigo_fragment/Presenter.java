package co.kr.hufstory.hubigo_fragment;

/**
 * Created by Hyeong Wook on 2016-05-02.
 */
public interface Presenter<V> {

    void attachView(V view);

    void detachView();
}
