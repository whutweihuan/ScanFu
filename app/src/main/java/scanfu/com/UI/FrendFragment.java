package scanfu.com.UI;


import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.FileObserver;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;

import scanfu.com.Adapter.FrendListAdapter;
import scanfu.com.DB.InitDataArray;
import scanfu.com.bean.FrendItem;
import scanfu.com.bean.MyApplication;
import scanfu.com.bean.PinYinComparator;
import scanfu.com.count.R;
import scanfu.com.frend.AddFrendActivity;
import scanfu.com.frend.FrendInformationActivity;
import scanfu.com.frend.SideBar;
import scanfu.com.saomiao.SaoMaActivity;
import scanfu.com.saomiao.SaoZiActivity;
import scanfu.com.utils.CharacterParser;
import scanfu.com.utils.HttpUtils;
import scanfu.com.utils.MyConstants;

public class FrendFragment extends Fragment {
    MyApplication app;
    FrendListAdapter mFrendAdapter = null;
    ArrayList<FrendItem> mFrendArrayList = null; // 存放所有好友的数据
    ArrayList<FrendItem> mSearchArrayList = null; // 存放搜索好友的数据
    CharacterParser characterParser;
    ListView lv_frend;
    TextView tv_dialog; // 导航条
    SideBar side_bar;

    // 查询功能
    Button btn_clear;
    EditText ll_et_search;
    InputMethodManager imm;
    boolean isCreated = false;

    // 快捷键
    Button btn_hotkey;
    RelativeLayout rl_frend_hotkey;
    LinearLayout ll_add_frend;
    LinearLayout scan_erweima;

    public FrendFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = MyApplication.getInstance();
        mFrendArrayList = InitDataArray.initFrendArray(app);
        mSearchArrayList = new ArrayList<FrendItem>();
        imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_frend, container, false);
        lv_frend = (ListView) view.findViewById(R.id.lv_frend);
        mFrendAdapter = new FrendListAdapter(mFrendArrayList, getContext());
        side_bar = (SideBar) view.findViewById(R.id.side_bar);
        tv_dialog = (TextView) view.findViewById(R.id.tv_dialog);
        side_bar.setTextDialog(tv_dialog);
        btn_clear = (Button) view.findViewById(R.id.btn_clear);
        ll_et_search = (EditText) view.findViewById(R.id.ll_et_search);
        btn_hotkey = (Button) view.findViewById(R.id.btn_hotkey);
        rl_frend_hotkey = (RelativeLayout) view.findViewById(R.id.rl_frend_hotkey);
        scan_erweima = (LinearLayout) view.findViewById(R.id.scan_erweima);
        ll_add_frend = (LinearLayout) view.findViewById(R.id.ll_add_frend);

        scan_erweima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rl_frend_hotkey.setVisibility(View.GONE);
                Intent intent = new Intent(getActivity(), SaoMaActivity.class);
                intent.putExtra("isScanAddFrend", true);
                startActivity(intent);
                getActivity().finish();
//                position = -1
            }
        });

        ll_add_frend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rl_frend_hotkey.setVisibility(View.GONE);
                Intent intent = new Intent(getActivity(), AddFrendActivity.class);
                getActivity().startActivity(intent);
            }
        });

        btn_hotkey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rl_frend_hotkey.setVisibility(View.VISIBLE);
            }
        });

        rl_frend_hotkey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rl_frend_hotkey.setVisibility(View.GONE);
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
                if (s.length() == 0) {
                    mFrendAdapter.reSetData(mFrendArrayList);
                    btn_clear.setVisibility(View.INVISIBLE);
                } else {
                    // 用户已经输入了关键词，需要过滤
                    // 定义几个过滤字段
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

        // 设置触摸监听
        side_bar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void OnTouchingLetterChanged(String s) {
                int postion = mFrendAdapter.getPositionForSection(s.charAt(0));
                if (postion != -1) {
                    lv_frend.setSelection(postion);
                }
            }
        });

        lv_frend.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), FrendInformationActivity.class);
                FrendListAdapter adapter = (FrendListAdapter) lv_frend.getAdapter();
                intent.putExtra("userid", adapter.getItem(position).getUserId());
                getActivity().startActivity(intent);
            }
        });

        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFrendAdapter.reSetData(mFrendArrayList);
                ll_et_search.setText("");
            }
        });

        lv_frend.setAdapter(mFrendAdapter);


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (ll_et_search != null) {
            ll_et_search.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        imm.showSoftInput(v, InputMethodManager.SHOW_FORCED);
                    } else {
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    }
                    if (!isCreated) {
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        isCreated = true;
                    }
                }
            });
        }

        getFrendList();


    }

    @Override
    public void onPause() {
        super.onPause();
        isCreated = false;
    }

    public void getFrendList() {
        new AsyncTask<String, Void, String>() {


            @Override
            protected String doInBackground(String... Params) {
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

                                    getActivity().runOnUiThread(new Runnable() {
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

}
