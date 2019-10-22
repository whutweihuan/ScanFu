package scanfu.com.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import scanfu.com.bean.FuKaArray;
import scanfu.com.count.R;

public class FuKaAdapter extends BaseAdapter {
    private Context ctx;
    LayoutInflater inflater;
    int selected = 0; // 默认选中

    public int getSelected() {
        return selected;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }

    public FuKaAdapter(Context ctx) {

        this.ctx = ctx;
        inflater = LayoutInflater.from(ctx);
    }

    @Override
    public int getCount() {
//        return FuKaArray.myFuKaNameArray.length;
        return 5;
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
        ViewHolder viewHolder = null;

        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView  = inflater.inflate(R.layout.fu_ka_item,null);
            viewHolder.iv_fuka_item = (ImageView) convertView.findViewById(R.id.iv_fuka_item);
            viewHolder.iv_fuka_item_select = (ImageView) convertView.findViewById(R.id.iv_fuka_item_select);
            viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            viewHolder.tv_number = (TextView) convertView.findViewById(R.id.tv_number);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder)convertView.getTag();
        }
        viewHolder.tv_name.setText(FuKaArray.myFuKaNameArray[position]);
        viewHolder.tv_number.setText(FuKaArray.myFuKaNumberArray[position]+"");

        if(FuKaArray.myFuKaNumberArray[position] == 0){
            viewHolder.tv_number.setBackgroundResource(R.drawable.fuka_number_bg_wu);
            viewHolder.tv_name.setTextColor(ctx.getResources().getColor(R.color.fu_zi_wu));
            viewHolder.iv_fuka_item.setBackgroundResource(R.drawable.fu_item_bg_wu);
        } else{
            viewHolder.tv_number.setBackgroundResource(R.drawable.fuka_number_bg_you);
            viewHolder.tv_name.setTextColor(ctx.getResources().getColor(R.color.fu_zi_you));
            viewHolder.iv_fuka_item.setBackgroundResource(R.drawable.fu_item_bg_you);
        }
        if(selected == position){
            // 当前选择的item
            viewHolder.iv_fuka_item_select.setVisibility(View.VISIBLE);
        }else{
            viewHolder.iv_fuka_item_select.setVisibility(View.INVISIBLE);
        }

        return convertView;

    }

    class  ViewHolder{
        TextView tv_number;
        TextView tv_name;
        ImageView iv_fuka_item;
        ImageView iv_fuka_item_select;

    }
}
