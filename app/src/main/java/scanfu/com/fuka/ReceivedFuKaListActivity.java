package scanfu.com.fuka;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import scanfu.com.Adapter.FuKaReceivedAdapter;
import scanfu.com.bean.MyApplication;
import scanfu.com.bean.ReceivedFuKaItem;
import scanfu.com.count.R;
import scanfu.com.utils.HttpUtils;

public class ReceivedFuKaListActivity extends AppCompatActivity {
    TextView tv_title;
    MyApplication app = MyApplication.getInstance();
    ArrayList<ReceivedFuKaItem> list = new ArrayList<ReceivedFuKaItem>();
    ListView lv_received_fu_ka;
    FuKaReceivedAdapter mAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_received_fu_ka_list);

        initView();
        mAdapter = new FuKaReceivedAdapter(list,this);
        getReceivedFuKaList();
    }

    private void getReceivedFuKaList() {
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... strings) {
                String url = MyApplication.ServerIP + "/FuKaServer/GetReceivedFuKaList"
                        + "?userid=" + app.getUserId();
                return HttpUtils.httpGet(url);
            }

            @Override
            protected void onPreExecute() {

            }

            @Override
            protected void onPostExecute(String s) {
                if (s != null) {
                    try {
                        JSONObject json = new JSONObject(s);
                        if ("success".equals(json.optString("status"))) {
                            JSONArray jsonArray = json.optJSONArray("list");
                            if (jsonArray == null) {
                                return;
                            }
                            ReceivedFuKaItem item = null;
                            list.clear();

                            for (int i = 0; i < jsonArray.length(); i++) {
                                json = jsonArray.getJSONObject(i);
                                item = new ReceivedFuKaItem();
                                item.setId(json.optInt("id"));
                                item.setFromid(json.optString("fromuserid"));
                                item.setFromusername(json.optString("fromusername"));
                                item.setPosition(json.optInt("position"));
                                item.setDate(json.optString("date"));
                                list.add(item);
                            }
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

    public void initView() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        lv_received_fu_ka = (ListView) findViewById(R.id.lv_received_fu_ka);


        tv_title.setText("收到的福卡");
        lv_received_fu_ka.setAdapter(mAdapter);
    }

}
