package com.home.dbykovskyy.imagesearch.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by dbykovskyy on 9/22/15.
 */
public class ImageResult implements Parcelable {

    private String title;
    private String thumbnailUrl;
    private String fullUrl;

    public ImageResult(JSONObject obj){
        try {

            this.thumbnailUrl = obj.getString("tbUrl");
            this.title = obj.getString("title");
            this.fullUrl = obj.getString("url");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<ImageResult> getFromJsonArray(JSONArray array){
        ArrayList<ImageResult> results = new ArrayList<ImageResult>();

        for(int i=0; i<array.length(); i++){
            try {
                results.add(new ImageResult(array.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return results;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getFullUrl() {
        return fullUrl;
    }

    public void setFullUrl(String fullUrl) {
        this.fullUrl = fullUrl;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.thumbnailUrl);
        dest.writeString(this.fullUrl);
    }

    protected ImageResult(Parcel in) {
        this.title = in.readString();
        this.thumbnailUrl = in.readString();
        this.fullUrl = in.readString();
    }

    public static final Parcelable.Creator<ImageResult> CREATOR = new Parcelable.Creator<ImageResult>() {
        public ImageResult createFromParcel(Parcel source) {
            return new ImageResult(source);
        }

        public ImageResult[] newArray(int size) {
            return new ImageResult[size];
        }
    };
}
