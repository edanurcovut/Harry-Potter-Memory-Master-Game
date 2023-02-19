package com.harrypottergame.memorymaster;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class KartAdapter extends RecyclerView.Adapter<KartAdapter.ViewHolder> {

        public interface OnItemClickListener {
            void onItemClick(KartModel kartModel, String tag, ImageView imageView,int position);
            void onItemClick2(KartModel kartModel, ImageView imageView,int position);
        }

        private Context context;
        private List<KartModel> kartModelList;
        private OnItemClickListener listener;
        private  int layout;
        private  ArrayList<ImageView> kartlar=new ArrayList<>();
        private KartModel kartModel2;

        public KartAdapter(Context applicationContext, List<KartModel> kartListesi,int layout, OnItemClickListener onItemClickListener) {
            this.context = applicationContext;
            this.kartModelList = kartListesi;
            this.listener = onItemClickListener;
            this.layout=layout;

        }

        @NonNull
        @Override
        public KartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(layout, viewGroup, false);
            return new KartAdapter.ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull KartAdapter.ViewHolder viewHolder, int i) {
            final KartModel kartModel = kartModelList.get(i);
            kartModel2=new KartModel();
            //viewHolder.textAd.setText(kartModel.kartAd);
            kartlar.add(viewHolder.imageView);
            Log.d("clickdeneme","kartlar listesi eleman sayısı"+kartlar.size());


            int position =i;

            viewHolder.mainCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(kartModel,"card",viewHolder.imageView,i);
                }
            });
            viewHolder.textAd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(kartModel,"card",viewHolder.imageView,i);
                }
            });


        }

        // method for filtering our recyclerview items.
        public void filterList(int position) {
            // below line is to add our filtered
            // list in our course array list.
            kartModel2 = kartModelList.get(position);
            listener.onItemClick2(kartModel2,kartlar.get(position),position);

        }


        @Override
        public int getItemCount() {
            return kartModelList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
           ImageView imageView;
           TextView  textAd;
            ConstraintLayout mainCard;


            ViewHolder(View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.kart_resim);
                textAd = itemView.findViewById(R.id.text_ad);
                mainCard=itemView.findViewById(R.id.cardmainlayout);


            }
        }
    }

