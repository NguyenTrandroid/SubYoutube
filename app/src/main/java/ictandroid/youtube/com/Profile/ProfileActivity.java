package ictandroid.youtube.com.Profile;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.rl_history, R.id.rl_contacadmin, R.id.tv_logout,R.id.iv_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_history:
                break;
            case R.id.rl_contacadmin:
                break;
            case R.id.tv_logout:
                break;
            case R.id.iv_back:
                finish();
                break;
        }
    }


}
