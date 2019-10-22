package scanfu.com.bean;

public class MyselfInformationItem {
    String key;
    String value;
    boolean isErWeiMa=false;
    boolean isNext=true;

    public MyselfInformationItem(String key, boolean isErWeiMa) {
        this.key = key;
        this.isErWeiMa = isErWeiMa;
    }

    public MyselfInformationItem(String key, String value) {

        this.key = key;
        this.value = value;
    }

    public MyselfInformationItem(String key, String value, boolean isErWeiMa) {
        this.key = key;
        this.value = value;
        this.isErWeiMa = isErWeiMa;
    }

    public MyselfInformationItem(boolean isNext,String key, String value) {
        this.isNext = isNext;
        this.key = key;
        this.value = value;
    }


    public MyselfInformationItem(String key, String value, boolean isErWeiMa, boolean isNext) {
        this.key = key;
        this.value = value;
        this.isErWeiMa = isErWeiMa;
        this.isNext = isNext;
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

    public boolean isErWeiMa() {
        return isErWeiMa;
    }

    public void setErWeiMa(boolean erWeiMa) {
        isErWeiMa = erWeiMa;
    }

    public boolean isNext() {
        return isNext;
    }

    public void setNext(boolean next) {
        isNext = next;
    }
}
