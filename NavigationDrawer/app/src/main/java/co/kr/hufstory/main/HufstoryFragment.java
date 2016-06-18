package co.kr.hufstory.main;

import android.app.Fragment;

/**
 * Created by Hyeong Wook on 2016-05-28.
 */
abstract public class HufstoryFragment extends Fragment {
    public void backKeyAction(MainActivity activity){
        activity.getWebViewManager().returnLastWebView();
    }

    public void startAction(MainActivity activity){

    }
}
