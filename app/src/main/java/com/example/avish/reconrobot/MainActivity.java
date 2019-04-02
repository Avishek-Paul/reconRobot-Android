package com.example.avish.reconrobot;

import android.graphics.PixelFormat;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements JoystickView.JoystickListener {

    EditText editTextAddress, editTextPort;
    String lastMessage = "";

    private Uri uri;
    private boolean isContinuously = false;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextAddress = findViewById(R.id.address);
        editTextPort = findViewById(R.id.port);

        //allows the background of the joystick to be transparent without black background
        SurfaceView joystick = findViewById(R.id.joystickRight);
        joystick.setZOrderOnTop(true);
        SurfaceHolder joystickHolder = joystick.getHolder();
        joystickHolder.setFormat(PixelFormat.TRANSPARENT);
    }

    public void streamVideo(View view){
        String liveStream = "http://192.168.0.101:8080/stream";

        final WebView webView = (WebView)findViewById(R.id.vv);
        webView.setInitialScale(100);
        int width = webView.getWidth();
        int height = webView.getHeight();
        webView.loadUrl(liveStream + "?width="+width+"&height="+height);
    }

    @Override
    public void onJoystickMoved(float xPercent, float yPercent, int id) {
        Log.d("Right Joystick", "X percent: " + xPercent + " Y percent: " + yPercent);
    }

    @Override
    public void getJoystickPosition(float x, float y, float cx, float cy, boolean active){
        Log.d("Right Joystick", "X Pos: " + x + " Y Pos: " + y);

        double angle;

        float deltaX = x - cx;
        float deltaY = y - cy;
        double thetaRadians = Math.atan2(deltaY, deltaX);

        if (thetaRadians < 0)
            thetaRadians = Math.abs(thetaRadians);
        else
            thetaRadians = 2 * Math.PI - thetaRadians;

        angle = Math.toDegrees(thetaRadians);

        Log.d("Angle: ",  Double.toString(angle));

        String message = "";

        if(!active){
            message = "stop";
        } else if (angle >= 45 && angle <= 135) {
            message = "up";
        } else if (angle > 135 && angle <= 225){
            message = "left";
        } else if (angle > 225 && angle <= 315){
            message = "down";
        } else{
            message = "right";
        }

        if (message != this.lastMessage){
            String dstAddress = editTextAddress.getText().toString();
            int dstPort = Integer.parseInt(editTextPort.getText().toString());

            UdpThread udpThread = new UdpThread(dstAddress, dstPort, message);
            udpThread.start();
            udpThread.interrupt();

            this.lastMessage = message;
        }

    }
}