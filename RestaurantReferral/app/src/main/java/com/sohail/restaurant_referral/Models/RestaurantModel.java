package com.sohail.restaurant_referral.Models;

public class RestaurantModel {
    String name,description,imageUrl;
    Double lat,lon;

    public RestaurantModel() {
    }

    public RestaurantModel(String name, String description, String imageUrl, Double lat, Double lon) {
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.lat = lat;
        this.lon = lon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }
}
