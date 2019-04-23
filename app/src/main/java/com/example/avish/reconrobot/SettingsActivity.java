package com.example.avish.reconrobot;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;

public class SettingsActivity extends AppCompatActivity {

    EditText editTextAddress, editTextPort, editBaseDutyCycle, editReverseDutyCycle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    public void streamVideo(View view){

        editTextAddress = findViewById(R.id.address);
        editTextPort = findViewById(R.id.port);

        SeekBar seekBaseDutyCycle = findViewById(R.id.baseDutyCycle);
        SeekBar seekReverseDutyCycle = findViewById(R.id.reverseDutyCycle);

//        editBaseDutyCycle = findViewById(R.id.baseDutyCycle);
//        editReverseDutyCycle = findViewById(R.id.reverseDutyCycle);

        String baseDutyCycle = "bdc=" + seekBaseDutyCycle.getProgress();//editBaseDutyCycle.getText().toString();
        String reverseDutyCycle = "rdc=" + seekReverseDutyCycle.getProgress();//editReverseDutyCycle.getText().toString();

        UdpThread bdcThread = new UdpThread(editTextAddress.getText().toString(), Integer.parseInt(editTextPort.getText().toString()), baseDutyCycle);
        bdcThread.start();
        bdcThread.interrupt();

        UdpThread rdcThread = new UdpThread(editTextAddress.getText().toString(), Integer.parseInt(editTextPort.getText().toString()), reverseDutyCycle);
        rdcThread.start();
        rdcThread.interrupt();

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("Address", editTextAddress.getText().toString());
        intent.putExtra("Port", editTextPort.getText().toString());
        startActivity(intent);
    }

}
