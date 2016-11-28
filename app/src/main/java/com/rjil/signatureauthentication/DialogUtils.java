package com.rjil.signatureauthentication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;

public class DialogUtils {

    private static volatile CustomProgressDialog mDialog;

    /**
     * Shows an {@link AlertDialog} inside a fragment
     *
     * @param title       the title for the mDialog
     * @param msg         the message to be displayed <br>
     * @param positiveBtn text of the positive button <br>
     * @param negativeBtn text of the negative button button <br>
     * @param listener    return the callback of the button click and if set null then
     *                    it will dismiss on the click <br>
     *                    <b>Note:</b> AlertDialog will be shown only with two button on
     *                    it
     *//*
    public static void showDialog(Context context, String title, String msg, String positiveBtn, String negativeBtn,
                                  OnCustomDialogClickListener listener) {
        showDialog(context, title, msg, positiveBtn, negativeBtn, listener, true);
    }
/*

    *//**//**
     * Shows an {@link AlertDialog} inside a fragment
     *
     * @param title    the title for the mDialog
     * @param msg      the message to be displayed <br>
     * @param listener return the callback of the button click and if set null then
     *                 it will dismiss on the click <br>
     *                 <b>Note:</b> AlertDialog will be shown only with 'Ok' button
     *                 on it
     *//**//*

    public static void showDialog(Context context, String title, String msg, OnCustomDialogClickListener listener) {
        showDialog(context, title, msg, null, null, listener, true);
    }

    public static void showDialog(Context context, String title, String msg, OnCustomDialogClickListener listener, boolean isAlert) {
        showDialog(context, title, msg, null, null, listener, isAlert);
    }*//*

    public static void showProgressDialog(Context context, String title,
                                          String message) {
        if (mDialog == null || !mDialog.isShowing()) {
            mDialog = new CustomProgressDialog(context);
        }
        mDialog.setTitle(title);
        mDialog.setMessage(message);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setCancelable(false);
        mDialog.show();
    }

    public static void dismissProgressDialog() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    public static Dialog CreateSingleChoiceDialog(Context context,
                                                  String title, String[] options,
                                                  DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title).setItems(options, listener);
        return builder.create();
    }*/

    /*public static void showDialog(Context context, String title, String msg,
                                  String positiveBtn, String negativeBtn,
                                  OnCustomDialogClickListener listener, boolean isAlert) {
        showDialog(context, title, msg,
                positiveBtn, negativeBtn,
                listener, isAlert, false, null);
    }*/


    /*public static void showDialog(Context context, String title, String msg,
                                  String positiveBtn, String negativeBtn,
                                  OnCustomDialogClickListener listener, boolean isAlert, boolean isCheckVisible, String checkBoxMsg) {
        CustomAlertDialog alertDialogBuilder = new CustomAlertDialog();
        Bundle bundle = new Bundle();
        bundle.putString(CustomAlertDialog.MESSAGE, msg);
        bundle.putString(CustomAlertDialog.TITLE, title);
        bundle.putBoolean(CustomAlertDialog.IS_ALERT, isAlert);
        if (!TextUtils.isEmpty(checkBoxMsg)) {
            bundle.putBoolean(CustomAlertDialog.IS_CHECK_BOX, isCheckVisible);
            bundle.putString(CustomAlertDialog.IS_CHECK_BOX_MSG, checkBoxMsg);
        }
        alertDialogBuilder.setArguments(bundle);
        alertDialogBuilder.setPositiveButton(positiveBtn, listener);
        if (!TextUtils.isEmpty(negativeBtn))
            alertDialogBuilder.setNegativeButton(negativeBtn, listener);
        alertDialogBuilder.setCancelable(false);


        FragmentTransaction ft = ((FragmentActivity) context).getSupportFragmentManager().beginTransaction();
        ft.add(alertDialogBuilder, title);
        ft.commitAllowingStateLoss();
    }*/


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
