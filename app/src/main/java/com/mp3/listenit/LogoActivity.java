package com.mp3.listenit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;

/**
 * Created by YDJ on 2016-03-10.
 */
public class LogoActivity extends Activity {

    ImageButton btn_in;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_logo);
        btn_in = (ImageButton) findViewById(R.id.btn_in);
    }

    public void onClick(View v) {

        Intent i = new Intent(this, uploadActivity.class);
        startActivity(i);
    }
}
