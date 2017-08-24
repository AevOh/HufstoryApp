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
public class MomoMainFragment extends HufstoryFragment {
    private MainActivity mActivity;

    public MomoMainFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_momo_main, container, false);
        v.findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showStoreList(MomoStoreListFragment.STORE_RESTAURANT);
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

    public void showStoreList(int store) {
        MomoStoreListFragment momoStoreListFragment = new MomoStoreListFragment()
                .setMainFragment(this)
                .setStore(store);
        mActivity.contentFragmentTransaction(MainActivity.FRAGMENT_LAYOUT, momoStoreListFragment);
    }
}
