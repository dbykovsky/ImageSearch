package com.home.dbykovskyy.imagesearch.models;

import android.os.Parcelable;

import java.util.List;

/**
 * Created by dbykovskyy on 9/24/15.
 */
public class FilterFragment {
    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    public String getImageSize() {
        return imageSize;
    }

    public void setImageSize(String imageSize) {
        this.imageSize = imageSize;
    }

    public String getImageColor() {
        return imageColor;
    }

    public void setImageColor(String imageColor) {
        this.imageColor = imageColor;
    }

    String imageColor;
    String imageSize;
    String imageType;
    String website;


    public FilterFragment(String color, String imageSize, String imageType, String website){

        this.imageColor = color;
        this.imageSize = imageSize;
        this.imageType = imageType;
        this.website = website;
    }

    public FilterFragment(List<String> ls){

        this.imageColor = ls.get(0);
        this.imageType = ls.get(1);
        this.imageSize = ls.get(2);
        this.website = ls.get(3);
    }

    public void clearFilters(){
        this.imageSize = "not selected";
        this.imageColor = "not selected";
        this.imageType = "not selected";
        this.website = "";
    }



}

