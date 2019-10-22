package scanfu.com.UI;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.JsonToken;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import scanfu.com.Adapter.FuKaAdapter;
import scanfu.com.bean.FuKaArray;
import scanfu.com.bean.MyApplication;
import scanfu.com.count.R;
import scanfu.com.frend.FrendActivity;
import scanfu.com.hongbao.HongBaoActivity;
import scanfu.com.utils.HttpUtils;
import scanfu.com.utils.MyConstants;

public class FuKaFragment extends Fragment {
    MyApplication app;
    GridView gv_fuka;
    FuKaAdapter mAdapter = null;
    Button btn_hongbao;
    Button btn_send_fu_ka;
    private int position = 0; // 显示当前福卡
    TextView tv_fuka_display_name;

    public FuKaFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = MyApplication.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_fu_ka, container, false);
        gv_fuka = (GridView) v.findViewById(R.id.gv_fuka);
        btn_hongbao = (Button) v.findViewById(R.id.btn_hongbao);
        btn_send_fu_ka = (Button) v.findViewById(R.id.btn_send_fu_ka);
        tv_fuka_display_name = (TextView)v.findViewById(R.id.tv_fuka_display_name);
        tv_fuka_display_name.setText(FuKaArray.myFuKaNameArray[position]);

        gv_fuka.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                position = pos;
               updateView();
            }
        });

        btn_hongbao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), HongBaoActivity.class);
                getActivity().startActivity(intent);
            }
        });

        btn_send_fu_ka.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FrendActivity.class);
                intent.putExtra("position",position);
                getActivity().startActivity(intent);

//                mAdapter.notifyDataSetChanged();
            }
        });

        if(FuKaArray.myFuKaNumberArray[position]==0){
            // 当前选择的福卡数量为 0 ，就不显示送给朋友这个按钮
            btn_send_fu_ka.setVisibility(View.INVISIBLE);
        }
        else {
            btn_send_fu_ka.setVisibility(View.VISIBLE);
        }

        return v;
    }

    public void updateView(){
        tv_fuka_display_name.setText(FuKaArray.myFuKaNameArray[position]);
        mAdapter.setSelected(position);
        mAdapter.notifyDataSetChanged();

        if(FuKaArray.myFuKaNumberArray[position]==0){
            // 当前选择的福卡数量为 0 ，就不显示送给朋友这个按钮
            btn_send_fu_ka.setVisibility(View.INVISIBLE);
        }
        else {
            btn_send_fu_ka.setVisibility(View.VISIBLE);
        }


    }

    public void onStart(){
        super.onStart();
        getFuKaList();
    }


    public void getFuKaList() {
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {
                String url = "http://" + MyConstants.IP_ADDRESS + ":8080/FuKaServer/GetFuKaListById?userid=" + app.getUserId();

                return HttpUtils.httpGet(url);
            }

            @Override
            protected void onPostExecute(String s) {

                if (s != null) {
                    try {
                        JSONObject json = new JSONObject(s);
                        if ("success".equals(json.optString("status"))) {
                            JSONObject jsonFuKa = json.getJSONObject("fuka");
                            FuKaArray.myFuKaNumberArray[FuKaArray.FENDOUFU] = jsonFuKa.optInt("fendoufu");
                            FuKaArray.myFuKaNumberArray[FuKaArray.ZIQIANGFU] = jsonFuKa.optInt("ziqiangfu");
                            FuKaArray.myFuKaNumberArray[FuKaArray.CHENGZHANGFU] = jsonFuKa.optInt("chengzhangfu");
                            FuKaArray.myFuKaNumberArray[FuKaArray.AIGUOFU] = jsonFuKa.optInt("aiguofu");
                            FuKaArray.myFuKaNumberArray[FuKaArray.ZUNSHIFU] = jsonFuKa.optInt("zunshifu");
                            int x = 1;
                            // Todo: 更新 UI
                            if (mAdapter != null) {
                                mAdapter.notifyDataSetChanged();
                            } else {
                                mAdapter = new FuKaAdapter(getActivity());
                                gv_fuka.setAdapter(mAdapter);
                            }
                            updateView();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.execute();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        mAdapter = new FuKaAdapter(getActivity());
        gv_fuka.setAdapter(mAdapter);

        getFuKaList();
    }
}
