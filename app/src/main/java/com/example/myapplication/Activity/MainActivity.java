package com.example.myapplication.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.myapplication.Adapter.CategoryAdapter;
import com.example.myapplication.Adapter.PopularAdapter;
import com.example.myapplication.Domain.CategoryDomain;
import com.example.myapplication.Domain.FoodDomain;
import com.example.myapplication.Domain.UserDomain;
import com.example.myapplication.Helper.SQLiteHeper;
import com.example.myapplication.Helper.TinyDB;
import com.example.myapplication.R;
import com.example.myapplication.Ultil.CheckConnection;
import com.example.myapplication.Ultil.Server;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView.Adapter adapter, adapterPz,adapterBg,adapterHd,adapterDn,adapterDk ;
    private RecyclerView recyclerCateloryView, recyclerViewPz, recyclerViewBg,recyclerViewHd,recyclerViewDn,recyclerViewDk;
    private TextView userName;
    private ViewFlipper viewFlipper;
    private ImageView imageView3, btnProfileHome;

    ArrayList<CategoryDomain> categoryDomains;
    ArrayList<FoodDomain> foodDomains;
    ArrayList<FoodDomain> foodDomainsPz;
    ArrayList<FoodDomain> foodDomainsBg;
    ArrayList<FoodDomain> foodDomainsHd;
    ArrayList<FoodDomain> foodDomainsDn;
    ArrayList<FoodDomain> foodDomainsDk;
    SQLiteHeper sqLiteHeper;
    ArrayList<UserDomain> userDomains;

    int id =0;
    String tenLoaisp="";
    String hinhanhloaisp="";
    String picUser ="";
    String data= "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewFlipper = findViewById(R.id.viewFlipper);
        imageView3 = findViewById(R.id.imageView3);
        btnProfileHome = findViewById(R.id.btnProfile);
        sqLiteHeper = new SQLiteHeper(this,"food.sqlite",null,1);
        userDomains = new ArrayList<>();

        if(CheckConnection.haveNetworkConnection(getApplicationContext())) {
            getDataFromLG();
            getDatasp();
            getDataLoaisp();
            bottomNavigation();
            actionViewFliper();


        } else {
            CheckConnection.showToastShort(getApplicationContext(),"Loi");
        }
    }

    private void getDataLoaisp() {
        categoryDomains = new ArrayList<>();
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Server.duongDanLoaiSp, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if(response != null) {
                    for(int i =0 ; i< response.length(); i++) {
                        try {
                            JSONObject jsonObject = response.getJSONObject(i);
                            id = jsonObject.getInt("id");
                            tenLoaisp = jsonObject.getString("title");
                            hinhanhloaisp = jsonObject.getString("pic");
                            categoryDomains.add(new CategoryDomain(id,tenLoaisp,hinhanhloaisp));

                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    recyclerCatelory();

                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                throw new RuntimeException(error.toString());
            }

        });
        requestQueue.add(jsonArrayRequest);

    }

    private void getDatasp() {
        foodDomains = new ArrayList<>();
        foodDomainsDk = new ArrayList<>();
        foodDomainsBg = new ArrayList<>();
        foodDomainsPz = new ArrayList<>();
        foodDomainsDn = new ArrayList<>();
        foodDomainsHd = new ArrayList<>();
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Server.duongDanSp, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if(response != null) {
                    for(int i =0 ; i< response.length(); i++) {
                        try {
                            JSONObject jsonObject = response.getJSONObject(i);
                            FoodDomain foodDomain = new FoodDomain(jsonObject.getInt("id"), jsonObject.getString("title"),jsonObject.getString("pic"),jsonObject.getString("description"), jsonObject.getDouble("fee"),jsonObject.getInt("numberInCart"),jsonObject.getInt("idCategories"));
                            foodDomains.add(foodDomain);
                            setData(foodDomain.getIdLoaiSp(), foodDomain);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    recyclerPopular();
                }


            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                throw new RuntimeException(error.toString());
            }

        });

        requestQueue.add(jsonArrayRequest);

    }

    private void actionViewFliper() {
        ArrayList<String> mangQuangCao = new ArrayList<>();
        mangQuangCao.add("pizza_1");
        mangQuangCao.add("burger_1");
        mangQuangCao.add("donut_1");
        mangQuangCao.add("drink_1");
        mangQuangCao.add("hotdot_4");
        for (int i = 0; i < mangQuangCao.size(); i++) {
            ImageView imageView = new ImageView(getApplicationContext());
            int drawableResourceId = getApplicationContext().getResources().getIdentifier(mangQuangCao.get(i),"drawable", getApplicationContext().getPackageName());
            Glide.with(getApplicationContext()).load(drawableResourceId).into(imageView);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            viewFlipper.addView(imageView);
        }
        viewFlipper.setFlipInterval(5000);
        viewFlipper.setAutoStart(true);
        Animation animation_in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_right);
        Animation animation_out = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out_right);
        viewFlipper.setInAnimation(animation_in);
        viewFlipper.setOutAnimation(animation_out);
    }

    public void bottomNavigation() {
        FloatingActionButton floatingActionButton = findViewById(R.id.cartBtn);
        LinearLayout homeBtn = findViewById(R.id.homeBtn);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, CartListActivity.class));
            }
        });
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, MainActivity.class));
            }
        });
        btnProfileHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ProfileUserActivity.class));
            }
        });
    }
    private void recyclerCatelory() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerCateloryView = findViewById(R.id.recyclerView);
        recyclerCateloryView.setLayoutManager(linearLayoutManager);

        adapter = new CategoryAdapter(categoryDomains);
        recyclerCateloryView.setAdapter(adapter);


    }
    public void recyclerPopular() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager linearLayoutManager3 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager linearLayoutManager4 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);


        recyclerViewPz = findViewById(R.id.recyclerViewPopular);
        adapterPz = new PopularAdapter(foodDomainsPz);
        recyclerViewPz.setLayoutManager(linearLayoutManager);
        recyclerViewPz.setAdapter(adapterPz);

        recyclerViewBg = findViewById(R.id.constranburger);
        recyclerViewBg.setLayoutManager(linearLayoutManager1);
        adapterBg = new PopularAdapter(foodDomainsBg);
        recyclerViewBg.setAdapter(adapterBg);

        recyclerViewHd = findViewById(R.id.constranhotpot);
        recyclerViewHd.setLayoutManager(linearLayoutManager2);
        adapterHd = new PopularAdapter(foodDomainsHd);
        recyclerViewHd.setAdapter(adapterHd);

        recyclerViewDn = findViewById(R.id.constrandounut);
        recyclerViewDn.setLayoutManager(linearLayoutManager3);
        adapterDn = new PopularAdapter(foodDomainsDn);
        recyclerViewDn.setAdapter(adapterDn);

        recyclerViewDk = findViewById(R.id.constrandrink);
        recyclerViewDk.setLayoutManager(linearLayoutManager4);
        adapterDk = new PopularAdapter(foodDomainsDk);
        recyclerViewDk.setAdapter(adapterDk);

    }

    public void setData(int id, FoodDomain foodDomain) {

            if(id ==1) {
                foodDomainsPz.add(foodDomain);
            }
            if(id ==2) {
                foodDomainsBg.add(foodDomain);
            }
            if(id ==3) {
                foodDomainsHd.add(foodDomain);
            }
            if(id ==4) {
                foodDomainsDk.add(foodDomain);
            } if(id ==5) {
            foodDomainsDn.add(foodDomain);
        }

    }
    public void getDataFromLG() {
        Cursor dataUser = sqLiteHeper.getDataSQL("SELECT * FROM customers");
        while(dataUser.moveToNext()) {
            String email = dataUser.getString(1);
            String ten = dataUser.getString(2);
            String pic = dataUser.getString(3);
            String pass = dataUser.getString(4);
            UserDomain u = new UserDomain(email,ten,pic,pass);
            userDomains.add(u);
        }
        String picture = userDomains.get(0).getPic();
        userName = findViewById(R.id.nameUser);
        userName.setText("Hi "+ userDomains.get(0).getUserName());
        String pass = userDomains.get(0).getPass();
        if (!pass.equals("googleLogin")) {
            int drawableResourceId = getApplicationContext().getResources().getIdentifier(picture,"drawable", getApplicationContext().getPackageName());
            Glide.with(this).load(drawableResourceId).circleCrop().into(imageView3);

        } else {
            Glide.with(this).load(picture).circleCrop().into(imageView3);


        }

    }
}