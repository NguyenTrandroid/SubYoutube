package ictandroid.youtube.com.Utils.GetData;

import android.content.Context;
import android.util.Log;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import ictandroid.youtube.com.Utils.GetData.Interface.GetInfoChanelListener;
import ictandroid.youtube.com.Utils.GetData.Interface.GetSubListener;
import ictandroid.youtube.com.Utils.GetData.Interface.RequestInfoChanel;
import ictandroid.youtube.com.Utils.GetData.Interface.RequestListSub;
import ictandroid.youtube.com.Utils.GetData.Models.InfoChanel.ChanelItem;
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
    private Context context;
    GetInfoChanelListener getInfoChanelListener;
    GetSubListener getSubListener;

    public void getInfo(Context context, String key, String id)
    {
        compositeDisposable = new CompositeDisposable();
        chanelItem = new ChanelItem();
        this.context = context;

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
    public void getSub(Context context, String key, String id)
    {
        compositeDisposable = new CompositeDisposable();
        chanelItem = new ChanelItem();
        subItem = new SubItem();
        this.context = context;

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

    private void successGetListSub() {
        Log.d("GETLISTSUB","COMPLETED");
    }
    private void responseGetListSub(SubItem itemSub) {
        subItem = itemSub;
        getSubListener.onSubCompleted(subItem);
    }
    private void errorGetListSub(Throwable error) {
        getSubListener.onSubError(error.getLocalizedMessage());
        Log.d("GETLISTSUB","ERROR: "+error.getLocalizedMessage());
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

}
