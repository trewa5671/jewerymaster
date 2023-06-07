package com.example.jewelrymaster;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class DetailsActivity extends AppCompatActivity {

    private TextView textName;
    private TextView textPrice;
    private TextView textDescription;
    private ImageView imageProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String price = intent.getStringExtra("price");
        String description = intent.getStringExtra("description");
        String imageUrl = intent.getStringExtra("imageUrl");

        // Инициализация элементов интерфейса
        textName = findViewById(R.id.text_name);
        textPrice = findViewById(R.id.text_price);
        textDescription = findViewById(R.id.text_description);
        imageProduct = findViewById(R.id.image_product);

        // Установка данных о продукте в элементы интерфейса
        textName.setText(name);
        textPrice.setText(price);
        textDescription.setText(description);
        Glide.with(this).load(imageUrl).into(imageProduct);

    }

}