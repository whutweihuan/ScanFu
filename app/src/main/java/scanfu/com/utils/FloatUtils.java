package scanfu.com.utils;

public class FloatUtils {

    /***
     * format :为设置小数的位数 比如两位: "0.00"
     *
     * */

    public static String getStringFromFloat(float a,String format){
        return new java.text.DecimalFormat(format).format(a);
    }

    //获取两位小数
    public static String getStringFromFloatBy2dot(float a){
        return new java.text.DecimalFormat("0.00").format(a);
    }
}
