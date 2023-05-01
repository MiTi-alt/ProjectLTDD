package com.example.myapplication.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.myapplication.Domain.UserDomain;
import com.example.myapplication.Helper.SQLiteHeper;
import com.example.myapplication.R;


public class IntroActivity extends AppCompatActivity {
    private ConstraintLayout startBtn;
    UserDomain userAready;
    private SQLiteHeper sqLiteHeper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        anhXa();
        sqLiteHeper = new SQLiteHeper(this,"food.sqlite",null,1);
        sqLiteHeper.queryData("CREATE TABLE IF NOT EXISTS customers(id INTEGER PRIMARY KEY AUTOINCREMENT, email VARCHAR(200), name VARCHAR(200), password VARCHAR(200), pic VARCHAR(200))");
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor dataUser = sqLiteHeper.getDataSQL("SELECT * FROM customers");
                while(dataUser.moveToNext()) {
                    String email = dataUser.getString(1);
                    String ten = dataUser.getString(2);
                    String pic = dataUser.getString(3);
                    String pass = dataUser.getString(4);
                    userAready = new UserDomain(email,ten,pass,pic);

                }
                if(userAready == null) {
                    startActivity(new Intent(IntroActivity.this, LoginACtivity.class));
                } else {
                    startActivity(new Intent(IntroActivity.this, MainActivity.class));
                }


            }
        });
    }



    private void anhXa() {
        startBtn = findViewById(R.id.startBtn);
        sqLiteHeper = new SQLiteHeper(this,"food.sqlite",null,1);

    }
}