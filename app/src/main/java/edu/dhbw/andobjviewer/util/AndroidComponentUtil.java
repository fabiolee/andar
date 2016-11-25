package edu.dhbw.andobjviewer.util;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import edu.dhbw.andarmodelviewer.R;

/**
 * @author fabio.lee
 */
public class AndroidComponentUtil {
    public static void showComingSoonDialog(Context context) {
        new AlertDialog.Builder(context)
                .setMessage(R.string.label_coming_soon)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .create()
                .show();
    }
}
