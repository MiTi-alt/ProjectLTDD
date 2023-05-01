package com.example.myapplication.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.Helper.SQLiteHeper;
import com.example.myapplication.R;

public class ProfileUserActivity extends AppCompatActivity {
    TextView btnProfileLogout;
    SQLiteHeper sqLiteHeper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_user);
        btnProfileLogout = findViewById(R.id.btnProfileLogout);
        sqLiteHeper = new SQLiteHeper(this,"food.sqlite",null,1);
        btnProfileLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sqLiteHeper.deleteAllData("customers");
                startActivity(new Intent(ProfileUserActivity.this, LoginACtivity.class));
            }
        });
    }
}