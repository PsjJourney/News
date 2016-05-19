package com.psj.news;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import util.HtmlImageGetter;

/**
 * Created by psj on 2016/5/19.
 */
public class NewsInfoActivity extends Activity {
    public String url = "";
    public String address = "http://api.ithome.com/xml/newscontent/";
    public String time = "";
    public String address1 = "";
    public String address2 = "";
    public String title = "";
    public String postdate = "";
    public TextView text, titleName, postDate, newsource, z;

    private Handler requestHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2:
//                    Toast.makeText(MainActivity.this, "request success", Toast.LENGTH_SHORT).show();
                    String tt = (String) msg.getData().get("detail");
                    String hh = (String) msg.getData().get("newssource");
                    newsource.setText(hh);
                    String jj = (String) msg.getData().get("z");
                    z.setText("(" + jj + ")");

//                    text.setText(Html.fromHtml(tt));
                    Drawable defaultDrawable = NewsInfoActivity.this.getResources().getDrawable(R.mipmap.defaulta);
                    Spanned sp = Html.fromHtml(tt, new HtmlImageGetter(text, "/MyNewsImg", defaultDrawable, NewsInfoActivity.this), null);
                    text.setText(sp);
                    break;
                case 0:
                    Toast.makeText(NewsInfoActivity.this, "request failed", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_info_layout);

        text = (TextView) findViewById(R.id.text);
        postDate = (TextView) findViewById(R.id.post_date);
        newsource = (TextView) findViewById(R.id.newssource);
        z = (TextView) findViewById(R.id.z);
        titleName = (TextView) findViewById(R.id.title_name);

        time = System.currentTimeMillis() + "";
        Intent intent = this.getIntent();
        url = intent.getStringExtra("url");
        title = intent.getStringExtra("title");
        titleName.setText(title);
        postdate = intent.getStringExtra("postdate");
        postDate.setText(postdate);

        String pattern = "/";
        Pattern pat = Pattern.compile(pattern);
        String[] rs = pat.split(url);
        for (int i = 0; i < rs.length; i++) {
//            Log.e("aaa", rs[i]);
        }
        url = rs[3];
//        Log.e("url", url);
        address1 = url.substring(0, 3);
//        Log.e("address1", address1);
        address2 = url.substring(3, 6);
//        Log.e("address2", address2);
//        Toast.makeText(this, url, Toast.LENGTH_LONG).show();
        address = address + address1 + "/" + address2 + ".xml?r=" + time;
//        Log.e("address", address);
//        getNewsData(address);


        new Thread(new Runnable() {
            @Override
            public void run() {

//                String path = "http://api.ithome.com/xml/newslist/news.xml?r="+time;
                HttpURLConnection con = null;
                try {
                    con = (HttpURLConnection) new URL(address)
                            .openConnection();
                    con.setConnectTimeout(15000);
                    con.setRequestMethod("GET");
                    int i = con.getResponseCode();
                    if (i == 200) {
                        InputStream in = con.getInputStream();
//                        return parseXML(in);
//                        Log.e("in",in.toString());
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                        StringBuilder responseBuilder = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            responseBuilder.append(line);
                        }
//                        Log.e("responseBuilder",responseBuilder+"");
                        parseXMLWithPull(responseBuilder + "");
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    public void getNewsData(String path) {
        OkHttpClient mOkHttpClient = new OkHttpClient();
        final Request request = new Request.Builder().url(path).build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("onFailure", "onFailure");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.e("reponse", response.body().string());
                parseXMLWithPull(response.body().string());
                Log.e("onResponse", response.body().string());
            }
        });
    }

    private void parseXMLWithPull(String xmlData) {
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = factory.newPullParser();
            xmlPullParser.setInput(new StringReader(xmlData));
            int eventtype = xmlPullParser.getEventType();

            String newssource = "";
            String newsauthor = "";
            String detail = "";
            String z = "";
            String tags = "";

            while (eventtype != XmlPullParser.END_DOCUMENT) {
                String nodeName = xmlPullParser.getName();
                switch (eventtype) {
                    case XmlPullParser.START_TAG: {
                        if ("newssource".equals(nodeName)) {
                            newssource = xmlPullParser.nextText();
                        } else if ("newsauthor".equals(nodeName)) {
                            newsauthor = xmlPullParser.nextText();
                        } else if ("detail".equals(nodeName)) {
                            detail = xmlPullParser.nextText();
                        } else if ("z".equals(nodeName)) {
                            z = xmlPullParser.nextText();
                        } else if ("tags".equals(nodeName)) {
                            tags = xmlPullParser.nextText();
                        }
                        break;
                    }
                    case XmlPullParser.END_TAG: {
                        if ("item".equals(nodeName)) {
//                            text.setText(Html.fromHtml(detail));

//                            Log.e("newsid",newsid);
//                            Log.e("title",title);
//                            Log.e("c",c);
//                            Log.e("v",v);
//                            Log.e("url",url);
//                            Log.e("postdate",postdate);
//                            Log.e("image",image);
//                            Log.e("description",description);
//                            Log.e("hitcount",hitcount);
//                            Log.e("commentcount",commentcount);
//                            Log.e("forbidcomment",forbidcomment);
//                            Log.e("tags",tags);

//                            bundle.putString("newsid", newsid);
//                            bundle.putString("title", title);
//                            bundle.putString("c", c);
//                            bundle.putString("v", v);
//                            bundle.putString("url", url);
//                            bundle.putString("postdate", postdate);
//                            bundle.putString("image", image);
//                            bundle.putString("description", description);
//                            bundle.putString("hitcount", hitcount);
//                            bundle.putString("commentcount", commentcount);
//                            bundle.putString("forbidcomment", forbidcomment);
//                            bundle.putString("tags", tags);


//                            NewsDate newsDate = new NewsDate();
//                            newsDate.setNewsid(newsid);
//                            newsDate.setTitle(title);
//                            newsDate.setC(c);
//                            newsDate.setV(v);
//                            newsDate.setUrl(url);
//                            newsDate.setPostdate(postdate);
//                            newsDate.setImage(image);
//                            newsDate.setDescription(description);
//                            newsDate.setHitcount(hitcount);
//                            newsDate.setCommentcount(commentcount);
//                            newsDate.setForbidcomment(forbidcomment);
//                            newsDate.setTags(tags);
//                            newsList.add(newsDate);

                        }
                        Message msg = new Message();
                        msg.what = 2;
                        Bundle bundle = new Bundle();
                        bundle.putString("detail", detail);
                        bundle.putString("newssource", newssource);
                        bundle.putString("z", z);
                        msg.setData(bundle);
                        requestHandler.sendMessage(msg);
                        break;
                    }
                    default:
                        break;
                }
                eventtype = xmlPullParser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
