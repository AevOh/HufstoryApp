package co.kr.hufstory.menu_communication;

/**
 * Created by Aev Oh on 2016-03-03.
 */

//MenusAPI를 통해 서버로 부터 전달 받는 인자 값들을 모아 놓은 클래스.
public class ServerMenu {
    private int type;
    private int cost;
    private String content;
    private int mon;
    private int day;

    public void setDay(int day) {
        this.day = day;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setMon(int mon) {
        this.mon = mon;
    }

    public int getType() {
        return type;
    }

    public int getCost() {
        return cost;
    }

    public String getContent() {
        return content;
    }

    public int getMon() {
        return mon;
    }

    public int getDay() {
        return day;
    }
}
