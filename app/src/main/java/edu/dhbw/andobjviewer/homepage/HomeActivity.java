package edu.dhbw.andobjviewer.homepage;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import edu.dhbw.andarmodelviewer.R;
import edu.dhbw.andobjviewer.CartActivity;
import edu.dhbw.andobjviewer.ModelChooser;
import edu.dhbw.andobjviewer.ProfileActivity;
import edu.dhbw.andobjviewer.securecheckout.SecureCheckoutActivity;

/**
 * @author fabio.lee
 */
public class HomeActivity extends AppCompatActivity {
    private ImageView cameraIcon;
    private ImageView myCartIcon;
    private ImageView secureCheckoutIcon;
    private ImageView accountIcon;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initToolbar();
        initViewPagerTab();
    }

    private void initToolbar() {
        cameraIcon = (ImageView) findViewById(R.id.iv_camera);
        myCartIcon = (ImageView) findViewById(R.id.iv_my_cart);
        secureCheckoutIcon = (ImageView) findViewById(R.id.iv_secure_checkout);
        accountIcon = (ImageView) findViewById(R.id.iv_account);
        cameraIcon.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, ModelChooser.class);
                startActivity(intent);
            }
        });
        myCartIcon.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, CartActivity.class);
                startActivity(intent);
            }
        });
        secureCheckoutIcon.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, SecureCheckoutActivity.class);
                startActivity(intent);
            }
        });
        accountIcon.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initViewPagerTab() {
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tablayout);

        HomeViewPagerAdapter homeAdapter = new HomeViewPagerAdapter(getSupportFragmentManager());
        homeAdapter.addFragment(new HarveyNormanFragment());
        homeAdapter.addFragment(new LorenzoFragment());
        homeAdapter.addFragment(new MacysFragment());
        viewPager.setAdapter(homeAdapter);
        tabLayout.setupWithViewPager(viewPager);

        List<Integer> tabIconList = new ArrayList<>();
        tabIconList.add(R.drawable.hn);
        tabIconList.add(R.drawable.lorenzo);
        tabIconList.add(R.drawable.macys);

        View customView;
        ImageView customImageView;
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            customView = getLayoutInflater().inflate(R.layout.tab_custom_layout, null);
            customImageView = (ImageView) customView.findViewById(R.id.ic_home);
            customImageView.setImageResource(tabIconList.get(i));
            tabLayout.getTabAt(i).setCustomView(customView);
        }
    }
}
