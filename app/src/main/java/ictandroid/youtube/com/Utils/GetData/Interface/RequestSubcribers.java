package ictandroid.youtube.com.Utils.GetData.Interface;

import ictandroid.youtube.com.Utils.GetData.Models.InfoChanel.ChanelItem;
import ictandroid.youtube.com.Utils.GetData.Models.InfoSubChannel.SubChannelItem;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RequestSubcribers {
    @GET("channels")
    Observable<SubChannelItem> register(@Query("part") String part,
                                        @Query("id") String id,
                                        @Query("key") String key);
}
