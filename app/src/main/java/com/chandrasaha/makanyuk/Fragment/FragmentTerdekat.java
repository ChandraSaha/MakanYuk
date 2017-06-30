package com.chandrasaha.makanyuk.Fragment;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.chandrasaha.makanyuk.MenuRadiusDialog;
import com.chandrasaha.makanyuk.Utils.LocationService;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.model.LatLng;
import com.chandrasaha.makanyuk.Adapter.PlaceAdapter;
import com.chandrasaha.makanyuk.Model.Place;
import com.chandrasaha.makanyuk.R;
import com.chandrasaha.makanyuk.Utils.APIservice;
import com.chandrasaha.makanyuk.Utils.RESTcontroller;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.RestAdapter;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Chandra Saha on 11/17/2015.
 */
public class FragmentTerdekat extends Fragment{
    private static final String TAG = "HomeFragment";
    ProgressDialog dialog;
    private static List<Place> placeList = new ArrayList<Place>();
    public static GridView gridView;
    private static PlaceAdapter adapterList;
    private static final String KEY_TITLE = "delivery://tracking//PIC";
    static Context ctx;
    public static boolean radiusChange = false;
    LocationService locationService;
    LatLng position;
    public static int radius;
    static String results;
    static String home = "";
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 17;

    public FragmentTerdekat() {

    }

    public static FragmentTerdekat newInstance(Context context, String title) {
        FragmentTerdekat f = new FragmentTerdekat();
        ctx = context;
        home = title;
        Bundle args = new Bundle();
        args.putString(KEY_TITLE, title);
        f.setArguments(args);
        return (f);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //ThemeManager.init(getActivity(), 1, 0, null);
        final View viewRoot = inflater.inflate(R.layout.fragment_terdekat, container, false);
        locationService = new LocationService(getContext());
        adapterList = new PlaceAdapter(getActivity(), placeList);
        radius = 100;
        if (ActivityCompat.checkSelfPermission(ctx.getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        } else {
            if (locationService.canGetLocation()) {
                position = new LatLng(locationService.getLatitude(), locationService.getLongitude());
                if (home.equals("")) {
                    loadfromApi("");
                } else {
                    search("");
                }
            } else {
                displayPromptForEnablingGPS(getActivity());
            }
        }
        gridView = (GridView) viewRoot.findViewById(R.id.gridview);
        gridView.setAdapter(adapterList);

        return viewRoot;
    }

    private void loadfromApi(String search) {
        loading();
        placeList.clear();
        final RESTcontroller rc = new RESTcontroller();
        rc.setTimeout(30);
        RestAdapter restAdapter = rc.adapter(getActivity().getApplicationContext());
        APIservice apiService = restAdapter.create(APIservice.class);
        Log.e("title",home);
        Map query = new HashMap();
        query.put("ll", position.latitude + "," + position.longitude);
        if (!search.equals("")) {
            query.put("query", search);
        } else {
            query.put("radius", radius * 100);
        }
        apiService.getLokasi(query, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                StringBuilder sb = rc.toStringBuilder(response);
                results = sb.toString();
                try {
                    JSONObject jsonobj = new JSONObject(results);
                    JSONObject jsonobj2 = jsonobj.getJSONObject("response");
                    JSONArray jsonObject = jsonobj2.getJSONArray("groups");
                    JSONObject jsonObject2 = jsonObject.getJSONObject(0);
                    JSONArray jarrydata = jsonObject2.getJSONArray("items");
                    JSONObject obj;
                    int i;
                    for (i = 0; i < jarrydata.length(); i++) {
                        obj = jarrydata.getJSONObject(i);
                        JSONObject jsonObject1 = obj.getJSONObject("venue");
                        Place place = new Place();
                        place.setIdTempat(jsonObject1.getString("id"));
                        place.setNama(jsonObject1.getString("name"));
                        JSONObject jsonObject3 = jsonObject1.getJSONObject("location");
                        place.setLatitude(jsonObject3.getDouble("lat"));
                        place.setLongtitude(jsonObject3.getDouble("lng"));
                        Log.i("i", String.valueOf(i));
                        if (!jsonObject3.isNull("address")) {
                            place.setAlamat(jsonObject3.getString("address"));
                        }
                        JSONObject jsonObject4 = jsonObject1.getJSONObject("photos");
                        JSONArray jsonArray = jsonObject4.getJSONArray("groups");
                        if (!jsonArray.isNull(0)) {
                            JSONObject jsonObject5 = jsonArray.getJSONObject(0);
                            JSONArray jsonArray1 = jsonObject5.getJSONArray("items");
                            JSONObject jsonObject6 = jsonArray1.getJSONObject(0);
                            place.setImgUrl(jsonObject6.getString("prefix") + jsonObject6.getString("width") + "x" + jsonObject6.getString("height") + jsonObject6.getString("suffix"));
                        } else {
                            place.setImgUrl("");
                        }
                        placeList.add(place);
                    }
                 } catch (JSONException e1) {
                    e1.printStackTrace();
                }
                adapterList.notifyDataSetChanged();
                dialog.dismiss();
            }
            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(ctx, "error connection" + "", Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        });
    }

    private void search(String search) {
        loading();
        placeList.clear();
        final RESTcontroller rc = new RESTcontroller();
        rc.setTimeout(30);
        RestAdapter restAdapter = rc.adapter(getActivity().getApplicationContext());
        APIservice apiService = restAdapter.create(APIservice.class);

        Map query = new HashMap();
        query.put("ll", position.latitude + "," + position.longitude);
        if(!home.equals("")){
            switch (home){
                case "Restaurant":
                    query.put("query", "Restaurant");
                    break;
                case "Cafe":
                    query.put("query", "Cafe");
                    break;
                case "FastFood":
                    query.put("query", "FastFood || KFC || MCD || Olive");
                    break;
                case "CofeeShop":
                    query.put("query", "Coffe");
                    break;
            }
        }
        if(!search.equals("")){
            query.put("query",search);
        }
        else{
            query.put("radius", radius*100);
        }

        apiService.searchLokasi(query, new Callback<Response>() {
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
                    int i;
                    for (i = 0; i < jarrydata.length(); i++) {
                        obj = jarrydata.getJSONObject(i);
                        JSONObject jsonObject1 = obj.getJSONObject("venue");
                        Place place = new Place();
                        place.setIdTempat(jsonObject1.getString("id"));
                        place.setNama(jsonObject1.getString("name"));
                        JSONObject jsonObject3 = jsonObject1.getJSONObject("location");
                        place.setLatitude(jsonObject3.getDouble("lat"));
                        place.setLongtitude(jsonObject3.getDouble("lng"));
                        if(!jsonObject3.isNull("address")) {
                            place.setAlamat(jsonObject3.getString("address"));
                        }
                        JSONObject jsonObject4 = jsonObject1.getJSONObject("photos");
                        JSONArray jsonArray = jsonObject4.getJSONArray("groups");
                        if(!jsonArray.isNull(0)) {
                            JSONObject jsonObject5 = jsonArray.getJSONObject(0);

                            JSONArray jsonArray1 = jsonObject5.getJSONArray("items");
                            JSONObject jsonObject6 = jsonArray1.getJSONObject(0);
                            place.setImgUrl(jsonObject6.getString("prefix") + jsonObject6.getString("width") + "x" + jsonObject6.getString("height") + jsonObject6.getString("suffix"));
                          }
                        else{
                            place.setImgUrl("");
                        }

                        placeList.add(place);

                    }


                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
                adapterList.notifyDataSetChanged();
                dialog.dismiss();
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(ctx, "error connection" + "", Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        });
    }



    public void loading() {
        dialog = ProgressDialog.show(getActivity(),"Loading","Please wait",true);
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Add your menu entries here
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id ==R.id.action_search){
            android.support.v7.widget.SearchView sv = new android.support.v7.widget.SearchView(((AppCompatActivity) getActivity()).getSupportActionBar().getThemedContext());
            MenuItemCompat.setShowAsAction(item, MenuItemCompat.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
            MenuItemCompat.setActionView(item, sv);
            sv.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    search(query);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });
        }
        if (id == R.id.action_settings) {
            loadfromApi("");
            return true;
        }
        return false;
    }
}
