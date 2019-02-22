package ictandroid.youtube.com.Utils.GetData.Models.ListSub;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Item {

@SerializedName("id")
@Expose
private String id;

public String getId() {
return id;
}

public void setId(String id) {
this.id = id;
}

}