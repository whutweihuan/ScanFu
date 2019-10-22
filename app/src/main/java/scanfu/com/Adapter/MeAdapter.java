package scanfu.com.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.ArrayList;

import scanfu.com.UI.MeFragment;
import scanfu.com.bean.MeFragmentItem;
import scanfu.com.count.R;

public class MeAdapter extends BaseAdapter {
    ArrayList<MeFragmentItem> list;
    Context ctx;

    public MeAdapter(ArrayList<MeFragmentItem> list, Context ctx) {
        this.list = list;
        this.ctx = ctx;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public MeFragmentItem getItem(int position) {
        return list == null ? null : list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MeFragmentItem item=getItem(position);

        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = View.inflate(ctx, R.layout.fragment_me_item, null);
            viewHolder.iv_logo=(ImageView) convertView.findViewById(R.id.iv_logo);
            viewHolder.tv_title=(TextView) convertView.findViewById(R.id.tv_title);
            convertView.setTag(viewHolder);
        }else{
            viewHolder=(ViewHolder)convertView.getTag();
        }
        viewHolder.iv_logo.setImageResource(item.getResourceId());
        viewHolder.tv_title.setText(item.getName());

        return convertView;
    }

    class ViewHolder {
        public ImageView iv_logo;
        public TextView tv_title;
    }


}
