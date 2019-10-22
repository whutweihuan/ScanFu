package scanfu.com.fuka;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import scanfu.com.Adapter.FuKaSendAdapter;
import scanfu.com.bean.FuKaArray;
import scanfu.com.bean.MyApplication;
import scanfu.com.bean.SendFuKaItem;
import scanfu.com.count.R;
import scanfu.com.utils.HttpUtils;

public class SendFuKaListActivity extends AppCompatActivity {
    MyApplication app = MyApplication.getInstance();
    TextView tv_title;
    ListView lv_send;
    final int pageSize = 8;
    int pageNumber = 1;

    FuKaSendAdapter mAdapter = null;
    ArrayList<SendFuKaItem> list = new ArrayList<SendFuKaItem>();
    LinearLayout mLoadLayoutBottomView = null;
    LinearLayout mLoadLayoutHeadView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_fu_ka_list);

        mAdapter = new FuKaSendAdapter(list, this);
        initView();

        getDataArray();
    }

    private void initView() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        lv_send = (ListView) findViewById(R.id.lv_send);

        mLoadLayoutBottomView = (LinearLayout)View.inflate(this,R.layout.footer_view,null);;
        mLoadLayoutHeadView = (LinearLayout)View.inflate(this,R.layout.footer_view,null);;


        lv_send.setOnScrollListener(mOnScrollListener);
        tv_title.setText("送出的福卡");
        lv_send.setAdapter(mAdapter);

    }

    private void getDataArray() {
        new AsyncTask<String, Void, String>() {

            @Override
            protected String doInBackground(String... strings) {

                String url = MyApplication.ServerIP + "/FuKaServer/GetSendFuKaList"
                        + "?userid=" + app.getUserId()
                        + "&pageSize=" + pageSize
                        + "&pageNumber=" + pageNumber;
                return HttpUtils.httpGet(url);
            }

            @Override
            protected void onPostExecute(String s) {
                if (s != null) {
                    try {
                        JSONObject json = new JSONObject(s);
                        if ("success".equals(json.optString("status"))) {
                            JSONArray jsonarray = json.optJSONArray("list");
                            if (jsonarray == null || jsonarray.length() == 0)
                                return;
                            SendFuKaItem item = null;
                            if (pageNumber == 1) {
                                list.clear();
                            }
                            for (int i = 0; i < jsonarray.length(); i++) {
                                item = new SendFuKaItem();
                                json = jsonarray.getJSONObject(i);

                                item.setId(json.optInt("id"));
                                item.setTouserid(json.optString("touserid"));
                                item.setTousername(json.optString("tousername"));
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

    AbsListView.OnScrollListener mOnScrollListener = new AbsListView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            switch (scrollState) {
                case SCROLL_STATE_FLING:
                    //手指已经起来了，但是listView还在滑动
                    break;
                case SCROLL_STATE_TOUCH_SCROLL:
                    //手指还没起来
                    //添加下拉刷新
                    if(view.getFirstVisiblePosition()==0&&
                            lv_send.getChildAt(0).getTop()>=lv_send.getPaddingTop()&&
                            lv_send.getHeaderViewsCount()==0){
                        lv_send.addHeaderView(mLoadLayoutHeadView);
                    }
                    break;
                case SCROLL_STATE_IDLE:
                    //当listview滑动结束了，就把FooterView给移除，并请求下一页数据
                    if (lv_send.getFooterViewsCount() > 0) {
                        lv_send.removeFooterView(mLoadLayoutBottomView);
                    }
//                    //
                    if(lv_send.getHeaderViewsCount()>0){
                        lv_send.removeHeaderView(mLoadLayoutHeadView);
                    }
                    int position = view.getLastVisiblePosition();//获取到listView最后一个可见的item的position
                    if (mAdapter.getCount() > 0 && position >= mAdapter.getCount() - 1) {
                        pageNumber++;
                        getDataArray();
                    }
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if (totalItemCount > 0 && firstVisibleItem + visibleItemCount >= totalItemCount) {
                if (lv_send.getFooterViewsCount() == 0 && totalItemCount >= pageSize) {
                    //添加BottomView
                    lv_send.addFooterView(mLoadLayoutBottomView);
                }

            }
        }
    };


    public void onBackUp(View v) {
        finish();
    }
}
