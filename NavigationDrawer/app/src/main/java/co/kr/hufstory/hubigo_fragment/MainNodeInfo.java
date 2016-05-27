package co.kr.hufstory.hubigo_fragment;

import java.util.Date;

/**
 * Created by Hyeong Wook on 2016-05-24.
 */
public class MainNodeInfo {
    private int evaluation_id;
    private int lecture_id;
    private Date updateAt;;
    private String comment;

    public int getEvaluation_id(){
        return evaluation_id;
    }

    public void setEvaluation_id(int evaluation_id){
        this.evaluation_id = evaluation_id;
    }

    public int getLecture_id(){
        return lecture_id;
    }

    public void setLecture_id(int lecture_id){
        this.lecture_id = lecture_id;
    }

    public Date getUpdateAt(){
        return updateAt;
    }

    public void setUpdateAt(Date updateAt){
        this.updateAt = updateAt;
    }

    public String getComment(){
        return comment;
    }

    public void setComment(String comment){
        this.comment = comment;
    }
}
