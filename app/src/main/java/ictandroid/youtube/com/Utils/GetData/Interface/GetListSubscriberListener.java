package ictandroid.youtube.com.Utils.GetData.Interface;

import java.util.List;

import ictandroid.youtube.com.Utils.GetData.Models.InfoSubChannel.SubChannelItem;

public interface GetListSubscriberListener {
    void onCompletedListSubcriber(List<SubChannelItem> listSubscribers);
    void onErrorListSubcripber(String error);
}
