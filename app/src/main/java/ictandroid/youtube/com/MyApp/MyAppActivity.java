package ictandroid.youtube.com.MyApp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import ictandroid.youtube.com.CONST;
import ictandroid.youtube.com.CloudFunction;
import ictandroid.youtube.com.Dialog.SLoading;
import ictandroid.youtube.com.ICloundFunction;
import ictandroid.youtube.com.MyApp.InCampaign.FragmentInCampaign;
import ictandroid.youtube.com.MyApp.InCampaign.GetSubFromActivityV2Listener;
import ictandroid.youtube.com.MyApp.Other.AddChannelOnFirebaseListener;
import ictandroid.youtube.com.MyApp.Other.FragmentOther;
import ictandroid.youtube.com.MyApp.Other.GetSubFomActivityListener;
import ictandroid.youtube.com.R;
import ictandroid.youtube.com.Utils.GetData.DataChannel;
import ictandroid.youtube.com.Utils.GetData.Interface.GetInfoChanelListener;
import ictandroid.youtube.com.Utils.GetData.Interface.SubscriberOnMyAppListener;
import ictandroid.youtube.com.Utils.GetData.Interface.SubscribersOnMyAppV2Listener;
import ictandroid.youtube.com.Utils.GetData.Models.InfoChanel.ChanelItem;
import ictandroid.youtube.com.Utils.GetData.Models.InfoChanel.Item;
import ictandroid.youtube.com.Utils.GetData.Models.InfoSubChannel.SubChannelItem;

public class MyAppActivity extends AppCompatActivity implements MyChanelAdapter.MyChannelInterface,
        GetInfoChanelListener, SubscriberOnMyAppListener, SubscribersOnMyAppV2Listener {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_coin)
    TextView tvCoin;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.svMyapp)
    SearchView svMyapp;
    CloudFunction cloudFunction;
    FirebaseAuth auth;
    SLoading sEdit;
    GetSubFomActivityListener getSubFomActivityListener;
    GetSubFromActivityV2Listener getSubFromActivityV2Listener;
    AddChannelOnFirebaseListener addChannelOnFirebaseListener;
    DocumentReference docRef;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_app);
        ButterKnife.bind(this);
        init();
        initViewPager();
        initAction();
        cloudFunction = new CloudFunction();
        auth = FirebaseAuth.getInstance();
        sEdit = new SLoading(this);
    }

    private void initViewPager() {
        PagerMyAppAdapter pagerMyAppAdapter = new PagerMyAppAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewpager.setAdapter(pagerMyAppAdapter);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewpager.setCurrentItem(tab.getPosition());
                if (tab.getPosition() == 1) {
                    // tabSeclec = 1;
                } else {
                    // tabSeclec = 0;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }

    private void initAction() {
    }

    private void init() {
        svMyapp.setQueryHint(Html.fromHtml("<font color = #ffffff>" + "Find a chanel..." + "</font>"));
        svMyapp.onActionViewExpanded();
        svMyapp.setFocusable(false);
        svMyapp.clearFocus();

    }

    public void backvip(View view) {
        onBackPressed();
    }

    @Override
    public void delete(String channelid) {
        sEdit.show();
        cloudFunction.removeMyChannel(channelid, new ICloundFunction() {
            @Override
            public void onSuccess() {
                sEdit.dismiss();
            }

            @Override
            public void onFailed() {
                sEdit.dismiss();
            }
        });
    }

    @Override
    public void addpoint(String channelid, int point) {
        sEdit.show();
        cloudFunction.addPointMyChannel(channelid, point, new ICloundFunction() {
            @Override
            public void onSuccess() {
                sEdit.dismiss();
            }

            @Override
            public void onFailed() {
                sEdit.dismiss();
            }
        });
    }

    @Override
    public void onInfoCompleted(ChanelItem chanelItem) {
        cloudFunction = new CloudFunction();
        Item item = chanelItem.getItems().get(0);
        db = FirebaseFirestore.getInstance();

        DocumentReference docRef = db.collection("USER").document(auth.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        for (Map.Entry<String, Object> entry : task.getResult().getData().entrySet()) {
                            if ("listadd".equals(entry.getKey())) {
                                Map<String, Object> nestedData = (Map<String, Object>) entry.getValue();
                                int sizeHash = 0;
                                boolean existed = false;
                                Log.d("VALUE", "item: " + item.getId());
                                for (Map.Entry<String, Object> entryNested : nestedData.entrySet()) {
                                    sizeHash++;
                                    if (entryNested.getKey().equals(item.getId()) && sizeHash != nestedData.size()) {
                                        Log.d("FFFF", "trufng");
                                        if (FragmentOther.sLoadingAddChannel != null) {
                                            FragmentOther.sLoadingAddChannel.dismiss();
                                        }
                                        if (FragmentOther.dialogAdd != null) {
                                            FragmentOther.dialogAdd.cancel();
                                        }
                                    }
                                    if (entryNested.getKey().equals(item.getId())) {
                                        existed = true;
                                    }

                                }
                                if (!existed) {
                                    cloudFunction.addChannel(item.getId(), item.getSnippet().getThumbnails().getMedium().getUrl(),
                                            item.getSnippet().getTitle(), "0", "0");
                                    if (CONST.tagFragmentOther != null) {
                                        FragmentOther fragmentOther = (FragmentOther) getSupportFragmentManager().findFragmentByTag("android:switcher:" + CONST.tagFragmentOther + ":1");
                                        if (fragmentOther != null) {
                                            addChannelOnFirebaseListener = (AddChannelOnFirebaseListener) fragmentOther;
                                            addChannelOnFirebaseListener.onCompletedAddChannel(chanelItem.getItems().get(0).getId());
                                        }
                                    }
                                }
                            }

                        }
                    } else {

                    }
                }
            }
        });
    }

    @Override
    public void onInfoError(String error) {
        if (FragmentOther.sLoadingAddChannel != null) {
            FragmentOther.sLoadingAddChannel.dismiss();
        }
    }

    @Override
    public void onCompletedListSubcriber(List<SubChannelItem> listSubscribers) {
        if (CONST.tagFragmentOther != null) {
            FragmentOther fragmentOther = (FragmentOther) getSupportFragmentManager().findFragmentByTag("android:switcher:" + CONST.tagFragmentOther + ":1");
            if (fragmentOther != null) {
                getSubFomActivityListener = (GetSubFomActivityListener) fragmentOther;
                getSubFomActivityListener.onCompletedSubFromActivity(listSubscribers);
            }
        }
    }

    @Override
    public void onErrorListSubcripber(String error) {

    }

    @Override
    public void onCompletedListSubcriberV2(List<SubChannelItem> listSubscribers) {
        if (CONST.tagFragmentOther != null) {
            FragmentInCampaign fragmentInCampaign = (FragmentInCampaign) getSupportFragmentManager().findFragmentByTag("android:switcher:" + CONST.tagFragmentOther + ":0");
            if (fragmentInCampaign != null) {
                getSubFromActivityV2Listener = (GetSubFromActivityV2Listener) fragmentInCampaign;
                getSubFromActivityV2Listener.onCompletedSubV2FromActivity(listSubscribers);
            }
        }
    }

    @Override
    public void onErrorListSubcripberV2(String error) {

    }
}
