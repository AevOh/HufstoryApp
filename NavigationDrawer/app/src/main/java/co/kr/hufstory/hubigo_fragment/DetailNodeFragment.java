package co.kr.hufstory.hubigo_fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;

import co.kr.hufstory.R;
import co.kr.hufstory.main.HufstoryFragment;
import co.kr.hufstory.main.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailNodeFragment extends HufstoryFragment {


    public DetailNodeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail_node, container, false);
    }

    @Override
    public void backKeyAction(MainActivity activity){
        activity.contentFragmentTransaction(R.id.content_frame, activity.getHubigoFragment());
    }

}
