package com.kerk12.smartcityassistant;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class HelpActivity extends AppCompatActivity {

    WebView helpWebview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        helpWebview = (WebView) findViewById(R.id.help_webview);
        WebSettings settings = helpWebview.getSettings();
        //settings.setJavaScriptEnabled(true);
        helpWebview.loadUrl(getString(R.string.help_url));

    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
