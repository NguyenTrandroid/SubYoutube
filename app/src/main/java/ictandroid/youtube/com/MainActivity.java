package ictandroid.youtube.com;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator;

import butterknife.BindView;
import butterknife.ButterKnife;
import ictandroid.youtube.com.Viewpager.OnPageSelect;
import ictandroid.youtube.com.Viewpager.PagerAdapter;
import ictandroid.youtube.com.Viewpager.ZoomOutPageTransformer;

public class MainActivity extends AppCompatActivity implements OnPageSelect {


    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.dots_indicator)
    WormDotsIndicator dotsIndicator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        init();

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
                break;
            case 1:
                break;
            case 2:
                break;
        }
    }
}
