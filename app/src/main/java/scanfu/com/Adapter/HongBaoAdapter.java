package scanfu.com.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import scanfu.com.bean.HongBaoItem;
import scanfu.com.count.R;
import scanfu.com.utils.FloatUtils;

public class HongBaoAdapter extends BaseAdapter {
    ArrayList<HongBaoItem> list;
    Context ctx;

    public HongBaoAdapter(ArrayList<HongBaoItem> list, Context ctx) {
        this.list = list;
        this.ctx = ctx;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public HongBaoItem getItem(int position) {
        return list == null ? null : list.get(position);

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //初始化UI控件
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = View.inflate(ctx, R.layout.activity_hong_bao_item, null);
            viewHolder.tv_org=(TextView) convertView.findViewById(R.id.tv_org);
            viewHolder.tv_money=(TextView) convertView.findViewById(R.id.tv_money);
            viewHolder.tv_date=(TextView) convertView.findViewById(R.id.tv_date);
            //view复用
            convertView.setTag(viewHolder);
        }else{
            viewHolder=(ViewHolder)convertView.getTag();
        }

        //设置数据
        HongBaoItem item=getItem(position);
        if(item!=null){
            viewHolder.tv_org.setText(item.getOrg());
            viewHolder.tv_money.setText(FloatUtils.getStringFromFloatBy2dot(item.getMoney()) +"  元");
            viewHolder.tv_date.setText(item.getDate());

        }

        return convertView;
    }
    class ViewHolder {
        TextView tv_org;
        TextView tv_money;
        TextView tv_date;
    }
}
