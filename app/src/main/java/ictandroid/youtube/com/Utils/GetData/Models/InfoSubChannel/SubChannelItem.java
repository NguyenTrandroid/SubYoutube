package ictandroid.youtube.com.Utils.GetData.Models.InfoSubChannel;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SubChannelItem {

@SerializedName("items")
@Expose
private List<Item> items = null;

public List<Item> getItems() {
return items;
}

public void setItems(List<Item> items) {
this.items = items;
}

}