package ictandroid.youtube.com.Utils.GetData.Models.ListSub;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PageInfo {

@SerializedName("totalResults")
@Expose
private Integer totalResults;

public Integer getTotalResults() {
return totalResults;
}

public void setTotalResults(Integer totalResults) {
this.totalResults = totalResults;
}

}