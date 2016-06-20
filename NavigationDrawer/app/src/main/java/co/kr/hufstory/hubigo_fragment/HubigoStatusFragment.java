package co.kr.hufstory.hubigo_fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ScrollView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;

import java.util.List;

import co.kr.hufstory.R;
import co.kr.hufstory.main.HufstoryFragment;
import co.kr.hufstory.main.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class HubigoStatusFragment extends HufstoryFragment {
    private HubigoStatusController mController;

    private ScrollView mScrollView;
    private BarChart mVisiterChart;
    private BarChart mWriteChart;
    private ListView mCategoryRankList;
    private ArrayAdapter<String> mRankAdapter;

    public HubigoStatusFragment() {
        mController = new HubigoStatusController();
        mController.attachView(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_hubigo_status, container, false);

        mScrollView = (ScrollView)rootView.findViewById(R.id.scroll_view);

        mVisiterChart = (BarChart)rootView.findViewById(R.id.visiter_chart);
        HubigoChartManager.initialBarChart(mVisiterChart);
        mWriteChart = (BarChart)rootView.findViewById(R.id.write_chart);
        HubigoChartManager.initialBarChart(mWriteChart);

        mCategoryRankList = (ListView)rootView.findViewById(R.id.category_rank_list);
        mRankAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1);

        mCategoryRankList.setClickable(false);
        mCategoryRankList.setAdapter(mRankAdapter);

        mController.loadStatusData();

        return rootView;
    }

    @Override
    public void backKeyAction(MainActivity activity){
        activity.contentFragmentTransaction(R.id.content_frame, activity.getHubigoFragment(), R.anim.fade_in, R.anim.no_animation);
    }

    public void scroll(int position){
        mScrollView.fullScroll(position);
    }

    public void showCategoryRank(List<String> list){
        mRankAdapter.clear();

        for(String rank : list)
            mRankAdapter.add(rank);
    }

    public void showVisiterChart(BarData data){
        showBarChart(mVisiterChart, data);
    }

    public void showWriteChart(BarData data){
        showBarChart(mWriteChart, data);
    }

    private void showBarChart(BarChart chart, BarData data){
        chart.setData(data);

        chart.notifyDataSetChanged();
        chart.invalidate();
    }
}
