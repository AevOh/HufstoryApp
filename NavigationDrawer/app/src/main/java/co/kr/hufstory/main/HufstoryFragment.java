package co.kr.hufstory.main;

import android.app.Fragment;
import android.support.v4.content.ContextCompat;

import co.kr.hufstory.R;

/**
 * Created by Hyeong Wook on 2016-05-28.
 */
abstract public class HufstoryFragment extends Fragment {
    public void backKeyAction(MainActivity activity){
        activity.getWebViewManager().returnLastWebView();
        activity.getToolbar().setBackgroundColor(ContextCompat.getColor(activity.getApplicationContext(), R.color.hufstory_color));
    }
}
