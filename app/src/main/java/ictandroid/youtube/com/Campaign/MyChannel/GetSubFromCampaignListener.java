package ictandroid.youtube.com.Campaign.MyChannel;

import java.util.List;

import ictandroid.youtube.com.Utils.GetData.Models.InfoSubChannel.SubChannelItem;

public interface GetSubFromCampaignListener {
    void onCompletedSubFromActivity(List<SubChannelItem> lisSubChannelItem);
}
