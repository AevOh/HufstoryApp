package co.kr.hufstory.menu_communication;

import java.util.Calendar;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Aev Oh on 2016-02-26.
 */

public class MenusNetwork {
    //2016.02.25, Aev Oh, Temp List for testing
    private static List<ServerMenu> menuList;
    //private List<Menus> menuList;
    private static final String MENU_URL = "http://hufstory.co.kr/cert/public/menu?";

    //2016.02.25, Aev Oh, 비동기적 방식
    public static void pullMenus(){
        System.out.println("getUsers!!");

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK , Calendar.MONDAY);
        RestAdapter adapter = new RestAdapter.Builder().setEndpoint(MENU_URL+"start_date="+calendar.get(calendar.DATE)+"&end_date="+calendar.get(calendar.DATE)+6+"&mon="+calendar.get(calendar.MONTH)).build();
        MenusAPI api = adapter.create(MenusAPI.class);

        // 비동기적 방법
         api.getMenus(new Callback<List<ServerMenu>>() {

            @Override
            public void success(List<ServerMenu> menus, Response response) {
                System.out.println("Success!!");
                menuList = menus;
            }

            @Override
            public void failure(RetrofitError error) {
                System.out.println("Failure!!");
                System.out.println("error :" + error);
            }
        });
    }

    public static List<ServerMenu> getMenu(){
        return menuList;
    }
}
