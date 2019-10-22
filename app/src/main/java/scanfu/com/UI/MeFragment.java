package scanfu.com.UI;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;

import scanfu.com.Adapter.MeAdapter;
import scanfu.com.bean.FrendItem;
import scanfu.com.bean.MeFragmentItem;
import scanfu.com.bean.MyApplication;
import scanfu.com.count.R;
import scanfu.com.fuka.ReceivedFuKaListActivity;
import scanfu.com.fuka.SendFuKaListActivity;
import scanfu.com.hongbao.HongBaoActivity;
import scanfu.com.me.AboutActivity;
import scanfu.com.me.BangzuYuFanKuiActivity;
import scanfu.com.me.MyselfInformationActivity;
import scanfu.com.me.SetActivity;
import scanfu.com.utils.HttpUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class MeFragment extends Fragment {

    TextView tv_title;
    Button btn_backup;
    ArrayList<MeFragmentItem> mList_1 = new ArrayList<MeFragmentItem>();
    ArrayList<MeFragmentItem> mList_2 = new ArrayList<MeFragmentItem>();
    android.content.SharedPreferences sp = null;


    ListView lv_1, lv_2;

    FrendItem me=new FrendItem();
    MyApplication app=MyApplication.getInstance();

    ImageView iv_head;
    TextView tv_name;
    TextView tv_userid;

    RelativeLayout rl_head;

    public MeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_me, container, false);
        initView(view);
        getMeInformation();
        return view;

    }

    private void initView(View view) {
        tv_title = (TextView) view.findViewById(R.id.tv_title);
        btn_backup = (Button) view.findViewById(R.id.btn_backup);

        lv_1=(ListView)view.findViewById(R.id.lv_1);
        lv_2=(ListView)view.findViewById(R.id.lv_2);

        iv_head=(ImageView)view.findViewById(R.id.iv_head);
        tv_name=(TextView) view.findViewById(R.id.tv_name);
        tv_userid=(TextView)view.findViewById(R.id.tv_userid);

        rl_head=(RelativeLayout) view.findViewById(R.id.rl_head);
        rl_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), MyselfInformationActivity.class);
                startActivity(intent);
            }
        });


        //设置item间距为0
        lv_1.setDividerHeight(0);
        lv_2.setDividerHeight(0);

        lv_1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if("红包".equals(mList_1.get(position).getName())){
                    Intent intent=new Intent(getActivity(), HongBaoActivity.class);
                    startActivity(intent);
                }else if("收到的福卡".equals(mList_1.get(position).getName())) {
                    Intent intent = new Intent(getActivity(), ReceivedFuKaListActivity.class);
                    startActivity(intent);
                }else if("送出的福卡".equals(mList_1.get(position).getName())) {
                    Intent intent = new Intent(getActivity(), SendFuKaListActivity.class);
                    startActivity(intent);
                }else if("帮助与反馈".equals(mList_1.get(position).getName())) {
                    Intent intent = new Intent(getActivity(), BangzuYuFanKuiActivity.class);
                    startActivity(intent);
                }
            }
        });

        lv_2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if("关于".equals(mList_2.get(position).getName())){
                    Intent intent=new Intent(getActivity(), AboutActivity.class);
                    startActivity(intent);
                }else if("注销".equals(mList_2.get(position).getName())) {
//                     getActivity().finish();

                    Intent intent = new Intent(getActivity(), scanfu.com.count.LoginActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }
            }
        });

        btn_backup.setVisibility(View.INVISIBLE);
        tv_title.setText("我");

        lv_1.setAdapter(new MeAdapter(mList_1,getActivity()));
        lv_2.setAdapter(new MeAdapter(mList_2,getActivity()));


        if(me.getBitmap()!=null){
            iv_head.setImageBitmap(me.getBitmap());
        }
        tv_name.setText(me.getName());
        tv_userid.setText(app.getUserId());

    }

    void initData() {
        mList_1.clear();
        mList_1.add(new MeFragmentItem(R.drawable.hongbao, "红包"));
        mList_1.add(new MeFragmentItem(R.drawable.fuka_receive, "收到的福卡"));
        mList_1.add(new MeFragmentItem(R.drawable.fuka_send, "送出的福卡"));
        mList_1.add(new MeFragmentItem(R.drawable.bangzuyufankui, "帮助与反馈"));


        mList_2.clear();
        mList_2.add(new MeFragmentItem(R.drawable.guanyu, "关于"));
        mList_2.add(new MeFragmentItem(R.drawable.shezhi, "注销"));
    }

    void getMeInformation(){
        new AsyncTask<String,Void,String>(){
            @Override
            protected String doInBackground(String... strings) {
                String url= MyApplication.ServerIP+"/ScanFuServer/rest/userprofile.action"
                        +"?username="+app.getUserId();
                return HttpUtils.httpGet(url);
            }

            @Override
            protected void onPostExecute(String s) {
                if(s!=null){
                    try{
                        JSONObject json=new JSONObject(s);
                        me.setName(json.optString("name"));
                        me.setUserId(app.getUserId());
                        me.setImgUrl(MyApplication.ServerIP+"/ScanFuServer/"+json.optString("gravatar"));

                        tv_name.setText(me.getName());
                        tv_userid.setText(app.getUserId());

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try{
                                    URL url=new URL(me.getImgUrl());
                                    Bitmap bitmap= BitmapFactory.decodeStream(url.openStream());
                                    me.setBitmap(bitmap);
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            //在UI线程执行，操控iv_head
                                            if(me.getBitmap()!=null){
                                                iv_head.setImageBitmap(me.getBitmap());
                                            }
                                        }
                                    });
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        }).start();

                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            }
        }.execute();
    }

    @Override
    public void onResume() {
        super.onResume();
        getMeInformation();
    }
}
