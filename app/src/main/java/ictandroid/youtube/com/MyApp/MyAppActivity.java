package ictandroid.youtube.com.MyApp;

import android.content.Context;
import android.content.SharedPreferences;
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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
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
    public static SLoading sLoading;
    GetSubFomActivityListener getSubFomActivityListener;
    GetSubFromActivityV2Listener getSubFromActivityV2Listener;
    AddChannelOnFirebaseListener addChannelOnFirebaseListener;
    FirebaseFirestore db;
    GetKeySearchMyChanel getKeySearchMyChanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_app);
        if (getIntent().getStringExtra(CONST.SHARE_LINK) != null)
            Log.d("FFFFFFFFF", getIntent().getStringExtra(CONST.SHARE_LINK));
        ButterKnife.bind(this);
        init();
        initViewPager();
        initAction();
        cloudFunction = new CloudFunction();
        auth = FirebaseAuth.getInstance();
        sLoading = new SLoading(this);
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
        if (CONST.SHARE_INTENT.equals(getIntent().getStringExtra(CONST.SHARE_INTENT))) {
            viewpager.setCurrentItem(1);
        }
    }

    private void initAction() {
        svMyapp.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (CONST.IDFragment != null) {
                    FragmentInCampaign fragmentInCampaign = (FragmentInCampaign) getSupportFragmentManager().findFragmentByTag("android:switcher:" + CONST.IDFragment + ":0");
                    if (fragmentInCampaign != null) {
                        getKeySearchMyChanel = fragmentInCampaign;
                        getKeySearchMyChanel.onGetKey(s);
                    }
                    FragmentOther fragmentOther = (FragmentOther) getSupportFragmentManager().findFragmentByTag("android:switcher:" + CONST.IDFragment + ":1");
                    if (fragmentOther != null) {
                        getKeySearchMyChanel = fragmentOther;
                        getKeySearchMyChanel.onGetKey(s);
                    }
                }
                return false;
            }
        });
    }

    private void init() {
        svMyapp.setQueryHint(Html.fromHtml("<font color = #ffffff>" + getString(R.string.find_channel) + "</font>"));
        svMyapp.onActionViewExpanded();
        svMyapp.setFocusable(false);
        svMyapp.clearFocus();

    }

    public void backvip(View view) {
        onBackPressed();
    }

    @Override
    public void delete(String channelid) {
        sLoading.show();
        cloudFunction.removeMyChannel(channelid, new ICloundFunction() {
            @Override
            public void onSuccess() {
                //sEdit.dismiss();
            }

            @Override
            public void onFailed() {
                sLoading.dismiss();
            }
        });
    }

    @Override
    public void addpoint(String channelid, int point) {
        sLoading.show();
        cloudFunction.addPointMyChannel(channelid, point, new ICloundFunction() {
            @Override
            public void onSuccess() {
                //sEdit.dismiss();
            }

            @Override
            public void onFailed() {
                sLoading.dismiss();
            }
        });
    }

    @Override
    public void onInfoCompleted(ChanelItem chanelItem) {
        cloudFunction = new CloudFunction();
        Item item = chanelItem.getItems().get(0);
        db = FirebaseFirestore.getInstance();

        DocumentReference docRef = db.collection("LIST").document(item.getId());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Toast.makeText(MyAppActivity.this, R.string.channel_existed, Toast.LENGTH_LONG).show();
                        if (FragmentOther.sLoadingAddChannel != null) {
                            FragmentOther.sLoadingAddChannel.dismiss();
                        }
                    } else {
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
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MyAppActivity.this, R.string.get_data_failed, Toast.LENGTH_LONG).show();
                        if (FragmentOther.dialogAdd != null) {
                            FragmentOther.dialogAdd.cancel();
                        }
                        if (FragmentOther.sLoadingAddChannel != null) {
                            FragmentOther.sLoadingAddChannel.dismiss();
                        }
                    }
                });
    }

    @Override
    public void onInfoError(String error) {
        Toast.makeText(this, R.string.incorrect_link, Toast.LENGTH_LONG).show();
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
        if (sLoading != null) {
            sLoading.dismiss();
            Log.d("ERRRORR", "er1");
        }
    }

    @Override
    protected void onDestroy() {
        SharedPreferences sharedPreferences = getSharedPreferences(CONST.NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(CONST.KEY, System.currentTimeMillis());
        editor.commit();
        super.onDestroy();
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
        if (sLoading != null) {
            sLoading.dismiss();
            Log.d("ERRRORR", "er2");
        }
    }
}
