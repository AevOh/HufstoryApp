package co.kr.hufstory.hubigo_fragment;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import co.kr.hufstory.R;
import co.kr.hufstory.Util.FocusOutInterrupt;
import co.kr.hufstory.main.HufstoryFragment;
import co.kr.hufstory.main.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailNodeFragment extends HufstoryFragment implements IDetailNodeView {
    private RDAdapter mRDAdapter;
    private DetailNodePresenter mPresenter;

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

    private LinearLayout mGradeButton;
    private LinearLayout mContentButton;
    private ImageView mGradeToggleButton;
    private ImageView mContentToggleButton;
    private EditText mWriteComment;
    private Button mWriteButton;

    public DetailNodeFragment() {
        // Required empty public constructor
        mPresenter = new DetailNodePresenter();
        mPresenter.attachView(this);

        mRDAdapter = new RDAdapter(this, mPresenter);
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

        HubigoChartManager.initialPieChart(mGradeChart);
        HubigoChartManager.initialPieChart(mContentChart);

        mGradeButton = (LinearLayout)rootView.findViewById(R.id.grade_button);
        mContentButton = (LinearLayout)rootView.findViewById(R.id.content_button);
        mGradeToggleButton = (ImageView)rootView.findViewById(R.id.grade_satis);
        mContentToggleButton = (ImageView)rootView.findViewById(R.id.content_satis);
        mGradeButton.setOnClickListener(new SatisFactionOnClickListener(mGradeToggleButton));
        mContentButton.setOnClickListener(new SatisFactionOnClickListener(mContentToggleButton));

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
        HubigoChartManager.setPieChartData(mGradeChart, detailInfo.getGradeSatisfaction(), ColorTemplate.rgb("#e7e7e7"));
        HubigoChartManager.setPieChartData(mContentChart, detailInfo.getContentSatisfaction(), ColorTemplate.rgb("#e7e7e7"));
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
        private HufstoryFragment mFragment;
        private DetailNodePresenter mPresenter;

        public RDAdapter(HufstoryFragment fragment, DetailNodePresenter presenter){
            mDataList = new ArrayList<>();
            mFragment = fragment;
            mPresenter = presenter;
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
            holder.mDeleteButton.setVisibility(nodeInfo.isWritten() ? View.VISIBLE : View.INVISIBLE);

            holder.mDeleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog deleteDialog  = getDeleteDialog(nodeInfo.getID());
                    deleteDialog.show();
                }
            });
        }

        private AlertDialog getDeleteDialog(final int evaluationID){
            AlertDialog.Builder builder = new AlertDialog.Builder(mFragment.getActivity());
            builder.setTitle("삭제");
            builder.setMessage("정말로 삭제하시겠습니까?");
            builder.setCancelable(true);

            builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mPresenter.deleteEvaluation(evaluationID);
                }
            });

            builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Log.i("삭제 취소", "cancel");
                }
            });

            return builder.create();
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
            protected TextView mDeleteButton;

            public EvaluationViewHolder(View v){
                super(v);
                mNickName = (TextView)v.findViewById(R.id.nickname);
                mDate = (TextView)v.findViewById(R.id.date);
                mGradeSatis = (ImageView)v.findViewById(R.id.grade_satis);
                mContentSatis = (ImageView)v.findViewById(R.id.content_satis);
                mComment = (TextView)v.findViewById(R.id.comment);
                mDeleteButton = (TextView)v.findViewById(R.id.delete_button);
            }
        }
    }

    private class SatisFactionOnClickListener implements View.OnClickListener{
        private View selectView;

        public SatisFactionOnClickListener(View v){
            selectView = v;
        }

        @Override
        public void onClick(View v) {
            selectView.setSelected(!selectView.isSelected());
        }
    }
}
