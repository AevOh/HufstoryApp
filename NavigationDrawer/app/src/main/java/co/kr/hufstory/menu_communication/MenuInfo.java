package co.kr.hufstory.menu_communication;

import java.util.List;

/**
 * Created by Aev Oh on 2016-02-26.
 */

// 2016.03.03, Aev Oh, 수신 받는 값 예시: [{"building":"2","caf": "0", "time": "11:30 ~ 14:30", "cost":"2000","content":"db,c,s,a","mon":"3","day":"1"}]
public class MenuInfo {

    private List<ServerMenu> serverMenuList;
    private List<Menu> menuList;

    public void pullMenu(){
        MenusNetwork.pullMenus();

        while(MenusNetwork.getMenu() == null || MenusNetwork.getMenu().isEmpty()){}
        serverMenuList = MenusNetwork.getMenu();
        serverToClientMenu();
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

    private int getWeekDay(int mon, int day){
        int weekDay = -1;
        //여기부터 시작.

        return weekDay;
    }

    public List<Menu> getMenuList(){
        return menuList;
    }
}

