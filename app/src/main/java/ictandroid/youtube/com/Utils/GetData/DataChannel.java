package ictandroid.youtube.com.Utils.GetData;

import android.content.Context;
import android.util.Log;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import ictandroid.youtube.com.Utils.GetData.Interface.GetInfoChanelListener;
import ictandroid.youtube.com.Utils.GetData.Interface.SubscriberOnMyAppListener;
import ictandroid.youtube.com.Utils.GetData.Interface.GetSubListener;
import ictandroid.youtube.com.Utils.GetData.Interface.GetSubscriberListener;
import ictandroid.youtube.com.Utils.GetData.Interface.RequestInfoChanel;
import ictandroid.youtube.com.Utils.GetData.Interface.RequestListSub;
import ictandroid.youtube.com.Utils.GetData.Interface.RequestSubcribers;
import ictandroid.youtube.com.Utils.GetData.Models.InfoChanel.ChanelItem;
import ictandroid.youtube.com.Utils.GetData.Models.InfoSubChannel.SubChannelItem;
import ictandroid.youtube.com.Utils.GetData.Models.ListSub.SubItem;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DataChannel {
    private CompositeDisposable compositeDisposable;
    private ChanelItem chanelItem;
    private SubItem subItem;
    private SubChannelItem subChannelItem;
    private int sizeList;
    GetInfoChanelListener getInfoChanelListener;
    GetSubListener getSubListener;
    GetSubscriberListener getSubscriberListener;
    private List<SubItem> listSubItem;
    private List<SubChannelItem> listSubChannelItem;
    private int sizeListSubscribers;
    SubscriberOnMyAppListener subscriberOnMyAppListener;

    //GetInfoChannel
    public void getInfo(Context context, String key, String id)
    {
        compositeDisposable = new CompositeDisposable();
        chanelItem = new ChanelItem();

        getInfoChanelListener = (GetInfoChanelListener) context;

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10,TimeUnit.SECONDS)
                .connectTimeout(10,TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .protocols(Arrays.asList(Protocol.HTTP_1_1))
                .build();
        RequestInfoChanel requestInfoChanel = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("https://www.googleapis.com/youtube/v3/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(RequestInfoChanel.class);
        Disposable disposable = requestInfoChanel.register("snippet",id,key)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::responseGetInfo, this::errorGetInfo, this::successGetInfo);
        compositeDisposable.add(disposable);
    }
    private void successGetInfo() {
        Log.d("GETINFO","Completed");
    }
    private void responseGetInfo(ChanelItem itemChanel) {
        chanelItem = itemChanel;
        getInfoChanelListener.onInfoCompleted(chanelItem);
    }
    private void errorGetInfo(Throwable error) {
        getInfoChanelListener.onInfoError(error.getLocalizedMessage());
        Log.d("GETINFO","ERROR: "+error.getLocalizedMessage());
    }

    //getSubscribers Channel
    public void getSubscribers(Context context, String key, String id)
    {
        compositeDisposable = new CompositeDisposable();
        subChannelItem = new SubChannelItem();

        getSubscriberListener = (GetSubscriberListener) context;

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
                .subscribe(this::responseGetSubcribers, this::errorGetSubcribers, this::successGetSubcribers);
        compositeDisposable.add(disposable);
    }
    private void successGetSubcribers() {
        Log.d("GETSUBer","COMPLETED");
    }
    private void responseGetSubcribers(SubChannelItem itemSub) {
        subChannelItem = itemSub;
        getSubscriberListener.onCompletedSubcriber(subChannelItem);
    }
    private void errorGetSubcribers(Throwable error) {
        getSubListener.onSubError(error.getLocalizedMessage());
    }

    //getListSubscribers
    private void getSubscribersOfChannel(Context context, String key, String id)
    {
        compositeDisposable = new CompositeDisposable();
        subChannelItem = new SubChannelItem();

        subscriberOnMyAppListener = (SubscriberOnMyAppListener) context;

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
        subChannelItem = itemSub;
        listSubChannelItem.add(subChannelItem);
        if(listSubChannelItem.size()==sizeListSubscribers)
        {
            subscriberOnMyAppListener.onCompletedListSubcriber(listSubChannelItem);
        }
    }
    private void errorGetSubcribersOfChannel(Throwable error) {
        subscriberOnMyAppListener.onErrorListSubcripber(error.getLocalizedMessage());
    }


    //getSubscription
    private void getSub(Context context, String key, String id)
    {
        compositeDisposable = new CompositeDisposable();
        subItem = new SubItem();

        getSubListener = (GetSubListener) context;

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10,TimeUnit.SECONDS)
                .connectTimeout(10,TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .protocols(Arrays.asList(Protocol.HTTP_1_1))
                .build();
        RequestListSub requestListSub = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("https://www.googleapis.com/youtube/v3/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(RequestListSub.class);
        Disposable disposable = requestListSub.register("","id",id,"0",key)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::responseGetListSub, this::errorGetListSub, this::successGetListSub);
        compositeDisposable.add(disposable);
    }

    //GetListSubscription
    public void getListSub(Context context, String key, List<String> listIdChannel)
    {
        listSubItem = new ArrayList<>();
        sizeList = listIdChannel.size();
        for(int i=0;i<listIdChannel.size();i++)
        {
            getSub(context,key,listIdChannel.get(i));
        }
    }
    private void successGetListSub() {
        Log.d("GETSUBer","COMPLETED");
    }
    private void responseGetListSub(SubItem itemSub) {
        subItem = itemSub;
        listSubItem.add(subItem);
        if(listSubItem.size() == sizeList)
        {
            getSubListener.onListSubCompleted(listSubItem);
        }
    }
    private void errorGetListSub(Throwable error) {
        getSubListener.onSubError(error.getLocalizedMessage());
        Log.d("GETLISTSUB","ERROR: "+error.getLocalizedMessage());
    }

}
