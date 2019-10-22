package scanfu.com.UI;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import scanfu.com.count.R;
import scanfu.com.saomiao.SaoMaActivity;
import scanfu.com.saomiao.SaoZiActivity;


public class SaoMiaoFragment extends Fragment {

    TextView tv_saozi;
    TextView tv_saoma;

    public SaoMiaoFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_sao_miao, container, false);
        tv_saoma = (TextView) v.findViewById(R.id.tv_saoma);
        tv_saozi = (TextView) v.findViewById(R.id.tv_saozi);

        tv_saozi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SaoZiActivity.class);
                startActivity(intent);
            }
        });

        tv_saoma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SaoMaActivity.class);
                startActivity(intent);
            }
        });

        return v;
    }


}
