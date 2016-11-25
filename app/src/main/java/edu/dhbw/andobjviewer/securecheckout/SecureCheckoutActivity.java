package edu.dhbw.andobjviewer.securecheckout;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import edu.dhbw.andarmodelviewer.R;

/**
 * @author fabio.lee
 */
public class SecureCheckoutActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secure_checkout);
        initToolbar();
        initViewPagerTab();
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.label_secure_checkout);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        setSupportActionBar(toolbar);
    }

    private void initViewPagerTab() {
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        String[] titleArray = getResources().getStringArray(R.array.payment_option);
        viewPager.setAdapter(new SecureCheckoutViewPagerAdapter(getSupportFragmentManager(), titleArray));
        tabLayout.setupWithViewPager(viewPager);
    }
}
