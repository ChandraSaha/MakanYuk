package com.chandrasaha.makanyuk.Utils;

import android.content.Context;

import com.squareup.okhttp.OkHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.client.Response;


/**
 * Created by SIAGUS on 16/09/2015.
 */

public class RESTcontroller {
    private int timeout = 30;

    public void setTimeout(int time){
        timeout = time;
    }

    public RestAdapter adapter(Context ctx) {
        final OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setReadTimeout(timeout, TimeUnit.SECONDS);
        okHttpClient.setConnectTimeout(timeout, TimeUnit.SECONDS);
        String base_url = "https://api.foursquare.com";
        return new RestAdapter.Builder()
                .setEndpoint(base_url)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setClient(new OkClient(okHttpClient))
                .build();
    }

    public RestAdapter adapter2(Context ctx) {
        final OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setReadTimeout(timeout, TimeUnit.SECONDS);
        okHttpClient.setConnectTimeout(timeout, TimeUnit.SECONDS);
        String base_url = "https://api.dandelion.eu";
        return new RestAdapter.Builder()
                .setEndpoint(base_url)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setClient(new OkClient(okHttpClient))
                .build();
    }
    public StringBuilder toStringBuilder(Response result){
        //Try to get response body
        BufferedReader reader = null;
        StringBuilder sb = new StringBuilder();
        try {

            reader = new BufferedReader(new InputStreamReader(result.getBody().in()));

            String line;

            try {
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb;
    }
    public String StripTags(String html){
        html = html.replaceAll("<(.*?)\\>"," ");//Removes all items in brackets
        html = html.replaceAll("<(.*?)\\\n"," ");//Must be undeneath
        html = html.replaceFirst("(.*?)\\>", " ");//Removes any connected item to the last bracket
        html = html.replaceAll("&nbsp;"," ");
        html = html.replaceAll("&amp;"," ");
        return html;
    }
}
