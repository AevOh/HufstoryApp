package co.kr.hufstory.menu_communication;

/**
 * Created by Aev Oh on 2016-03-03.
 */
// 2016.03.03, Aev Oh, 클라이언트에게 제공되는 메뉴(날짜 -> 요일)
public class Menu {

    private int building;
    private String caf;
    private String time;
    private int cost;
    private String content;
    private String week;       //요일
    private String date;    //날짜

    public int getBuilding() {
        return building;
    }

    public void setBuilding(int building) {
        this.building = building;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setCaf(String caf) {
        this.caf = caf;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public String getCaf() {
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
