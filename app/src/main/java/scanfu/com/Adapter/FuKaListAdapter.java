package scanfu.com.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import scanfu.com.bean.FuKaArray;
import scanfu.com.count.R;

public class FuKaListAdapter extends BaseAdapter {
    Context ctx;

    public FuKaListAdapter(Context ctx) {
        this.ctx = ctx;
    }

    @Override
    public int getCount() {
        return FuKaArray.myFuKaNameArray.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder=null;
        if(convertView==null){
            viewHolder=new ViewHolder();
            convertView=View.inflate(ctx,R.layout.activity_fu_ka_list_item,null);
            viewHolder.rl_fu_ka_list_item=(RelativeLayout) convertView.findViewById(R.id.rl_fu_ka_list_item);
            viewHolder.iv_fuka_logo=(ImageView) convertView.findViewById(R.id.iv_fuka_logo);
            viewHolder.iv_next=(ImageView) convertView.findViewById(R.id.iv_next);
            viewHolder.tv_fuka_name=(TextView)convertView.findViewById(R.id.tv_fuka_name);
            viewHolder.tv_fuka_number=(TextView)convertView.findViewById(R.id.tv_fuka_number);

            //复用
            convertView.setTag(viewHolder);
        }else{
            viewHolder=(ViewHolder)convertView.getTag();
        }

        viewHolder.tv_fuka_name.setText(FuKaArray.myFuKaNameArray[position]);
        viewHolder.tv_fuka_number.setText(FuKaArray.myFuKaNumberArray[position]+"");//int转化为String

        if(FuKaArray.myFuKaNumberArray[position]==0){
            viewHolder.rl_fu_ka_list_item.setBackgroundResource(R.color.f2f2f2);
            viewHolder.iv_fuka_logo.setImageResource(R.drawable.fu_item_bg_wu);
            viewHolder.tv_fuka_name.setTextColor(ctx.getResources().getColor(R.color.fu_zi_wu));
            viewHolder.tv_fuka_number.setTextColor(ctx.getResources().getColor(R.color.fu_zi_wu));
            viewHolder.iv_next.setVisibility(View.INVISIBLE);//不能用View.GONE
        }else {
            viewHolder.rl_fu_ka_list_item.setBackgroundResource(R.drawable.list_item_style);
            viewHolder.iv_fuka_logo.setImageResource(R.drawable.fu_item_bg_you);
            viewHolder.tv_fuka_name.setTextColor(ctx.getResources().getColor(R.color.black));
            viewHolder.tv_fuka_number.setTextColor(ctx.getResources().getColor(R.color.red));
            viewHolder.iv_next.setVisibility(View.VISIBLE);

        }

        return convertView;
    }

    class ViewHolder{
        RelativeLayout rl_fu_ka_list_item;

        ImageView iv_fuka_logo;
        ImageView iv_next;
        TextView tv_fuka_name;
        TextView tv_fuka_number;

    }

}
