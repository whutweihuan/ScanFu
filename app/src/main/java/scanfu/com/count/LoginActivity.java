package scanfu.com.count;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import scanfu.com.UI.MainUIActivity;
import scanfu.com.bean.MyApplication;
import scanfu.com.utils.MyConstants;
import scanfu.com.utils.HttpUtils;
import scanfu.com.utils.MD5Checksum;
import scanfu.com.utils.MyStringUtils;
import scanfu.com.utils.ToastUnits;

// 登录界面
public class LoginActivity extends AppCompatActivity {
    EditText ed_userID;  // 登录账号
    EditText ed_pwd;     // 登录密码

    SharedPreferences sp = null;  // 类似与缓存，保存登录信息等信息
    MyApplication app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sp = getSharedPreferences("login", MODE_PRIVATE);
        initView();
        app = MyApplication.getInstance();
        // 如果上次已经登录成功，那么可以直接跳转主 UI 界面
        String userId = sp.getString("userId", "");
        String password = sp.getString("userpwd", "");
        app.setUserId(userId);
        app.setValiduid(sp.getString("validuid", ""));
        ed_userID.setText(userId);
        ed_pwd.setText(password);

        // 两者都不为空，直接登录
//        if (!MyStringUtils.isEmpty(userId) && !MyStringUtils.isEmpty(password)) {
//            // 下面的方法需要网络验证
//            // onLogin(null);
//
//            // 已经保存的账号不需要网络验证
//            Intent intent = new Intent(LoginActivity.this, MainUIActivity.class);
//            startActivity(intent);
//            LoginActivity.this.finish();
//        } else {
//            onLogin(null);
//        }

    }

    public void initView() {
        ed_userID = (EditText) findViewById(R.id.et_userId);
        ed_pwd = (EditText) findViewById(R.id.ed_pwd);
    }

    // 实现跳转到注册界面
    public void onRegister(View v) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    // 实现跳转到用户忘记密码界面
    public void toForgetActivity(View v) {
        Intent intent = new Intent(this, ReSetPasswordActivity.class);
        startActivity(intent);
    }

    // 实现用户登录功能
    public void onLogin(View v) {
        final String userId = ed_userID.getText().toString().trim();
        final String userpwd = ed_pwd.getText().toString().trim();

        if (MyStringUtils.isEmpty(userId)) {
            ToastUnits.show(this, "账号不能为空");
            return;
        } else if (MyStringUtils.isEmpty(userpwd)) {
            ToastUnits.show(this, "密码不能为空");
            return;
        }
//        ToastUnits.show(this,"你已经点击登录");
        new AsyncTask<String, Void, String>() {
            // 网络请求服务器，返回请求结果
            @Override
            protected String doInBackground(String... strings) {
                String url = "http://" + MyConstants.IP_ADDRESS + ":8080/ScanFuServer/rest/login.action"
                        + "?name=" + userId
                        + "&encryptedpwd=" + MD5Checksum.getMD5String(userpwd);
//                ToastUnits.show(LoginActivity.this,url);
//                Log.d("weihuan",url);

                return HttpUtils.httpGet(url);
            }

            // 根据服务器返回结果确定登录是否成功
            @Override
            protected void onPostExecute(String s) {
                if (s != null) { // 网络及服务器没有问题
                    try {
                        JSONObject json = new JSONObject(s);
                        if ("1".equals(json.optString("status"))) {
                            ToastUnits.show(LoginActivity.this, "登录成功");
                            SharedPreferences.Editor edit = sp.edit();
                            edit.putString("userId", userId);
                            edit.putString("userpwd", userpwd);
                            String md5str = userId.trim() + ":" + MD5Checksum.getMD5String(userpwd.trim());
                            app.setValiduid(Base64.encodeToString(md5str.trim().getBytes(), Base64.DEFAULT));
                            edit.putString("validuid", app.getValiduid());
                            edit.commit();
                            app.setUserId(userId);
                            Intent intent = new Intent(LoginActivity.this, MainUIActivity.class);
                            startActivity(intent);
                            LoginActivity.this.finish();

                        } else {
                            ToastUnits.show(LoginActivity.this, "登录失败");
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    ToastUnits.show(LoginActivity.this, "网络或服务器故障");
                }
            }

        }.execute();

    }

}
