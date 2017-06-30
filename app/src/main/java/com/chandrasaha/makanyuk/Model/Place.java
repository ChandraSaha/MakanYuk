package com.chandrasaha.makanyuk.Model;

/**
 * Created by Chandra Saha on 11/17/2015.
 */
public class Place {
    String idTempat;
    String nama;
    String alamat;
    Double latitude;
    Double longtitude;
    String imgUrl;
    int checkIn;


    public Place() {
    }

    public Place(String idTempat, String nama, String alamat, Double latitude, Double longtitude, String imgUrl) {
        this.idTempat = idTempat;
        this.nama = nama;
        this.alamat = alamat;
        this.latitude = latitude;
        this.longtitude = longtitude;
        this.imgUrl = imgUrl;
    }

    public String getIdTempat() {
        return idTempat;
    }

    public void setIdTempat(String idTempat) {
        this.idTempat = idTempat;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(Double longtitude) {
        this.longtitude = longtitude;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(int checkIn) {
        this.checkIn = checkIn;
    }
}
