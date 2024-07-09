package com.example.app.gallerey;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

import com.example.app.gallerey.adapters.RecyclerViewAdapter;

/*
*
* Класс основного экрана (список картинок)
*
* */
public class MainActivity extends AppCompatActivity {

    // Создаем нужные переменные

    private static final int PERMISSION_REQUEST_CODE = 200; // ID для отправки Intent с разрешением на чтение файлов

    private ArrayList<String> imagePaths; // список картинок (ArrayList)
    private RecyclerView imagesRV; // RecyclerView из activity_main
    private RecyclerViewAdapter imageRVAdapter; // адаптер (смотреть RecyclerViewAdapter)

    // СКОЛЬКО ИЗОБРАЖЕНИЙ ПОЛУЧАЕМ
    private final int MAX_IMAGES = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // создаем arrayList и связываем recyclerView (imagesRV) с айди из макета
        imagePaths = new ArrayList<>();
        imagesRV = findViewById(R.id.imagesRecyclerView);
        // инициализация механизма разрешений

        initializePermissionFlow();


        // готовим данные для RecyclerView

        prepareRecyclerView();
    }

    // метод проверки есть ли у нас (от пользователя) разрешение READ_EXTERNAL_STORAGE
    // возвращает true/false
    private boolean checkPermission() {

        int result = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void initializePermissionFlow() {
        if (checkPermission()) {
            // если уже есть разрешения показываем картинки

            showImages();
        } else {
            // Если нет разрешения запускаем процесс запроса
            requestPermission();
        }
    }

    private void requestPermission() {
        // Запускаем процесс запроса у пользователя разрешения READ_EXTERNAL_STORAGE
        ActivityCompat.requestPermissions(this, new String[]{READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

    private void prepareRecyclerView() {

        // Создаем адаптер(передав context - текущее активити и список картинок)

        imageRVAdapter = new RecyclerViewAdapter(MainActivity.this, imagePaths);

        // создаем сетку для квадратиков
        GridLayoutManager manager = new GridLayoutManager(MainActivity.this, 4);

        // Связываем сетку и макет (Adapter) к imagesRV
        imagesRV.setLayoutManager(manager);
        imagesRV.setAdapter(imageRVAdapter);
    }

    private void showImages() {

        // Проверяем есть ли вообще СД карта
        boolean isSDPresent = android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);

        if (isSDPresent) {

            // Столбцы которые хотим получить из курсора
            final String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID};

            // OrderBy для курсора
            final String orderBy = MediaStore.Images.Media._ID;

            // Получаем по курсору данные из Галлереи)
            Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null, null, orderBy);

            // получение общего количества изображений
            int count = cursor.getCount();

            int to_elements_n = MAX_IMAGES;
            if(MAX_IMAGES > count)
                to_elements_n = count;

            // добавляем каждый элемент в imagePaths
            for (int i = 0; i < to_elements_n; i++) {

                // переходим к следующему элементу курсора
                cursor.moveToPosition(i);

                // получаем из курсора данные
                int dataColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);

                // добавляем их в наш массив
                imagePaths.add(cursor.getString(dataColumnIndex));
            }
            imageRVAdapter.notifyDataSetChanged();

            // закрываем курсор
            cursor.close();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // этот метод вызывается после предоставления разрешений
        switch (requestCode) {
            // Если код запроса PERMISSION_REQUEST_CODE.
            case PERMISSION_REQUEST_CODE:
                // Проверяем ответ дали ли разрешение или нет
                if (grantResults.length > 0) {
                    boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (storageAccepted) {
                        // разрешение, показываем картинки

                        Toast.makeText(this, "Permissions Granted..", Toast.LENGTH_SHORT).show();
                        showImages();
                    } else {
                        // Разрешение не дали
                        Toast.makeText(this, "Permissions denined, Permissions are required to use the app..", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}