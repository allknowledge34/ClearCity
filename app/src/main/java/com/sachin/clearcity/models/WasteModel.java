package com.sachin.clearcity.models;

import java.util.List;

public class WasteModel {

    private String uId;
    private String title;
    private String category;
    private String location;
    private List<String> imageUrls;

    public WasteModel() {
    }

    public WasteModel(String uId, String title, String category, String location, List<String> imageUrls) {
        this.uId = uId;
        this.title = title;
        this.category = category;
        this.location = location;
        this.imageUrls = imageUrls;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }
}
