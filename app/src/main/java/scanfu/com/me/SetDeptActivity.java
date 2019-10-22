package scanfu.com.me;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import scanfu.com.Adapter.SetDeptAdapter;
import scanfu.com.bean.DeptItem;
import scanfu.com.bean.MyApplication;
import scanfu.com.count.R;
import scanfu.com.utils.HttpUtils;

public class SetDeptActivity extends AppCompatActivity {

    TextView tv_title;
    ListView lv_dept;
    SetDeptAdapter mAdapter;
    ArrayList<DeptItem> list=new ArrayList<DeptItem>();
    MyApplication app=MyApplication.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_dept);

        mAdapter=new SetDeptAdapter(list,this);

        initView();
        getDeptArray();
    }

    private void initView() {
        tv_title=(TextView)findViewById(R.id.tv_title);
        lv_dept=(ListView) findViewById(R.id.lv_dept);

        lv_dept.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DeptItem item=mAdapter.getItem(position);
                Intent intent=new Intent();
                intent.putExtra("id",item.getDeptId());
                intent.putExtra("name",item.getName());
                setResult(RESULT_OK,intent);
                finish();

            }
        });

        lv_dept.setAdapter(mAdapter);

        tv_title.setText("选择部门");
    }

    public void onBackUp(View v){
        finish();
    }

    public void getDeptArray(){
        new AsyncTask<String,Void,String>(){
            @Override
            protected String doInBackground(String... strings) {
                String url= MyApplication.ServerIP+"/ScanFuServer/rest/getDeptTree.action"
                        +"?flag=0&validuid="+app.getValiduid();
                return HttpUtils.httpGet(url.trim());
            }

            @Override
            protected void onPostExecute(String s) {
                if(s!=null){
                    list.clear();
                    DeptItem item=null;
                    try{
                        JSONArray jsonarray=new JSONArray(s);
                        JSONObject json;
                        for(int i=0;i<jsonarray.length();i++){
                            json=jsonarray.getJSONObject(i);
                            json=json.getJSONObject("dept");
                            item=new DeptItem();
                            item.setDeptId(json.optInt("id"));
                            item.setName(json.optString("deptName"));
                            list.add(item);
                        }
                        mAdapter.notifyDataSetChanged();
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            }
        }.execute();
    }


}
