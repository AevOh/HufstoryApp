package co.kr.hufstory.hubigo_fragment;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

/**
 * Created by Hyeong Wook on 2016-05-31.
 */
public final class HubigoPieChartManager {
    private HubigoPieChartManager(){}

    static public void initialPieChart(PieChart pieChart){
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

    static public void setPieChartData(PieChart pieChart, float satisfaction){
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
        pieChart.setCenterText(String.valueOf((int) (satisfaction * 100)) + "%");
    }
}
