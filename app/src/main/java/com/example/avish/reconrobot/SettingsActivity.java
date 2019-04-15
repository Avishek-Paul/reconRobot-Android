package com.example.avish.reconrobot;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;

public class SettingsActivity extends AppCompatActivity {

    EditText editTextAddress, editTextPort;
    String liveStream;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    public void streamVideo(View view){

        editTextAddress = findViewById(R.id.address);
        editTextPort = findViewById(R.id.port);

//        liveStream = "http://" + editTextAddress.getText().toString() + ":8080/stream";//"http://192.168.0.101:8080/stream";

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("Address", editTextAddress.getText().toString());
        intent.putExtra("Port", editTextPort.getText().toString());
        startActivity(intent);
//        final WebView webView = (WebView)findViewById(R.id.vv);
//        webView.setInitialScale(100);
//        int width = webView.getWidth();
//        int height = webView.getHeight();
//        webView.loadUrl(liveStream + "?width="+width+"&height="+height);
    }

}
