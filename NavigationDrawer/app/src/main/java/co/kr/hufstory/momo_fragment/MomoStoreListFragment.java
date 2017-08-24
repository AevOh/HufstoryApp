package co.kr.hufstory.momo_fragment;


import android.content.Context;
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
public class MomoStoreListFragment extends HufstoryFragment {
    public static final int STORE_RESTAURANT = 0;
    public static final int STORE_CONVENIENCE = 1;

    private MainActivity mActivity;
    private MomoMainFragment mMainFragment;
    private int mStore;

    public MomoStoreListFragment() {
        // Required empty public constructor
    }

    public MomoStoreListFragment setMainFragment(MomoMainFragment mainFragment) {
        mMainFragment = mainFragment;
        return this;
    }

    public MomoStoreListFragment setStore(int store) {
        mStore = store;
        return this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_momo_strore_list, container, false);
        v.findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showStoreInfo(0);
            }
        });
        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mActivity = (MainActivity)context;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    /**
     * storeInfoFragment를 보여준다.
     * @param storeId 원하는 store의 id, 서버로부터 가져온다.
     */
    public void showStoreInfo(int storeId) {
        MomoStoreInfoFragment storeInfoFragment = new MomoStoreInfoFragment()
                .setStoreListFragment(this)
                .setStoreId(storeId);
        mActivity.contentFragmentTransaction(MainActivity.FRAGMENT_LAYOUT, storeInfoFragment);
    }

    @Override
    public void backKeyAction(MainActivity activity) {
        activity.contentFragmentTransaction(MainActivity.FRAGMENT_LAYOUT, mMainFragment);
    }

}
