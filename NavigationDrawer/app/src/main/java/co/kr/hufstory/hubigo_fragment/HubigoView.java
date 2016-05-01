package co.kr.hufstory.hubigo_fragment;

import java.util.List;

/**
 * Created by Hyeong Wook on 2016-05-02.
 */
public interface HubigoView extends MvpView {

    void showSimpleNodeList(List<HubigoSimpleNode> nodeList);

    void showDetailNode();
}
