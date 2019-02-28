package ictandroid.youtube.com.Utils.GetData;

import android.content.Context;
import android.util.Log;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import ictandroid.youtube.com.Utils.GetData.Interface.SubscriberOnCampaignV2Listener;
import ictandroid.youtube.com.Utils.GetData.Interface.SubscribersOnMyAppV2Listener;
import ictandroid.youtube.com.Utils.GetData.Interface.RequestSubcribers;
import ictandroid.youtube.com.Utils.GetData.Models.InfoSubChannel.SubChannelItem;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SubcribersOnCampaign {
    private CompositeDisposable compositeDisposable;
    private SubChannelItem subChannelItem;
    private List<SubChannelItem> listSubChannelItem;
    private int sizeListSubscribers;
    SubscriberOnCampaignV2Listener subscriberOnCampaignV2Listener;

    //getListSubscribers
    private void getSubscribersOfChannel(Context context, String key, String id)
    {
        compositeDisposable = new CompositeDisposable();
        subChannelItem = new SubChannelItem();

        subscriberOnCampaignV2Listener = (SubscriberOnCampaignV2Listener) context;

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10,TimeUnit.SECONDS)
                .connectTimeout(10,TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .protocols(Arrays.asList(Protocol.HTTP_1_1))
                .build();
        RequestSubcribers requestListSub = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("https://www.googleapis.com/youtube/v3/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(RequestSubcribers.class);
        Disposable disposable = requestListSub.register("statistics",id,key)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::responseGetSubcribersOfChannel, this::errorGetSubcribersOfChannel, this::successGetSubcribersOfChannel);
        compositeDisposable.add(disposable);
    }
    public void getListSubscripbers(Context context, String key, List<String> listIdChannel)
    {
        listSubChannelItem = new ArrayList<>();
        sizeListSubscribers = listIdChannel.size();
        for(int i = 0; i<listIdChannel.size();i++)
        {
            getSubscribersOfChannel(context,key,listIdChannel.get(i));
        }
    }
    private void successGetSubcribersOfChannel() {
        Log.d("GETSUBer","COMPLETED");
    }
    private void responseGetSubcribersOfChannel(SubChannelItem itemSub) {
        Log.d("GETSUBer", itemSub.toString());
        subChannelItem = itemSub;
        listSubChannelItem.add(subChannelItem);
        if(listSubChannelItem.size()==sizeListSubscribers)
        {
            subscriberOnCampaignV2Listener.onCompletedListSubcriberCampaignV2(listSubChannelItem);
        }
    }
    private void errorGetSubcribersOfChannel(Throwable error) {
        //subscriberOnCampaignV2Listener.onErrorListSubcripberCampaignV2(error.getLocalizedMessage());
    }
}
