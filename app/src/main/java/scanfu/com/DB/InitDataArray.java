package scanfu.com.DB;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;

import scanfu.com.bean.FrendItem;
import scanfu.com.bean.MyApplication;
import scanfu.com.bean.PinYinComparator;
import scanfu.com.utils.CharacterParser;
import scanfu.com.utils.MyConstants;

public class InitDataArray {
    public static SharedPreferences spInitDataArray;
    public static CharacterParser characterParser;

    // 保存 json 字符串到共享数据里
    public static void saveFrendArray(MyApplication app, String result) {
        spInitDataArray = app.getSharedPreferences("spInitDataArray", Context.MODE_PRIVATE);
        if (spInitDataArray == null || result == null) {
            return;
        }

        SharedPreferences.Editor editor = spInitDataArray.edit();
        editor.putString(MyApplication.ServerIP + MyApplication.getInstance().getUserId() + "frendArray", result);
        editor.commit();

    }

    // 返回好友列表
    public static ArrayList<FrendItem> initFrendArray(MyApplication app) {
        spInitDataArray = app.getSharedPreferences("spInitDataArray", Context.MODE_PRIVATE);
        String s = spInitDataArray.getString(MyApplication.ServerIP + MyApplication.getInstance().getUserId() + "frendArray", "");
        ArrayList<FrendItem> mFrendArrayList = new ArrayList<FrendItem>();
        if ("".equals(s)) {
            return mFrendArrayList;
        }

        JSONObject jsonObject;
        FrendItem item;

        if(characterParser == null){
            characterParser = new CharacterParser();
        }

        if (s != null) {
            try {
                JSONArray jsonArray = new JSONArray(s);
                mFrendArrayList.clear();
                for (int i = 0; i < jsonArray.length(); i++) {
                    jsonObject = jsonArray.getJSONObject(i);
                    item = new FrendItem();
                    item.setUserId(jsonObject.optString("username"));
                    item.setName(jsonObject.optString("name"));
                    item.setDept(jsonObject.optString("dept"));
                    item.setFullSpellpy(jsonObject.optString("fullspellpy"));
                    item.setImgUrl("http://" + MyConstants.IP_ADDRESS + ":8080/ScanFuServer/" +
                            jsonObject.optString("gravatar"));
                    item.setE_mail(jsonObject.optString("email"));
                    item.setPhone(jsonObject.optString("mobile"));
                    item.setAddress(jsonObject.optString("officeAddr"));
                    item.setJobtitle(jsonObject.optString("jobtitle"));

                    // 如果返回来的拼音全拼为空的话，那么我们就得自己处理，因为我们需要拼音排序
                    if (item.getFullSpellpy() == null) {
                        item.setFullSpellpy(characterParser.getSpelling(item.getName()));
                    }
                    String sortString = null;
                    try {
                        sortString = item.getFullSpellpy().substring(0, 1).toUpperCase();
                    } catch (Exception e) {
                        sortString = null;
                        e.printStackTrace();
                    }
                    // 通过正则表达式，判断你字母是否是英文字母
                    if (sortString != null && sortString.matches("[A-z]")) {
                        item.setSortLetter(sortString);
                    } else {
                        item.setSortLetter("#");
                    }
                    mFrendArrayList.add(item);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

         return mFrendArrayList;
    }

    public static FrendItem getFrendByUserId(MyApplication app,String usrId){
        if(usrId == null || usrId.length() == 0){
            return  null;
        }
        ArrayList<FrendItem> mFrendArrayList = initFrendArray(app);
        for(FrendItem it: mFrendArrayList){
            if(usrId.equals(it.getUserId())){
                it.setFrends(true);
                return  it;
            }
        }

        return  null;
    }

}
