package scanfu.com.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import java.util.List;

import scanfu.com.bean.FrendItem;
import scanfu.com.count.R;

public class FrendListAdapter extends BaseAdapter implements SectionIndexer {
    private List<FrendItem> list = null;
    private Context context;

    public FrendListAdapter(List<FrendItem> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public FrendItem getItem(int position) {
        return list == null ? null : list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        FrendItem item = (FrendItem) getItem(position);
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.fragment_frend_item, null);
            viewHolder.iv_head = (ImageView) convertView.findViewById(R.id.iv_head);
            viewHolder.tv_Letter = (TextView) convertView.findViewById(R.id.tv_catalog);
            viewHolder.tv_dept = (TextView) convertView.findViewById(R.id.tv_dept);
            viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // 根据 position 获取分类的首字母的 Char ascii 值
        int section = getSectionForPosition(position);
        viewHolder.tv_Letter.setVisibility(View.GONE);

        // 如果当前位置等于该位置分类首字母的 Char 位置，则认为是第一次出现
        if(position == getPositionForSection(section)){
            viewHolder.tv_Letter.setVisibility(View.VISIBLE);
            viewHolder.tv_Letter.setText(item.getSortLetter());
        }

        // 设置头像
        if(item.getBitmap() != null){
            viewHolder.iv_head.setImageBitmap(item.getBitmap());
        } else{
            viewHolder.iv_head.setBackgroundResource(R.drawable.tou_default);
        }
        viewHolder.tv_name.setText(item.getName());
        viewHolder.tv_dept.setText(item.getDept());

        return convertView;
    }

    class ViewHolder {
        TextView tv_Letter;
        TextView tv_name;
        TextView tv_dept;
        ImageView iv_head;

    }


    @Override
    public Object[] getSections() {
        return new Object[0];
    }

    // 根据分类首字母的 char ASCLL 码值获取第一次出现首字母的位置
    @Override
    public int getPositionForSection(int sectionIndex) {
        for (int i = 0; i < getCount(); i++) {
            String sortStr = getItem(i).getSortLetter();
            char firtChar = sortStr.toUpperCase().charAt(0);
            if (firtChar == sectionIndex) {
                return i;
            }

        }
        return -1;
    }

    // 根据Listview的当前首字母获取分类的首字母 Char ASCLL 值
    @Override
    public int getSectionForPosition(int position) {
        return getItem(position).getSortLetter().charAt(0);
    }

    // 提取英文首字母，非英文字母用 # 代替
    private String getAlpha(String  str){
        String sortStr = str.trim().substring(0,1).toUpperCase();
        if(sortStr.matches("[A-Z]")){
            return sortStr;
        }else {
            return "#";
        }
    }

    // 更新数据源，刷新界面
    public void reSetData(List<FrendItem> list){
        this.list = list;
        notifyDataSetChanged();
    }



}












