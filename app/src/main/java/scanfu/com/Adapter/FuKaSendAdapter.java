package scanfu.com.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import scanfu.com.bean.FuKaArray;
import scanfu.com.bean.SendFuKaItem;
import scanfu.com.count.R;
import scanfu.com.frend.FrendInformationActivity;

public class FuKaSendAdapter extends BaseAdapter {
    ArrayList<SendFuKaItem> list;
    Context ctx;

    public FuKaSendAdapter(ArrayList<SendFuKaItem> list, Context ctx) {
        this.list = list;
        this.ctx = ctx;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public SendFuKaItem getItem(int position) {
        return list == null ? null : list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder=null;
        if(convertView==null){
            viewHolder =new ViewHolder();
            convertView=View.inflate(ctx, R.layout.activity_send_fu_ka_list_item,null);

            viewHolder.tv_fuka_name=(TextView)convertView.findViewById(R.id.tv_fuka_name);
            viewHolder.tv_user_name=(TextView)convertView.findViewById(R.id.tv_user_name);
            viewHolder.tv_date=(TextView)convertView.findViewById(R.id.tv_date);

            //复用
            convertView.setTag(viewHolder);
        }else{
            viewHolder=(ViewHolder)convertView.getTag();
        }

        //设置数据
        final SendFuKaItem item=getItem(position);
        viewHolder.tv_fuka_name.setText(FuKaArray.myFuKaNameArray[item.getPosition()]);
        viewHolder.tv_fuka_name.setText(FuKaArray.myFuKaNameArray[item.getPosition()]);
        viewHolder.tv_user_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ctx, FrendInformationActivity.class);
                intent.putExtra("userid",item.getTouserid());
                ctx.startActivity(intent);
            }
        });
        viewHolder.tv_date.setText(item.getDate());

        return convertView;
    }

    class ViewHolder{
        TextView tv_fuka_name;
        TextView tv_user_name;
        TextView tv_date;
    }

}
