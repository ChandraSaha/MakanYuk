package com.chandrasaha.makanyuk.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chandrasaha.makanyuk.Model.Place;
import com.chandrasaha.makanyuk.Model.Review;
import com.chandrasaha.makanyuk.R;

import java.util.List;

/**
 * Created by Chandra Saha on 5/21/2016.
 */
public class ReviewAdapter extends BaseAdapter {
    List<Review> reviewList;
    private LayoutInflater inflater;
    Context context;
    List<String> userList;

    public ReviewAdapter(Context context, List<Review> reviewList, List<String> userList) {
        this.context = context;
        this.reviewList = reviewList;
        this.userList = userList;
    }

    @Override
    public int getCount() {
        return reviewList.size();
    }

    @Override
    public Object getItem(int position) {
        return reviewList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null)
            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_item_review, null);
        TextView tvReview = (TextView) convertView.findViewById(R.id.tvReview);
        TextView tvUser = (TextView) convertView.findViewById(R.id.tvUser);
        tvUser.setText(userList.get(position));
        tvReview.setText(reviewList.get(position).getReview());

        return convertView;
    }
}
