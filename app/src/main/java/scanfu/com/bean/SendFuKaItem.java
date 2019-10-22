package scanfu.com.bean;

public class SendFuKaItem {
    int id;
    String touserid;
    String tousername;
    int position;
    String date;

    public SendFuKaItem() {
    }

    public SendFuKaItem(int id, String touserid, String tousername, int position, String date) {

        this.id = id;
        this.touserid = touserid;
        this.tousername = tousername;
        this.position = position;
        this.date = date;
    }

    public int getId() {

        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTouserid() {
        return touserid;
    }

    public void setTouserid(String touserid) {
        this.touserid = touserid;
    }

    public String getTousername() {
        return tousername;
    }

    public void setTousername(String tousername) {
        this.tousername = tousername;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
