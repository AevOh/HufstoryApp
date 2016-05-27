package co.kr.hufstory.hubigo_fragment;

import java.util.Date;

/**
 * Created by Hyeong Wook on 2016-05-23.
 */
public class CommentInfo {
    private int evaluation_id;
    private int lecture_id;
    private int score_recommend;
    private int contents_recommend;
    private int writer;
    private Date updatedAt;
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

    public int getScore_recommend(){
        return score_recommend;
    }

    public void setScore_recommend(int score_recommend){
        this.score_recommend = score_recommend;
    }

    public int getContents_recommend(){
        return contents_recommend;
    }

    public void setContents_recommend(int contents_recommend){
        this.contents_recommend = contents_recommend;
    }

    public int getWriter(){
        return writer;
    }

    public void setWriter(int writer){
        this.writer = writer;
    }

    public Date getUpdatedAt(){
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt){
        this.updatedAt = updatedAt;
    }

    public String getComment(){
        return comment;
    }

    public void setComment(String comment){
        this.comment = comment;
    }
}
