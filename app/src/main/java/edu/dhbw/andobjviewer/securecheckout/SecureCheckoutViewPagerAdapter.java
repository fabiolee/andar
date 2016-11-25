package edu.dhbw.andobjviewer.securecheckout;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * @author fabio.lee
 */
public class SecureCheckoutViewPagerAdapter extends FragmentStatePagerAdapter {
    private String[] titleArray;

    public SecureCheckoutViewPagerAdapter(FragmentManager fm, String[] titleArray) {
        super(fm);
        this.titleArray = titleArray;
    }

    @Override public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new CreditDebitCardFragment();
            case 1:
                return new OnlineBankingFragment();
            case 2:
                return new CashOnDeliveryFragment();
            default:
                throw new IllegalArgumentException("invalid position");
        }
    }

    @Override public int getCount() {
        return titleArray.length;
    }

    @Override public CharSequence getPageTitle(int position) {
        return titleArray[position];
    }
}
