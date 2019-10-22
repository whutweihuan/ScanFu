package scanfu.com.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import scanfu.com.bean.MyselfInformationItem;
import scanfu.com.count.R;

public class MyselfInformationAdapter extends BaseAdapter {

    ArrayList<MyselfInformationItem> mList;
    Context ctx;

    public MyselfInformationAdapter(ArrayList<MyselfInformationItem> mList, Context ctx) {
        this.mList = mList;
        this.ctx = ctx;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public MyselfInformationItem getItem(int position) {
        return mList == null ? null : mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder=null;
        MyselfInformationItem item=getItem(position);

        if(convertView==null){
            viewHolder=new ViewHolder();
            convertView = View.inflate(ctx, R.layout.activity_myself_information_item, null);
            viewHolder.tv_key=(TextView) convertView.findViewById(R.id.tv_key);
            viewHolder.tv_value=(TextView) convertView.findViewById(R.id.tv_value);

            viewHolder.iv_erweima=(ImageView) convertView.findViewById(R.id.iv_erweima);
            viewHolder.iv_next=(ImageView) convertView.findViewById(R.id.iv_next);
            convertView.setTag(viewHolder);
        }else{
            viewHolder=(ViewHolder)convertView.getTag();
        }
        viewHolder.tv_key.setText(item.getKey());
        viewHolder.tv_value.setText(item.getValue());

        if(item.isErWeiMa()){
            viewHolder.iv_erweima.setVisibility(View.VISIBLE);
            viewHolder.tv_value.setVisibility(View.INVISIBLE);
        }else {
            viewHolder.iv_erweima.setVisibility(View.INVISIBLE);
            viewHolder.tv_value.setVisibility(View.VISIBLE);
        }

        if(item.isNext()){
            viewHolder.iv_next.setVisibility(View.VISIBLE);
        }else{
            viewHolder.iv_next.setVisibility(View.INVISIBLE);
        }

        return convertView;
    }

    class ViewHolder {
        TextView tv_key;
        TextView tv_value;
        ImageView iv_erweima;
        ImageView iv_next;
    }
}
