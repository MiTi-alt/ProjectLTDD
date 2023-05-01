package com.example.myapplication.Ultil;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;


public class LoginInfo extends AppCompatActivity {

    TextView t1;
    TextView t2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_info);

        t1 = findViewById(R.id.namegg);
        t2 = findViewById(R.id.emailgg);

        }

}