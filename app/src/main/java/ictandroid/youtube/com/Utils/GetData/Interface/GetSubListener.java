package ictandroid.youtube.com.Utils.GetData.Interface;

import java.util.List;

import ictandroid.youtube.com.Utils.GetData.Models.ListSub.SubItem;

public interface GetSubListener {
    void onListSubCompleted(List<SubItem> listSubItem);
    void onSubError(String error);
}
