package scanfu.com.bean;

public class ReceivedFuKaItem {
    int id;
    String fromid;
    String fromusername;

    public String getFromusername() {
        return fromusername;
    }

    public void setFromusername(String fromusername) {
        this.fromusername = fromusername;
    }

    int position;
    String date;

    public ReceivedFuKaItem() {
    }

    public ReceivedFuKaItem(int id, String fromid, int position, String date) {
        this.id = id;
        this.fromid = fromid;
        this.position = position;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFromid() {
        return fromid;
    }

    public void setFromid(String fromid) {
        this.fromid = fromid;
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
