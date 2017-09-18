package com.bigking.usevolley;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private ImageView imageView;
    private Context context;
    private static final String TAG = "ainActivity";
    private NetworkImageView networkImageView;

    //添加Volley依赖：compile 'com.mcxiaoke.volley:library:1.0.19'
   //创建一个RequestQueue对象
    //将StringRequest对象添加到RequestQueue里面


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();
        imageView = (ImageView) findViewById(R.id.imageView_IV);
        networkImageView = (NetworkImageView) findViewById(R.id.network_image_view);

        // TODO: 2017/9/18  最基本的StringRequest方法
        //实现GET请求
        RequestQueue mQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest("http://www.baidu.com",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "onResponse: ---"+response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: ---");
            }
        });

        mQueue.add(stringRequest);



        //实现POST请求
        StringRequest stringrequest1 = new StringRequest(Request.Method.POST, "http://www.baid.com",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                map.put("params1","value1");
                map.put("params2","value2");
                return super.getParams();
            }
        };

        mQueue.add(stringrequest1);


        // TODO: 2017/9/18 jsonRequest的用法 jsonRequset不能直接创建他的实例
        // TODO: 2017/9/18  通过他的子类jsonObjectRequest或者jsonArrayRequest创建

        JsonObjectRequest jsonobjRequest = new JsonObjectRequest("http://m.weather.com.cn/data/101010100.html", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
       mQueue.add(jsonobjRequest);


        // TODO: 2017/9/18  ImageRequest的用法

        //RGB_565 16位RGB图像 ARGB_4444 16位ARGB图像 ARGB_8888 32位ARGB图像 ALPHA_8 8位的ALPHA图像
        //位数越大代表能储存的图像信息更多，图片越清晰

        /**
         * params3  0 图片允许的最大宽度
         * params4  0 图片允许的最大高度  超过设定宽高后，图片就会压缩
         *
         *如果两个都设置为0的话表示图片无论多大都不会进行压缩处理
         * 一个字节是8bit
         */

        ImageRequest imageRequest = new ImageRequest("http://developer.android.com/images/home/aw_dac.png",
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        // TODO: 2017/9/18  图片加载完成后的设置
                    }
                }, 0, 0, Bitmap.Config.RGB_565, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                    //TODO ：图片未加载到时的设置
            }
        });


        // TODO: 2017/9/18 ImageLoader用法
        /**
         * ImageLoader里面封装了ImageRequest，而且除此之外还能缓存图片，过滤重复的链接，避免重复发送请求
         *
         */
        //第二种可以加载大图片的
        ImageLoader imageLoader1 = new ImageLoader(mQueue,new BitmapCache());


        ImageLoader imageLoader = new ImageLoader(mQueue, new ImageLoader.ImageCache() {
            @Override
            public Bitmap getBitmap(String url) {
                return null;
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {

            }
        });


        /**
         * params1 控件
         * params2 指定加载图片的过程中显示的图片
         * params3 指定图片加载失败时显示的图片
         */
        ImageLoader.ImageListener listener = ImageLoader.getImageListener(imageView,
                R.mipmap.ic_launcher,R.mipmap.ic_launcher_round);


        imageLoader.get("http://img.my.csdn.net/uploads/201404/13/1397393290_5765.jpeg",listener);

        //可指定加载图片的宽度和高度
       // imageLoader.get("http://img.my.csdn.net/uploads/201404/13/1397393290_5765.jpeg",listener,200,200);

       //若是此处需要加载大图的话，最好用到LruCache


        // TODO: 2017/9/18 加载网络图片 NetWorkImageView的用法
        /**
         * NetworkImageView是一个控件，在加载图片的时候会自动获取自身的宽高，然后对比图片的宽度，然后决定是否需要压缩
         * 压缩是全自动化的，不需要干预，而且处理的非常合适
         * 如果设置成warp_content，则图片就会以原始大小显示，不会被压缩
         */
        ImageLoader imageLoader2 = new ImageLoader(mQueue,new BitmapCache());
        networkImageView.setDefaultImageResId(R.mipmap.ic_launcher);
        networkImageView.setErrorImageResId(R.mipmap.ic_launcher_round);
        networkImageView.setImageUrl("http://img.my.csdn.net/uploads/201404/13/1397393290_5765.jpeg",imageLoader2);

        // TODO: 2017/9/18 使用自定义的XMLRequest
        CustomMyRequest customMyRequest = new CustomMyRequest(Request.Method.GET,
                "http://flash.weather.com.cn/wmaps/xml/china.xml",
                new Response.Listener<XmlPullParser>() {
                    @Override
                    public void onResponse(XmlPullParser response) {
                        try {
                            int eventType = response.getEventType();
                            while (eventType!=XmlPullParser.END_DOCUMENT){
                                switch (eventType){
                                    case XmlPullParser.START_TAG:
                                        String nodeName = response.getName();
                                        if ("city".equals(nodeName)){
                                            String pName = response.getAttributeValue(0);
                                            Log.d(TAG, "onResponse: "+pName);
                                        }
                                        break;
                                }
                                eventType = response.next();
                            }
                        } catch (XmlPullParserException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "onErrorResponse: error");
                    }
                }
                );
         mQueue.add(customMyRequest);


        // TODO: 2017/9/18 自定义GSONRequest数据解析
    }
}
