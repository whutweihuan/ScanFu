package scanfu.com.Adapter;

import android.content.Context;
import android.content.Intent;
import android.icu.util.ValueIterator;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import scanfu.com.bean.FuKaArray;
import scanfu.com.bean.ReceivedFuKaItem;
import scanfu.com.count.R;
import scanfu.com.frend.FrendInformationActivity;

public class FuKaReceivedAdapter extends BaseAdapter {
    ArrayList<ReceivedFuKaItem> list;
    Context ctx;

    public FuKaReceivedAdapter(ArrayList<ReceivedFuKaItem> list, Context ctx) {
        this.list = list;
        this.ctx = ctx;
    }

    @Override
    public int getCount() {
        return list == null ? null : list.size();
    }

    @Override
    public ReceivedFuKaItem getItem(int position) {
        return list == null ? null : list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView ==null){
            viewHolder = new ViewHolder();
            convertView = View.inflate(ctx,R.layout.activity_received_fu_ka_list_item,null);
            viewHolder.tv_fuka_name = (TextView)convertView.findViewById(R.id.tv_fuka_name);
            viewHolder.tv_user_name = (TextView)convertView.findViewById(R.id.tv_user_name);
            viewHolder.tv_date = (TextView)convertView.findViewById(R.id.tv_date);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final ReceivedFuKaItem item  = getItem(position);
        viewHolder.tv_user_name.setText(item.getFromusername());
        viewHolder.tv_fuka_name.setText(FuKaArray.myFuKaNameArray[item.getPosition()]);
        viewHolder.tv_date.setText(item.getDate());
        viewHolder.tv_fuka_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ctx, FrendInformationActivity.class);
                intent.putExtra("userid",item.getFromusername());
                ctx.startActivity(intent);
            }
        });


        return convertView;
    }

    class ViewHolder {
        TextView tv_fuka_name;
        TextView tv_user_name;
        TextView tv_date;

    }

}
