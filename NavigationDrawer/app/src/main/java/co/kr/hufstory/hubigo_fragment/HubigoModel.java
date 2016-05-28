package co.kr.hufstory.hubigo_fragment;

import android.util.Log;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Hyeong Wook on 2016-05-23.
 */
public class HubigoModel {
    static private HubigoModel hubigoInstance = null;

    private List<HubigoSimpleNode> mMainNodeList;
    private UserInfo mUserInfo;

    static public HubigoModel getInstance(){
        if(hubigoInstance == null)
            hubigoInstance = new HubigoModel();

        return hubigoInstance;
    }

    private HubigoModel(){
        mMainNodeList = new ArrayList<>();
    }

    public HubigoSimpleNode getHubigoSimpleNode(int index){
        return mMainNodeList.get(index);
    }

    public void addHubigoSimpleNode(HubigoSimpleNode newNode){
        mMainNodeList.add(newNode);
    }

    public void deleteHubigoSimpleNode(int index){
        mMainNodeList.remove(index);
    }

    public List<HubigoSimpleNode> getMainNodeList(){
        return mMainNodeList;
    }

    public void setMainNodeList(List<HubigoSimpleNode> mainNodeList){
        mMainNodeList = mainNodeList;
    }

    public UserInfo getUserInfo(){
        return mUserInfo;
    }
    public void setUserInfo(UserInfo userInfo){
        mUserInfo = userInfo;
    }

}
