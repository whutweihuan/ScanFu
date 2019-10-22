package scanfu.com.frend;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;

import scanfu.com.Adapter.FrendListAdapter;
import scanfu.com.DB.InitDataArray;
import scanfu.com.UI.FuKaFragment;
import scanfu.com.bean.FrendItem;
import scanfu.com.bean.FuKaArray;
import scanfu.com.bean.MyApplication;
import scanfu.com.bean.PinYinComparator;
import scanfu.com.count.R;
import scanfu.com.utils.CharacterParser;
import scanfu.com.utils.HttpUtils;
import scanfu.com.utils.MyConstants;
import scanfu.com.utils.ToastUnits;

public class FrendActivity extends AppCompatActivity {
    TextView tv_title;
    TextView tv_dialog;
    MyApplication app = MyApplication.getInstance();
    FrendListAdapter mFrendAdapter = null;
    ArrayList<FrendItem> mFrendArrayList = null; // 存放所有好友的数据
    ArrayList<FrendItem> mSearchArrayList = new ArrayList<FrendItem>(); // 存放搜索好友的数据
    CharacterParser characterParser;
    ListView lv_frend;

    EditText ll_et_search;
    Button btn_clear;
    SideBar side_bar;

    Dialog mDialog = null;

    int fukaIndex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frend);
        mFrendArrayList = InitDataArray.initFrendArray(app);
        mFrendAdapter = new FrendListAdapter(mFrendArrayList, this);
        fukaIndex = getIntent().getIntExtra("position",-1);
        if(fukaIndex == -1){
            finish();
            return;
        }


        initView();
        updateFrendArray();

    }

    private void initView() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_dialog = (TextView) findViewById(R.id.tv_dialog);
        tv_title.setText("好友");
        lv_frend = (ListView) findViewById(R.id.lv_frend);
        side_bar = (SideBar) findViewById(R.id.side_bar);
        ll_et_search = (EditText)findViewById(R.id.ll_et_search);
        btn_clear = (Button)findViewById(R.id.btn_clear);


        lv_frend.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FrendItem item = mFrendAdapter.getItem(position);
//                int x = getIntent().getIntExtra("position", 1);
//                ToastUnits.show(FrendActivity.this, item.getName()+ x + "");
                showSelectDialog(item);
            }
        });

        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_et_search.setText("");
                btn_clear.setVisibility(View.INVISIBLE);
                mFrendAdapter.reSetData(mFrendArrayList);
            }
        });

        ll_et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() == 0){
                    mFrendAdapter.reSetData(mFrendArrayList);
                    btn_clear.setVisibility(View.INVISIBLE);
                }
                else {
                    mSearchArrayList.clear();
                    btn_clear.setVisibility(View.VISIBLE);
                    String name, dept, pinyin, keyword;
                    keyword = s.toString().toLowerCase();
                    for (FrendItem it : mFrendArrayList) {
                        name = it.getName();
                        pinyin = it.getFullSpellpy().toLowerCase();
                        dept = it.getDept();
                        if (name != null && name.contains(s)
                                || dept != null && dept.contains(s)
                                || pinyin != null && pinyin.contains(keyword)) {
                            mSearchArrayList.add(it);
                        }
                    }
                    mFrendAdapter.reSetData(mSearchArrayList);
                }
            }
        });

        lv_frend.setAdapter(mFrendAdapter);
        side_bar.setTextDialog(tv_dialog);
        side_bar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void OnTouchingLetterChanged(String s) {
                int postion = mFrendAdapter.getPositionForSection(s.charAt(0));
                if (postion != -1) {
                    lv_frend.setSelection(postion);
                }

            }
        });

    }

    private void showSelectDialog(final FrendItem item) {
        mDialog = new Dialog(this);
        View view = View.inflate(this,R.layout.dialog_send_fu_ka_notice,null);
        TextView tv_content = view.findViewById(R.id.tv_content);
        TextView tv_sure = view.findViewById(R.id.tv_sure);
        TextView tv_cancel = view.findViewById(R.id.tv_cancel);

        tv_content.setText("送给好友:"+item.getName()+"   "
                + FuKaArray.myFuKaNameArray[getIntent().getIntExtra("position", 1)]+"卡吗?");
//        ToastUnits.show(this,"送给好友:"+item.getName()+FuKaArray.myFuKaNameArray[getIntent().getIntExtra("position", 1)]+"卡?");
        tv_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(item == null){
                    return;
                }
                // 请求服务器，完成赠送
                mDialog.cancel();

                new AsyncTask<String,Void,String >(){

                    @Override
                    protected String doInBackground(String... strings) {
                        String url = MyApplication.ServerIP+"/FuKaServer/SendFuKa"
                                +"?fromuserid="+app.getUserId()
                                +"&touserid=" + item.getUserId()
                                +"&position=" + fukaIndex
                                ;

                        return HttpUtils.httpGet(url);
                    }

                    @Override
                    protected void onPostExecute(String s) {
                        if(s!=null){
                            try {
                                JSONObject json = new JSONObject(s);
                                if("success".equals(json.optString("status"))){
                                    ToastUnits.show(FrendActivity.this,json.optString("tip"));
                                    finish();
                                    return;
                                }else{
                                    ToastUnits.show(FrendActivity.this,json.optString("tip"));
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            ToastUnits.show(FrendActivity.this,"服务器或者网络故障");
                            finish();
                            return;
                        }
                    }
                }.execute();

//                ToastUnits.show(FrendActivity.this,"成功送给好友:"+item.getName()+"   "
//                        +FuKaArray.myFuKaNameArray[getIntent().getIntExtra("position", 1)]);
            }
        });

        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mDialog.setContentView(view);
        mDialog.show();
    }

    private void updateFrendArray() {
        new AsyncTask<String, Void, String>() {

            @Override
            protected String doInBackground(String... strings) {
                String url = "http://" + MyConstants.IP_ADDRESS + ":8080/" +
                        "ScanFuServer/rest/myfriends.action?userid=" + app.getUserId();
                String res = HttpUtils.httpGet(url);

                // 获取到好友数据后，保存到共享数据
                InitDataArray.saveFrendArray(app, res);

                return res;
            }

            @Override
            protected void onPostExecute(String s) {
                FrendItem item;
                JSONObject jsonObject;
                if (characterParser == null) {
                    characterParser = new CharacterParser();
                }
                if (s != null) {
                    // 直接从共享数据中拿出好友数据，这样好处是其他地方可以共享数据
                    mFrendArrayList = InitDataArray.initFrendArray(app);

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            for (FrendItem it : mFrendArrayList) {
                                try {
                                    URL url = new URL(it.getImgUrl());
                                    Bitmap bitmap = BitmapFactory.decodeStream(url.openStream());
                                    it.setBitmap(bitmap);

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            // 这里代码是在 UI 线程执行
                                            mFrendAdapter.notifyDataSetChanged();

                                        }
                                    });

                                } catch (MalformedURLException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }).start();


                    // 根据 A - Z 排序好友列表
                    Collections.sort(mFrendArrayList, new PinYinComparator());

                    //todo mAdsapter
                    mFrendAdapter.reSetData(mFrendArrayList);


                }

            }
        }.execute();
    }


    public void onBackUp() {
        finish();
    }

}
