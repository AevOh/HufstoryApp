package co.kr.hufstory.hubigo_fragment;

/**
 * Created by Hyeong Wook on 2016-06-19.
 */
public class EvaluationInfo{
    private int mID;
    private String mNickName;
    private String mDate;
    private boolean satisGrade;
    private boolean satisContent;
    private String mComment;
    private boolean written;

    public EvaluationInfo(int id, String nickName, String date, boolean isSatisGrade, boolean isSatisContent, String comment, boolean isWritten){
        mID = id;
        mNickName = nickName;
        mDate = date;
        satisGrade = isSatisGrade;
        satisContent = isSatisContent;
        mComment = comment;
        written = isWritten;
    }

    public int getID(){
        return mID;
    }

    public String getNickName(){
        return mNickName;
    }

    public String getDate(){
        return mDate;
    }

    public boolean isSatisGrade(){
        return satisGrade;
    }

    public boolean isSatisContent(){
        return satisContent;
    }

    public String getComment(){
        return mComment;
    }

    public boolean isWritten() {
        return written;
    }
}
