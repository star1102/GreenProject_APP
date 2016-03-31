package com.mp3.listenit;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Created by YDJ on 2016-03-10.
 */

public class exploreActivity extends Activity{

    ArrayList mlist = new ArrayList(); //파일명이 저장되어있는 Arraylist
    ArrayList slist = new ArrayList(); //파일경로가 저장되어있는 Arraylist
    TextView txt_path;
    ListView FileList;
    exploreAdapter adapter;
    AlertDialog.Builder builder;
    String PATH = Environment.getExternalStorageDirectory().getAbsolutePath();

    String ss; // 상위폴더로 가게해주는 변수 (substring 사용)

    //서버에 올릴 음악명:mName, 가수명:sName
    TextView mName;
    TextView sName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);
        txt_path = (TextView) findViewById(R.id.path);
        FileList = (ListView) findViewById(R.id.exploreList);
        getDir(PATH);
        adapter = new exploreAdapter(this, mlist);
        FileList.setAdapter(adapter);

        FileList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                File file = new File(slist.get(position).toString());

                PATH = slist.get(position).toString();

                if (file.isDirectory()) {
                    mlist.clear();
                    listClear();
                    getDir(PATH); // Listview 에 파일리스트들을 뿌려주는 메소드
                    adapter.notifyDataSetChanged();
                } else if (file.isFile()) {

                    Toast.makeText(exploreActivity.this, "업로드 하려면 길게 누르세요.", Toast.LENGTH_SHORT).show();

                }
            }
        });

        //리스트 롱클릭시 AlertDialog 활용하여 업로드창 실행, 업로드버튼 클릭시 업로드메소드 fileUpload() 실행

            FileList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    //폴더를 길게 클릭시 발생하는 이벤트를 방지하는 if문
                    if (!mlist.get(position).toString().endsWith("/")) {
                        if (fileCheck(position)) {

                            final File uploadfile = new File(slist.get(position).toString());

                            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            view = inflater.inflate(R.layout.upload_dialog, null);

                            mName = (TextView)view.findViewById(R.id.txt_mName);
                            sName = (TextView)view.findViewById(R.id.txt_sName);
                            ImageButton btn_upload = (ImageButton)view.findViewById(R.id.btn_upload);

                            builder = new AlertDialog.Builder(exploreActivity.this);
                            builder.setTitle("업로드창");
                            builder.setView(view);

                            btn_upload.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String strM = mName.getText().toString();
                                    String strS = sName.getText().toString();

                                    Thread t = new FileUpload(uploadfile,strM,strS);
                                    t.start();
                                }
                            });
                            builder.show();
                        }
                    }
                    return false;
                }
            });
        }

    //음원파일 체크매소드
    //앱 기획 상 음원파일만 업로드할수 있으므로, 음원파일이 아닐경우 false 를 리턴하여 업로드창이 실행되지 못하게함
    public boolean fileCheck(int position){

            if (slist.get(position).toString().substring(slist.get(position).toString().lastIndexOf(".")).endsWith(".mp3")
                    || slist.get(position).toString().substring(slist.get(position).toString().lastIndexOf(".")).endsWith(".m4a"))
                return true;
            else
                Toast.makeText(exploreActivity.this, "음원파일이 아닙니다.", Toast.LENGTH_SHORT).show();
            return false;
    }

    // 파일업로드 쓰레드

  class FileUpload extends Thread {

      File uploadFile;
      String song_name;
      String singer_name;

      public FileUpload(File up, String m, String s) {

          uploadFile = up;
          song_name = m;
          singer_name = s;
      }

      //외부라이브러리 http component를 이용 웹 서버에 음악파일과 문자열값 전송

      public void run() {

          try {
              String url = "http://192.168.0.24:8080/ListenIt/uploadProc.it";
              HttpClient client = new DefaultHttpClient();
              HttpPost post = new HttpPost(url);
              post.setHeader("Connection", "Keep-Alive");
              post.setHeader("Accept-Charset", "utf-8");
              post.setHeader("ENCTYPE", "multipart/form-data");

              FileBody fbody = new FileBody(uploadFile);
              StringBody mbody = new StringBody(song_name, Charset.forName("UTF-8"));
              StringBody sbody = new StringBody(singer_name, Charset.forName("UTF-8"));

              MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
              entity.addPart("uploadFile0", fbody);
              entity.addPart("song_name", mbody);
              entity.addPart("singer_name", sbody);
              post.setEntity(entity);

              HttpResponse response = client.execute(post);
              HttpEntity resEntity = response.getEntity();
              if (resEntity != null) {
                  Log.i("test", EntityUtils.toString(resEntity));
              }

          }catch(Exception e){
              e.printStackTrace();
          }
      }
  }

    // filelist에 file 목록들을 넣어주는 메소드
    public void getDir(String dirPath) {

        txt_path.setText(PATH);

        File f = new File(dirPath);
        File[] files = f.listFiles();

        for (int i = 0; i < files.length; i++) {

            File file = files[i];
            slist.add(file.getPath());

            if (file.isDirectory()) {

                mlist.add(file.getName() + "/");

            } else {

                mlist.add(file.getName());
            }
        }
    }

    // slist의 데이터 초기화 메소드 (리스트에 중복으로 등록되는것을 방지)

    public void listClear() {

        if (slist.isEmpty()) {
            getDir(PATH);
        } else {
            slist.clear();
        }
    }

    // 버튼 클릭 메소드

    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.go_up:
                // 최상위폴더로 가는것을 방지해주는 if문
                if (txt_path.getText().toString().compareTo("/storage/emulated") == 0 || txt_path.getText().toString().compareTo("/mnt") == 0) {
                    PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
                    getDir(PATH);
                }

                ss = txt_path.getText().toString().substring(0,
                        txt_path.getText().toString().lastIndexOf("/"));

                mlist.clear();
                listClear();
                getDir(ss);
                txt_path.setText(ss);
                adapter.notifyDataSetChanged();
                break;

            case R.id.go_back:
                Intent i = new Intent(this, uploadActivity.class);
                startActivityForResult(i, 0);
                break;
        }
    }
}


