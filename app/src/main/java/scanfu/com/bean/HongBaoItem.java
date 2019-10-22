package scanfu.com.bean;

public class HongBaoItem {
    int id;
    String userid;
    String username;
    String org;
    float money;
    String date;

    public HongBaoItem() {
    }

    public HongBaoItem(int id, String userid, String username, String org, float money, String date) {
        this.id = id;
        this.userid = userid;
        this.username = username;
        this.org = org;
        this.money = money;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getOrg() {
        return org;
    }

    public void setOrg(String org) {
        this.org = org;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }
}
