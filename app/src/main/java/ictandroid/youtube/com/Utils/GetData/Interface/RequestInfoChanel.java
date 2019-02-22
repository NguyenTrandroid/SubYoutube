package ictandroid.youtube.com.Utils.GetData.Interface;

import ictandroid.youtube.com.Utils.GetData.Models.InfoChanel.ChanelItem;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RequestInfoChanel {
    @GET("channels")
    Observable<ChanelItem> register(@Query("part") String part,
                                    @Query("id") String id,
                                    @Query("key") String key);
}