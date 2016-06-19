package co.kr.hufstory.hubigo_fragment;

import android.util.Log;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Hyeong Wook on 2016-05-23.
 */
public class HubigoModel {
    static private HubigoModel uniqueInstance = null;

    private List<HubigoSimpleNode> mMainNodeList;

    private UserInfo mUserInfo;
    private String mUserSession;

    private List<Integer> mBookmarkList;
    private List<Integer> mWrittenList;

    private int mSelectLectureID;

    static public synchronized HubigoModel getInstance(){
        if(uniqueInstance == null)
            uniqueInstance = new HubigoModel();

        return uniqueInstance;
    }

    private HubigoModel(){
        mMainNodeList = new ArrayList<>();
        mBookmarkList = new ArrayList<>();
        mWrittenList = new ArrayList<>();
    }

    public void addHubigoSimpleNode(HubigoSimpleNode newNode){
        mMainNodeList.add(newNode);
    }

    public List<HubigoSimpleNode> getMainNodeList(){
        return mMainNodeList;
    }

    public void addBookmarkLecture(int lectureID){
        mBookmarkList.add(lectureID);
    }

    public void deleteBookmarkLecture(int lectureID){
        for(int i = 0; i < mBookmarkList.size(); i++){
            if(mBookmarkList.get(i) == lectureID) {
                mBookmarkList.remove(i);
                break;
            }
        }
    }

    public List<Integer> getBookmarkList(){
        return mBookmarkList;
    }

    public boolean isBookmarked(int lectureID){
        return mBookmarkList.indexOf(lectureID) != -1;
    }

    public void addWrittenEvaluation(int evaluationID){
        mWrittenList.add(evaluationID);
    }

    public void deleteWrittenEvaluation(int evaluationID){
        for(int i = 0; i < mWrittenList.size(); i++){
            if(mWrittenList.get(i) == evaluationID)
                mWrittenList.remove(i);
        }
    }

    public boolean isWrittenEvaluation(int evaluationID){
        for(int i = 0; i < mWrittenList.size(); i++){
            if(mWrittenList.get(i) == evaluationID)
                return true;
        }
        return false;
    }

    public List<Integer> getWrittenEvaluationList(){
        return mWrittenList;
    }

    public UserInfo getUserInfo(){
        return mUserInfo;
    }
    public void setUserInfo(UserInfo userInfo){
        mUserInfo = userInfo;
    }

    public String getUserSession(){
        return mUserSession;
    }

    public void setUserSession(String userSession){
        mUserSession = userSession;
    }

    public void setSelectLectureID(int id){
        mSelectLectureID = id;
    }

    public int getSelectLectureID(){
        return mSelectLectureID;
    }

    public void clear(){
        mMainNodeList.clear();
        mBookmarkList.clear();
        mWrittenList.clear();
    }

}
