package com.mp3.listenit;


import android.app.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;

import android.content.Context;
import android.content.Intent;

import android.media.AudioManager;
import android.media.MediaPlayer;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


import java.io.File;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import java.net.HttpURLConnection;

import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by YDJ on 2016-03-16.
 */

public class uploadActivity extends AppCompatActivity {

    ListView list;
    ArrayList<uploadItem> items = new ArrayList();
    uploadAdapter adapter;
    Handler handler = new Handler();
    ProgressDialog progress;
    String PATH = Environment.getExternalStorageDirectory().getAbsolutePath();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        list = (ListView) findViewById(R.id.uploadList);
        progress = ProgressDialog.show(this, "불러오기", "정보 불러오는중..");
        // parsing
        Thread t = new uploadParse();
        t.start();

        adapter = new uploadAdapter(this, items);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String url = "http://192.168.0.24:8080/ListenIt/download.it?singer_name="+items.get(position).getSinger()+
                        "&song_name="+items.get(position).getMusic()+"&file_name="+items.get(position).getFile();

                try {

                    MediaPlayer mediaPlayer = new MediaPlayer();
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    mediaPlayer.setDataSource(url);
                    mediaPlayer.prepare(); // might take long! (for buffering, etc)
                    mediaPlayer.start();

                }catch(Exception e){
                    Toast.makeText(uploadActivity.this,e.toString(), Toast.LENGTH_LONG).show();
                }

                Toast.makeText(uploadActivity.this, "다운하려면 길게 누르세요.", Toast.LENGTH_SHORT).show();
            }
        });

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String file_name = items.get(position).getSinger() + "-" + items.get(position).getMusic() + ".mp3";
                Toast.makeText(uploadActivity.this,file_name+" 다운로드를 시작합니다.",Toast.LENGTH_SHORT).show();

                String url = "http://192.168.0.24:8080/ListenIt/download.it?singer_name="+items.get(position).getSinger()+
                        "&song_name="+items.get(position).getMusic()+"&file_name="+items.get(position).getFile();

                // 파일다운로드 Thread 실행

                Thread t = new filedownTask(url,file_name);
                t.start();
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuItem item = menu.add(0, 1, 0, "도움말");

        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                AlertDialog.Builder builder = new AlertDialog.Builder(uploadActivity.this);
                builder.setIcon(R.drawable.help);
                builder.setTitle("도움말");
                LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.activity_help,null);
                builder.setNegativeButton("Ok! I got it.",null);
                builder.setView(view);
                builder.show();

                return false;
            }
        });

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionbar.setCustomView(R.layout.upload_actionbar);

        return true;
    }

    //버튼 클릭 메소드

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_explore:
                Intent i = new Intent(this, exploreActivity.class);
                startActivity(i);
                break;
        }
    }


    // 파일다운 Thread
    private class filedownTask extends Thread {

        String strurl;
        String fName;

        public filedownTask(String u, String f) {
            strurl = u;
            fName = f;
        }

        public void run() {

            try {

                URL url = new URL(strurl);
                HttpURLConnection con = (HttpURLConnection)url.openConnection();
                con.setConnectTimeout(10000);
                con.setUseCaches(false);

                if(con.getResponseCode()==con.HTTP_OK) {

                    InputStream in = con.getInputStream();
                    File file = new File(PATH, fName);
                    FileOutputStream out = new FileOutputStream(file);

                    int size=0;
                    byte[] buf = new byte[4096];
                    int bufSize = 0;
                    while ((bufSize = in.read(buf)) > 0) {
                        out.write(buf, 0, bufSize);
                        size+=bufSize;

                    }
                    out.flush();
                    out.close();
                    in.close();
                }

            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    //parsing thread

    class uploadParse extends Thread{

        public void run(){

            String strUrl = "http://192.168.0.24:8080/ListenIt/xmlParse.jsp";

            try{
                URL url = new URL(strUrl);
                HttpURLConnection con = (HttpURLConnection)url.openConnection();
                con.setRequestMethod("POST");
                con.setDoOutput(true);
                con.setDoInput(true);
                InputStream is = con.getInputStream();

                //DOM 파서
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();

                Document doc = builder.parse(is);
                Element root = doc.getDocumentElement();

                NodeList itemList = root.getElementsByTagName("record");

                if(itemList !=null && itemList.getLength()>0){
                    for(int i=0; i<itemList.getLength(); i++){
                        uploadItem tmp = parseNode((Element)itemList.item(i));
                        items.add(tmp);
                    }
                }

            }catch(IOException e){
                e.printStackTrace();
            }catch(ParserConfigurationException e){
                e.printStackTrace();
            }catch (SAXException e){
                e.printStackTrace();
            }

            handler.post(new Runnable() {
                @Override
                public void run() {
                    progress.dismiss();
                    adapter.notifyDataSetChanged();
                }
            });
        }
    }

    // parse 된 데이터를 uploadItem 에 넣어주는 메소드

    public uploadItem parseNode(Element entry){

        String strSinger = "";
        String strMusic ="";
        String strFile="";
        String strDate="";

        Element singer = (Element)entry.getElementsByTagName("singer_name").item(0);
        Element music = (Element)entry.getElementsByTagName("song_name").item(0);
        Element file = (Element)entry.getElementsByTagName("file_name").item(0);
        Element date = (Element)entry.getElementsByTagName("upload_date").item(0);

        if(singer != null){
            Node firstChild = singer.getFirstChild();
            strSinger = firstChild.getNodeValue();
        }
        if(music != null){
            Node firstChild = music.getFirstChild();
            strMusic = firstChild.getNodeValue();
        }
        if(file != null){
            Node firstChild = file.getFirstChild();
            strFile = firstChild.getNodeValue();
        }if(date != null){
            Node firstChild = date.getFirstChild();
            strDate = firstChild.getNodeValue();
        }

        //파싱된 정보를 저장하는 uploadItem 클래스

        uploadItem item = new uploadItem(strSinger,strMusic,strFile,strDate);
        return item;
    }
}
