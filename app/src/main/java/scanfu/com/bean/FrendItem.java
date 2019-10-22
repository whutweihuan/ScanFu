package scanfu.com.bean;


import android.graphics.Bitmap;

import java.io.Serializable;

public class FrendItem implements Serializable {
    private String sortLetter; // 显示数据拼音的首字母
    private String name;        // 用户显示的名称
    private String dept;        // 用户部门
    private String userId;      // 用户 ID
    private String fullSpellpy;  // 好友名称的拼音全拼

    private String imgUrl;    // 头像链接
    private Bitmap bitmap;   // 头像图片

    private String phone;   // 手机号
    private String e_mail;  // 邮件地址
    private String address; // 地址
    private String jobtitle;
    boolean isFrends = false;
    private String company; // 公司
    private int deptId;

    public int getDeptId() {
        return deptId;
    }

    public void setDeptId(int deptId) {
        this.deptId = deptId;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public boolean isFrends() {
        return isFrends;
    }

    public void setFrends(boolean frends) {
        isFrends = frends;
    }

    public String getJobtitle() {
        return jobtitle;
    }

    public void setJobtitle(String jobtitle) {
        this.jobtitle = jobtitle;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getE_mail() {
        return e_mail;
    }

    public void setE_mail(String e_mail) {
        this.e_mail = e_mail;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getSortLetter() {
        return sortLetter;
    }

    public void setSortLetter(String sortLetter) {
        this.sortLetter = sortLetter;
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
}
