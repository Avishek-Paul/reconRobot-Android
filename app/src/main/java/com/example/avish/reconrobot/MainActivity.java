package com.example.avish.reconrobot;

import android.content.Intent;
import android.graphics.PixelFormat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageView;


public class MainActivity extends AppCompatActivity implements JoystickView.JoystickListener {

    String lastMessage = "";
    String dstAddress;
    int dstPort;
    boolean flipped = false;
    boolean lightsOn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        String streamAddress = intent.getStringExtra("Address");
        String portNumber = intent.getStringExtra("Port");

        String liveStream = "http://" + streamAddress + ":8080/stream";//"http://192.168.0.101:8080/stream";
        Log.d("Main_stream", liveStream);
        Log.d("Main_Port", portNumber);

        dstPort = Integer.parseInt(portNumber);
        dstAddress = streamAddress;

        final WebView webView = (WebView)findViewById(R.id.vv);
//        webView.setInitialScale(100);
        int width = webView.getWidth();
        int height = webView.getHeight();
        Log.d("width", String.valueOf(width));
        Log.d("height", String.valueOf(height));
        webView.loadUrl(liveStream);// + "?width="+width+"&height="+height);
        Log.d("Main_Livestream", liveStream+ "?width="+width+"&height="+height);


        //allows the background of the joystick to be transparent without black background
        SurfaceView joystickRight = findViewById(R.id.joystickRight);
        joystickRight.setZOrderOnTop(true);
        SurfaceHolder joystickHolderRight = joystickRight.getHolder();
        joystickHolderRight.setFormat(PixelFormat.TRANSPARENT);

        SurfaceView joystickLeft = findViewById(R.id.joystickLeft);
        joystickLeft.setZOrderOnTop(true);
        SurfaceHolder joystickHolderLeft = joystickLeft.getHolder();
        joystickHolderLeft.setFormat(PixelFormat.TRANSPARENT);

    } //ends the onCreate method

    public void toggleLights(View view){
        ImageView lightSwitchLeft = findViewById(R.id.lightSwitchLeft);
        ImageView lightSwitchRight = findViewById(R.id.lightSwitchRight);

        String message;
        if(!lightsOn){
            message = "l_on";
            lightsOn = true;
            lightSwitchLeft.setImageResource(R.drawable.light_off_icon);
            lightSwitchRight.setImageResource(R.drawable.light_off_icon);

        } else {
            message = "l_off";
            lightsOn = false;
            lightSwitchLeft.setImageResource(R.drawable.light_on_icon);
            lightSwitchRight.setImageResource(R.drawable.light_on_icon);

        }
        UdpThread udpThread = new UdpThread(dstAddress, dstPort, message);
        udpThread.start();
        udpThread.interrupt();
    }

    public void flipCamera(View view){
        SurfaceView joystickRight = findViewById(R.id.joystickRight);
        SurfaceView joystickLeft = findViewById(R.id.joystickLeft);
        ImageView flipCameraLeft = findViewById(R.id.flipCameraLeft);
        ImageView flipCameraRight = findViewById(R.id.flipCameraRight);
        ImageView lightSwitchLeft = findViewById(R.id.lightSwitchLeft);
        ImageView lightSwitchRight = findViewById(R.id.lightSwitchRight);

        if (flipped == false) {
            joystickLeft.setVisibility(View.VISIBLE);
            flipCameraLeft.setVisibility(View.VISIBLE);
            lightSwitchLeft.setVisibility(View.VISIBLE);
            joystickRight.setVisibility(View.INVISIBLE);
            flipCameraRight.setVisibility(View.INVISIBLE);
            lightSwitchRight.setVisibility(View.INVISIBLE);
            flipped = true;
        } else {
            joystickRight.setVisibility(View.VISIBLE);
            flipCameraRight.setVisibility(View.VISIBLE);
            lightSwitchRight.setVisibility(View.VISIBLE);
            joystickLeft.setVisibility(View.INVISIBLE);
            flipCameraLeft.setVisibility(View.INVISIBLE);
            lightSwitchLeft.setVisibility(View.INVISIBLE);
            flipped = false;
        }
    }

    @Override
    public void onJoystickMoved(float xPercent, float yPercent, int id) {
//        Log.d("Right Joystick", "X percent: " + xPercent + " Y percent: " + yPercent);
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

        if(flipped){
            switch (message){
                case "up":
                    message = "down";
                    break;
                case "left":
                    message = "right";
                    break;
                case "down":
                    message = "up";
                    break;
                case "right":
                    message = "left";
                    break;
            }
        }

        if (message != this.lastMessage){

            UdpThread udpThread = new UdpThread(dstAddress, dstPort, message);
            udpThread.start();
            udpThread.interrupt();

            this.lastMessage = message;
        }

    }
}