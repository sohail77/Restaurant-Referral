package com.sohail.restaurant_referral.Models;

public class RestaurantModel {

    //This is a model class for Restaurant.
    //Below variables display all the attributes present in the Restaurant data.
    String name,description,imageUrl,phone,email;
    Double lat,lon;

    public RestaurantModel() {
    }

    public RestaurantModel(String name, String description, String imageUrl, String phone, String email, Double lat, Double lon) {
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.phone = phone;
        this.email = email;
        this.lat = lat;
        this.lon = lon;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
