package edu.dhbw.andobjviewer.homepage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.dhbw.andarmodelviewer.R;

/**
 * Created by Edwin on 24/11/2016.
 */
public class MacysFragment extends Fragment {
    private final static String ARG_FRAG = "MACY_FRAG";

    public static Fragment newInstance(int message) {
        LorenzoFragment macy = new LorenzoFragment();
        Bundle arg = new Bundle();
        arg.getInt(ARG_FRAG, message);
        macy.setArguments(arg);
        return macy;

    }

    @Nullable @Override public View onCreateView(LayoutInflater inflater,
            @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_macys, container, false);
    }
}
