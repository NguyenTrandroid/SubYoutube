package ictandroid.youtube.com.Campaign;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
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
import com.google.android.gms.tasks.Task;
import com.google.api.services.youtube.model.Channel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import ictandroid.youtube.com.CloudFunction;
import ictandroid.youtube.com.Dialog.SLoading;
import ictandroid.youtube.com.ICloundFunction;
import ictandroid.youtube.com.R;
import ictandroid.youtube.com.Utils.CallingYoutube.CallingYoutube;
import ictandroid.youtube.com.Utils.CallingYoutube.GetResultApiListener;

public class CampaignActivity extends AppCompatActivity implements GetResultApiListener,CampaignChanelAdapter.OnChannelClick{


    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_coin)
    TextView tvCoin;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.svCamp)
    SearchView svCamp;
    SLoading sopenyoutube;
    SLoading skiemtra;
    boolean intentyoutube =false;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth auth =FirebaseAuth.getInstance();
    CloudFunction cloudFunction = new CloudFunction();
    CallingYoutube callingYoutube;
    String idchannelchecking="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campaign);
        ButterKnife.bind(this);
        init();
        InitViewPager();
        initAction();
//
        callingYoutube = new CallingYoutube(this,1);
//        callingYoutube.checkSubscriberFromApi("UCnSMr4hcl6E3Yh2n1MTNnhg");
//        openYoutube("UCJXs5fI5Xa5S9d1cimdI9_A");
//        cloudFunction.addChannel("UCJXs5fI5Xa5S9d1cimdI9_A","https://yt3.ggpht.com/a-/AAuE7mBC4jpqCO5STDy74-FQsLZBwHnZxFINdR1edg=s288-mo-c-c0xffffffff-rj-k-no","kitchen","15","1");



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callingYoutube.activityResult(requestCode,resultCode,data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(intentyoutube){
            Log.d("testresume", "onResume: ");
            intentyoutube=false;
            kiemtra();
        }
    }

    private void kiemtra() {
        skiemtra.show();
        DocumentReference docRef = db.collection("USERSUB").document(auth.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Boolean haves=false;
                        for (Map.Entry<String, Object> entry : task.getResult().getData().entrySet()) {
                            if(String.valueOf(entry.getValue()).equals("finished")){
                                skiemtra.dismiss();
                            }else if (String.valueOf(entry.getValue()).equals("break")) {
                                skiemtra.dismiss();
                            } else {
                                String idchannel =String.valueOf(entry.getKey());
                                idchannelchecking=idchannel;
                                Log.d("xawjndqw", idchannel);
                                callingYoutube.checkSubscriberFromApi(idchannel);

                            }

                        }
                    }else {
                        /////////////////////

                    }
                }
            }
        });
    }

    private void initAction() {

    }

    private void init() {
        sopenyoutube = new SLoading(this);
        skiemtra = new SLoading(this);
        svCamp.setQueryHint(Html.fromHtml("<font color = #ffffff>" + "Find a chanel..." + "</font>"));
        svCamp.onActionViewExpanded();
        svCamp.setFocusable(false);
        svCamp.clearFocus();



    }

    private void InitViewPager() {
        PagerCampaignAdapter pagerCampaignAdapter = new PagerCampaignAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewpager.setAdapter(pagerCampaignAdapter);
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
    private void openYoutube(String idchannel){
        sopenyoutube.show();
        DocumentReference docRef = db.collection("USERSUB").document(auth.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        if (task.getResult().getData().containsKey(idchannel)) {
                            for (Map.Entry<String, Object> entry : task.getResult().getData().entrySet()) {
                                if (entry.getKey().equals(idchannel)) {
                                    if (String.valueOf(entry.getValue()).equals("finished")) {
                                    } else if(String.valueOf(entry.getValue()).equals("break")){
                                        cloudFunction.addPointChannel(idchannel, -1, new ICloundFunction() {
                                            @Override
                                            public void onSuccess() {
                                                cloudFunction.addUserSub(idchannel,String.valueOf(System.currentTimeMillis()),null);
                                            }

                                            @Override
                                            public void onFailed() {

                                            }
                                        });

                                    }

                                }
                            }
                        }else {
                            cloudFunction.addPointChannel(idchannel, -1, new ICloundFunction() {
                                @Override
                                public void onSuccess() {
                                    cloudFunction.addUserSub(idchannel,String.valueOf(System.currentTimeMillis()),null);
                                }

                                @Override
                                public void onFailed() {

                                }
                            });
                        }
                    }else {
                        cloudFunction.addPointChannel(idchannel, -1, new ICloundFunction() {
                            @Override
                            public void onSuccess() {
                                cloudFunction.addUserSub(idchannel,String.valueOf(System.currentTimeMillis()),null);
                            }

                            @Override
                            public void onFailed() {

                            }
                        });
                    }

                    sopenyoutube.dismiss();
                    Intent intent;
                    String url ="http://www.youtube.com/channel/"+idchannel;
                    try {
                        intent =new Intent(Intent.ACTION_VIEW);
                        intent.setPackage("com.google.android.youtube");
                        intent.setData(Uri.parse(url));
                        startActivity(intent);
                    } catch (ActivityNotFoundException e) {
                        intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(url));
                        startActivity(intent);
                    }
                    intentyoutube=true;
                }

            }
        });

    }

    public void backvip(View view) {
        onBackPressed();
    }

    @Override
    public void onGetInfoChannel(Channel infoChannel) {

    }

    @Override
    public void onCheckSub(boolean existed) {
        Log.d("xawjndqw", existed+"");
        if(existed){
            cloudFunction.addPointUser(1);
            cloudFunction.addUserSub(idchannelchecking, "finished", new ICloundFunction() {
                @Override
                public void onSuccess() {
                    skiemtra.dismiss();
                }

                @Override
                public void onFailed() {
                    skiemtra.dismiss();

                }
            });
        }else {
            cloudFunction.addUserSub(idchannelchecking,"break",null);
            cloudFunction.addPointChannel(idchannelchecking, 1, new ICloundFunction() {
                @Override
                public void onSuccess() {
                    skiemtra.dismiss();
                }

                @Override
                public void onFailed() {
                    skiemtra.dismiss();
                }
            });
        }
    }

    @Override
    public void OnClicked(String channelid) {
        openYoutube(channelid);
    }
}
