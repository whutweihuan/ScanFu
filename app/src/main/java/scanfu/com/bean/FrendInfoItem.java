package scanfu.com.bean;

public class FrendInfoItem {
    int reSourceId;
    String key;
    String value;
    boolean isShowNext = false;

    public FrendInfoItem(int reSourceId, String key, String value) {
        this.reSourceId = reSourceId;
        this.key = key;
        this.value = value;
    }

    public FrendInfoItem(int reSourceId, String key, String value, boolean isShowNext) {
        this.reSourceId = reSourceId;
        this.key = key;
        this.value = value;
        this.isShowNext = isShowNext;
    }

    public int getReSourceId() {
        return reSourceId;
    }

    public void setReSourceId(int reSourceId) {
        this.reSourceId = reSourceId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isShowNext() {
        return isShowNext;
    }

    public void setShowNext(boolean showNext) {
        isShowNext = showNext;
    }
}
