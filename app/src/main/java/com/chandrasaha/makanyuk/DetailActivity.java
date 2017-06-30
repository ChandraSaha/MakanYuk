package com.chandrasaha.makanyuk;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chandrasaha.makanyuk.Model.Place;
import com.chandrasaha.makanyuk.Utils.APIservice;
import com.chandrasaha.makanyuk.Utils.RESTcontroller;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
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

public class DetailActivity extends AppCompatActivity {
    String idPlace;
    String results;
    TextView tvDetailAlamat;
    TextView tvDetailReview;
    TextView tvSeeAllReview;
    String nama;
    ImageView ivDetaill;
    Toolbar toolbar;
    Button bNav;
    private Double lat,lng;

    private GoogleMap map;
    private ProgressDialog dialog;
    List<String> reviewList = new ArrayList<String>();
    List<String> userList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                }
            });
        }
        ivDetaill = (ImageView) findViewById(R.id.iv_Detail);
        tvDetailAlamat = (TextView) findViewById(R.id.tv_detail_address);
        tvDetailReview = (TextView) findViewById(R.id.tv_detail_review);
        tvSeeAllReview = (TextView) findViewById(R.id.tvSeeAllReview);
        bNav = (Button) findViewById(R.id.bNav);

        Intent i = getIntent();
        idPlace = i.getStringExtra("idPlace");
        loadFromApi();
        setupMapIfNeeded();

    }

    private void setupMapIfNeeded() {
        if (map == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            SupportMapFragment supportMapFragment = (SupportMapFragment) fragmentManager
                    .findFragmentById(R.id.maps);
            map = supportMapFragment.getMap();
        }
    }

    private void loadFromApi() {
        loading();
        final RESTcontroller rc = new RESTcontroller();
        rc.setTimeout(30);
        RestAdapter restAdapter = rc.adapter(getApplicationContext());
        APIservice apiService = restAdapter.create(APIservice.class);

        Map query = new HashMap();
        apiService.getDetail(idPlace,query, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                StringBuilder sb = rc.toStringBuilder(response);
                results = sb.toString();


                try {
                    JSONObject jsonobj = new JSONObject(results);
                    JSONObject jsonobj2 = jsonobj.getJSONObject("response");
                    JSONObject jsonObjectVenue = jsonobj2.getJSONObject("venue");
                    nama = jsonObjectVenue.getString("name");
                    toolbar.setTitle(jsonObjectVenue.getString("name"));
                    JSONObject jsonObjectLocation = jsonObjectVenue.getJSONObject("location");
                    if(!jsonObjectLocation.isNull("address")&&!jsonObjectLocation.isNull("city")) {
                        tvDetailAlamat.setText(jsonObjectLocation.getString("address") + " " + jsonObjectLocation.getString("city"));
                    }

                    JSONObject jsonObjectPhoto = jsonObjectVenue.getJSONObject("photos");
                    JSONArray jsonArrayGroups = jsonObjectPhoto.getJSONArray("groups");
                    JSONArray jsonArrayItems = jsonArrayGroups.getJSONObject(0).getJSONArray("items");
                    Glide.with(getApplicationContext()).load(jsonArrayItems.getJSONObject(0).getString("prefix")+jsonArrayItems.getJSONObject(0).getString("width")+"x"+jsonArrayItems.getJSONObject(0).getString("height")+jsonArrayItems.getJSONObject(0).getString("suffix")).into(ivDetaill);
                    lat =jsonObjectLocation.getDouble("lat");
                    lng=jsonObjectLocation.getDouble("lng");
                    bNav.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?daddr=" + lat + "," + lng)));
                        }
                    });
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 14));
                    map.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).icon(BitmapDescriptorFactory.fromResource(R.drawable.food)));
                    JSONObject jsonObject =jsonObjectVenue.getJSONObject("tips");
                    if(jsonObject.getInt("count")>0){
                        JSONObject jsonObject1 = jsonObject.getJSONArray("groups").getJSONObject(0);
                        JSONObject text= jsonObject1.getJSONArray("items").getJSONObject(0);
                        tvDetailReview.setText(text.getString("text"));
                        for(int j=0;j<jsonObject1.getJSONArray("items").length();j++){
                            reviewList.add(jsonObject1.getJSONArray("items").getJSONObject(j).getString("text") + "");
                            String fn = jsonObject1.getJSONArray("items").getJSONObject(j).getJSONObject("user").getString("firstName");
                            userList.add(fn);
                        }
                        tvSeeAllReview.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent i = new Intent(DetailActivity.this,ReviewActivity.class);
                                i.putStringArrayListExtra("list",(ArrayList<String>)reviewList);
                                i.putStringArrayListExtra("user",(ArrayList<String>)userList);
                                i.putExtra("nama",nama);
                                startActivity(i);
                            }
                        });
                    }

                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
                dialog.dismiss();

            }
            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getApplicationContext(), "error connection " + "", Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        });
    }

    public void loading() {
        dialog = ProgressDialog.show(DetailActivity.this, "Loading", "Please wait", true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
