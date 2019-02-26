package ictandroid.youtube.com.Campaign.AllChannel;

import java.util.List;

import ictandroid.youtube.com.Utils.GetData.Models.InfoSubChannel.SubChannelItem;

public interface GetSubFromCampaignV2Listener {
    void onCompletedSubV2FromActivity(List<SubChannelItem> lisSubChannelItem);
}
