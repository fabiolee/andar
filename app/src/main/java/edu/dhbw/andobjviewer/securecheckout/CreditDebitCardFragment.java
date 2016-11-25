package edu.dhbw.andobjviewer.securecheckout;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import edu.dhbw.andarmodelviewer.R;

/**
 * @author fabio.lee
 */
public class CreditDebitCardFragment extends Fragment {
    @Nullable @Override public View onCreateView(LayoutInflater inflater,
            @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_credit_debit_card, container, false);
    }

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initButton(view);
    }

    private void initButton(View view) {
        Button button = (Button) view.findViewById(R.id.button_order);
        button.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                new AlertDialog.Builder(getActivity())
                        .setMessage(R.string.label_thank_you)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override public void onClick(DialogInterface dialogInterface, int i) {
                                getActivity().finish();
                            }
                        })
                        .create()
                        .show();
            }
        });
    }
}
