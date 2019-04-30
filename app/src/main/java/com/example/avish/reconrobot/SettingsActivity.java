package com.example.avish.reconrobot;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity {

    EditText editTextAddress, editTextPort;
    SeekBar seekBaseDutyCycle, seekReverseDutyCycle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        seekBaseDutyCycle = findViewById(R.id.baseDutyCycle);
        seekReverseDutyCycle = findViewById(R.id.reverseDutyCycle);

        seekBaseDutyCycle.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                TextView textView = findViewById(R.id.bdcText);
                String text = "Forward Speed Setting: " + progress +"%";
                textView.setText(text);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekReverseDutyCycle.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                TextView textView = findViewById(R.id.rdcText);
                String text = "Turn/Reverse Speed Setting: " + progress +"%";
                textView.setText(text);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    } //ends the onCreate method

    public void streamVideo(View view) {

        editTextAddress = findViewById(R.id.address);
        editTextPort = findViewById(R.id.port);

        int p1 = Math.round(85 * (seekBaseDutyCycle.getProgress()/ (float) 100));
        int p2 = Math.round(85 * (seekReverseDutyCycle.getProgress()/ (float) 100));

        String baseDutyCycle = "bdc=" + p1;
        String reverseDutyCycle = "rdc=" + p2;

        Log.d("map", baseDutyCycle + " " + reverseDutyCycle);

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
