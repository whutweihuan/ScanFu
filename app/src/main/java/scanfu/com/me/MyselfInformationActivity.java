package scanfu.com.me;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import scanfu.com.Adapter.MyselfInformationAdapter;
import scanfu.com.bean.FrendItem;
import scanfu.com.bean.MyApplication;
import scanfu.com.bean.MyselfInformationItem;
import scanfu.com.count.R;
import scanfu.com.utils.HttpUtils;
import scanfu.com.utils.ToastUnits;

public class MyselfInformationActivity extends AppCompatActivity {

    ArrayList<MyselfInformationItem> mList_1 = new ArrayList<MyselfInformationItem>();
    ArrayList<MyselfInformationItem> mList_2 = new ArrayList<MyselfInformationItem>();
    FrendItem me = new FrendItem();
    TextView tv_title;
    ListView lv_1, lv_2;
    ImageView iv_head;
    RelativeLayout rl_set_head_image;
    MyselfInformationAdapter mAdapter_1, mAdapter_2;
    MyApplication app = MyApplication.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myself_information);

        iniData();
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getInformation();
    }

    private void initView() {

        rl_set_head_image = (RelativeLayout) findViewById(R.id.rl_set_head_image);
        rl_set_head_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyselfInformationActivity.this, PicCutHeadImageActivity.class);
                intent.putExtra("headurl",me.getImgUrl());
                startActivityForResult(intent,4);
            }
        });

        iv_head = (ImageView) findViewById(R.id.iv_head);

        lv_1 = (ListView) findViewById(R.id.lv_1);
        lv_2 = (ListView) findViewById(R.id.lv_2);

        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("个人信息");

        lv_1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0://姓名
                        startModifyActivity("修改姓名", me.getName(), "修改姓名可以让你的朋友更容易记住您！", 10);
                        break;
                    case 1://二维码名片
                        Intent intent = new Intent(MyselfInformationActivity.this, MyErWeiMaMingPianActivity.class);
                        intent.putExtra("name", me.getName());
                        intent.putExtra("job", me.getJobtitle());
                        intent.putExtra("url", me.getImgUrl());
                        startActivity(intent);
                        break;
                    case 2://单位
                        break;
                    case 3://部门
                        intent = new Intent(MyselfInformationActivity.this, SetDeptActivity.class);
                        startActivityForResult(intent, 13);
                        break;
                    case 4://职务
                        startModifyActivity("修改职务", me.getJobtitle(), "职务写明，便于员工沟通", 14);
                }
            }
        });

        lv_2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0://手机
                        startModifyActivity("修改手机号", me.getPhone(), "及时修改手机号便于沟通交流！", 20);
                        break;
                    case 1://E-mail
                        startModifyActivity("修改E-mail", me.getE_mail(), "及时修改E-mail便于沟通交流！", 21);
                        break;
                    case 2://地址
                        startModifyActivity("修改地址", me.getAddress(), "及时修改地址便于联系！", 22);
                        break;
                }
            }
        });

        lv_1.setDividerHeight(0);
        lv_2.setDividerHeight(0);
        lv_1.setAdapter(mAdapter_1);
        lv_2.setAdapter(mAdapter_2);

        if (me.getBitmap() != null) {
            iv_head.setImageBitmap(me.getBitmap());
        }

    }

    void iniData() {
        mList_1.clear();
        mList_1.add(new MyselfInformationItem("姓名", me.getName()));
        mList_1.add(new MyselfInformationItem("二维码名片", true));
        mList_1.add(new MyselfInformationItem("单位", me.getCompany(), false, false));
        mList_1.add(new MyselfInformationItem("部门", me.getDept()));
        mList_1.add(new MyselfInformationItem("职务", me.getJobtitle()));


        mList_2.clear();
        mList_2.add(new MyselfInformationItem("手机", me.getPhone()));
        mList_2.add(new MyselfInformationItem("E-mail", me.getE_mail()));
        mList_2.add(new MyselfInformationItem("地址", me.getAddress()));

        if (mAdapter_1 == null) {
            mAdapter_1 = new MyselfInformationAdapter(mList_1, this);
        }
        if (mAdapter_2 == null) {
            mAdapter_2 = new MyselfInformationAdapter(mList_2, this);
        }
    }

    void getInformation() {
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... strings) {
                String url = MyApplication.ServerIP + "/ScanFuServer/rest/userprofile.action"
                        + "?username=" + app.getUserId();
                return HttpUtils.httpGet(url);
            }

            @Override
            protected void onPostExecute(String s) {
                if (s != null) {
                    try {
                        JSONObject json = new JSONObject(s);
                        me.setUserId(app.getUserId());
                        me.setName(json.optString("name"));
                        me.setCompany(json.optString("org"));
                        me.setDept(json.optString("dept"));
                        me.setJobtitle(json.optString("jobtitle"));

                        me.setPhone(json.optString("mobile"));
                        me.setE_mail(json.optString("email"));
                        me.setAddress(json.optString("officeAddr"));

                        me.setImgUrl(MyApplication.ServerIP + "/ScanFuServer/" + json.optString("gravatar"));

                        iniData();
                        mAdapter_1.notifyDataSetChanged();
                        mAdapter_2.notifyDataSetChanged();

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    URL url = new URL(me.getImgUrl());
                                    Bitmap bitmap = BitmapFactory.decodeStream(url.openStream());
                                    me.setBitmap(bitmap);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            //在UI线程执行，操控iv_head
                                            if (me.getBitmap() != null) {
                                                iv_head.setImageBitmap(me.getBitmap());
                                            }
                                        }
                                    });
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.execute();
    }

    public void onBackUp(View v) {
        finish();
    }

    void startModifyActivity(String title, String content, String desc, int requestCode) {
        Intent intent = new Intent(this, ModifyMyselfActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("content", content);
        intent.putExtra("desc", desc);
        startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK && data != null) {
            switch (requestCode) {
                case 4://设置头像
                    break;
                case 10://姓名
                    me.setName(data.getStringExtra("content"));
                    break;
                case 13://部门
                    me.setDept(data.getStringExtra("name"));
                    me.setDeptId(data.getIntExtra("id", -1));
                    break;
                case 14://职务
                    me.setJobtitle(data.getStringExtra("content"));
                    break;
                case 20://手机
                    me.setPhone(data.getStringExtra("content"));
                    break;
                case 21://E-mail
                    me.setE_mail(data.getStringExtra("content"));
                    break;
                case 22://地址
                    me.setAddress(data.getStringExtra("content"));
                    break;
            }
            iniData();
            mAdapter_1.notifyDataSetChanged();
            mAdapter_2.notifyDataSetChanged();
            commitData();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void commitData() {
        Log.d("test","test");
        new AsyncTask<String, Void, String>(){
            @Override
            protected String doInBackground(String... strings) {
                JSONObject json = new JSONObject();
                //把个人信息放到json里
                try {
                    json.put("username", me.getUserId());
                    json.put("name", me.getName());
                    json.put("mobile", me.getPhone());
                    json.put("email", me.getE_mail());
                    json.put("officeAddr", me.getAddress());
                    json.put("org", me.getCompany());
                    json.put("dept", me.getDept());
                    json.put("deptId", me.getDeptId());
                    json.put("jobtitle", me.getJobtitle());
                    json.put("category", "text");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String url = MyApplication.ServerIP + "/ScanFuServer/rest/updateMyProfile.action";
                //post方法需要把参数放到par里
                List<NameValuePair> par = new ArrayList<NameValuePair>();
                par.add(new BasicNameValuePair("jsonbody", json.toString()));
                return HttpUtils.httpPost(url, par);
            }

            @Override
            protected void onPostExecute(String s) {
                if (s != null) {
                    try {
                        JSONObject json = new JSONObject(s);
                        if ("success".equals(json.optString("status"))) {
                            ToastUnits.show(MyselfInformationActivity.this, "修改成功！");
                        } else {
                            ToastUnits.show(MyselfInformationActivity.this, json.optString("tip"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    ToastUnits.show(MyselfInformationActivity.this, "网络异常！");
                }
            }
        }.execute();
    }
}
