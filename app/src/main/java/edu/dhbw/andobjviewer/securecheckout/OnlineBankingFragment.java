package edu.dhbw.andobjviewer.securecheckout;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.dhbw.andarmodelviewer.R;

/**
 * @author fabio.lee
 */
public class OnlineBankingFragment extends Fragment {
    @Nullable @Override public View onCreateView(LayoutInflater inflater,
            @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_online_banking, container, false);
    }
}
