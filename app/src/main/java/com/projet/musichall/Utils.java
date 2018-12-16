package com.projet.musichall;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.content.Context;



public class Utils {

    // show alert dialog
    public static void MyMessageButton(String message, Context context) {
        final AlertDialog.Builder dlgAlert = new AlertDialog.Builder(context);
        dlgAlert.setTitle(message);
        dlgAlert.setIcon(android.R.drawable.ic_dialog_alert);

        dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        dlgAlert.create().show();
    }

}
