package co.kr.hufstory.hubigo_fragment;


import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import co.kr.hufstory.R;
import co.kr.hufstory.main.HufstoryFragment;
import co.kr.hufstory.main.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailNodeFragment extends HufstoryFragment implements IDetailNodeView {
    private RDAdapter mRDAdapter;
    private DetailNodePresenter mPresenter;
    private Bundle mBundle;

    private GridLayout mInfoLayout;
    private TextView mLecture;
    private TextView mProfessor;
    private TextView mMajor;
    private TextView mCredit;
    private TextView mGrade;
    private TextView mTime;
    private PieChart mGradeChart;
    private PieChart mContentChart;
    private TextView mEvaluationCount;
    private RecyclerView mRecyclerView;

    private ImageView mGradeToggleButton;
    private ImageView mContentToggleButton;
    private EditText mWriteComment;
    private Button mWriteButton;

    public DetailNodeFragment() {
        // Required empty public constructor
        mRDAdapter = new RDAdapter();
        mPresenter = new DetailNodePresenter();
        mPresenter.attachView(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_detail_node, container, false);

        mInfoLayout = (GridLayout)rootView.findViewById(R.id.detail_info);
        mLecture = (TextView)rootView.findViewById(R.id.lecture_name);
        mProfessor = (TextView)rootView.findViewById(R.id.professor);
        mMajor = (TextView)rootView.findViewById(R.id.major);
        mCredit = (TextView)rootView.findViewById(R.id.credit);
        mGrade = (TextView)rootView.findViewById(R.id.grade);
        mTime = (TextView)rootView.findViewById(R.id.time);
        mGradeChart = (PieChart) rootView.findViewById(R.id.grade_chart);
        mContentChart = (PieChart) rootView.findViewById(R.id.content_chart);
        mEvaluationCount = (TextView)rootView.findViewById(R.id.evaluation_number);

        HubigoPieChartManager.initialPieChart(mGradeChart);
        HubigoPieChartManager.initialPieChart(mContentChart);

        mGradeToggleButton = (ImageView)rootView.findViewById(R.id.grade_satis);
        mGradeToggleButton.setOnClickListener(new SatisFactionOnClickListener());
        mContentToggleButton = (ImageView)rootView.findViewById(R.id.content_satis);
        mContentToggleButton.setOnClickListener(new SatisFactionOnClickListener());

        mWriteComment = (EditText)rootView.findViewById(R.id.write_text);
        mWriteButton = (Button)rootView.findViewById(R.id.write_button);
        mWriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.registerEvaluation(
                        mGradeToggleButton.isSelected(), mContentToggleButton.isSelected(), mWriteComment.getText().toString());
            }
        });
        mInfoLayout.setOnClickListener(FocusOutInterrupt.getEditTextFocusOutClickListener(mWriteComment, getActivity()));

        mRecyclerView = (RecyclerView)rootView.findViewById(R.id.evaluation_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mRDAdapter);

        mPresenter.loadDetailInfo();

        return rootView;
    }

    @Override
    public void show(HubigoDetailInfo detailInfo) {
        mLecture.setText(detailInfo.getLecture());
        mProfessor.setText(detailInfo.getProfessor());
        mMajor.setText(detailInfo.getMajor());
        mCredit.setText(String.valueOf(detailInfo.getCredit()) + "학점");
        mGrade.setText(detailInfo.getGrade() + "학년");
        mTime.setText(String.valueOf(detailInfo.getTime()) + "시간");
        HubigoPieChartManager.setPieChartData(mGradeChart, detailInfo.getGradeSatisfaction(), ColorTemplate.rgb("#e7e7e7"));
        HubigoPieChartManager.setPieChartData(mContentChart, detailInfo.getContentSatisfaction(), ColorTemplate.rgb("#e7e7e7"));
        mEvaluationCount.setText(String.valueOf(detailInfo.getEvaluationCount()));

        mGradeToggleButton.setSelected(true);
        mContentToggleButton.setSelected(true);
        mWriteComment.setText("");

        mRDAdapter.dataSetChange(detailInfo.getEvaluationInfoList());
        FocusOutInterrupt.editText(mWriteComment, getActivity());
    }

    @Override
    public void showErrorToast(String message){
        Toast.makeText(getActivity().getBaseContext(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void scroll(int position){
        mRecyclerView.scrollToPosition(position);
    }

    @Override
    public void backKeyAction(MainActivity activity){
        mPresenter.loadMain(activity);
    }

    public void start(MainActivity activity){
        activity.getSupportActionBar().hide();
    }

    private static class RDAdapter extends RecyclerView.Adapter<RDAdapter.EvaluationViewHolder>{
        private List<EvaluationInfo> mDataList;

        public RDAdapter(){
            mDataList = new ArrayList<>();
        }

        public void dataSetChange(List<EvaluationInfo> dataList){
            mDataList.clear();
            mDataList.addAll(dataList);
            notifyDataSetChanged();
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
        }

        @Override
        public EvaluationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.
                    from(parent.getContext()).
                    inflate(R.layout.hubigo_evaluation_cardview, parent, false);

            return new EvaluationViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(EvaluationViewHolder holder, final int position) {
            final EvaluationInfo nodeInfo = mDataList.get(position);

            holder.mNickName.setText(nodeInfo.getNickName());
            holder.mDate.setText(nodeInfo.getDate());
            holder.mGradeSatis.setSelected(nodeInfo.isSatisGrade());
            holder.mContentSatis.setSelected(nodeInfo.isSatisContent());
            holder.mComment.setText(nodeInfo.getComment());
        }

        @Override
        public int getItemCount() {
            return mDataList.size();
        }

        public class EvaluationViewHolder extends RecyclerView.ViewHolder{
            protected TextView mNickName;
            protected TextView mDate;
            protected ImageView mGradeSatis;
            protected ImageView mContentSatis;
            protected TextView mComment;

            public EvaluationViewHolder(View v){
                super(v);
                mNickName = (TextView)v.findViewById(R.id.nickname);
                mDate = (TextView)v.findViewById(R.id.date);
                mGradeSatis = (ImageView)v.findViewById(R.id.grade_satis);
                mContentSatis = (ImageView)v.findViewById(R.id.content_satis);
                mComment = (TextView)v.findViewById(R.id.comment);
            }
        }
    }

    private class SatisFactionOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            v.setSelected(!v.isSelected());
        }
    }
}
