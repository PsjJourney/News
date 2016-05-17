package com.psj.news;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import java.util.ArrayList;

import adapter.MyAdapter;
import date.NewsDate;

public class MainActivity extends Activity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private String[] myDataset = {"ab", "cd", "ef", "gh", "ij", "kl"};
    private ArrayList<NewsDate> newsList = new ArrayList<>();


    private Handler requestHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2:
                    Toast.makeText(MainActivity.this, "SUCCESSFUL", Toast.LENGTH_SHORT).show();
                    break;
                case 0:
                    Toast.makeText(MainActivity.this, "request failed", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recyleview_layout);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
//        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        // specify an adapter (see also next example)
        mAdapter = new MyAdapter(myDataset);
        mRecyclerView.setAdapter(mAdapter);
//        getData();

//        OkHttpClient client = new OkHttpClient();
//        Request request = new Request.Builder()
//                .url("http://api.ithome.com/xml/newslist/news.xml?r=1463450986839")
//                .build();
//        String url = "http://api.ithome.com/xml/newslist/news.xml?r=1463450986839";
//        try {
//            OkHttpUtil.getStringFromServer(url);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        performSyncHttpRequest();
        new Thread(new Runnable() {
            @Override
            public void run() {
                /*HttpUtil.sendHttpRequest("http://api.ithome.com/xml/newslist/news.xml", new HttpCallbackListener() {
                    @Override
                    public void onFinish(String response) {
                        Message msg = new Message();
                        msg.what = 2;
                        Bundle bundle = new Bundle();
                        bundle.putString("response", response);
//                        Log.e("response",response);
                        msg.setData(bundle);
                        requestHandler.sendMessage(msg);
                    }
                    @Override
                    public void onError(Exception e) {

                    }
                });*/
                String path = "http://api.ithome.com/xml/newslist/news.xml";
                HttpURLConnection con = null;
                try {
                    con = (HttpURLConnection) new URL(path)
                            .openConnection();
                    con.setConnectTimeout(15000);
                    con.setRequestMethod("GET");
                    int i=con.getResponseCode();
                    if(i==200){
                        InputStream in = con.getInputStream();
//                        return parseXML(in);
//                        Log.e("in",in.toString());
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                        StringBuilder responseBuilder = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            responseBuilder.append(line);
                        }
                        Log.e("responseBuilder",responseBuilder+"");
                        parseXMLWithPull(responseBuilder+"");
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

//                return null;
            }
        }).start();


    }

    private void parseXMLWithPull(String xmlData) {
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = factory.newPullParser();
            xmlPullParser.setInput(new StringReader(xmlData));
            int eventtype = xmlPullParser.getEventType();
            String newsid = "";
            String title = "";
            String c = "";
            String v = "";
            String url = "";
            String postdate = "";
            String image = "";
            String description = "";
            String hitcount = "";
            String commentcount = "";
            String forbidcomment = "";
            String tags = "";
            while (eventtype != XmlPullParser.END_DOCUMENT) {
                String nodeName = xmlPullParser.getName();
                switch (eventtype) {
                    case XmlPullParser.START_TAG: {
                        if ("newsid".equals(nodeName)) {
                            newsid = xmlPullParser.nextText();
                        } else if ("title".equals(nodeName)) {
                            title = xmlPullParser.nextText();
                        } else if ("c".equals(nodeName)) {
                            c = xmlPullParser.nextText();
                        } else if ("v".equals(nodeName)) {
                            v = xmlPullParser.nextText();
                        } else if ("url".equals(nodeName)) {
                            url = xmlPullParser.nextText();
                        } else if ("postdate".equals(nodeName)) {
                            postdate = xmlPullParser.nextText();
                        } else if ("image".equals(nodeName)) {
                            image = xmlPullParser.nextText();
                        } else if ("description".equals(nodeName)) {
                            description = xmlPullParser.nextText();
                        } else if ("hitcount".equals(nodeName)) {
                            hitcount = xmlPullParser.nextText();
                        } else if ("commentcount".equals(nodeName)) {
                            commentcount = xmlPullParser.nextText();
                        } else if ("forbidcomment".equals(nodeName)) {
                            forbidcomment = xmlPullParser.nextText();
                        } else if ("tags".equals(nodeName)) {
                            tags = xmlPullParser.nextText();
                        }
                        break;
                    }
                    case XmlPullParser.END_TAG: {
                        if ("item".equals(nodeName)) {
                            Log.e("newsid",newsid);
                            Log.e("title",title);
                            Log.e("c",c);
                            Log.e("v",v);
                            Log.e("url",url);
                            Log.e("postdate",postdate);
                            Log.e("image",image);
                            Log.e("description",description);
                            Log.e("hitcount",hitcount);
                            Log.e("commentcount",commentcount);
                            Log.e("forbidcomment",forbidcomment);
                            Log.e("tags",tags);
                            Message msg = new Message();
                            msg.what = 2;
                            Bundle bundle = new Bundle();
                            bundle.putString("newsid", newsid);
                            bundle.putString("title", title);
                            bundle.putString("c", c);
                            bundle.putString("v", v);
                            bundle.putString("url", url);
                            bundle.putString("postdate", postdate);
                            bundle.putString("image", image);
                            bundle.putString("description", description);
                            bundle.putString("hitcount", hitcount);
                            bundle.putString("commentcount", commentcount);
                            bundle.putString("forbidcomment", forbidcomment);
                            bundle.putString("tags", tags);
                            msg.setData(bundle);
                            requestHandler.sendMessage(msg);

                            NewsDate newsDate = new NewsDate();
                            newsDate.setNewsid(newsid);
                            newsDate.setTitle(title);
                            newsDate.setC(c);
                            newsDate.setV(v);
                            newsDate.setUrl(url);
                            newsDate.setPostdate(postdate);
                            newsDate.setImage(image);
                            newsDate.setDescription(description);
                            newsDate.setHitcount(hitcount);
                            newsDate.setCommentcount(commentcount);
                            newsDate.setForbidcomment(forbidcomment);
                            newsDate.setTags(tags);
                            newsList.add(newsDate);
                        }
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
