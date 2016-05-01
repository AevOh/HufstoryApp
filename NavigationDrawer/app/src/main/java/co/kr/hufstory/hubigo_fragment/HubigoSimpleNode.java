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
    private boolean mGradeSatisfaction;
    private boolean mContentSatisfaction;

    public HubigoSimpleNode(int id, String lecture, String professor, String major, String recentEvaluation, boolean gradeSatisfaction, boolean contentSatisfaction){
        mId = id;
        mLecture = lecture;
        mProfessor = professor;
        mMajor = major;
        mRecentEvaluation = recentEvaluation;
        mGradeSatisfaction = gradeSatisfaction;
        mContentSatisfaction = contentSatisfaction;
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

    public String getmMajor(){
        return mMajor;
    }

    public String getRecentEvaluation(){
        return mRecentEvaluation;
    }

    public boolean getGradeSatisfaction(){
        return mGradeSatisfaction;
    }

    public boolean getContentSatisfaction(){
        return mContentSatisfaction;
    }
}
