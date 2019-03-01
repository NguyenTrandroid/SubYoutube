package ictandroid.youtube.com.Profile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import ictandroid.youtube.com.CONST;
import ictandroid.youtube.com.Campaign.AllChannel.FragmentAllChannel;
import ictandroid.youtube.com.Campaign.AllChannel.GetSubFromCampaignV2Listener;
import ictandroid.youtube.com.Campaign.CampaignChanelAdapter;
import ictandroid.youtube.com.Campaign.ItemChanel;
import ictandroid.youtube.com.CloudFunction;
import ictandroid.youtube.com.Dialog.SLoading;
import ictandroid.youtube.com.ICloundFunction;
import ictandroid.youtube.com.Login.LoginActivity;
import ictandroid.youtube.com.MyApp.InCampaign.GetSubFromActivityV2Listener;
import ictandroid.youtube.com.R;
import ictandroid.youtube.com.Utils.GetData.Interface.SubscriberOnCampaignV2Listener;
import ictandroid.youtube.com.Utils.GetData.Models.InfoSubChannel.SubChannelItem;
import ictandroid.youtube.com.Utils.GetData.SubcribersOnCampaign;
import ictandroid.youtube.com.Utils.GetData.SubcribersOnHistory;
import ictandroid.youtube.com.Utils.GetData.SubscribersOnMyApp;

public class ProfileActivity extends AppCompatActivity {

    @BindView(R.id.iv_avata)
    CircleImageView ivAvata;
    @BindView(R.id.tv_nameProfile)
    TextView tvNameProfile;
    @BindView(R.id.tv_coin)
    TextView tvCoin;
    @BindView(R.id.rl_history)
    RelativeLayout rlHistory;
    @BindView(R.id.rl_contacadmin)
    RelativeLayout rlContacadmin;
    @BindView(R.id.tv_logout)
    TextView tvLogout;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.iv_line)
    ImageView ivLine;
    @BindView(R.id.tv_tongdiem)
    TextView tvTongdiem;
    @BindView(R.id.iv_line1)
    ImageView ivLine1;
    @BindView(R.id.iv_line2)
    ImageView ivLine2;
    @BindView(R.id.iv_line3)
    ImageView ivLine3;
    @BindView(R.id.cv_info)
    CardView cvInfo;
    @BindView(R.id.rl_info)
    RelativeLayout rlInfo;
    @BindView(R.id.tv_clear)
    TextView tvClear;
    @BindView(R.id.rl_history1)
    RelativeLayout rlHistory1;
    @BindView(R.id.tv_title)
    ImageView tvTitle;
    @BindView(R.id.iv_back_red)
    ImageView ivBackRed;
    @BindView(R.id.rv_history)
    RecyclerView rvHistory;
    private FirebaseAuth auth;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseFirestore db;
    private CloudFunction cloudFunction;
    private ArrayList<ItemHistory> listHistory;
    private SLoading sLoadingHistory;
    SubcribersOnHistory subcribersOnCampaign;
    HistoryAdapter historyAdapter ;
    GridLayoutManager layoutManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        sLoadingHistory=new SLoading(this);
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        cloudFunction = new CloudFunction();
        listHistory = new ArrayList<>();
        setPoints();
        loadHistory();
        tvNameProfile.setText(auth.getCurrentUser().getDisplayName());
        Glide.with(this).load(auth.getCurrentUser().getPhotoUrl()).into(ivAvata);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // [END config_signin]
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

    }

    private void setPoints() {
        DocumentReference reference = db.collection("USER").document(auth.getUid());
        reference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (documentSnapshot != null && documentSnapshot.exists()) {
                    tvCoin.setText(String.valueOf(documentSnapshot.get("points")));
                }
            }
        });
    }
    private void loadHistory() {
        listHistory.clear();
        DocumentReference docRef = db.collection("HISTORY").document(auth.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    ArrayList<String> listChannel = (ArrayList<String>) task.getResult().getData().get("channelid");
                    for (int i = 0; i < listChannel.size(); i++) {
                        String[] str = listChannel.get(i).split("<ict>");
                        listHistory.add(new ItemHistory(str[0], str[1],str[2], "0"));
                    }
                    Log.d("AAA",listHistory.size()+"");


                    List<String> listIdChannel = new ArrayList<>();
                    for (int i = 0; i < listHistory.size(); i++) {
                        listIdChannel.add(listHistory.get(i).getChannelid());
                    }


                    subcribersOnCampaign = new SubcribersOnHistory();
                    subcribersOnCampaign.getListSubscripbers(new SubscriberOnCampaignV2Listener() {
                        @Override
                        public void onCompletedListSubcriberCampaignV2(List<SubChannelItem> listSubscribers) {
                            for (int i = 0; i < listSubscribers.size(); i++) {
                                for (int j = 0; j < listHistory.size(); j++) {
                                    if (listSubscribers.get(i).getItems().get(0).getId().equals(listHistory.get(j).getChannelid())) {
                                        listHistory.get(j)
                                                .setSub(listSubscribers.get(i)
                                                        .getItems()
                                                        .get(0)
                                                        .getStatistics()
                                                        .getSubscriberCount());

                                    }
                                }
                            }

                            historyAdapter = new HistoryAdapter(ProfileActivity.this, listHistory);
                            layoutManager = new GridLayoutManager(ProfileActivity.this, 1);
                            rvHistory.setLayoutManager(layoutManager);
                            rvHistory.setItemAnimator(new DefaultItemAnimator());
                            rvHistory.setAdapter(historyAdapter);
                        }

                        @Override
                        public void onErrorListSubcripberCampaignV2(String error) {

                        }
                    }, CONST.KEY, listIdChannel);
                    }else {
                        /////////////////////

                    }
                }
        });
    }



    @OnClick({R.id.rl_history, R.id.rl_contacadmin, R.id.tv_logout, R.id.iv_back, R.id.iv_back_red, R.id.tv_clear})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_history:
                rlInfo.setVisibility(View.INVISIBLE);
                rlHistory1.setVisibility(View.VISIBLE);

                break;
            case R.id.rl_contacadmin:
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse("mailto:huynhquachphi12@gmail.com"));
                startActivity(Intent.createChooser(emailIntent, "Send feedback"));
                break;
            case R.id.tv_logout:
                SLoading sLoading = new SLoading(this);
                sLoading.show();
                auth.signOut();
                // Google sign out
                mGoogleSignInClient.signOut().addOnCompleteListener(this,
                        new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                sLoading.dismiss();
                                Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                                intent.putExtra("action", "nosplash");
                                startActivity(intent);
                                finish();

                            }
                        });
                break;
            case R.id.iv_back:
                finish();
            case R.id.iv_back_red:
                rlHistory1.setVisibility(View.INVISIBLE);
                rlInfo.setVisibility(View.VISIBLE);
                break;
            case R.id.tv_clear:
                sLoadingHistory.show();
                cloudFunction.clearHistory(new ICloundFunction() {
                    @Override
                    public void onSuccess() {
                        listHistory.clear();
                        historyAdapter = new HistoryAdapter(ProfileActivity.this, listHistory);
                        rvHistory.setAdapter(historyAdapter);
                        sLoadingHistory.dismiss();
                    }

                    @Override
                    public void onFailed() {
                        sLoadingHistory.dismiss();
                    }
                });
                break;
        }
    }


}
