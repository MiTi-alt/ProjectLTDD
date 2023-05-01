package com.example.myapplication.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.Domain.UserDomain;
import com.example.myapplication.Helper.SQLiteHeper;
import com.example.myapplication.R;
import com.example.myapplication.Ultil.CheckConnection;
import com.example.myapplication.Ultil.Server;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;


public class LoginACtivity extends AppCompatActivity {
    EditText email;
    EditText pass;
    MaterialButton btnlg;
    MaterialButton signInRGT;
    UserDomain user;

    ImageView fb;
    ArrayList<UserDomain> userDomains;
    private SQLiteHeper sqLiteHeper;
    // google
    ImageView googleBtn;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    // facebook
    ImageView fbBtn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        anhXa();
        getDataUser();
        sqLiteHeper = new SQLiteHeper(this,"food.sqlite",null,1);
        sqLiteHeper.queryData("CREATE TABLE IF NOT EXISTS customers(id INTEGER PRIMARY KEY AUTOINCREMENT, email VARCHAR(200), name VARCHAR(200), password VARCHAR(200), pic VARCHAR(200))");

        btnlg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if( check(email.getText().toString().trim(), pass.getText().toString().trim()) == true) {
                    sqLiteHeper.deleteAllData("customers");
                    sqLiteHeper.queryData("INSERT INTO customers(email, name, password, pic) VALUES('"+user.getEmail()+"','"+user.getUserName()+"','"+user.getPass()+"','"+user.getPic()+"')");
                    Intent intent = new Intent(LoginACtivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    CheckConnection.showToastShort(getApplicationContext(),"Đăng nhập thất bại");
                }
            }
        });
        signInRGT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginACtivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        // google
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this,gso);
        LoginWithGoogle();
        
    }

    private void LoginWithGoogle() {
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);



        googleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
                GoogleSignInAccount acc = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
                if(acc!=null){
                    String personName = acct.getDisplayName();
                    String personEmail = acct.getEmail();
                    Uri personPhoto = acct.getPhotoUrl();
                    if (personPhoto != null) {
                        String picture = personPhoto.toString();
                        sqLiteHeper.deleteAllData("customers");
                        sqLiteHeper.queryData("INSERT INTO customers(email, name, password, pic) VALUES('"+personEmail+"','"+personName+"','"+"googleLogin"+"','"+picture+"')");
                        navigateToSecondActivity();
                    }
                    Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();


                } else {
                    Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
    void signIn(){
        Intent signInIntent = gsc.getSignInIntent();
        startActivityForResult(signInIntent,1000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1000){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                task.getResult(ApiException.class);
                navigateToSecondActivity();
            } catch (ApiException e) {
                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }

    }
    void navigateToSecondActivity(){
        finish();
        Intent intent = new Intent(LoginACtivity.this,MainActivity.class);
        startActivity(intent);
    }

    private void getDataUser() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Server.duongDanCustomer, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                if(response != null) {
                    for(int i =0 ; i< response.length(); i++) {
                        try {
                            JSONObject jsonObject = response.getJSONObject(i);
                            UserDomain userDomain = new UserDomain(jsonObject.getInt("id"),jsonObject.getString("email"), jsonObject.getString("ten"), jsonObject.getString("pass"), jsonObject.getString("pic"));
                            userDomains.add(userDomain);

                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }

                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                throw new RuntimeException(error.toString());
            }

        });
        int timeout = 30000;
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(timeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonArrayRequest);

    }


    private void anhXa() {
        email = findViewById(R.id.lgusername);
        pass = findViewById(R.id.lgpass);
        btnlg = findViewById(R.id.signInbtn);
        signInRGT = findViewById(R.id.signInRGT);
        userDomains = new ArrayList<>();
        user= null;
        googleBtn = findViewById(R.id.googleLG);
        fbBtn = findViewById(R.id.fb);

    }
    public boolean check( String email, String pass) {
        boolean check = false;
        for(int i =0; i< userDomains.size(); i++) {
            if(userDomains.get(i).getEmail().equals(email)) {
                    if( userDomains.get(i).getPass().equals(pass)) {
                        user = userDomains.get(i);
                        check = true;
                        break;
                    }
            }
        }
        return  check;

    }




}