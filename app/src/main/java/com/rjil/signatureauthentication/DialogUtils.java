package com.rjil.signatureauthentication;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;

public class DialogUtils {

    public static void showDialog(Context context, String title, String msg,
                                  String positiveBtn, String negativeBtn,
                                  OnCustomDialogClickListener listener, boolean isAlert, Bitmap bitmap) {
        CustomAlertDialog alertDialogBuilder = new CustomAlertDialog();
        Bundle bundle = new Bundle();
        bundle.putString(CustomAlertDialog.MESSAGE, msg);
        bundle.putString(CustomAlertDialog.TITLE, title);
        bundle.putBoolean(CustomAlertDialog.IS_ALERT, isAlert);
        bundle.putParcelable(CustomAlertDialog.IS_RESULT_IMAGE,bitmap);
        alertDialogBuilder.setArguments(bundle);
        alertDialogBuilder.setPositiveButton(positiveBtn, listener);
        if (!TextUtils.isEmpty(negativeBtn))
            alertDialogBuilder.setNegativeButton(negativeBtn, listener);
        alertDialogBuilder.setCancelable(false);
        FragmentTransaction ft = ((FragmentActivity) context).getSupportFragmentManager().beginTransaction();
        alertDialogBuilder.show(ft, alertDialogBuilder.getClass().getSimpleName());
    }

}
