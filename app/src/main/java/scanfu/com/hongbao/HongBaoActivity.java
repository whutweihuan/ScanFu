package scanfu.com.hongbao;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;

import scanfu.com.Adapter.HongBaoAdapter;
import scanfu.com.bean.HongBaoItem;
import scanfu.com.bean.MyApplication;
import scanfu.com.count.R;
import scanfu.com.utils.FloatUtils;
import scanfu.com.utils.HttpUtils;

public class HongBaoActivity extends AppCompatActivity {

    TextView tv_title;
    TextView tv_money;
    TextView tv_number;
    MyApplication app=MyApplication.getInstance();
    ArrayList<HongBaoItem> list=new ArrayList<HongBaoItem>();
    ListView lv_hongbao;
    HongBaoAdapter mAdapter;
    ImageView iv_head;
    Bitmap bm_head;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hong_bao);

        mAdapter=new HongBaoAdapter(list,this);
        initView();
        getHongBaoData();
        updateHeadImage();
    }



    private void initView() {
        tv_title=(TextView)findViewById(R.id.tv_title);
        tv_number=(TextView)findViewById(R.id.tv_number);
        tv_money=(TextView)findViewById(R.id.tv_money);

        lv_hongbao=(ListView) findViewById(R.id.lv_hongbao);
        iv_head=(ImageView) findViewById(R.id.iv_head);

        lv_hongbao.setAdapter(mAdapter);

        tv_title.setText("红包");

    }

    private void getHongBaoData() {
        new AsyncTask<String,Void,String>(){
            @Override
            protected String doInBackground(String... strings) {
                String url=MyApplication.ServerIP+"/FuKaServer/getUserHongBaoList"
                        +"?userid="+app.getUserId();
                return HttpUtils.httpGet(url);
            }

            @Override
            protected void onPostExecute(String s) {
                if(s!=null){
                    try {
                        JSONArray jsonArray=new JSONArray(s);
                        JSONObject json;
                        HongBaoItem item;
                        list.clear();
                        for(int i=0;i<jsonArray.length();i++){
                            json=jsonArray.getJSONObject(i);
                            item=new HongBaoItem();
                            item.setId(json.optInt("id"));
                            item.setUserid(json.optString("userid"));
                            item.setUsername(json.optString("username"));
                            item.setOrg(json.optString("org"));
                            item.setMoney((float) json.optDouble("money",0));
                            item.setDate(json.optString("date"));
                            list.add(item);
                        }
                        //获取数据后刷新界面
                        mAdapter.notifyDataSetChanged();
                        updateData();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.execute();
    }

    private void updateData() {
        float sum=0;
        //计算所有红包的总金额
        for(HongBaoItem item:list){
            sum+=item.getMoney();
        }
        //更新UI
        tv_money.setText(FloatUtils.getStringFromFloatBy2dot(sum) +"");
        tv_number.setText(list.size()+"");//
    }

    private void updateHeadImage() {

        new AsyncTask<String,Void,String>(){
            //这里执行在后台，所以不能操作UI控件，也就不能调用iv_head.setImageBitmap(bm_head);
            @Override
            protected String doInBackground(String... strings) {
                //第一步，获取到个人信息
                String personUrl = MyApplication.ServerIP + "/ScanFuServer/rest/userprofile.action"
                        + "?username=" + app.getUserId();
                String res=HttpUtils.httpGet(personUrl);

                //第二步，获取头像的url
                try {
                    JSONObject json=new JSONObject(res);
                    String imgUrl=MyApplication.ServerIP+"/ScanFuServer/"+json.optString("gravatar");
                    URL url=new URL(imgUrl);
                    bm_head = BitmapFactory.decodeStream(url.openStream());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                if(bm_head!=null){
                    iv_head.setImageBitmap(bm_head);
                }
            }
        }.execute();
    }



    public void onBackUp(View v){
        finish();
    }

}
