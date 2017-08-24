package co.kr.hufstory.momo_fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import co.kr.hufstory.R;
import co.kr.hufstory.main.HufstoryFragment;
import co.kr.hufstory.main.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class MomoStoreInfoFragment extends HufstoryFragment {
    private MomoStoreListFragment mStoreListFragment;
    int mStoreId;

    public MomoStoreInfoFragment() {
        // Required empty public constructor
    }

    public MomoStoreInfoFragment setStoreListFragment(MomoStoreListFragment storeListFragment) {
        mStoreListFragment = storeListFragment;
        return this;
    }

    public MomoStoreInfoFragment setStoreId(int storeId) {
        mStoreId = storeId;
        return this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_momo_store_info, container, false);
    }

    @Override
    public void backKeyAction(MainActivity activity) {
        activity.contentFragmentTransaction(MainActivity.FRAGMENT_LAYOUT, mStoreListFragment);
    }

}
