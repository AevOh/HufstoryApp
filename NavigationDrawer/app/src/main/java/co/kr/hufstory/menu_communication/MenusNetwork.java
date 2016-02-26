package co.kr.hufstory.menu_communication;

import java.util.List;

import co.kr.hufstory.MenuFragment;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Aev Oh on 2016-02-26.
 */

public class MenusNetwork {
    //2016.02.25, Aev Oh, Temp List for testing
    private static List<User> menuList;
    //private List<Menus> menuList;
    private static final String URL = "http://52.79.39.104:3000";

    //2016.02.25, Aev Oh, 비동기적 방식
    public static void pullMenus(){
        System.out.println("getUsers!!");

        RestAdapter adapter = new RestAdapter.Builder().setEndpoint(URL).build();
        MenusAPI api = adapter.create(MenusAPI.class);

        // 비동기적 방법
         api.getMenus(new Callback<List<User>>() {

            @Override
            public void success(List<User> users, Response response) {
                System.out.println("Success!!");
                menuList = users;
            }

            @Override
            public void failure(RetrofitError error) {
                System.out.println("Failure!!");
                System.out.println("error :" + error);
            }
        });
    }

    public static List<User> getMenu(){
        return menuList;
    }
}
