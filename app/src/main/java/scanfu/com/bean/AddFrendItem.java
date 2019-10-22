package scanfu.com.bean;

import android.graphics.Bitmap;

public class AddFrendItem {
    private String name;        // 用户显示的名称
    private String dept;        // 用户部门
    private String userId;      // 用户 ID
    private String fullSpellpy;  // 好友名称的拼音全拼

    private String imgUrl;    // 头像链接
    private Bitmap bitmap;   // 头像图片
    private String company; // 公司

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public AddFrendItem() {
    }

    public AddFrendItem(String name, String dept, String userId, String fullSpellpy, String imgUrl, Bitmap bitmap) {
        this.name = name;
        this.dept = dept;
        this.userId = userId;
        this.fullSpellpy = fullSpellpy;
        this.imgUrl = imgUrl;
        this.bitmap = bitmap;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFullSpellpy() {
        return fullSpellpy;
    }

    public void setFullSpellpy(String fullSpellpy) {
        this.fullSpellpy = fullSpellpy;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
