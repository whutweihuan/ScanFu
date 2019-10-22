package scanfu.com.frend;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.JsonToken;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import scanfu.com.Adapter.FrendInformationAdapter;
import scanfu.com.DB.InitDataArray;
import scanfu.com.bean.FrendInfoItem;
import scanfu.com.bean.FrendItem;
import scanfu.com.bean.FuKaArray;
import scanfu.com.bean.MyApplication;
import scanfu.com.count.R;
import scanfu.com.fuka.FuKaListActivity;
import scanfu.com.utils.HttpUtils;
import scanfu.com.utils.MyConstants;
import scanfu.com.utils.ToastUnits;

public class FrendInformationActivity extends AppCompatActivity {
    //    TextView tv_infor;
    boolean isScanAddFrend;
    TextView tv_title;
    String userId = null;
    MyApplication app;
    FrendItem item;

    ArrayList<FrendInfoItem> list_1 = new ArrayList<FrendInfoItem>();
    ArrayList<FrendInfoItem> list_2 = new ArrayList<FrendInfoItem>();

    ListView lv_1, lv_2;
    TextView tv_name;
    TextView tv_job;
    FrendInformationAdapter mAdapter_1, mAdapter_2;

    Button btn_send_fu_ka, btn_add_frend, btn_delete_frend;

    // 显示大图
    ImageView iv_head, iv_big_head;
    RelativeLayout rl_big_head;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frend_information);
//        tv_infor = (TextView)findViewById(R.id.tv_infor);
        app = MyApplication.getInstance();


        isScanAddFrend = getIntent().getBooleanExtra("isScanAddFrend", false);
//        if(isScanAddFrend){
//            String infor = getIntent().getStringExtra("infor");
//            tv_infor.setText(infor);
//        }
//        else {
//            tv_infor.setText(getIntent().getStringExtra("userid"));
//        }

        // 通过好友列表获取，如果获取了就设置为 isfrend 为 true
        userId = getIntent().getStringExtra("userid");
        if (userId == null || userId.length() == 0) {
            finish();
        }

        item = InitDataArray.getFrendByUserId(app, userId);
        if (item == null) {
            // 否则设置 isFrend 为 false
            item = new FrendItem(); // isFrend = false;
        }
        initArray();

        mAdapter_1 = new FrendInformationAdapter(list_1, this);
        mAdapter_2 = new FrendInformationAdapter(list_2, this);

        initView();
        getFrendInform(userId);
    }

    public void initView() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("详细资料");
        iv_head = (ImageView) findViewById(R.id.iv_head);
        iv_big_head = (ImageView) findViewById(R.id.iv_big_head);
        rl_big_head = (RelativeLayout) findViewById(R.id.rl_big_head);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_job = (TextView) findViewById(R.id.tv_job);
        lv_1 = (ListView) findViewById(R.id.lv_1);
        lv_2 = (ListView) findViewById(R.id.lv_2);

        btn_add_frend = (Button) findViewById(R.id.btn_add_frend);
        btn_delete_frend = (Button) findViewById(R.id.btn_delete_frend);
        btn_send_fu_ka = (Button) findViewById(R.id.btn_send_fu_ka);

        //        item.setFrends(false);
        if (item.isFrends()) {
            btn_send_fu_ka.setVisibility(View.VISIBLE);
            btn_add_frend.setVisibility(View.GONE);
            btn_delete_frend.setVisibility(View.VISIBLE);
        } else {
            btn_send_fu_ka.setVisibility(View.GONE);
            btn_add_frend.setVisibility(View.VISIBLE);
            btn_delete_frend.setVisibility(View.GONE);
        }

        lv_1.setAdapter(mAdapter_1);
        lv_2.setAdapter(mAdapter_2);
        tv_name.setText(item.getName());
        tv_job.setText(item.getJobtitle());


        rl_big_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rl_big_head.setVisibility(View.INVISIBLE);
            }
        });

        iv_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rl_big_head.setVisibility(View.VISIBLE);
                if (item.getBitmap() != null) {
                    iv_big_head.setImageBitmap(item.getBitmap());
                }
            }
        });

        btn_send_fu_ka.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int sum = 0;
                for (int i = 0; i < FuKaArray.myFuKaNameArray.length; i++) {
                    sum += FuKaArray.myFuKaNumberArray[i];
                }
                if (sum <= 0) {
                    ToastUnits.show(FrendInformationActivity.this, "您现在没有任何的福卡！");
                    return;
                } else {
                    Intent intent = new Intent(FrendInformationActivity.this, FuKaListActivity.class);
                    intent.putExtra("friendName", item.getName());
                    intent.putExtra("friendId", item.getUserId());

                    startActivity(intent);
                    finish();
                    return;
                }
            }
        });

        lv_2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        String phone = mAdapter_2.getItem(position).getValue();
                        if (isEmptyString(phone)) {
                            ToastUnits.show(FrendInformationActivity.this, "手机号无效");
                            return;
                        } else {
                            Intent intent = new Intent();
                            // 设置动作
                            intent.setAction(Intent.ACTION_DIAL);
                            // 设置协议头，设置数据
                            intent.setData(Uri.parse("tel:" + phone));
                            // 开启系统拨号界面
                            startActivity(intent);

                        }
                        break;
                    case 1://
                        String email = mAdapter_2.getItem(position).getValue();
                        if (isEmptyString(email)) {
                            ToastUnits.show(FrendInformationActivity.this, "邮箱号无效");
                            return;
                        } else {
//                            String[] emails = {email}; // 邮件必须以数组形式转入
                            Intent intent = new Intent(Intent.ACTION_SENDTO,Uri.fromParts("mailto",email,null));
                            // 设置动作
                            intent.putExtra(Intent.EXTRA_SUBJECT, "");
                            intent.putExtra(Intent.EXTRA_TEXT, "");
                            startActivity(Intent.createChooser(intent, "请选择邮件类应用"));
//                               startActivity(intent); // 如果系统没有应用会报异常

                        }
                        break;

                }


            }
        });

        btn_add_frend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncTask<String, Void, String>() {
                    @Override
                    protected String doInBackground(String... strings) {
                        String url = MyApplication.ServerIP + "/ScanFuServer/rest/addFriend.action"
                                + "?username=" + app.getUserId()
                                + "&friend=" + item.getUserId();

                        return HttpUtils.httpGet(url);
                    }

                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        btn_add_frend.setEnabled(false);
                    }

                    @Override
                    protected void onPostExecute(String s) {
                        if (s != null) {
                            try {
                                JSONObject json = new JSONObject(s);
                                if ("success".equals(json.optString("status"))) {
                                    ToastUnits.show(FrendInformationActivity.this, json.optString("tip"));
//                                    btn_add_frend.setVisibility(View.GONE);
//                                    btn_send_fu_ka.setVisibility(View.VISIBLE);
//                                    btn_delete_frend.setVisibility(View.VISIBLE);
                                    item.setFrends(true);
                                    finish();
                                    return;

                                } else {
                                    ToastUnits.show(FrendInformationActivity.this, json.optString("tip"));
                                    return;
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            ToastUnits.show(FrendInformationActivity.this, "网络异常或服务器故障");
                        }


                    }
                }.execute();
            }
        });

        btn_delete_frend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncTask<String, Void, String>() {
                    @Override
                    protected String doInBackground(String... strings) {
                        String url = MyApplication.ServerIP + "/ScanFuServer/rest/rmfriend.action"
                                + "?userid=" + app.getUserId()
                                + "&friuid=" + item.getUserId();
                        return HttpUtils.httpGet(url);
                    }

                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        btn_delete_frend.setEnabled(false);
                    }

                    @Override
                    protected void onPostExecute(String s) {
                        btn_delete_frend.setEnabled(true);
                        if (s != null) {
                            try {
                                JSONObject json = new JSONObject(s);
                                if ("success".equals(json.optString("status"))) {
                                    ToastUnits.show(FrendInformationActivity.this, json.optString("tip"));
                                    finish();
                                    return;
                                } else {
                                    ToastUnits.show(FrendInformationActivity.this, json.optString("tip"));
                                    return;
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            ToastUnits.show(FrendInformationActivity.this, "网络或服务器异常");
                        }

                    }
                }.execute();

            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        initView();
    }

    public void onBackUp(View v) {
        finish();
    }

    // 通过 ID 获取用户信息
    private void getFrendInform(final String userId) {
        if (userId == null || userId.length() == 0) {
            return;
        }

        new AsyncTask<String, Void, String>() {

            @Override
            protected String doInBackground(String... strings) {
                String url = MyApplication.ServerIP + "/ScanFuServer/rest/userprofile.action"
                        + "?username=" + userId;
                return HttpUtils.httpGet(url);
            }

            @Override
            protected void onPostExecute(String s) {
                if (s != null) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(s);
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
                        item.setCompany(jsonObject.optString("org"));
                        initArray();
                        mAdapter_1.notifyDataSetChanged();
                        mAdapter_2.notifyDataSetChanged();
                        tv_name.setText(item.getName());
                        tv_job.setText(item.getJobtitle());

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                URL url;
                                try {
                                    url = new URL(item.getImgUrl());
                                    Bitmap bitmap = BitmapFactory.decodeStream(url.openStream());
                                    item.setBitmap(bitmap);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (item.getBitmap() != null) {
                                                iv_head.setImageBitmap(item.getBitmap());
                                            }
                                        }
                                    });

                                } catch (MalformedURLException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
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

    public void initArray() {
        list_1.clear();
        list_1.add(new FrendInfoItem(R.drawable.person_company, "单位：", getResult(item.getCompany())));
        list_1.add(new FrendInfoItem(R.drawable.person_dept, "部门：", getResult(item.getDept())));
        list_1.add(new FrendInfoItem(R.drawable.person_job, "职位：", getResult(item.getJobtitle())));

        list_2.clear();
        list_2.add(new FrendInfoItem(R.drawable.person_mobile, "手机号：", getResult(item.getPhone()), true));
        list_2.add(new FrendInfoItem(R.drawable.person_e_mail, "E-Mail：", getResult(item.getE_mail()), true));
        list_2.add(new FrendInfoItem(R.drawable.person_address, "地址：", getResult(item.getAddress()), true));
    }

    // 这个方法处理空字符串或者空指针，“null”为"(空)"
    private String getResult(String s) {
        if (s == null || s.length() == 0 || "null".equals(s)) {
            return " (空) ";
        }
        return s;
    }

    private boolean isEmptyString(String s) {
        boolean rs = false;
        if (s == null || s.length() == 0 || "null".equals(s) || " (空) ".equals(s))
            rs = true;
        return rs;
    }
}
