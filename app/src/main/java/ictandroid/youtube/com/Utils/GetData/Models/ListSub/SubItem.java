package ictandroid.youtube.com.Utils.GetData.Models.ListSub;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SubItem {

    @SerializedName("pageInfo")
    @Expose
    private PageInfo pageInfo;

    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }

}