package cn.edu.scujcc.diandian;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/*
* 频道数据源
* 使用了单例模式保证此类仅有一个对象
**/
public class ChannelLab {
    //单例第1步
    private static ChannelLab INSTANCE = null;

    private List<Channel> data;

    //单例第2步
    public ChannelLab(){
        //TODO 把下面的到吗换成从网络获取数据
        data = new ArrayList<>();
        //删除网络访问
//        getData();
    }

    //单例第3步
    public static ChannelLab getInstance(){
        if (INSTANCE == null){
            INSTANCE = new ChannelLab();
        }
        return INSTANCE;
    }

    /**
     * 生成测试数据
     */
    public void test(){
        data = new ArrayList<>();
        Channel  c = new Channel();
        c.setTitle("中央一台 综合频道");
        c.setQuality("高清");
        c.setCover(R.drawable.cctv1);
        c.setUrl("http://ivi.bupt.edu.cn/hls/cctv1hd.m3u8");
        data.add(c);
        c = new Channel();
        c.setTitle("中央二台 财经频道");
        c.setQuality("高清");
        c.setCover(R.drawable.cctv2);
        c.setUrl("http://ivi.bupt.edu.cn/hls/cctv2hd.m3u8");
        data.add(c);
        c = new Channel();
        c.setTitle("中央三台 综艺频道");
        c.setQuality("高清");
        c.setCover(R.drawable.cctv3);
        c.setUrl("http://ivi.bupt.edu.cn/hls/cctv3hd.m3u8");
        data.add(c);
        c = new Channel();
        c.setTitle("中央四台 中文国际");
        c.setQuality("高清");
        c.setCover(R.drawable.cctv4);
        c.setUrl("http://ivi.bupt.edu.cn/hls/cctv4hd.m3u8");
        data.add(c);
        for (int i=0;i<10;i++) {
            c = new Channel();
            c.setTitle("中央五台 体育频道");
            c.setQuality("高清");
            c.setCover(R.drawable.cctv5);
            data.add(c);
        }
    }

    /**
     * 返回数据总数量
     * @return
     */
    public  int getSize(){
        return data.size();
    }
    /**
     *返回指定位置的频道信息
     * position 数据编号，从0开始
     * position对应的频道对象
     */
    public Channel getChannel(int position){
        return this.data.get(position);
    }
    /*
    * 访问网络得到真实数据，代替以前的test()方法
    */
    void getData(Handler handler){
        //调用单例
        Retrofit retrofit = RetrofitClient.getINSTANCE();
        ChannelApi api = retrofit.create(ChannelApi.class);
        Call<List<Channel>> call = api.getAllChannels();
        //enqueue会自己生成子线程，去执行后续代码
        call.enqueue(new Callback<List<Channel>>() {
            @Override
            public void onResponse(Call<List<Channel>> call,
                                   Response<List<Channel>> response) {
                if (null != response && null != response.body()){
                    Log.d("DianDian","从阿里云得到的数据是：");
                    Log.d("DianDian",response.body().toString());
                    data = response.body();
                    //发出通知
                    Message msg = new Message();
                    msg.what = 1; //自己规定1代表从阿里云获取数据完毕
                    handler.sendMessage(msg);
                }else {
                    Log.d("DianDian","response没有数据！");
                }
            }

            @Override
            public void onFailure(Call<List<Channel>> call, Throwable t) {
                Log.e("DianDian","访问网络失败！",t);
            }
        });
    }
}
