package com.imooc.mooo;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.imooc.mooo.bean.ChatMessage;
import com.imooc.mooo.utils.HttpUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * 本例使用图灵API实现智能机器人客服的聊天效果
 *
 * 图灵机器人官网：http://www.tuling123.com
 */
public class MainActivity extends Activity {
    private ListView mMsgs;
    private ChatMessageAdapter mAdapter;
    private List<ChatMessage> mDatas;

    private EditText mInputMsg;
    private Button mSendMsg;

    /**
     * 用于动态更新聊天界面。也可使用异步替代Hanlder来实现
     */
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            //等待接收，子线程完成数据的返回，并将返回的消息添加到集合，更新适配器
            ChatMessage fromMessage= (ChatMessage) msg.obj;
            mDatas.add(fromMessage);
            mAdapter.notifyDataSetChanged();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        testHttpUtils();//测试工具类

        initView();
        initDatas();
        initListener();
    }

    /**
     * 初始化事件
     */
    private void initListener() {
        mSendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String toMsg=mInputMsg.getText().toString();

                if (TextUtils.isEmpty(toMsg)){
                    Toast.makeText(MainActivity.this,"发送消息不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }

                //发送消息
                ChatMessage toMessage=new ChatMessage();
                toMessage.setDate(new Date());
                toMessage.setMsg(toMsg);
                toMessage.setType(ChatMessage.Type.OUTCOMING);

                mDatas.add(toMessage);
                mAdapter.notifyDataSetChanged();

                mInputMsg.setText("");//发送后输入框置空

                //接收消息，
                //耗时的网络操作不能在主线程执行
                new Thread(){
                    @Override
                    public void run() {
                        ChatMessage fromMessage=HttpUtils.sendMessage(toMsg);

                        Message m=Message.obtain();
                        m.obj=fromMessage;
                        mHandler.sendMessage(m);
                    }
                }.start();
            }
        });
    }

    /**
     * 初始化数据
     */
    private void initDatas() {
        mDatas=new ArrayList<>();
        //添加测试数据
        mDatas.add(new ChatMessage("你好，小图灵为您服务", ChatMessage.Type.INCOMING,new Date()));
        //mDatas.add(new ChatMessage("你好", ChatMessage.Type.OUTCOMING,new Date()));

        mAdapter=new ChatMessageAdapter(this,mDatas);
        mMsgs.setAdapter(mAdapter);
    }

    /**
     * 布局初始化
     */
    private void initView() {
        mMsgs= (ListView) this.findViewById(R.id.id_listview_msgs);
        mInputMsg= (EditText) this.findViewById(R.id.id_input_msg);
        mSendMsg= (Button) this.findViewById(R.id.id_send_msg);
    }

    /**
     * 测试HttpUtils工具类是否正确
     */
    private void testHttpUtils() {//主线程不能进行网络访问等耗时操作
        new Thread(){
            @Override
            public void run() {
                String res= HttpUtils.doHttpRequest("给我讲个笑话");
                Log.e("TAG", res);
                //Toast.makeText(MainActivity.this,"res="+res,Toast.LENGTH_SHORT).show();

                res=HttpUtils.doHttpRequest("给我讲个鬼故事");
                Log.i("TAG", res);

                res=HttpUtils.doHttpRequest("你好");
                Log.i("TAG", res);

                res=HttpUtils.doHttpRequest("你真美");
                Log.i("TAG",res);
            }
        }.start();
    }

}
