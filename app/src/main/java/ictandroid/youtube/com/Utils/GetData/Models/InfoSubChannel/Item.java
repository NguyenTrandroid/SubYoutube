package ictandroid.youtube.com.Utils.GetData.Models.InfoSubChannel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Item {

@SerializedName("statistics")
@Expose
private Statistics statistics;

public Statistics getStatistics() {
return statistics;
}

public void setStatistics(Statistics statistics) {
this.statistics = statistics;
}

}