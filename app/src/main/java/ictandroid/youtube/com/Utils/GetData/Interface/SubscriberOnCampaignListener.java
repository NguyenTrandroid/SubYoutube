package ictandroid.youtube.com.Utils.GetData.Interface;

import java.util.List;

import ictandroid.youtube.com.Utils.GetData.Models.InfoSubChannel.SubChannelItem;

public interface SubscriberOnCampaignListener {
    void onCompletedListSubcriberCampaign(List<SubChannelItem> listSubscribers);
    void onErrorListSubcripberCampaign(String error);
}
