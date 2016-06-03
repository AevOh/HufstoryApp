package co.kr.hufstory.hubigo_fragment;

import android.app.FragmentTransaction;
import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import co.kr.hufstory.R;
import co.kr.hufstory.main.HufstoryFragment;
import co.kr.hufstory.main.MainActivity;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * create an instance of this fragment.
 */
public class HubigoFragment extends HufstoryFragment implements  HubigoView {
    private MainActivity mActivity;
    private DetailNodeFragment mDetailFragment;

    private HubigoPresenter mPresenter;
    private RHAdapter mRHAdapter;
    private RecyclerView mRecyclerView;

    private View mUserButtonView;
    private CoordinatorLayout mToolbarLayout;
    private ImageButton mStatusButton;
    private ImageButton mWrittenButton;
    private ImageButton mBookmarkButton;

    private EditText mSearchBar;
    private ImageView mSearchButton;

    public HubigoFragment() {
        mDetailFragment = new DetailNodeFragment();

        mPresenter = new HubigoPresenter();
        mPresenter.attachView(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        showToolbarButtons();

        mSearchBar = (EditText)rootView.findViewById(R.id.search_bar);
        mSearchBar.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_ENTER) {
                    mPresenter.loadMainSimpleNodes();
                    return true;
                }
                return false;
            }
        });
        mSearchButton = (ImageView)rootView.findViewById(R.id.search_button);
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.loadMainSimpleNodes();
            }
        });

        mRHAdapter = new RHAdapter(mPresenter);

        mRecyclerView = (RecyclerView)rootView.findViewById(R.id.hubigoSimpleNodeList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mRHAdapter);

        mPresenter.loadMainSimpleNodes();

        return rootView;
    }

    @Override
    public Context getContext(){
        return this.getContext();
    }

    @Override
    public void showSimpleNodeList(List<HubigoSimpleNode> nodeList) {
        mRHAdapter.dataSetChange(nodeList);
        mSearchBar.clearFocus();

        InputMethodManager imm= (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mSearchBar.getWindowToken(), 0);
    }

    @Override
    public void showDetailNode() {
        mActivity.contentFragmentTransaction(MainActivity.FRAGMENT_LAYOUT, mDetailFragment, R.anim.fade_in, R.anim.fade_out);
    }

    @Override
    public void showErrorToast(String message){
        Toast.makeText(getActivity().getBaseContext(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showToolbarButtons(){
        if(mStatusButton == null)
            initialUserButtons();

        mToolbarLayout.removeAllViews();
        mToolbarLayout.addView(mUserButtonView);
    }

    @Override
    public void showAdminButtons(){
        mStatusButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideAdminButtons(){
        mStatusButton.setVisibility(View.INVISIBLE);
    }

    @Override
    public void scroll(int position){
        mRecyclerView.scrollToPosition(position);
    }

    @Override
    public void close(){
        this.backKeyAction(mActivity);
    }

    @Override
    public void backKeyAction(MainActivity activity){
        super.backKeyAction(activity);
        mToolbarLayout.removeView(mUserButtonView);
    }

    public void attachActivity(MainActivity activity){
        mActivity = activity;
    }

    public void cookieChange(String cookies){
        mPresenter.userInfoChange(cookies);
    }

    private void initialUserButtons(){
        mUserButtonView = mActivity.getLayoutInflater().inflate(R.layout.hubigo_user_buttons, null);
        mToolbarLayout = (CoordinatorLayout)mActivity.getToolbar().findViewById(R.id.toolbarLayout);
        mStatusButton = (ImageButton)mUserButtonView.findViewById(R.id.hubigoStatsButton);
        mWrittenButton = (ImageButton)mUserButtonView.findViewById(R.id.hubigoWrittenButton);
        mBookmarkButton = (ImageButton)mUserButtonView.findViewById(R.id.hubigoBookmarkButton);

        mStatusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mWrittenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.loadWrittenSimpleNodes();
            }
        });
        mBookmarkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.loadBookmarkSimpleNodes();
            }
        });
    }

    private static class RHAdapter extends RecyclerView.Adapter<RHAdapter.HubigoNodeViewHolder>{
        private List<HubigoSimpleNode> mDataList;
        private HubigoPresenter mPresenter;

        public RHAdapter(HubigoPresenter presenter){
            mDataList = new ArrayList<>();
            mPresenter = presenter;
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
        public void onBindViewHolder(HubigoNodeViewHolder holder, final int position) {
            final HubigoSimpleNode nodeInfo = mDataList.get(position);

            holder.mLecture.setText(nodeInfo.getLecture());
            holder.mProfessor.setText(nodeInfo.getProfessor());
            holder.mMajor.setText(nodeInfo.getMajor());
            holder.mRecentEvaluation.setText(nodeInfo.getRecentEvaluation());
            HubigoPieChartManager.setPieChartData(holder.mGradeChart, nodeInfo.getGradeSatisfaction());
            HubigoPieChartManager.setPieChartData(holder.mContentChart, nodeInfo.getContentSatisfaction());
            holder.mBookmark.setSelected(nodeInfo.isBookmarked());

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPresenter.loadDetailNode(nodeInfo.getId());
                }
            });

            holder.mBookmark.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPresenter.changeBookmark(v, !v.isSelected(), position);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mDataList.size();
        }

        public class HubigoNodeViewHolder extends RecyclerView.ViewHolder{
            protected View mView;
            protected TextView mLecture;
            protected TextView mProfessor;
            protected TextView mMajor;
            protected TextView mRecentEvaluation;
            protected PieChart mGradeChart;
            protected PieChart mContentChart;
            protected ImageView mBookmark;

            public HubigoNodeViewHolder(View v){
                super(v);
                mView = v;
                mLecture = (TextView) v.findViewById(R.id.lecture);
                mProfessor = (TextView) v.findViewById(R.id.professor);
                mMajor = (TextView) v.findViewById(R.id.major);
                mRecentEvaluation = (TextView) v.findViewById(R.id.recent_evaluation);
                mGradeChart = (PieChart) v.findViewById(R.id.grade_chart);
                mContentChart = (PieChart) v.findViewById(R.id.content_chart);
                mBookmark = (ImageView) v.findViewById(R.id.bookmark);

                HubigoPieChartManager.initialPieChart(mGradeChart);
                HubigoPieChartManager.initialPieChart(mContentChart);
            }
        }
    }
}
