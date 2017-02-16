package com.imooc.mooo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.imooc.mooo.bean.ChatMessage;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * 消息适配器
 */
public class ChatMessageAdapter extends BaseAdapter{
    private List<ChatMessage> mDatas;
    private LayoutInflater mInflater;//用来压榨Item的布局

    public ChatMessageAdapter(Context context,List<ChatMessage> mDatas){
        mInflater=LayoutInflater.from(context);
        this.mDatas=mDatas;
    }

    @Override
    public int getCount() {
        return mDatas!=null ? mDatas.size():0;
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {//Item有左右两个布局，故重写此方法
        ChatMessage chatMessage=mDatas.get(position);
        if (chatMessage.getType()== ChatMessage.Type.INCOMING){
            return 0;//返回接收消息布局
        }
        return 1;//返回发送消息布局
    }

    @Override
    public int getViewTypeCount() {//Item有左右两个布局，故重写此方法
        return 2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ChatMessage chatMessage=mDatas.get(position);
        ViewHolder viewHolder=null;

        if (convertView==null){
            //通过ItemType设置不同的布局
            if (getItemViewType(position)==0){
                convertView=mInflater.inflate(R.layout.item_from_msg,parent,false);

                viewHolder=new ViewHolder();
                viewHolder.mDate= (TextView) convertView.findViewById(R.id.id_from_msg_date);
                viewHolder.mMsg= (TextView) convertView.findViewById(R.id.id_from_msg_info);
            }else {
                convertView=mInflater.inflate(R.layout.item_to_msg,parent,false);

                viewHolder=new ViewHolder();
                viewHolder.mDate= (TextView) convertView.findViewById(R.id.id_to_msg_date);
                viewHolder.mMsg= (TextView) convertView.findViewById(R.id.id_to_msg_info);
            }

            convertView.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }

        //设置数据
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        viewHolder.mDate.setText(sdf.format(chatMessage.getDate()));
        viewHolder.mMsg.setText(chatMessage.getMsg());

        return convertView;
    }

    private static final class ViewHolder{
        TextView mDate;
        TextView mMsg;
    }

}
