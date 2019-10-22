package scanfu.com.me;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONObject;

import java.net.URL;

import scanfu.com.bean.MyApplication;
import scanfu.com.count.R;
import scanfu.com.zxing.EncodingHandler;

public class MyErWeiMaMingPianActivity extends AppCompatActivity {

    String name;
    String imgurl;
    String job;

    TextView tv_name;
    TextView tv_job;
    TextView tv_title;
    Bitmap bitmap;
    Bitmap erWeimaBitMap;

    ImageView iv_head;
    ImageView iv_er_wei_ma;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_er_wei_ma_ming_pian);

        name = getIntent().getStringExtra("name");
        imgurl = getIntent().getStringExtra("url");
        job = getIntent().getStringExtra("job");

        initView();
        if (imgurl == null)
            return;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(imgurl);
                    bitmap = BitmapFactory.decodeStream(url.openStream());

                    //生成二维码内容
                    JSONObject json=new JSONObject();
                    json.put("isMinPian",true);
                    json.put("userId", MyApplication.getInstance().getUserId());

                    //生成图片
                    String content=json.toString();
                    erWeimaBitMap= EncodingHandler.createQRCode(content,1024);


                    if (bitmap != null) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                iv_head.setImageBitmap(bitmap);
                                iv_er_wei_ma.setImageBitmap(erWeimaBitMap);
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void initView() {
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_job = (TextView) findViewById(R.id.tv_job);
        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_head=(ImageView)findViewById(R.id.iv_head);
        iv_er_wei_ma=(ImageView)findViewById(R.id.iv_er_wei_ma);

        tv_name.setText(name);
        tv_job.setText(job);
        tv_title.setText("二维码名片");
    }

    public void onBackUp(View v){
        finish();
    }

}
