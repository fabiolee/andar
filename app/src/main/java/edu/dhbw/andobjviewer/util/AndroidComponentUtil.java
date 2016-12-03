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
                    @Override public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create()
                .show();
    }

    public static void showMessageDialog(Context context, int messageId,
            DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(context)
                .setMessage(messageId)
                .setPositiveButton(android.R.string.ok, okListener)
                .setNegativeButton(android.R.string.cancel, null)
                .create()
                .show();
    }
}
