package com.chandrasaha.makanyuk.Fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.chandrasaha.makanyuk.Adapter.MarkerAdapter;
import com.chandrasaha.makanyuk.DetailActivity;
import com.chandrasaha.makanyuk.Model.Place;
import com.chandrasaha.makanyuk.R;
import com.chandrasaha.makanyuk.Utils.APIservice;
import com.chandrasaha.makanyuk.Utils.RESTcontroller;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.chandrasaha.makanyuk.Utils.LocationService;

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

/**
 * Created by Chandra Saha on 11/17/2015.
 */
public class FragmentMap extends Fragment{
    private static final String TAG = "MapFragment";
    static Context ctx;
    private View v;
    private static List<Place> placeList = new ArrayList<Place>();
    private static List<MarkerAdapter> markerMarkerAdapter =new ArrayList<>();
    public static String results;
    private Button bFind;
    private LatLng Venue;
    private SupportMapFragment mapFragment;
    private GoogleMap map;
    private LocationService locationService;
    private LatLng position;
    private int radius;
    private ProgressDialog dialog;
    private static final String ARG_PARAM1 = "param1";
    private String mParam1;
    private SeekBar sliderRadius;
    private TextView tvRadius;


    public static FragmentMap newInstance(Context context, String param1) {
        FragmentMap fragment = new FragmentMap();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        ctx = context;
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentMap() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        radius = 50;
        v = inflater.inflate(R.layout.fragment_map, container, false);
        sliderRadius = (SeekBar) v.findViewById(R.id.sliderRadius);
        bFind = (Button) v.findViewById(R.id.bFind);
        sliderRadius.setProgress(radius);
        sliderRadius.setMax(100);
        tvRadius = (TextView) v.findViewById(R.id.tvRadius);
        tvRadius.setText(String.format("%d m", sliderRadius.getProgress()*100));
        locationService = new LocationService(getContext());
        mapFragment= ((SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map));

        bFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                map.clear();
                loadfromApi();
            }
        });
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;
                map.setMyLocationEnabled(true);
                if (locationService.canGetLocation()) {
                    position = new LatLng(locationService.getLatitude(), locationService.getLongitude());
                    loadfromApi();
                } else {
                    displayPromptForEnablingGPS(getActivity());
                }


            }
        });


        sliderRadius.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                tvRadius.setText(i * 100 + " m");
                radius = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        return v;
    }


    private void loadfromApi() {
        loading();
        placeList.clear();
        markerMarkerAdapter.clear();
        final RESTcontroller rc = new RESTcontroller();
        rc.setTimeout(30);
        RestAdapter restAdapter = rc.adapter(getActivity().getApplicationContext());
        APIservice apiService = restAdapter.create(APIservice.class);

        Map query = new HashMap();
        query.put("radius", radius*100);
        query.put("ll", position.latitude + "," + position.longitude);

        apiService.getLokasi(query, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                StringBuilder sb = rc.toStringBuilder(response);
                results = sb.toString();
                String data = "";


                try {
                    JSONObject jsonobj = new JSONObject(results);
                    JSONObject jsonobj2 = jsonobj.getJSONObject("response");
                    JSONArray jsonObject = jsonobj2.getJSONArray("groups");
                    JSONObject jsonObject2 = jsonObject.getJSONObject(0);
                    JSONArray jarrydata = jsonObject2.getJSONArray("items");
                    JSONObject obj;
                    for (int i = 0; i < jarrydata.length(); i++) {
                        obj = jarrydata.getJSONObject(i);
                        JSONObject jsonObject1 = obj.getJSONObject("venue");
                        Place place = new Place();
                        place.setIdTempat(jsonObject1.getString("id"));

                        place.setNama(jsonObject1.getString("name"));
                        JSONObject jsonObject3 = jsonObject1.getJSONObject("location");
                        place.setLatitude(jsonObject3.getDouble("lat"));
                        place.setLongtitude(jsonObject3.getDouble("lng"));
                        if (!jsonObject3.isNull("address")) {
                            place.setAlamat(jsonObject3.getString("address"));
                        }

                        draw(place);
                        placeList.add(place);
                    }
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
                dialog.dismiss();
            }
            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(ctx, "error connection" + "", Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        });
    }

    private void draw(final Place place) {
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(Venue = new LatLng(position.latitude, position.longitude), 14));

        Marker marker=        map.addMarker(new MarkerOptions()
                .title(place.getNama())
                .snippet(String.valueOf(place.getAlamat()))
                .position(Venue = new LatLng(place.getLatitude(), place.getLongtitude()))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.food))
                .draggable(false));



        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                for (int i = 0; i < placeList.size(); i++) {
                    if (marker.getPosition().latitude == placeList.get(i).getLatitude() && marker.getPosition().longitude == placeList.get(i).getLongtitude()) {
                        Intent intent = new Intent(getActivity().getApplicationContext(), DetailActivity.class);
                        intent.putExtra("idPlace", placeList.get(i).getIdTempat());
                        startActivity(intent);
                    }
                }
            }
        });

        if(position.longitude!=0||position.longitude!=0){
            map.addMarker(new MarkerOptions()
                    .title("Anda")
                    .snippet("disini")
                    .position(position)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                    .draggable(false))
                    .showInfoWindow();
            int colour = (Color.parseColor("#20A294") & 0x00FFFFFF) | 0x0D000000;
            int colour2 = (Color.parseColor("#20A294") & 0x00FFFFFF) | 0x1A000000;
            map.addCircle(new CircleOptions().radius(radius * 100).center(position));
        }
    }

    public static void displayPromptForEnablingGPS(final Activity activity) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        final String action = Settings.ACTION_LOCATION_SOURCE_SETTINGS;
        final String message = "GPS belum dinyalakan."
                + " Tekan OK untuk menuju setting untuk menyalakan GPS.";

        builder.setMessage(message)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface d, int id) {
                                activity.startActivity(new Intent(action));
                                d.dismiss();
                            }
                        })
                .setNegativeButton("Kembali",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface d, int id) {
                                d.cancel();
                            }
                        });
        builder.create().show();
    }
    public void loading() {
        dialog = ProgressDialog.show(getActivity(), "Loading", "Please wait", true);
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }
}
