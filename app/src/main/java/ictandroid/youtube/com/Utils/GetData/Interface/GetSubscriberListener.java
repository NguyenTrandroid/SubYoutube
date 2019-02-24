package ictandroid.youtube.com.Utils.GetData.Interface;

import ictandroid.youtube.com.Utils.GetData.Models.InfoSubChannel.SubChannelItem;

public interface GetSubscriberListener {
    void onCompletedSubcriber(SubChannelItem subChannelItem);
    void onErrorSubcripber(String error);
}
