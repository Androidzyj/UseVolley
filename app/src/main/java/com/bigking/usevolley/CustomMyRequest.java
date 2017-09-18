package com.bigking.usevolley;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.io.UnsupportedEncodingException;

/**
 * Created by Administrator on 2017/9/18.
 */

public class CustomMyRequest extends Request<XmlPullParser> {
    private final Response.Listener<XmlPullParser> mListener;


    public CustomMyRequest(int method, String url, Response.Listener<XmlPullParser> listener, Response.ErrorListener mListener) {
        super(method, url, listener);
        this.mListener = mListener;
    }

   /* public CustomMyRequest(String url, Response.Listener<XmlPullParser> listener, Response.ErrorListener errorListener){
     this(Method.GET,url,listener,errorListener);
    }*/


    @Override
    protected Response<XmlPullParser> parseNetworkResponse(NetworkResponse response) {
        // TODO: 2017/9/18 xml文件解析 PULL方式解析
        try {
            String xmlString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = factory.newPullParser();
            xmlPullParser.setInput(new StringReader(xmlString));
             return  Response.success(xmlPullParser,HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return Response.error(new ParseError(e));
        } catch (XmlPullParserException e) {
            e.printStackTrace();
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(XmlPullParser response) {
        mListener.onResponse(response);
    }
}
