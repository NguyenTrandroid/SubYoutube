package ictandroid.youtube.com.Utils.GetData.Interface;

import ictandroid.youtube.com.Utils.GetData.Models.ChanelItem;

public interface GetInfoChanelListener {
    void onInfoCompleted(ChanelItem chanelItem);
    void onInfoError(String error);
}
