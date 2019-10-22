package scanfu.com.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import scanfu.com.bean.DeptItem;
import scanfu.com.count.R;

public class SetDeptAdapter extends BaseAdapter {
    ArrayList<DeptItem> list;
    Context ctx;

    public SetDeptAdapter(ArrayList<DeptItem> list, Context ctx) {
        this.list = list;
        this.ctx = ctx;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public DeptItem getItem(int position) {
        return list == null ? null : list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder=null;

        DeptItem item=getItem(position);

        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = View.inflate(ctx, R.layout.activity_set_dept_item, null);
            viewHolder.tv_dept=(TextView) convertView.findViewById(R.id.tv_dept);
            convertView.setTag(viewHolder);
        }else{
            viewHolder=(ViewHolder)convertView.getTag();
        }
        viewHolder.tv_dept.setText(item.getName());

        return convertView;
    }

    class ViewHolder {
        public TextView tv_dept;
    }
}
