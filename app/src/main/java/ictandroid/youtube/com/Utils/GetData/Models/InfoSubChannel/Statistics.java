package ictandroid.youtube.com.Utils.GetData.Models.InfoSubChannel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Statistics {

@SerializedName("subscriberCount")
@Expose
private String subscriberCount;

public String getSubscriberCount() {
return subscriberCount;
}

public void setSubscriberCount(String subscriberCount) {
this.subscriberCount = subscriberCount;
}

}