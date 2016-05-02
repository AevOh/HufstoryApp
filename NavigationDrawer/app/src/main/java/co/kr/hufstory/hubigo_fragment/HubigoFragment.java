package co.kr.hufstory.hubigo_fragment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
//import android.support.v4.app.Fragment;
import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
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
    private RHAdapter mRHAdapter;
    private RecyclerView mRecyclerView;

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
    public void onDestroy(){
        mPresenter.detachView();
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_hubigo, container, false);

        mRHAdapter = RHAdapter.getInstance();

        mRecyclerView = (RecyclerView)rootView.findViewById(R.id.hubigoSimpleNodeList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mRHAdapter);

        mPresenter.loadSimpleNodes();

        return rootView;
    }

    @Override
    public Context getContext(){
        return this.getContext();
    }

    @Override
    public void showSimpleNodeList(List<HubigoSimpleNode> nodeList) {
        mRHAdapter.dataSetChange(nodeList);
    }

    @Override
    public void showDetailNode() {

    }

    private static class RHAdapter extends RecyclerView.Adapter<RHAdapter.HubigoNodeViewHolder>{
        private List<HubigoSimpleNode> mDataList;

        static private RHAdapter mInstance = null;

        static public RHAdapter getInstance(){
            if(mInstance == null)
                mInstance = new RHAdapter((List)new ArrayList<>());

            return mInstance;
        }

        private RHAdapter(List<HubigoSimpleNode> dataList){
            mDataList = dataList;
        }

        public void dataSetChange(List<HubigoSimpleNode> dataList){
            mDataList.clear();
            mDataList.addAll(dataList);
            notifyDataSetChanged();
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
        }

        @Override
        public HubigoNodeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.
                    from(parent.getContext()).
                    inflate(R.layout.hubigo_simple_node_layout, parent, false);

            return new HubigoNodeViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(HubigoNodeViewHolder holder, int position) {
            HubigoSimpleNode nodeInfo = mDataList.get(position);
            holder.mLecture.setText(nodeInfo.getLecture());
            holder.mProfessor.setText(nodeInfo.getProfessor());
            holder.mMajor.setText(nodeInfo.getMajor());
            holder.mRecentEvaluation.setText(nodeInfo.getRecentEvaluation());
            holder.setPieChartData(holder.mGradeChart, nodeInfo.getGradeSatisfaction());
            holder.setPieChartData(holder.mContentChart, nodeInfo.getContentSatisfaction());
        }

        @Override
        public int getItemCount() {
            return mDataList.size();
        }

        public class HubigoNodeViewHolder extends RecyclerView.ViewHolder{
            protected TextView mLecture;
            protected TextView mProfessor;
            protected TextView mMajor;
            protected TextView mRecentEvaluation;
            protected PieChart mGradeChart;
            protected PieChart mContentChart;

            public HubigoNodeViewHolder(View v){
                super(v);
                mLecture = (TextView) v.findViewById(R.id.lecture);
                mProfessor = (TextView) v.findViewById(R.id.professor);
                mMajor = (TextView) v.findViewById(R.id.major);
                mRecentEvaluation = (TextView) v.findViewById(R.id.recent_evaluation);
                mGradeChart = (PieChart) v.findViewById(R.id.grade_chart);
                mContentChart = (PieChart) v.findViewById(R.id.content_chart);

                initialPieChart(mGradeChart);
                initialPieChart(mContentChart);
            }

            private void initialPieChart(PieChart pieChart){
                pieChart.setHoleRadius(80f);
                pieChart.setRotationAngle(-90f);
                pieChart.setDescription("");
                pieChart.setDrawCenterText(true);
                pieChart.setTouchEnabled(false);
                pieChart.setDragDecelerationEnabled(false);
                pieChart.setHighlightPerTapEnabled(false);
                pieChart.getLegend().setEnabled(false);

                pieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
            }

            private void setPieChartData(PieChart pieChart, float satisfaction){
                ArrayList<Entry> entries = new ArrayList<>();
                entries.add(new Entry(satisfaction, 0));
                entries.add(new Entry(1 - satisfaction, 1));

                PieDataSet dataSet = new PieDataSet(entries, "");
                int[] colors = {ColorTemplate.rgb("#61baf7"), ColorTemplate.rgb("#353535")};
                dataSet.setColors(colors);
                dataSet.setDrawValues(false);

                ArrayList<String> labels = new ArrayList<>();
                for(int i = 0; i < entries.size(); i++)
                    labels.add("");

                PieData data = new PieData(labels, dataSet);
                pieChart.setData(data);
                pieChart.setCenterText(String.valueOf((int)(satisfaction * 100)) + "%");
            }
        }
    }
}
