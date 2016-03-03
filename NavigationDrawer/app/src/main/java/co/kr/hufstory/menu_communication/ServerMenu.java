package co.kr.hufstory.menu_communication;

/**
 * Created by Aev Oh on 2016-03-03.
 */

//MenusAPI를 통해 서버로 부터 전달 받는 인자 값들을 모아 놓은 클래스.
public class ServerMenu {
    private int building;
    private int caf;
    private String time;
    private int cost;
    private String content;
    private int mon;
    private int day;

    public int getBuilding() {
        return building;
    }

    public int getCaf() {
        return caf;
    }

    public String getTime() {
        return time;
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
