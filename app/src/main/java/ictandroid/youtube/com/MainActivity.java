package ictandroid.youtube.com;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        init();
//        cloudFunction = new CloudFunction();
//        cloudFunction.addPointMyChannel("nhat2", -40, new ICloundFunction() {
//            @Override
//            public void onSuccess() {
//                Toast.makeText(MainActivity.this, "add point success", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onFailed() {
//                Toast.makeText(MainActivity.this, "add point fail", Toast.LENGTH_SHORT).show();
//
//            }
//        });
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
                startActivity(new Intent(MainActivity.this, CampaignActivity.class));
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
