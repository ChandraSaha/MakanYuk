package com.chandrasaha.makanyuk.Utils;

import android.database.Observable;

import java.io.File;
import java.util.Map;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.DELETE;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.Path;
import retrofit.http.QueryMap;
import retrofit.mime.TypedFile;
import retrofit.mime.TypedString;

/**
 * Created by SIAGUS on 16/09/2015.
 */

public interface APIservice {

    //REFERENCE API
    @GET("/v2/venues/explore?client_id=E5KI0OVUNGJ4VEUC2IOMADRSSZ5RPN4UEJPG32CVNFBPE3X3&client_secret=XZ12EESN54I4ZH1PPU0AHAZM4XWMJPYI5SLBVYWPJS1EDYFX&v=20141011&venuePhotos=1&section=food")
    void getLokasi(@QueryMap Map<String, String> options, Callback<Response> cb);

    @GET("/v2/venues/{id}?client_id=E5KI0OVUNGJ4VEUC2IOMADRSSZ5RPN4UEJPG32CVNFBPE3X3&client_secret=XZ12EESN54I4ZH1PPU0AHAZM4XWMJPYI5SLBVYWPJS1EDYFX&v=20141011")
    void getDetail(@Path("id") String id,@QueryMap Map<String, String> options, Callback<Response> cb);

    @GET("/v2/venues/explore?client_id=E5KI0OVUNGJ4VEUC2IOMADRSSZ5RPN4UEJPG32CVNFBPE3X3&client_secret=XZ12EESN54I4ZH1PPU0AHAZM4XWMJPYI5SLBVYWPJS1EDYFX&v=20141011&venuePhotos=1")
    void searchLokasi(@QueryMap Map<String, String> options, Callback<Response> cb);

    @GET("/datatxt/sent/v1?token=fb9a55a1f061461e9c373b4b06c931e4")
    void sentSentimen(@QueryMap Map<String, String> options, Callback<Response> cb);
}
