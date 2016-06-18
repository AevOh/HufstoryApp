package co.kr.hufstory.hubigo_fragment;

import java.util.Date;
import java.util.List;

/**
 * Created by Hyeong Wook on 2016-06-19.
 */
public class HubigoDetailInfo {
    private String mLecture;
    private String mProfessor;
    private String mMajor;
    private int mCredit;
    private int mGrade;
    private int mTime;
    private float mGradeSatisfaction;
    private float mContentSatisfaction;
    private int mEvaluationCount;
    private List<EvaluationInfo> mEvaluationInfoList;

    public HubigoDetailInfo(String lecture, String professor, String major, int credit, int grade, int time,
                            float gradeSatisfaction, float contentSatisfaction, int evaluationCount, List<EvaluationInfo> evaluationInfoList){
        mLecture = lecture;
        mProfessor = professor;
        mMajor = major;
        mCredit = credit;
        mGrade = grade;
        mTime = time;
        mGradeSatisfaction = gradeSatisfaction;
        mContentSatisfaction = contentSatisfaction;
        mEvaluationCount = evaluationCount;
        mEvaluationInfoList = evaluationInfoList;
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

    public int getCredit(){
        return mCredit;
    }

    public int getGrade(){
        return mGrade;
    }

    public int getTime(){
        return mTime;
    }

    public float getGradeSatisfaction(){
        return mGradeSatisfaction;
    }

    public float getContentSatisfaction(){
        return mContentSatisfaction;
    }

    public int getEvaluationCount() { return mEvaluationCount; }

    public List<EvaluationInfo> getEvaluationInfoList(){
        return mEvaluationInfoList;
    }

}
