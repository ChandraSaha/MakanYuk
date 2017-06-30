package com.chandrasaha.makanyuk.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chandrasaha.makanyuk.DetailActivity;
import com.chandrasaha.makanyuk.Model.Place;
import com.chandrasaha.makanyuk.R;

import java.util.List;

/**
 * Created by Chandra Saha on 11/17/2015.
 */
public class PlaceAdapter extends BaseAdapter {

    static String results;
    private LayoutInflater inflater;
    Context context;
    List<Place> placeList;
    private FragmentActivity myContext;

    public PlaceAdapter(Context context, List<Place> placeList) {
        this.context = context;
        this.placeList = placeList;

        //imageLoader = AppController.getInstance().getImageLoader();
    }

    public PlaceAdapter(Activity activity, List<Place> movieItems) {
        this.context = activity;
        this.placeList = movieItems;
        myContext = (FragmentActivity) activity;
    }

    @Override
    public int getCount() {
        return placeList.size();
    }

    @Override
    public Object getItem(int position) {
        return placeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        //  if (imageLoader == null)
        //    imageLoader = AppController.getInstance().getImageLoader();

        if (inflater == null)
            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_item_place, null);

        CardView cardView = (CardView) convertView.findViewById(R.id.card_view);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.imgview);
        TextView tvNama = (TextView) convertView.findViewById(R.id.tvNama);
       // TextView tvAlamat = (TextView) convertView.findViewById(R.id.tvLokasi);
        tvNama.setText(placeList.get(position).getNama());
        //tvAlamat.setText(placeList.get(position).getAlamat());
        if (placeList.get(position).getImgUrl().equals("")){
            Log.e("gambar","kosong");
        } else {
            Glide.with(context).load(placeList.get(position).getImgUrl()).into(imageView);
        }

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent i = new Intent(context, DetailActivity.class);
                i.putExtra("idPlace",placeList.get(position).getIdTempat());
                context.startActivity(i);
            }
        });
        return convertView;
    }
}