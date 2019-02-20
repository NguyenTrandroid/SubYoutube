package ictandroid.youtube.com.Utils.GetData.Interface;


import ictandroid.youtube.com.Utils.GetData.Models.ChanelItem;

public interface GetListSubListener {
    void onSubCompleted(ChanelItem chanelItem);
    void onSubError(String error);
}
