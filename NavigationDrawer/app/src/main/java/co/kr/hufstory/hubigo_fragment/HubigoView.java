package co.kr.hufstory.hubigo_fragment;

import java.util.List;

import co.kr.hufstory.main.MainActivity;

/**
 * Created by Hyeong Wook on 2016-05-02.
 */
public interface HubigoView extends MvpView {
    void showSimpleNodeList(List<HubigoSimpleNode> nodeList);

    void showDetailNode(int lectureID);

    void showErrorToast(String message);

    void showToolbarButtons();

    void showAdminButtons();

    void hideAdminButtons();

    void scroll(int position);

    void close();
}
