package scanfu.com.fuka;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import scanfu.com.Adapter.FuKaListAdapter;
import scanfu.com.bean.FuKaArray;
import scanfu.com.bean.MyApplication;
import scanfu.com.count.R;
import scanfu.com.utils.HttpUtils;
import scanfu.com.utils.MyStringUtils;
import scanfu.com.utils.ToastUnits;

public class FuKaListActivity extends AppCompatActivity {
    TextView tv_title;
    MyApplication app = MyApplication.getInstance();
    FuKaListAdapter mAdapter;
    ListView lv_fu_ka;

    String friendName;
    String friendId;
    Dialog dialog;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fu_ka_list);

        friendName=getIntent().getStringExtra("friendName");
        friendId=getIntent().getStringExtra("friendId");

        if(MyStringUtils.isEmpty(friendId)){
            ToastUnits.show(this,"好友帐号无效");
            finish();
            return;
        }
        mAdapter = new FuKaListAdapter(this);
        initView();

    }

    private void initView() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        lv_fu_ka = (ListView) findViewById(R.id.lv_fu_ka);

        pd=new ProgressDialog(this);
        pd.setTitle("提示");
        pd.setMessage("正在请求服务器。。。。");

        lv_fu_ka.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,final int position, long id) {
                if (FuKaArray.myFuKaNumberArray[position] == 0) {
                    return;
                }
                dialog = new Dialog(FuKaListActivity.this);
                View view =View.inflate(FuKaListActivity.this,R.layout.dialog_send_fu_ka_notice,null);
                TextView tv_content=(TextView)view.findViewById(R.id.tv_content);
                TextView tv_sure=(TextView)view.findViewById(R.id.tv_sure);
                TextView tv_cancel=(TextView)view.findViewById(R.id.tv_cancel);
                tv_content.setText("确定送给好友：\""+friendName+"\"     \""+
                        FuKaArray.myFuKaNameArray[position]+"\"   吗?");

                tv_sure.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                        new AsyncTask<String,Void,String>(){
                            @Override
                            protected void onPreExecute() {
                                pd.show();
                            }

                            @Override
                            protected String doInBackground(String... strings) {
                                String url=MyApplication.ServerIP+"/FuKaServer/SendFuKa"
                                        +"?fromuserid="+app.getUserId()
                                        +"&touserid="+friendId
                                        +"&position="+position;
                                return HttpUtils.httpGet(url);
                            }

                            @Override
                            protected void onPostExecute(String s) {
                                pd.cancel();
                                if(s!=null){
                                    try {
                                        JSONObject json=new JSONObject(s);
                                        if("success".equals(json.optString("status"))){
                                            ToastUnits.show(FuKaListActivity.this,"已经赠送给好友：\""+friendName
                                                    +"\" "+FuKaArray.myFuKaNameArray[position]);
                                            finish();
                                            return;
                                        }else{
                                            ToastUnits.show(FuKaListActivity.this,"赠送失败:"+json.optString("tip"));
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }else{
                                    ToastUnits.show(FuKaListActivity.this,"网络异常");
                                    return;
                                }
                            }
                        }.execute();

                    }
                });
                tv_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                        dialog=null;
                    }
                });

                dialog.setContentView(view);

                dialog.show();

            }
        });

        tv_title.setText("送福卡");
        lv_fu_ka.setAdapter(mAdapter);

    }

    @Override
    protected void onResume() {
        getFuKaList();
        super.onResume();
    }

    private void getFuKaList() {
        new AsyncTask<String, Void, String>() {
            @Override
            protected void onPreExecute() {
                pd.show();
            }
            @Override
            protected String doInBackground(String... strings) {

                String url = MyApplication.ServerIP + "/FuKaServer/GetFuKaListById"
                        + "?userid=" + app.getUserId();
                return HttpUtils.httpGet(url);
            }

            @Override
            protected void onPostExecute(String s) {
                pd.cancel();
                if (s != null) {
                    try {
                        JSONObject json = new JSONObject(s);
                        if ("success".equals(json.optString("status"))) {
                            json = json.getJSONObject("fuka");
                            FuKaArray.myFuKaNumberArray[FuKaArray.FENDOUFU] = json.optInt("fendoufu");
                            FuKaArray.myFuKaNumberArray[FuKaArray.ZIQIANGFU] = json.optInt("ziqiangfu");
                            FuKaArray.myFuKaNumberArray[FuKaArray.CHENGZHANGFU] = json.optInt("chengzhangfu");
                            FuKaArray.myFuKaNumberArray[FuKaArray.AIGUOFU] = json.optInt("aiguofu");
                            FuKaArray.myFuKaNumberArray[FuKaArray.ZUNSHIFU] = json.optInt("zunshifu");

                            //TODO 刷新Adapter
                            mAdapter.notifyDataSetChanged();

                        }
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
}
