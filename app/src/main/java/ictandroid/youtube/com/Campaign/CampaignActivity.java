package ictandroid.youtube.com.Campaign;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

public class CampaignActivity extends AppCompatActivity {


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campaign);
        ButterKnife.bind(this);
        init();
        InitViewPager();
        initAction();
//        openYoutube("UC_SJaHSpmDQYqoZQAnH8XNQ");


    }

    private void initAction() {

    }

    private void init() {
        sopenyoutube = new SLoading(this);
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
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth auth =FirebaseAuth.getInstance();
        CloudFunction cloudFunction = new CloudFunction();
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

                                    }else {
                                        cloudFunction.addPointChannel(idchannel, -1, new ICloundFunction() {
                                            @Override
                                            public void onSuccess() {
                                                cloudFunction.addUserSub(idchannel,"break",null);
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
                }

            }
        });

    }

    public void backvip(View view) {
        onBackPressed();
    }
}
