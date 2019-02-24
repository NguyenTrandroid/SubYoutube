package ictandroid.youtube.com.Profile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import javax.annotation.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import ictandroid.youtube.com.Dialog.SLoading;
import ictandroid.youtube.com.Login.LoginActivity;
import ictandroid.youtube.com.R;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        setPoints();
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
                break;
        }
    }


}
