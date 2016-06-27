package co.kr.hufstory.hubigo_fragment;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import co.kr.hufstory.R;
import co.kr.hufstory.Util.DateManager;
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

    private Button mStartDate;
    private Button mEndDate;
    private Button mNumOfUser;
    private RecyclerView mUserRankList;
    private RUWAdapter mRUWAdapter;

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

        mStartDate = (Button)rootView.findViewById(R.id.start_date);
        mStartDate.setText(String.format("%d-%d-%d",
                DateManager.getCurrentDate("yyyy"), DateManager.getCurrentDate("MM"), DateManager.getCurrentDate("dd")));
        mStartDate.setOnClickListener(new OnDatePickerListener(mStartDate));

        mEndDate = (Button)rootView.findViewById(R.id.end_date);
        mEndDate.setText(String.format("%d-%d-%d",
                DateManager.getCurrentDate("yyyy"), DateManager.getCurrentDate("MM"), DateManager.getCurrentDate("dd")));
        mEndDate.setOnClickListener(new OnDatePickerListener(mEndDate));

        mNumOfUser = (Button)rootView.findViewById(R.id.num_of_user);
        mNumOfUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInputTextDialog(mNumOfUser);
            }
        });

        mUserRankList = (RecyclerView)rootView.findViewById(R.id.user_rank_list);
        mRUWAdapter = new RUWAdapter();

        mUserRankList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mUserRankList.setAdapter(mRUWAdapter);

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

    public void showTopUserChart(List<UserWrittenInfo> info){
        mRUWAdapter.dataSetChange(info);
    }

    public void showErrorToast(String message){
        Toast.makeText(getActivity().getBaseContext(), message, Toast.LENGTH_LONG).show();
    }

    private void showBarChart(BarChart chart, BarData data){
        chart.setData(data);

        chart.notifyDataSetChanged();
        chart.invalidate();
    }

    private void showInputTextDialog(final Button btn){
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setTitle("입력");
        alert.setMessage("범위를 입력해 주십시오.");

        final EditText input = new EditText(getActivity());
        alert.setView(input);

        alert.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    Integer.valueOf(input.getText().toString());
                } catch (NumberFormatException e) {
                    showErrorToast("숫자를 입력해 주십시오.");
                    return;
                }

                btn.setText(input.getText());
                requestTopUserInfo();
            }
        });

        alert.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.i("input text", "cancel");
            }
        });
        alert.show();
    }

    private void requestTopUserInfo(){
        int[] start = getSelectDate(String.valueOf(mStartDate.getText()));
        int[] end = getSelectDate(String.valueOf(mEndDate.getText()));
        int numOfUser = Integer.valueOf(String.valueOf(mNumOfUser.getText()));

        mController.loadTopUsers(start[0], start[1], start[2], end[0], end[1], end[2], numOfUser);
    }


    private int[] getSelectDate(String str){
        String[] dateString = str.split("-");
        int[] date = {
                Integer.valueOf(dateString[0]),
                Integer.valueOf(dateString[1]),
                Integer.valueOf(dateString[2])};

        return date;
    }

    private class OnDatePickerListener implements View.OnClickListener{
        private Button btn;
        public OnDatePickerListener(Button btn){
            this.btn = btn;
        }

        @Override
        public void onClick(View v) {
            new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    btn.setText(String.format("%d-%d-%d", year, monthOfYear + 1, dayOfMonth));
                    requestTopUserInfo();
                }
            }, DateManager.getCurrentDate("yyyy"),
                    DateManager.getCurrentDate("MM") - 1,
                    DateManager.getCurrentDate("dd")).show();
        }
    }

    private static class RUWAdapter extends RecyclerView.Adapter<RUWAdapter.UserWrittenViewHolder>{
        private List<UserWrittenInfo> mDataList;

        public RUWAdapter(){
            mDataList = new ArrayList<>();
        }

        public void dataSetChange(List<UserWrittenInfo> dataList){
            mDataList.clear();
            mDataList.addAll(dataList);
            notifyDataSetChanged();
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
        }

        @Override
        public UserWrittenViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.
                    from(parent.getContext()).
                    inflate(R.layout.hubigo_user_written_cardview, parent, false);

            return new UserWrittenViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(UserWrittenViewHolder holder, final int position) {
            final UserWrittenInfo info = mDataList.get(position);
            holder.mRank.setText(String.valueOf(info.getRank()));
            holder.mNickName.setText(info.getNickName());
            holder.mCount.setText(String.valueOf(info.getCount()) + "개");
        }

        @Override
        public int getItemCount() {
            return mDataList.size();
        }

        public class UserWrittenViewHolder extends RecyclerView.ViewHolder{
            protected TextView mRank;
            protected TextView mNickName;
            protected TextView mCount;

            public UserWrittenViewHolder(View v){
                super(v);
                mRank = (TextView)v.findViewById(R.id.rank);
                mNickName = (TextView)v.findViewById(R.id.nick_name);
                mCount = (TextView)v.findViewById(R.id.count);
            }
        }
    }
}
