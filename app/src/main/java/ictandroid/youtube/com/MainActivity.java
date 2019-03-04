package ictandroid.youtube.com;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator;

import butterknife.BindView;
import butterknife.ButterKnife;
import ictandroid.youtube.com.Campaign.CampaignActivity;
import ictandroid.youtube.com.MyApp.MyAppActivity;
import ictandroid.youtube.com.Profile.ProfileActivity;
import ictandroid.youtube.com.Viewpager.OnPageSelect;
import ictandroid.youtube.com.Viewpager.PagerAdapter;
import ictandroid.youtube.com.Viewpager.ZoomOutPageTransformer;

public class MainActivity extends AppCompatActivity implements OnPageSelect {


    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.dots_indicator)
    WormDotsIndicator dotsIndicator;
    CloudFunction cloudFunction;
    private InterstitialAd mInterstitialAd;
    boolean isClick = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("LIFEEE", "onCreate1");

        super.onCreate(savedInstanceState);
        MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713");
        Log.d("LIFEEE", "onCreate2");

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        if (getIntent().getStringExtra(CONST.SHARE_INTENT) != null) {
            Log.d("GGGGGGG", getIntent().getStringExtra(CONST.SHARE_INTENT));
            Intent intent = new Intent(MainActivity.this, MyAppActivity.class);

            intent.putExtra(CONST.SHARE_LINK, getIntent().getStringExtra(CONST.SHARE_INTENT));
            startActivity(intent);

        }
        ButterKnife.bind(this);
        init();
//        cloudFunction = new CloudFunction();
//        cloudFunction.addChannel("UCejGjkkZ1y_WeI3fX87eD_Q","https://yt3.ggpht.com/a-/AAuE7mB-dmJKMzvpYGrDWmoqKG43inAga8M1TDzHdA=s288-mo-c-c0xffffffff-rj-k-no","nhattran","20","1");
////        cloudFunction = new CloudFunction();
////        cloudFunction.addPointMyChannel("nhat2", -40, new ICloundFunction() {
////            @Override
////            public void onSuccess() {
////                Toast.makeText(MainActivity.this, "add point success", Toast.LENGTH_SHORT).show();
////            }
////
////            @Override
////            public void onFailed() {
////                Toast.makeText(MainActivity.this, "add point fail", Toast.LENGTH_SHORT).show();
////
////            }
////        });
//        Intent intent;
//        String url ="http://www.youtube.com/channel/UC_SJaHSpmDQYqoZQAnH8XNQ";
//        try {
//            intent =new Intent(Intent.ACTION_VIEW);
//            intent.setPackage("com.google.android.youtube");
//            intent.setData(Uri.parse(url));
//            startActivity(intent);
//        } catch (ActivityNotFoundException e) {
//            intent = new Intent(Intent.ACTION_VIEW);
//            intent.setData(Uri.parse(url));
//            startActivity(intent);
//        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_full_screen));

        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
                if (isClick)
                    startActivity(new Intent(MainActivity.this, CampaignActivity.class));
                isClick = false;
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when the ad is displayed.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the interstitial ad is closed.
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
                if (isClick)
                    startActivity(new Intent(MainActivity.this, CampaignActivity.class));
                isClick = false;
            }
        });
    }

//    private void loadInterstitialAd()
//    {
//        AdRequest adRequest = new AdRequest.Builder()
//                .build();
//        mInterstitialAd.loadAd(adRequest);
//    }

    private void init() {
        PagerAdapter adapter = new PagerAdapter();
        viewPager.setAdapter(adapter);
        viewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        dotsIndicator.setViewPager(viewPager);


    }


    @Override
    public void sendPageSelect(int page) {
        switch (page) {
            case 0:
                isClick = true;
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }

                break;
            case 1:
                startActivity(new Intent(MainActivity.this, MyAppActivity.class));
                break;
            case 2:
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));

                break;
        }
    }
}
