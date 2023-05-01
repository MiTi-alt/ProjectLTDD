package com.example.myapplication.Helper;

import android.content.Context;
import android.widget.Toast;

import com.example.myapplication.Domain.FoodDomain;
import com.example.myapplication.Interface.ChangeNameberItemsListener;

import java.util.ArrayList;

public class ManagementCart {
    private Context context;
    private TinyDB tinyDB;

    public ManagementCart(Context context) {
        this.context = context;
        this.tinyDB = new TinyDB(context);
    }
    public void insertFood(FoodDomain item) {
        ArrayList<FoodDomain> listFood = getListCart();
        boolean exitsAlready = false;
        int n =0;
        for(int i =0; i < listFood.size();i++) {
            if(listFood.get(i).getTitle().equals(item.getTitle())) {
                exitsAlready=true;
                n = i;
                break;
            }
        }
        if(exitsAlready) {
            listFood.get(n).setNumberInCart(item.getNumberInCart());
        } else {
            listFood.add(item);
        }
        tinyDB.putListObject("CartList",listFood);
        Toast.makeText(context,"Added to your cart", Toast.LENGTH_SHORT).show();
    }
    public ArrayList<FoodDomain> getListCart() {
        return tinyDB.getListObject("CartList");
    }
    public void plusNumberFood(ArrayList<FoodDomain> listFood, int position, ChangeNameberItemsListener changeNameberItemsListener) {
        listFood.get(position).setNumberInCart(listFood.get(position).getNumberInCart() +1);
        tinyDB.putListObject("CartList", listFood);
        changeNameberItemsListener.changed();
    }
    public void minusNumberFood(ArrayList<FoodDomain> listFood, int position, ChangeNameberItemsListener changeNameberItemsListener) {
       if(listFood.get(position).getNumberInCart() ==1) {
           listFood.remove(position);
       } else {
           listFood.get(position).setNumberInCart(listFood.get(position).getNumberInCart() - 1);

       }
        tinyDB.putListObject("CartList", listFood);
        changeNameberItemsListener.changed();


    }
    public Double getTotalFee() {
        ArrayList<FoodDomain> list = getListCart();
        double fee =0;
        for (int i = 0; i < list.size(); i++) {
            fee = fee+ (list.get(i).getFee()*list.get(i).getNumberInCart());
        }
        return fee;
    }
}
