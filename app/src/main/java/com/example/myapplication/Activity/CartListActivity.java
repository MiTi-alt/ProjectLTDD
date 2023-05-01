package com.example.myapplication.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Adapter.CartListAdapter;
import com.example.myapplication.Domain.FoodDomain;
import com.example.myapplication.Domain.UserDomain;
import com.example.myapplication.Helper.ManagementCart;
import com.example.myapplication.Helper.SQLiteHeper;
import com.example.myapplication.Interface.ChangeNameberItemsListener;
import com.example.myapplication.R;
import com.example.myapplication.Ultil.CheckConnection;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.location.Address;

import android.util.Log;

import android.widget.Toast;


import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

public class CartListActivity extends AppCompatActivity {
    private RecyclerView.Adapter adapter;
    private RecyclerView recyclerViewList;
    private ManagementCart managementCart;
    TextView totalFeeTxt, totalDeliveryTxt, totalFaxTxt,totalTxt, emptyTxt, checkOutBtn;
    private double tax;
    private ScrollView scrollView;
    private SQLiteHeper sqLiteHeper;
    private FusedLocationProviderClient fusedLocationClient;
    private TextView textView;
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_list);
        managementCart = new ManagementCart(this);
        anhXa();
        addDataInSQL();
        initList();
        calculateCart();
        bottomNavigation();
        getCurrentLocation();
    }
public void bottomNavigation() {
    FloatingActionButton floatingActionButton = findViewById(R.id.cartBtn);
    LinearLayout homeBtn = findViewById(R.id.homeBtn);
    LinearLayout btnProfile = findViewById(R.id.btnProfile);

//    floatingActionButton.setOnClickListener(new View.OnClickListener() {
//        @Override
//        public void onClick(View view) {
//            startActivity(new Intent(CartListActivity.this, CartListActivity.class));
//        }
//    });
    homeBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startActivity(new Intent(CartListActivity.this, MainActivity.class));
        }
    });
    btnProfile.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startActivity(new Intent(CartListActivity.this, ProfileUserActivity.class));
        }
    });
}
    private void anhXa() {
       recyclerViewList = findViewById(R.id.recyViewTxt);
       totalFeeTxt = findViewById(R.id.totalFeeTxt);
       totalDeliveryTxt = findViewById(R.id.totalDeliveryTxt);
       totalFaxTxt = findViewById(R.id.totalTaxTxt);
       totalTxt = findViewById(R.id.totalTxt);
       emptyTxt = findViewById(R.id.emtyTxt);
       scrollView = findViewById(R.id.scrollViewTxt);
       sqLiteHeper = new SQLiteHeper(this,"food.sqlite",null,1);
       sqLiteHeper.queryData("DROP TABLE foods");
        sqLiteHeper.queryData("DROP TABLE cartFoods");
       sqLiteHeper.queryData("CREATE TABLE IF NOT EXISTS foods(id INTEGER PRIMARY KEY AUTOINCREMENT, title VARCHAR(200), pic VARCHAR(200), description VARCHAR(1000), fee DOUBLE, numberInCart INT, idLoaiSp INT, idOrder VARCHAR(200))");
       sqLiteHeper.queryData("CREATE TABLE IF NOT EXISTS cartFoods(id INTEGER PRIMARY KEY AUTOINCREMENT, idOrder VARCHAR(200),feeShip INT, tax INT, total INT,status INT ,idUser INT)");

        textView = findViewById(R.id.tvAddress);
        checkOutBtn = findViewById(R.id.checkOutBtn);
    }
    private void initList() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerViewList.setLayoutManager(linearLayoutManager);
        adapter= new CartListAdapter(getFoodDomainList(), this, new ChangeNameberItemsListener() {
            @Override
            public void changed() {
                calculateCart();
            }
        });
        recyclerViewList.setAdapter(adapter);
        if(managementCart.getListCart().isEmpty()) {

            emptyTxt.setVisibility(View.VISIBLE);
            scrollView.setVisibility(View.GONE);
        } else {

            emptyTxt.setVisibility(View.GONE);
            scrollView.setVisibility(View.VISIBLE);
        }
    }
    public void addDataInSQL() {

        for (int i=0; i< managementCart.getListCart().size();i++) {
            FoodDomain a = managementCart.getListCart().get(i);
            String query = "INSERT INTO foods(title, pic, description, fee, numberInCart, idLoaiSp,idOrder) VALUES(?, ?, ?, ?, ?, ?,?)";
            SQLiteStatement statement = sqLiteHeper.getWritableDatabase().compileStatement(query);

            statement.bindString(1, a.getTitle());
            statement.bindString(2, a.getPic());
            statement.bindString(3, a.getDescription());
            statement.bindDouble(4, a.getFee());
            statement.bindLong(5, a.getNumberInCart());
            statement.bindLong(6, a.getIdLoaiSp());
            statement.bindString(7,"DH"+(getLastId()+1));
            statement.executeInsert();
        }

    }
    private void calculateCart() {
        double percentText =0.2;
        double delivery = 10;
        tax = Math.round((managementCart.getTotalFee()*percentText)*100)/100;
        double total = Math.round((managementCart.getTotalFee()+tax+delivery)*100)/100;
        double itemTotal = Math.round(managementCart.getTotalFee()*100)/100;
        totalFeeTxt.setText("$"+itemTotal);
        totalFaxTxt.setText("$"+tax);
        totalDeliveryTxt.setText("$"+delivery);
        totalTxt.setText("$"+total);

    }
    public ArrayList<FoodDomain> getFoodDomainList() {
        ArrayList<FoodDomain> result = new ArrayList<>();
        Cursor dataFood = sqLiteHeper.getDataSQL("SELECT * FROM foods");
        while(dataFood.moveToNext()) {
            int id = dataFood.getInt(0);
            String title = dataFood.getString(1);
            String pic = dataFood.getString(2);
            String desciption = dataFood.getString(3);
            int fee = dataFood.getInt(4);
            int numberInCart = dataFood.getInt(5);
            int idLoaiSp = dataFood.getInt(6);
            FoodDomain foodDomain = new FoodDomain(id, title,pic,desciption, (double) fee,numberInCart,idLoaiSp);
            result.add(foodDomain);
        }

        return result;
    }
    public void getCurrentLocation() {
        // Khởi tạo FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());

        // Kiểm tra quyền truy cập vị trí
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Nếu chưa được cấp quyền, yêu cầu cấp quyền từ người dùng
            ActivityCompat.requestPermissions((Activity) getApplicationContext(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
            textView.setText("Không xác định được vị trí ");
        } else {
            // Nếu đã được cấp quyền, lấy vị trí hiện tại
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                                try {
                                    List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                    if (addresses != null && addresses.size() > 0) {
                                        Address address = addresses.get(0);
                                        String fullAddress = "";
                                        for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                                            fullAddress += address.getAddressLine(i) + ", ";
                                        }
                                        fullAddress = fullAddress.substring(0, fullAddress.length() - 11);
                                        textView.setText(fullAddress);
                                    }
                                } catch (IOException e) {
                                    textView.setText("Không xác định được vị trí ");
                                }
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplication(), "Lỗi khi lấy vị trí hiện tại: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
    public String getIdUser() {
        String result = " ";
        Cursor dataUser = sqLiteHeper.getDataSQL("SELECT id FROM customers");
        while(dataUser.moveToNext()) {
             result = dataUser.getString(0);

        }
        return result;
    }


    public int getLastId() {
        int lastId = 0;
        String query = "SELECT MAX(id) FROM cartFoods";
        Cursor cursor = sqLiteHeper.getDataSQL(query);
        if (cursor.moveToFirst()) {
            lastId = cursor.getInt(0);
        }
        cursor.close();
        return lastId;
    }

    public void CheckOut() {
        
    }




}