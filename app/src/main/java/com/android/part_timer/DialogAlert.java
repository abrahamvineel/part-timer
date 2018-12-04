package com.android.part_timer;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class DialogAlert {

    private Context context;

    public DialogAlert(Context mContext) {
        this.context = mContext;
    }

    //builder with ok button
    public AlertDialog.Builder buildOkDialog(String title, String message) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setIcon(R.drawable.ic_error);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(Constants.OK_BUTTON, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        return builder;
    }

    //dialog builder with yes,no buttons
    public AlertDialog.Builder yesNoDialog(DialogInterface.OnClickListener dialogClickListener,String title) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(Constants.YES_NO_DIALOG_MESSAGE).setPositiveButton(Constants.YES_NO_DIALOG_YES_BUTTON, dialogClickListener)
                .setNegativeButton(Constants.YES_NO_DIALOG_NO_BUTTON, dialogClickListener);
        return builder;
    }


}
