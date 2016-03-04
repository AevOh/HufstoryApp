package co.kr.hufstory.menu_communication;

import java.util.List;

/**
 * Created by Aev Oh on 2016-02-26.
 */

// 2016.03.03, Aev Oh, 수신 받는 값 예시: [{"building":"2","caf": "0", "time": "11:30 ~ 14:30", "cost":"2000","content":"db,c,s,a","mon":"3","day":"1"}]
public class MenuInfo {

    private List<ServerMenu> serverMenuList;
    private List<Menu> menuList;

    public boolean pullMenu(){
        MenusNetwork.pullMenus();

        long start_time = System.currentTimeMillis();;

        while(MenusNetwork.getMenu() == null || MenusNetwork.getMenu().isEmpty()){
            long current_time = System.currentTimeMillis();;
            long gap_time = (current_time - start_time) / 100; //gap_time 단위: 0.1초
            if(gap_time >= 5)   //0.5초 이상 반응이 없으면 강제 종료.
                return false;
        }
        serverMenuList = MenusNetwork.getMenu();
        serverToClientMenu();
        return true;
    }

    private void serverToClientMenu(){
        for(ServerMenu serverMenu: serverMenuList){
            Menu menu = new Menu();
            menu.setBuilding(serverMenu.getBuilding());
            menu.setCaf(getCafName(serverMenu.getCaf()));
            menu.setContent(serverMenu.getContent());
            menu.setCost(serverMenu.getCost());
            menu.setTime(getCafTime(serverMenu.getBuilding(), serverMenu.getCaf()));
            menu.setDate(getDate(serverMenu.getMon(), serverMenu.getDay()));
            menu.setWeek(getWeekDay(serverMenu.getMon(), serverMenu.getDay()));
        }
    }

    private String getCafName(int caf){
        Caf cafeteria = new Caf();
        return cafeteria.CAF_MAP.get(caf);
    }

    private String getCafTime(int building, int caf){
        CafTime cafTime = new CafTime();
        return cafTime.CAF_TIME_MAP.get(caf);
    }

    private String getDate(int mon, int day){
        String date = "" + mon + "/" + day;
        return date;
    }

    private String getWeekDay(int mon, int day){
        String weekDay = "-1";
        //여기부터 시작.

        return weekDay;
    }

    public List<Menu> getMenuList(){
        return menuList;
    }

    public void test(){
        MenuInfo menuInfo = new MenuInfo();
        if(menuInfo.pullMenu()){
            List<Menu> menuList = menuInfo.getMenuList();
            for(Menu menu: menuList){
                int building = menu.getBuilding();
                String cafType = menu.getCaf();
                int cost = menu.getCost();
                String content = menu.getContent();
                String date = menu.getDate();
                String weekDay = menu.getWeek();
                String time = menu.getTime();
            }
        }

    }
}

