package ictandroid.youtube.com.Utils.GetData.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import ictandroid.youtube.com.Utils.GetData.Models.Medium;

public class Thumbnails {

    @SerializedName("medium")
    @Expose
    private Medium medium;

    public Medium getMedium() {
        return medium;
    }

    public void setMedium(Medium medium) {
        this.medium = medium;
    }

}