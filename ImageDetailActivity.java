package com.example.app.gallerey;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.ScaleGestureDetector;
import android.widget.ImageView;


import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import java.io.File;

public class ImageDetailActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail);

        // Создаем нужные переменные

        // путь к картинке
        // Получаем переданную информацию

        String imgPath = getIntent().getStringExtra("imgPath");

        // инициализируем imageView (связываем с переменной)
        // imageView
        ImageView imageView = findViewById(R.id.myImageView);



        // Создаем File
        File imgFile = new File(imgPath);

        // если есть файл отображаем
        if (imgFile.exists()) {
            Picasso.get().load(imgFile).placeholder(R.drawable.ic_launcher_background).into(imageView);
        // placeholder - картинка которая отображается пока идет загрузка
        }
    }



}