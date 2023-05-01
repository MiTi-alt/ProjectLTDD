package com.example.myapplication.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.myapplication.Domain.FoodDomain;
import com.example.myapplication.Helper.ManagementCart;
import com.example.myapplication.R;

public class ShowDetailActivity extends AppCompatActivity {
    private TextView addToCartBtn;
    private TextView titleText, feeText, descriptionText, numberOrderText;
    private ImageView plusBtn, minusBtn, picFood;
    private FoodDomain object;
    int numberOrder =1;
    private ManagementCart managementCart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_detail);
        managementCart = new ManagementCart(this);
        anhXa();
        getBundle();
    }

    private void getBundle() {
        object =(FoodDomain) getIntent().getSerializableExtra("object");
        int drawableResourceId = this.getResources().getIdentifier(object.getPic(), "drawable", this.getPackageName());
        Glide.with(this).load(drawableResourceId).into(picFood);
        titleText.setText(object.getTitle());
        feeText.setText("$"+ object.getFee());
        descriptionText.setText(object.getDescription());
        numberOrderText.setText(String.valueOf(numberOrder));

        minusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numberOrder = numberOrder +1;
                numberOrderText.setText(String.valueOf(numberOrder));

            }
        });
        plusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(numberOrder > 1) {
                    numberOrder = numberOrder - 1;
                    numberOrderText.setText(String.valueOf(numberOrder));
                }
                numberOrderText.setText(String.valueOf(numberOrder));

            }
        });
        addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                object.setNumberInCart(numberOrder);
                managementCart.insertFood(object);
            }
        });
    }

    private void anhXa() {
        addToCartBtn = findViewById(R.id.addToCart);
        titleText = findViewById(R.id.titleText);
        feeText = findViewById(R.id.priceText);
        descriptionText = findViewById(R.id.description);
        numberOrderText = findViewById(R.id.countText);
        plusBtn = findViewById(R.id.plusBtn);
        minusBtn = findViewById(R.id.minusBtn);
        picFood = findViewById(R.id.imagePicFood);
    }
}