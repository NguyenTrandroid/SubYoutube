package ictandroid.youtube.com.Utils.GetData.Interface;

import java.util.List;

import ictandroid.youtube.com.Utils.GetData.Models.InfoSubChannel.SubChannelItem;

public interface SubscriberOnCampaignV2Listener {
    void onCompletedListSubcriberCampaignV2(List<SubChannelItem> listSubscribers);
    void onErrorListSubcripberCampaignV2(String error);
}
