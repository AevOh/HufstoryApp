package co.kr.hufstory.Util;

import java.util.HashMap;

/**
 * Created by Hyeong Wook on 2016-06-20.
 */
public class ToggleManager<V> {
    private HashMap<Integer, V> mToggles;
    private V mCurrentActive;
    private int count = 0;

    public ToggleManager(){
        mToggles = new HashMap<>();
    }

    public void put(V value){
        mToggles.put(count++, value);
    }

    public void delete(V value){
        for(Integer i : mToggles.keySet()){
            if(mToggles.get(i).equals(value)){
                mToggles.remove(i);
                break;
            }
        }
    }

    public void clear(){
        mToggles.clear();
    }

    public void setActive(V value){
        for(Integer i : mToggles.keySet()){
            if(mToggles.get(i).equals(value)){
                mCurrentActive = value;
                break;
            }
        }
    }

    public boolean isActive(V value){
        return mCurrentActive != null && mCurrentActive.equals(value);
    }

    public void offAll(){
        mCurrentActive = null;
    }
}
