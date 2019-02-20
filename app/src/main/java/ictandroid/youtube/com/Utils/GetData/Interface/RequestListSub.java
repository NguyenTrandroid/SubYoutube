package ictandroid.youtube.com.Utils.GetData.Interface;

import ictandroid.youtube.com.Utils.GetData.Models.ChanelItem;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RequestListSub {
    @GET("subscriptions")
    Observable<ChanelItem> register(@Query("pageToken") String pageToken,
                                    @Query("part") String part,
                                    @Query("channelId") String channelId,
                                    @Query("maxResults") String maxResults,
                                    @Query("key") String key);
}
