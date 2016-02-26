package co.kr.hufstory.menu_communication;

/**
 * Created by Aev Oh on 2016-02-13.
 */
public class User {
    private int id;
    private String userid;
    private String password;
    private String email;

    private String money;
    private String purchase_history;
    private String bidding_history;
    private String sales_history;
    private String createdAt;
    private String updatedAt;

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserId() {
        return userid;
    }

    public void setUserId(String userId) {
        this.userid = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getPurchase_history() {
        return purchase_history;
    }

    public void setPurchase_history(String purchase_history) {
        this.purchase_history = purchase_history;
    }

    public String getBidding_history() {
        return bidding_history;
    }

    public void setBidding_history(String bidding_history) {
        this.bidding_history = bidding_history;
    }

    public String getSales_history() {
        return sales_history;
    }

    public void setSales_history(String sales_history) {
        this.sales_history = sales_history;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
