package com.chandrasaha.makanyuk;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toolbar;

import com.chandrasaha.makanyuk.Adapter.ReviewAdapter;
import com.chandrasaha.makanyuk.Model.Review;
import com.chandrasaha.makanyuk.Utils.APIservice;
import com.chandrasaha.makanyuk.Utils.RESTcontroller;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ReviewActivity extends AppCompatActivity {
    List<Review> reviewList = new ArrayList<>();
    List<String> reviewListl;
    List<String> userList;
    ListView listView;
    String nama;
    Intent i;
    android.support.v7.widget.Toolbar toolbar;
    private ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Review :"+nama);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                }
            });
        }
        listView = (ListView) findViewById(R.id.listView);
        i = getIntent();
        reviewListl = i.getStringArrayListExtra("list");
        userList = i.getStringArrayListExtra("user");
        nama = i.getStringExtra("nama");
        loading();
        final ReviewAdapter adapter = new ReviewAdapter(getApplicationContext(),reviewList,userList);
        for(int i=0;i<reviewListl.size();i++){
            Review review = new Review();
            review.setReview(reviewListl.get(i));
            review.setUser(userList.get(i));
            reviewList.add(review);
        }
        adapter.notifyDataSetChanged();
        dialog.dismiss();
        listView.setAdapter(adapter);
    }
    public void loading() {
        dialog = ProgressDialog.show(ReviewActivity.this, "Loading", "Please wait", true);
    }
}
