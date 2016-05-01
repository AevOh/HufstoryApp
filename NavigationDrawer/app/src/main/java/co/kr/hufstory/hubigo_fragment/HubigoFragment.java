package co.kr.hufstory.hubigo_fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import co.kr.hufstory.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * create an instance of this fragment.
 */
public class HubigoFragment extends Fragment implements  HubigoView {
    private HubigoPresenter mPresenter;

    public HubigoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPresenter = new HubigoPresenter();
        mPresenter.attachView(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_hubigo, container, false);
    }

    @Override
    public Context getContext(){
        return this.getContext();
    }

    @Override
    public void showSimpleNodeList(List<HubigoSimpleNode> nodeList) {
        for(HubigoSimpleNode node : nodeList){

        }
    }

    @Override
    public void showDetailNode() {

    }


}
