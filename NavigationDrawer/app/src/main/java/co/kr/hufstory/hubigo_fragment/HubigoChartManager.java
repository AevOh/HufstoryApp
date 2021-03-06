package co.kr.hufstory.hubigo_fragment;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hyeong Wook on 2016-05-31.
 */
abstract public class HubigoChartManager {
    private HubigoChartManager(){}

    static public void initialPieChart(PieChart pieChart){
        pieChart.setHoleRadius(80f);
        pieChart.setRotationAngle(-90f);
        pieChart.setDescription("");
        pieChart.setHoleColor(ColorTemplate.getColorWithAlphaComponent(ColorTemplate.rgb("#000000"), 0));
        pieChart.setDrawCenterText(true);
        pieChart.setTouchEnabled(false);
        pieChart.setDragDecelerationEnabled(false);
        pieChart.setHighlightPerTapEnabled(false);
        pieChart.getLegend().setEnabled(false);
    }

    static public void setPieChartData(PieChart pieChart, float satisfaction, int centerTextColor){
        ArrayList<Entry> entries = new ArrayList<>();
        if(satisfaction >= 0) {
            entries.add(new Entry(satisfaction, 0));
            entries.add(new Entry(1 - satisfaction, 1));
        }

        PieDataSet dataSet = new PieDataSet(entries, "");
        int[] colors = {ColorTemplate.rgb("#61baf7"), ColorTemplate.rgb("#282828")};
        dataSet.setColors(colors);
        dataSet.setDrawValues(false);

        ArrayList<String> labels = new ArrayList<>();
        for(int i = 0; i < entries.size(); i++)
            labels.add("");

        PieData data = new PieData(labels, dataSet);
        pieChart.setData(data);
        pieChart.setCenterText(satisfaction < 0 ? "강의평가 미존재" : String.valueOf((int) (satisfaction * 100)) + "%");
        pieChart.setCenterTextColor(centerTextColor);
        pieChart.notifyDataSetChanged();
        pieChart.invalidate();
        pieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
    }

    static public void initialBarChart(BarChart barChart){
        barChart.setDescription("");
    }

    static public BarDataSet getBarChartDateSet(String dataSetName, int color, List<Integer> values){
        ArrayList<BarEntry> datas = new ArrayList<>();

        for(int data : values)
            datas.add(new BarEntry(data, datas.size()));

        BarDataSet result = new BarDataSet(datas, dataSetName);
        result.setColor(color);

        return result;
    }
}
