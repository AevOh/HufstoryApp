package co.kr.hufstory.hubigo_fragment;

import android.util.Log;
import android.view.View;

import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.gson.JsonObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import co.kr.hufstory.Util.DateManager;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Hyeong Wook on 2016-06-20.
 */
public class HubigoStatusController implements Presenter<HubigoStatusFragment> {
    private static int DATA_PERIOD = 14;
    private String[]  day_of_week = {"일", "월", "화", "수", "목", "금", "토"};

    private HubigoStatusFragment mView;

    private RestAdapter mRestAdapter;
    private HubigoService mHubigoService;

    public HubigoStatusController(){
        mRestAdapter = new RestAdapter.Builder()
                .setEndpoint("http://hufstory.co.kr:5000")
                .build();

        mHubigoService = mRestAdapter.create(HubigoService.class);
    }

    @Override
    public void attachView(HubigoStatusFragment view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
    }

    public void loadStatusData(){
        int currentYear = DateManager.getCurrentDate("yyyy");
        int currentMonth = DateManager.getCurrentDate("MM") - 1;
        int currentDay = DateManager.getCurrentDate("dd");

        loadCountData();
        loadCategoryRankData();
        loadTopUsers(currentYear, currentMonth, currentDay, currentYear, currentMonth, currentDay, 20);
    }

    public void loadTopUsers(int startYear, int startMonth, int startDay,
                             int endYear, int endMonth, int endDay, int numOfUser){
        mHubigoService.getTopActiveWriter(startYear, startMonth, startDay, endYear, endMonth, endDay, numOfUser,
                new Callback<List<JsonObject>>() {
                    @Override
                    public void success(List<JsonObject> jsonObjects, Response response) {
                        int rank = 0;
                        List<UserWrittenInfo> newData = new ArrayList<>();
                        for(JsonObject obj : jsonObjects){
                            newData.add(new UserWrittenInfo(
                                    ++rank,
                                    obj.get("nick_name").getAsString(),
                                    obj.get("count").getAsInt()));
                        }

                        mView.showTopUserChart(newData);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.e("load top user error", error.toString());
                    }
                });
    }

    private void loadCountData(){
        mHubigoService.getAmountStatus(
                DateManager.getLastSunDayDate("yyyy"),
                DateManager.getLastSunDayDate("MM"),
                DateManager.getLastSunDayDate("dd"),
                DATA_PERIOD,

                new Callback<List<JsonObject>>() {
                    @Override
                    public void success(List<JsonObject> jsonObjects, Response response) {
                        ArrayList<Integer> visitData = new ArrayList<>();
                        ArrayList<Integer> writeData = new ArrayList<>();

                        for (JsonObject obj : jsonObjects) {
                            visitData.add(obj.get("user_count").getAsInt());
                            writeData.add(obj.get("write_count").getAsInt());
                        }

                        mView.showVisiterChart(createBarData(visitData, DATA_PERIOD));
                        mView.showWriteChart(createBarData(writeData, DATA_PERIOD));
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.e("status error", error.toString());
                    }
                });
    }

    private void loadCategoryRankData(){
        mHubigoService.getCategoryRank(new Callback<List<JsonObject>>() {
            @Override
            public void success(List<JsonObject> jsonObjects, Response response) {
                List<String> rankList = new ArrayList<>();

                for (JsonObject obj : jsonObjects) {
                    String rank = String.valueOf(rankList.size() + 1) + ". "
                            + obj.get("major_name").getAsString() + ": "
                            + obj.get("count").getAsString() + "개";

                    rankList.add(rank);
                }

                mView.showCategoryRank(rankList);
                mView.scroll(View.FOCUS_UP);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("category rank error", error.toString());
            }
        });
    }

    private BarData createBarData(ArrayList<Integer> dataSet, int period){
        BarDataSet lastWeek =
                HubigoChartManager.getBarChartDateSet("지난주", ColorTemplate.rgb("#61baf7"), dataSet.subList(0, period / 2));
        BarDataSet thisWeek =
                HubigoChartManager.getBarChartDateSet("이번주", ColorTemplate.rgb("#FFFFB04E"), dataSet.subList(period / 2, period));

        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(lastWeek);
        dataSets.add(thisWeek);

        return new BarData(day_of_week, dataSets);
    }
}
