package com.example.avish.reconrobot;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class SettingsActivity extends AppCompatActivity {

    EditText editTextAddress, editTextPort;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    public void streamVideo(View view){

        editTextAddress = findViewById(R.id.address);
        editTextPort = findViewById(R.id.port);

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("Address", editTextAddress.getText().toString());
        intent.putExtra("Port", editTextPort.getText().toString());
        startActivity(intent);
    }

}
