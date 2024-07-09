package com.example.app.gallerey.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.app.gallerey.ImageDetailActivity;
import com.example.app.gallerey.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

/**
 *
 * Класс RecyclerViewAdapter типа RecyclerView
 * Используется для отображения списка изображений
 *
 * @see   {@linktourl https://developer.android.com/guide/topics/ui/layout/recyclerview}
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder> {
    // Переменные context и ArrayList<String> который содержит сообщения
    private final Context context;
    private final ArrayList<String> imagesArrayList;

    // Конструктор класса который инициализирует класс адаптера
    public RecyclerViewAdapter(Context context, ArrayList<String> imagesArrayList) {
        this.context = context;
        this.imagesArrayList = imagesArrayList;
    }

    /*
    * Данный метод отрисовывает(вставляет) сам макет
     */
    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Всавляем макет (layout) из файла R.layout.card_layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_card, parent, false);
        return new RecyclerViewHolder(view);
    }

    /*
     * Данный метод вставляет данные в макет
     */
    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, @SuppressLint("RecyclerView") int position) {

        // Получаем (по position) название файла из imagesArrayList и создаем объект типа файл
        File imgFile = new File(imagesArrayList.get(position));

        // проверяем существует ли файл
        if (imgFile.exists()) {

            // Отображаем файл в макете (по айди imageV)
            Picasso.get().load(imgFile).placeholder(R.drawable.ic_launcher_background).into(holder.imageV);

            // задаем слушатель для КЛИКА
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // ПРИ КЛИКЕ


                    // Создаем интент для перехода ImageDetailActivity
                    Intent i = new Intent(context, ImageDetailActivity.class);

                    // Передаем данные картинки (путь) в intent .
                    i.putExtra("imgPath", imagesArrayList.get(position));

                    // Запускаем activity по интенту
                    context.startActivity(i);
                }
            });
        }
    }

    /*
    * Данный метод возвращает количество изображений из imagesArrayList
     */
    @Override
    public int getItemCount() {
        return imagesArrayList.size();
    }

    /*
     * ViewHolder описывает макет и связывает с переменными, для дальнейшего использования в коде
     */
    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {

        // Создаем переменные из макета ( в этом примере картинка)
        private final ImageView imageV;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            // связываем переменные с их ID из макета (layout_card)
            imageV = itemView.findViewById(R.id.imageV);
        }
    }
}
