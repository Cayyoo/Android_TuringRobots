package com.imooc.mooo.test;

import android.test.AndroidTestCase;
import android.test.InstrumentationTestCase;
import android.util.Log;

import com.imooc.mooo.utils.HttpUtils;

/**
 * 单元测试的工具类，测试HttpUtils类是否正确
 *
 * 需要在AndroidManifest.xml中配置环境<uses-library/>、<instrumentation/>
 */
public class TestHttpUtils extends AndroidTestCase {

    public TestHttpUtils() {
        super();
    }

    /**
     * 选中方法名，右键点击运行，测试方法
     * 测试类中的方法名必须以test开头
     */
    public void testSendInfo(){
        String res=HttpUtils.doHttpRequest("给我讲个笑话");
        Log.e("TAG",res);

        res=HttpUtils.doHttpRequest("给我讲个鬼故事");
        Log.e("TAG",res);

        res=HttpUtils.doHttpRequest("你好");
        Log.e("TAG",res);

        res=HttpUtils.doHttpRequest("你真美");
        Log.e("TAG",res);
    }

}
