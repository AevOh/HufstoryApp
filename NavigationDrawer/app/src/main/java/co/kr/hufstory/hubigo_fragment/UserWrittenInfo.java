package co.kr.hufstory.hubigo_fragment;

/**
 * Created by Hyeong Wook on 2016-06-27.
 */
public class UserWrittenInfo {
    private int mRank;
    private String mNickName;
    private int mCount;

    public UserWrittenInfo(int rank, String nickName, int count){
        mRank = rank;
        mNickName = nickName;
        mCount = count;
    }

    public int getRank(){
        return mRank;
    }

    public String getNickName(){
        return mNickName;
    }

    public int getCount(){
        return mCount;
    }
}
