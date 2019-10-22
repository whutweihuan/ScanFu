package scanfu.com.bean;

import java.util.Comparator;

public class PinYinComparator implements Comparator<FrendItem> {
    @Override
    public int compare(FrendItem o1, FrendItem o2) {
        if (o1.getSortLetter().equals("@") || o2.equals("#")) {
            return -1;
        } else if (o1.getSortLetter().equals("#") || o2.getSortLetter().equals("@")) {
            return 1;
        }else {
            return o1.getSortLetter().compareTo(o2.getSortLetter());
        }
    }
}
