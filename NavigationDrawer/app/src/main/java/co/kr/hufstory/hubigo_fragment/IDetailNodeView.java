package co.kr.hufstory.hubigo_fragment;

/**
 * Created by Hyeong Wook on 2016-06-19.
 */
public interface IDetailNodeView extends MvpView {
    void show(HubigoDetailInfo detailInfo);

    void showErrorToast(String message);

    void scroll(int position);
}
