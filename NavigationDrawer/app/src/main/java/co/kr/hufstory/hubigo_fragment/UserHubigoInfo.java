package co.kr.hufstory.hubigo_fragment;

/**
 * Created by Hyeong Wook on 2016-06-02.
 */
public class UserHubigoInfo {
    private Integer written_evaluation;
    private Integer favorite_lecture;

    public Integer getWritten_evaluation(){
        return written_evaluation;
    }

    public void setWritten_evaluation(int written_evaluation){
        this.written_evaluation = written_evaluation;
    }

    public Integer getFavorite_lecture(){
        return favorite_lecture;
    }

    public void setFavorite_lecture(int favorite_lecture){
        this.favorite_lecture = favorite_lecture;
    }
}
