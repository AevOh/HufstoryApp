package co.kr.hufstory.menu_communication;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import co.kr.hufstory.menu_fragment.HCANetworkModule;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Aev Oh on 2016-02-26.
 */

// 2016.03.03, Aev Oh, 수신 받는 값 예시: [{"building":"2","caf": "0", "time": "11:30 ~ 14:30", "cost":"2000","content":"db,c,s,a","mon":"3","day":"1"}]
public class MenuInfo extends Thread{

    //http://hufstory.co.kr/cert/public/menu?start_date=7&end_date=12&mon=3
    private static final String MENU_URL = "http://hufstory.co.kr/cert/public/menu?";

    private List<ServerMenu> serverMenuList;
    private List<Menu> menuList = new ArrayList<>();
    public static boolean menuDownCheck;

    private HCANetworkModule networkModule;
    public MenuInfo(HCANetworkModule networkModule){
        this.networkModule = networkModule;
    }

    public void pullMenu(){
        menuDownCheck = false;
        System.out.println("pullMenu Start!!");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        int start_date = calendar.get(Calendar.DATE);
        int end_date = calendar.get(Calendar.DATE) + 6;
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR)%100;

        System.out.println("URL: "+MENU_URL+"start_date="+start_date+"&end_date="+ end_date+"&mon="+ month+"&year="+year);
        RestAdapter adapter = new RestAdapter.Builder().setEndpoint(MENU_URL+"start_date="+start_date+"&end_date="+ end_date+"&mon="+month+"&year="+year).build();
        MenusAPI api = adapter.create(MenusAPI.class);

        api.getMenus(new Callback<List<ServerMenu>>() {

            @Override
            public void success(List<ServerMenu> menus, Response response) {
                System.out.println("api.getMenus Success!!");
                serverMenuList = menus;
                serverToClientMenu();
                menuDownCheck = true;
                networkModule.networkSuccessTrigger();
            }

            @Override
            public void failure(RetrofitError error) {
                System.out.println("Failure!!");
                System.out.println("error :" + error);
                menuDownCheck = false;
            }
        });

        System.out.println("pullMenu End!!");
    }

    private void serverToClientMenu(){
        for(ServerMenu serverMenu: serverMenuList){
            Menu menu = new Menu();
            menu.setBuilding(getBuilding(serverMenu.getType()));
            menu.setCaf(getCafName(serverMenu.getType()));
            menu.setContent(serverMenu.getContent());
            menu.setCost(serverMenu.getCost());
            menu.setTime(getCafTime(serverMenu.getType()));
            menu.setDate(getDate(serverMenu.getMon(), serverMenu.getDay()));
            menu.setWeek(getWeekDay(serverMenu.getMon(), serverMenu.getDay()));
            menuList.add(menu);
        }

        for(Menu serverMenu: menuList){
            System.out.println("Type: " + serverMenu.getCaf());
            System.out.println("Content: " + serverMenu.getContent());
            System.out.println("Cost: " + serverMenu.getCost());
            System.out.println("Week: " + serverMenu.getWeek());
            System.out.println("Date: " + serverMenu.getDate());
            System.out.println("Building: " + serverMenu.getBuilding());
            System.out.println("Time: " + serverMenu.getTime());
            System.out.println();
        }
    }

    private int getBuilding(int cafType){
        int building = cafType/10;
        return building;
    }

    private String getCafName(int caf){
        Caf cafeteria = new Caf();
        return cafeteria.CAF_MAP.get(caf);
    }

    private String getCafTime(int caf){
        CafTime cafTime = new CafTime();
        return cafTime.CAF_TIME_MAP.get(caf);
    }

    private String getDate(int mon, int day){
        String date = "" + mon + "/" + day;
        return date;
    }

    private int getWeekDay(int mon, int day){
        int weekDay = 0;
        mon = mon - 1;  //0 = 1월, 1 = 2월
        Calendar cal= Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, mon);
        cal.set(Calendar.DATE, day);
        weekDay = cal.get(Calendar.DAY_OF_WEEK) - 1;    //일요일 = 1 -> 일요일 0, 월요일 = 1

        System.out.println("getWeekDay(): "+mon + " / " + day + " / " + year + " / " + weekDay);

        return weekDay;
    }

    public List<Menu> getMenuList(){
        return menuList;
    }
}

