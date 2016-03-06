package co.kr.hufstory.menu_fragment;

import android.support.v4.app.Fragment;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import co.kr.hufstory.menu_communication.Menu;
import co.kr.hufstory.menu_communication.MenuInfo;

/**
 * Created by Hyeong Wook on 2016-02-18.
 */
public class HCANetworkModule{
    private List<Menu> mDataList;
    private MenuInfo mMenuInfo;
    private MenuFragment.SectionsPagerAdapter mSectionsPagerAdapter;

    public HCANetworkModule(MenuFragment.SectionsPagerAdapter sectionsPagerAdapter){
        mSectionsPagerAdapter = sectionsPagerAdapter;
        mDataList = new ArrayList<>();
        mMenuInfo = new MenuInfo(this);
        mMenuInfo.pullMenu();
    }

    private String parseDataContent(String content){
        String[] contents = content.split(",");
        String parseContent = "";

        for(String food : contents)
            parseContent += food + "\n";

        parseContent = parseContent.substring(0, parseContent.length() - 1);

        return parseContent;
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
            fragment.addCard(menu.getCaf(), menu.getTime(), String.valueOf(menu.getCost()) ,parseDataContent(menu.getContent()));
    }

    public String getDate(int week){
        for(Menu menu : mDataList) {
            if (menu.getWeek() == week)
                return menu.getDate();
        }

        return "";
    }

    public void networkSuccessTrigger(){
        mDataList = mMenuInfo.getMenuList();
        mSectionsPagerAdapter.refresh();
    }
}