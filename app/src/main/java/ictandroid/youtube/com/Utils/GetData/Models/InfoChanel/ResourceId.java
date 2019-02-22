package ictandroid.youtube.com.Utils.GetData.Models.InfoChanel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResourceId {

@SerializedName("channelId")
@Expose
private String channelId;

public String getChannelId() {
return channelId;
}

public void setChannelId(String channelId) {
this.channelId = channelId;
}

}