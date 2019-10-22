package scanfu.com.frend;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import scanfu.com.Adapter.AddFrendAdapter;
import scanfu.com.Adapter.FrendListAdapter;
import scanfu.com.Adapter.MyselfInformationAdapter;
import scanfu.com.UI.MainUIActivity;
import scanfu.com.bean.AddFrendItem;
import scanfu.com.bean.FrendItem;
import scanfu.com.bean.MyApplication;
import scanfu.com.bean.MyselfInformationItem;
import scanfu.com.count.R;
import scanfu.com.me.MyselfInformationActivity;
import scanfu.com.utils.HttpUtils;
import scanfu.com.utils.MyConstants;
import scanfu.com.utils.ToastUnits;

public class AddFrendActivity extends AppCompatActivity {
    TextView tv_title;
    EditText et_key;
    int page = 1;
    ListView lv_user;

    final int rowsperpage = 15;
    ArrayList<AddFrendItem> mUserArrayList = new ArrayList<AddFrendItem>();
    ArrayList<AddFrendItem> mUserNewList = new ArrayList<AddFrendItem>();
    AddFrendAdapter mAdapter = null;

    InputMethodManager imm;
    String key;

    LinearLayout mLoadLayoutBottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_frend);
        mAdapter = new AddFrendAdapter(mUserArrayList, this);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        initView();
    }

    public void initView() {
        tv_title = findViewById(R.id.tv_title);
        et_key = (EditText) findViewById(R.id.et_key);
        lv_user = (ListView) findViewById(R.id.lv_user);

        mLoadLayoutBottom = new LinearLayout(this);
        mLoadLayoutBottom.setMinimumHeight(180);
        mLoadLayoutBottom.setGravity(Gravity.CENTER);
        mLoadLayoutBottom.setOrientation(LinearLayout.HORIZONTAL);
        ProgressBar mProgressBar = new ProgressBar(this);
        mProgressBar.setPadding(0, 0, 0, 0);
        LinearLayout.LayoutParams mProgressBarLayoutParams =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        mLoadLayoutBottom.addView(mProgressBar, mProgressBarLayoutParams);
        mProgressBarLayoutParams.height = 180;
        TextView tv_content = new TextView(this);
        tv_content.setText("正在获取数据中...");
        mLoadLayoutBottom.addView(tv_content, mProgressBarLayoutParams);

        lv_user.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(AddFrendActivity.this, FrendInformationActivity.class);
                String  userId = mAdapter.getItem(position).getUserId();
                if(MyApplication.getInstance().getUserId().equals(userId)){
                    // 判断一下，如果是自己，跳转到个人信息
                    intent = new Intent(AddFrendActivity.this, MyselfInformationActivity.class);
                    startActivity(intent);
                    finish();
                    return;
                }
                intent.putExtra("userid", userId);
                startActivity(intent);
                finish();
            }
        });

        lv_user.setOnScrollListener(mScrollListener);
        tv_title.setText("添加好友");

        lv_user.setAdapter(mAdapter);

    }

    public void onSearch(View v) {
        key = et_key.getText().toString().trim();
        if (key == null || key.length() == 0) {
            ToastUnits.show(this, "请输入查询的内容");
            return;
        }
        imm.hideSoftInputFromWindow(et_key.getWindowToken(), 0);

        new AsyncTask<String, Void, String>() {

            @Override
            protected String doInBackground(String... strings) {
                try {
                    String url = MyApplication.ServerIP + "/ScanFuServer/rest/searchuser.action"
                            + "?q=" + URLEncoder.encode(key, "utf-8")
                            + "&page=" + page
                            + "&rowsperpage=" + rowsperpage;


                    return HttpUtils.httpGet(url);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                if (s != null) {
                    try {
                        page = 1;
                        mUserArrayList.clear();
                        JSONArray jsonArray = new JSONArray(s);
                        JSONObject json = null;
                        AddFrendItem item = null;
                        for (int i = 0; i < jsonArray.length(); i++) {
                            json = jsonArray.getJSONObject(i);
                            item = new AddFrendItem();
                            item.setUserId(json.optString("username"));
                            item.setName(json.optString("name"));
                            item.setDept(json.optString("dept"));
                            item.setCompany(json.optString("org"));
                            item.setImgUrl(MyApplication.ServerIP + "/ScanFuServer/" +
                                    json.optString("gravatar"));
                            mUserArrayList.add(item);
                        }
                        mAdapter.notifyDataSetChanged();

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                for (AddFrendItem it : mUserArrayList) {
                                    try {
                                        URL url = new URL(it.getImgUrl());
                                        it.setBitmap(BitmapFactory.decodeStream(url.openStream()));
                                        if (it.getBitmap() != null) {
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    mAdapter.notifyDataSetChanged();
                                                }
                                            });
                                        }
                                    } catch (MalformedURLException e) {
                                        e.printStackTrace();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }).start();


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    ToastUnits.show(AddFrendActivity.this, "网络异常或者服务器故障");
                }
            }
        }.execute();


    }

    AbsListView.OnScrollListener mScrollListener = new AbsListView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            switch (scrollState) {
                case SCROLL_STATE_TOUCH_SCROLL:
                    int position = view.getLastVisiblePosition(); // 获得最后一个可见的 item 的 position
                    if (mAdapter.getCount() > 0 && position >= mAdapter.getCount() - 1) {
                        search();
                    }
                    break;
                case SCROLL_STATE_IDLE:
//                    if (lv_user.getFooterViewsCount() > 0)
//                        lv_user.removeFooterView(mLoadLayoutBottom);

            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if (totalItemCount > 0 && firstVisibleItem + visibleItemCount >= totalItemCount) {
//                if (lv_user.getFooterViewsCount() == 0 && totalItemCount >= rowsperpage)
//                    lv_user.addFooterView(mLoadLayoutBottom);
            }

        }
    };

    // 下划后获得后续
    private void search() {
        if (key == null || key.length() == 0) {
            ToastUnits.show(this, "请输入查询的内容");
            return;
        }
        imm.hideSoftInputFromWindow(et_key.getWindowToken(), 0);

        new AsyncTask<String, Void, String>() {

            @Override
            protected void onPreExecute() {
                lv_user.addFooterView(mLoadLayoutBottom);
            }

            @Override
            protected String doInBackground(String... strings) {
                try {
                    page++;
                    String url = MyApplication.ServerIP + "/ScanFuServer/rest/searchuser.action"
                            + "?q=" + URLEncoder.encode(key, "utf-8")
                            + "&page=" + page
                            + "&rowsperpage=" + rowsperpage;


                    return HttpUtils.httpGet(url);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                lv_user.removeFooterView(mLoadLayoutBottom);
                if (s != null) {
                    try {
                        mUserNewList.clear();
                        JSONArray jsonArray = new JSONArray(s);
                        JSONObject json = null;
                        AddFrendItem item = null;
                        if (jsonArray.length() == 0) {
                            ToastUnits.show(AddFrendActivity.this, "没有数据了，已经到最后一页了!");
                            return;
                        }


                        for (int i = 0; i < jsonArray.length(); i++) {
                            json = jsonArray.getJSONObject(i);
                            item = new AddFrendItem();
                            item.setUserId(json.optString("username"));
                            item.setName(json.optString("name"));
                            item.setDept(json.optString("dept"));
                            item.setCompany(json.optString("org"));
                            item.setImgUrl(MyApplication.ServerIP + "/ScanFuServer/" +
                                    json.optString("gravatar"));
                            mUserNewList.add(item);
                            mUserArrayList.add(item);
                        }
                        mAdapter.notifyDataSetChanged();

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                for (AddFrendItem it : mUserNewList) {
                                    try {
                                        URL url = new URL(it.getImgUrl());
                                        it.setBitmap(BitmapFactory.decodeStream(url.openStream()));
                                        if (it.getBitmap() != null) {
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    mAdapter.notifyDataSetChanged();
                                                }
                                            });
                                        }
                                    } catch (MalformedURLException e) {
                                        e.printStackTrace();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }).start();


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    ToastUnits.show(AddFrendActivity.this, "网络异常或者服务器故障");
                }
            }
        }.execute();
    }

    public void onBackUp(View v) {
        finish();
    }

}
