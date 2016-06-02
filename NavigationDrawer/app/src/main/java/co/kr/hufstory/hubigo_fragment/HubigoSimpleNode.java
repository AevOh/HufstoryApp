package co.kr.hufstory.hubigo_fragment;

/**
 * Created by Hyeong Wook on 2016-05-02.
 */
public class HubigoSimpleNode {
    private int mId;
    private String mLecture;
    private String mProfessor;
    private String mMajor;
    private String mRecentEvaluation;
    private float mGradeSatisfaction;
    private float mContentSatisfaction;
    private boolean isBookmarked;

    public HubigoSimpleNode(int id, String lecture, String professor, String major, String recentEvaluation,
                            float gradeSatisfaction, float contentSatisfaction, boolean isBookmarked){
        mId = id;
        mLecture = lecture;
        mProfessor = professor;
        mMajor = major;
        mRecentEvaluation = recentEvaluation;
        mGradeSatisfaction = gradeSatisfaction;
        mContentSatisfaction = contentSatisfaction;
        this.isBookmarked = isBookmarked;
    }

    public int getId(){
        return mId;
    }

    public String getLecture(){
        return mLecture;
    }

    public String getProfessor(){
        return mProfessor;
    }

    public String getMajor(){
        return mMajor;
    }

    public String getRecentEvaluation(){
        return mRecentEvaluation;
    }

    public float getGradeSatisfaction(){
        return mGradeSatisfaction;
    }

    public float getContentSatisfaction(){
        return mContentSatisfaction;
    }

    public boolean isBookmarked(){
        return isBookmarked;
    }

    public void setBookmarked(boolean isBookmarked){
        this.isBookmarked = isBookmarked;
    }
}
