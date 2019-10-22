package scanfu.com.count;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import scanfu.com.bean.MyApplication;
import scanfu.com.utils.HttpUtils;
import scanfu.com.utils.MD5Checksum;
import scanfu.com.utils.MyStringUtils;
import scanfu.com.utils.ToastUnits;
import scanfu.com.utils.MyConstants;

public class RegisterActivity extends AppCompatActivity {

    Button btn_get_yzm;
    final int MAXTIME = 5;
    int time = 0;
    EditText ed_phone_number;   // 手机号
    EditText ed_name;           // 用户名
    EditText ed_password;       // 密码
    EditText ed_password2;      // 确认密码
    EditText ed_yzm;            // 验证码

    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what > 1) {
                time = msg.what - 1;
                btn_get_yzm.setText(time + "秒后重新获取");
//                mHandler.sendEmptyMessage(time);
                mHandler.sendEmptyMessageDelayed(time, 1000); // 倒计时
            } else {
                btn_get_yzm.setEnabled(true);
                btn_get_yzm.setText("获取验证码");
            }

            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initView();

    }

    // 注册完成事件，包括一些检测判断
    public void onToRegister(View v) {
        final String phone_number = ed_phone_number.getText().toString().trim();
        final String name = ed_name.getText().toString().trim();
        final String password = ed_password.getText().toString().trim();
        final String password2 = ed_password2.getText().toString().trim();
        final String yzm = ed_yzm.getText().toString().trim();

        if (MyStringUtils.isEmpty(phone_number)) {
            ToastUnits.show(RegisterActivity.this, "手机号不能为空");
        }
        else if (MyStringUtils.isEmpty(name)) {
            ToastUnits.show(RegisterActivity.this, "姓名不能为空");
        }
        else if (MyStringUtils.isEmpty(password)) {
            ToastUnits.show(RegisterActivity.this, "密码不能为空");
        }
        else if (MyStringUtils.isEmpty(password2)) {
            ToastUnits.show(RegisterActivity.this, "确认密码不能为空");
        }
        else if (!password.equals(password2)) {
            ToastUnits.show(RegisterActivity.this, "两次密码不相同");
        }
        else if (MyStringUtils.isEmpty(yzm)) {
            ToastUnits.show(RegisterActivity.this, "验证码不能为空");
        }

        // 调用注册接口
        new AsyncTask<String,Void,String>(){
            @Override
            protected void onPreExecute() {
                // 可以添加一个等待对话框
            }

            @Override
            protected String doInBackground(String... strings) {
                JSONObject json = new JSONObject();
                try {
                    json.put("phone",phone_number);
                    json.put("org","武汉理工");
                    json.put("md5pwd", MD5Checksum.getMD5String(password));
                    json.put("name",name);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // jsonbody,kaptcha 图片验证码(4 digit),verisms(短信验证码 6 digit),limit （默认一个人只能注册一个）
                try {
                    String url = MyApplication.ServerIP + "/ScanFuServer/rest//regbymobile.action"
                            + "?jsonbody="+ URLEncoder.encode(json.toString(),"UTF-8") // 编码问题
                            + "&kaptcha=" +""
                            + "&verisms="+yzm
                            + "&limit="
                            ;
                    Log.d("url",url);
                    return HttpUtils.httpGet(url);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }


                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                if(s!=null){
                    try {
                        JSONObject json = new JSONObject(s);
                        if("success".equals(json.optString("status"))){
                            ToastUnits.show(RegisterActivity.this,"注册成功");
//                            Intent intent = new Intent(RegisterActivity.this);
                            RegisterActivity.this.finish();
                            return;
                        }else {
                            ToastUnits.show(RegisterActivity.this,json.optString("tip"));
                            return;
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                ToastUnits.show(RegisterActivity.this,"网络异常");
                return;
            }
        }.execute();


    }


    public void initView() {
        ed_phone_number = (EditText) findViewById(R.id.ed_phone_number);
        ed_name = (EditText) findViewById(R.id.ed_name);
        ed_password = (EditText) findViewById(R.id.ed_password);
        ed_password2 = (EditText) findViewById(R.id.ed_password2);
        ed_yzm = (EditText) findViewById(R.id.ed_yzm);


        btn_get_yzm = (Button) findViewById(R.id.btn_get_yzm);

        btn_get_yzm.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onClick(View v) {
                // 判断电话号码是否为空
                final String phone_number = ed_phone_number.getText().toString().trim();
                if (phone_number == null || phone_number.length() == 0) {
                    Toast.makeText(RegisterActivity.this, "手机号不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                btn_get_yzm.setEnabled(false);
                mHandler.sendEmptyMessage(MAXTIME);

                // 异步线程请求获取验证码，原因是Android 4.0 以后耗时比较长的要放在异步线程，不让放在 UI 线程

                new AsyncTask<String, Void, String>() {


                    @Override
                    protected void onPreExecute() {
                        // 这个方法是执行在 UI 线程里，是在子线程前执行
                    }

                    @Override
                    protected String doInBackground(String... strings) {
                        // 这个是在子线程执行的，耗时比较长的放在这里面
                        //TODO 网络请求
                        String url = MyApplication.ServerIP  +"/ScanFuServer/" +
                                "misc/sendsmscode.action" + "?phone=" + phone_number;
                        return HttpUtils.httpGet(url);
                    }

                    @Override
                    protected void onPostExecute(String s) {
                        // 这个方法也是在 UI 执行，执行顺序是在 doInBackground 之后，可以操作 UI
                        // 如果返回内容不为空，需要做 json 解析
                        if (s != null) {
                            try {
                                JSONObject json = new JSONObject(s);
                                ToastUnits.show(RegisterActivity.this,json.optString("tip"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        ToastUnits.show(RegisterActivity.this,"网络或者服务器异常");
                    }

                }.execute();
            }
        });
    }

    public void onBackUp(View v) {
        finish();
    }

    public void toLogin(View v) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

}
