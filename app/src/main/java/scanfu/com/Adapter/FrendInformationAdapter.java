package scanfu.com.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import scanfu.com.bean.FrendInfoItem;
import scanfu.com.bean.FrendItem;
import scanfu.com.count.R;

public class FrendInformationAdapter extends BaseAdapter {
    ArrayList<FrendInfoItem> list;
    Context ctx;

    public FrendInformationAdapter(ArrayList<FrendInfoItem> list, Context ctx) {
        this.list = list;
        this.ctx = ctx;
    }

    @Override
    public int getCount() {
        return list == null?0:list.size();
    }

    @Override
    public FrendInfoItem getItem(int position) {
        return list == null? null:list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        FrendInfoItem item =  getItem(position);
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = View.inflate(ctx,R.layout.activity_frend_information_item,null);
            viewHolder.iv_logo = (ImageView) convertView.findViewById(R.id.iv_logo);
            viewHolder.tv_key = (TextView) convertView.findViewById(R.id.tv_key);
            viewHolder.tv_value = (TextView) convertView.findViewById(R.id.tv_value);
            viewHolder.iv_next = (ImageView)convertView.findViewById(R.id.iv_next);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.iv_logo.setImageResource(item.getReSourceId());
        viewHolder.tv_key.setText(item.getKey());
        viewHolder.tv_value.setText(item.getValue());
        if(item.isShowNext()){
            viewHolder.iv_next.setVisibility(View.VISIBLE);
        }
        else {
            viewHolder.iv_next.setVisibility(View.INVISIBLE);
        }

        return convertView;
    }

    class ViewHolder{
        TextView tv_key;
        ImageView iv_logo;
        TextView tv_value;
        ImageView iv_next;
    }
}
