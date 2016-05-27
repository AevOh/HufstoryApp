package co.kr.hufstory.hubigo_fragment;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by Hyeong Wook on 2016-05-27.
 */
public class HubigoSimpleNodeInfo {
    private int evaluation_id;
    private int lecture_id;
    private Date updatedAt;
    private String comment;

    private LectureInfo lecture;

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

    public LectureInfo getLecture(){
        return lecture;
    }

    public void setLecture(LectureInfo lecture){
        this.lecture = lecture;
    }

    static class LectureInfo{
        private String name;
        private int major_id;
        private int professor_id;
        private int score_count;
        private int content_count;
        private int evaluation_count;

        private ProfessorInfo professor;

        public String getName(){
            return name;
        }

        public void setName(String name){
            this.name = name;
        }

        public int getMajor_id(){
            return major_id;
        }

        public void setMajor_id(int major_id){
            this.major_id = major_id;
        }

        public int getProfessor_id(){
            return professor_id;
        }

        public void setProfessor_id(int professor_id){
            this.professor_id = professor_id;
        }

        public int getScore_count(){
            return score_count;
        }

        public void setScore_count(int score_count){
            this.score_count = score_count;
        }

        public int getContent_count(){
            return content_count;
        }

        public void setContent_count(int content_count){
            this.content_count = content_count;
        }

        public int getEvaluation_count(){
            return evaluation_count;
        }

        public void setEvaluation_count(int evaluation_count){
            this.evaluation_count = evaluation_count;
        }

        public ProfessorInfo getProfessor(){
            return professor;
        }

        public void setProfessor(ProfessorInfo professor){
            this.professor = professor;
        }

        static class ProfessorInfo{
            private String name;

            private MajorInfo major;

            public String getName(){
                return name;
            }

            public void setName(String name){
                this.name = name;
            }

            public MajorInfo getMajor(){
                return major;
            }

            public void setMajor(MajorInfo major){
                this.major = major;
            }

            static class MajorInfo{
                private String name;

                public String getName(){
                    return name;
                }

                public void setName(String name){
                    this.name = name;
                }
            }
        }
    }
}
