package ictandroid.youtube.com.Utils.GetData.Interface;

import java.util.List;

import ictandroid.youtube.com.Utils.GetData.Models.InfoSubChannel.SubChannelItem;

public interface GetListSubscribersV2Listener {
    void onCompletedListSubcriberV2(List<SubChannelItem> listSubscribers);
    void onErrorListSubcripberV2(String error);
}
