package scanfu.com.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import scanfu.com.bean.AddFrendItem;
import scanfu.com.count.R;

public class AddFrendAdapter extends BaseAdapter {
    ArrayList<AddFrendItem> mUserArrayList;
    Context ctx;

    public AddFrendAdapter(ArrayList<AddFrendItem> mUserArrayList, Context ctx) {
        this.mUserArrayList = mUserArrayList;
        this.ctx = ctx;
    }

    @Override
    public int getCount() {
        return mUserArrayList == null ? 0 : mUserArrayList.size();
    }

    @Override
    public AddFrendItem getItem(int position) {
        return mUserArrayList == null ? null : mUserArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = View.inflate(ctx, R.layout.activity_add_frend_item, null);
            viewHolder.iv_head = (ImageView) convertView.findViewById(R.id.iv_head);
            viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            viewHolder.tv_dept = (TextView) convertView.findViewById(R.id.tv_dept);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // 数据处理
        AddFrendItem item = getItem(position);
        if (item.getBitmap() != null) {
            viewHolder.iv_head.setImageBitmap(item.getBitmap());
        }else {
            viewHolder.iv_head.setImageResource(R.drawable.tou_default);
        }
        viewHolder.tv_name.setText(item.getName());
        viewHolder.tv_dept.setText(item.getDept());

        return convertView;
    }

    class ViewHolder {
        ImageView iv_head;
        TextView tv_dept;
        TextView tv_name;
    }
}
