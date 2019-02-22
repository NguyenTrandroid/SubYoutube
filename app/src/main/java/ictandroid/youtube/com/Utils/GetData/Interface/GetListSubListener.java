package ictandroid.youtube.com.Utils.GetData.Interface;

import ictandroid.youtube.com.Utils.GetData.Models.ListSub.SubItem;

public interface GetListSubListener {
    void onSubCompleted(SubItem subItem);
    void onSubError(String error);
}
