package scanfu.com.count;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import scanfu.com.bean.MyApplication;
import scanfu.com.utils.HttpImage;
import scanfu.com.utils.HttpUtils;
import scanfu.com.utils.MyConstants;
import scanfu.com.utils.ToastUnits;

public class ReSetPasswordActivity extends AppCompatActivity {
    MyApplication app;
    ImageView iv_zifu_yzm = null;
    boolean isGettingZiFuYZM = false;
    Button btn_get_zifuyzm = null;
    EditText et_phone_number = null;

    final int MAXTIME = 5;
    int time = 0;

    // 倒计时程序
    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what > 1) {
                time = msg.what - 1;
                btn_get_zifuyzm.setText(time + "秒后重新获取");
//                mHandler.sendEmptyMessage(time);
                mHandler.sendEmptyMessageDelayed(time, 1000); // 倒计时
            } else {
                btn_get_zifuyzm.setEnabled(true);
                btn_get_zifuyzm.setText("获取验证码");
            }

            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        app = MyApplication.getInstance();

        initView();
        getZiFuYZM();



    }

    // 返回事件
    public void onBackUp(View v){
        finish();
    }

    // 获得图片验证码异步线程
    public void getZiFuYZM(){
        new AsyncTask<Bitmap,Void,Bitmap>(){
            @Override
            protected Bitmap doInBackground(Bitmap... bitmaps) {
                String url = "http://"+ MyConstants.IP_ADDRESS+":8080/ScanFuServer/misc/kaptcha.action";
                Bitmap bit = HttpImage.getHttpBitmap(url,app);
                Log.d("resetweihuan",url);
                return bit;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                isGettingZiFuYZM = false;
                iv_zifu_yzm.setImageBitmap(bitmap) ;
            }
        }.execute();


    }

    public  void initView() {
        iv_zifu_yzm = (ImageView) findViewById(R.id.iv_zifu_yzm);
        btn_get_zifuyzm = (Button) findViewById(R.id.btn_get_zifuyzm);
        et_phone_number = (EditText)findViewById(R.id.et_phone_number);

        iv_zifu_yzm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isGettingZiFuYZM) {
                    ToastUnits.show(ReSetPasswordActivity.this, "正在获取，请稍等");
                    return;
                }
                isGettingZiFuYZM = true;
                getZiFuYZM();

            }
        });

        btn_get_zifuyzm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 判断电话号码是否为空
                final String phone_number = et_phone_number.getText().toString().trim();
                if (phone_number == null || phone_number.length() == 0) {
                    Toast.makeText(ReSetPasswordActivity.this, "手机号不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                btn_get_zifuyzm.setEnabled(false);
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
                        String url = "http://" + MyConstants.IP_ADDRESS + ":8080/ScanFuServer/" +
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
                                ToastUnits.show(ReSetPasswordActivity.this, json.optString("tip"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }.execute();
            }

        });
    }
}













