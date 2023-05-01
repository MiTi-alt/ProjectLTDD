package com.example.myapplication.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.Domain.UserDomain;
import com.example.myapplication.R;
import com.example.myapplication.Ultil.CheckConnection;
import com.example.myapplication.Ultil.Server;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    EditText username;
    EditText email;
    EditText pass;
    EditText repass;
    ImageView gg;
    ImageView fb;
    ImageView tw;
    MaterialButton regbtn;

    List<UserDomain> listUsers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        anhXa();
        if(CheckConnection.haveNetworkConnection(getApplicationContext())) {
            btnAction();
        } else {
            CheckConnection.showToastShort(getApplicationContext(),"Bạn hãy kiểm tra lại kết nối");
        }


    }

    private void btnAction() {
        regbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String emailRG = email.getText().toString().trim();
                final String nameRG = username.getText().toString().trim();
                final String passRG = pass.getText().toString().trim();
                final String passReRG = repass.getText().toString().trim();
                if (emailRG.length() > 0 && nameRG.length() > 0 && passRG.length() > 0) {
                    if (!passReRG.equals(passRG)) {
                        CheckConnection.showToastShort(getApplicationContext(), "Mật khẩu không trùng");
                    } else {
                        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.dangKi, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                            }

                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        }) {
                            @Nullable
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                HashMap<String, String> hashMap = new HashMap<>();
                                hashMap.put("email", emailRG);
                                hashMap.put("ten", nameRG);
                                hashMap.put("password", passRG);
                                return hashMap;
                            }


                        };
                        requestQueue.add(stringRequest);
                        startActivity(new Intent(RegisterActivity.this, LoginACtivity.class));

                    }

                } else {
                    CheckConnection.showToastShort(getApplicationContext(), "Hãy kiểm tra lại dữ liệu");
                }
            }
        });
    }


    private void anhXa() {
        listUsers = new ArrayList<>();
        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        pass = findViewById(R.id.password);
        repass = findViewById(R.id.repassword);
        regbtn = findViewById(R.id.signbtn);
    }
}
