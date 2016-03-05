package co.kr.hufstory.menu_fragment;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import co.kr.hufstory.menu_communication.Menu;
import co.kr.hufstory.menu_communication.MenuInfo;
import co.kr.hufstory.menu_communication.MenusNetwork;

/**
 * Created by Hyeong Wook on 2016-02-18.
 */
public class HCANetworkModule{


    /* 2016.02.26, Aev Oh, 테스트 용으로 수정함. */
    private List<Menu> mDataList;

    public HCANetworkModule(){
        MenuInfo menuInfo = new MenuInfo();
        if(menuInfo.pullMenu()) {
            Log.i("success", "good");
            mDataList = menuInfo.getMenuList();
            parseDataContents();
        } else
            mDataList = new ArrayList<>();
    }

    private void parseDataContents(){
        String content;
        for(Menu menu : mDataList){
            content = menu.getContent();
            String[] contents = content.split(",");

            for(String food : contents)
                content += food + "\n";

            menu.setContent(content);
        }
    }

    private List<Menu> getDataSet(int building, int week){
        List<Menu> dataSet = new ArrayList<>();

        for(Menu menu : mDataList){
            if(menu.getBuilding() == building && menu.getWeek() == week)
                dataSet.add(menu);
        }

        return dataSet;
    }

    public void getData(MenuFragment.PlaceholderFragment fragment, int selectedCafeteria, int sectionNumber){
        List<Menu> dataSet = getDataSet(selectedCafeteria + 1, sectionNumber + 1);

        for(Menu menu : dataSet)
            fragment.addCard(menu.getCaf(), menu.getTime(), String.valueOf(menu.getCost()) ,menu.getContent());
    }

    public String getDate(int week){
        for(Menu menu : mDataList) {
            if (menu.getWeek() == week)
                return menu.getDate();
        }

        return "";
    }
}