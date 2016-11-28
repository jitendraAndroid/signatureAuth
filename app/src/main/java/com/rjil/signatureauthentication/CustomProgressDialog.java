package com.rjil.signatureauthentication;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomProgressDialog extends ProgressDialog {

    private ImageView loadingImage;
    private TextView tvMsg;
    private AnimationDrawable animationDrawable;
    private CharSequence mMessage;

    public CustomProgressDialog(Context context, int theme) {
        super(context, theme);
    }

    public CustomProgressDialog(Context context) {
        super(context, R.style.AppTheme_NoActionBar_Dialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.progress_layout);
        loadingImage = (ImageView) findViewById(R.id.progress_image);
        animationDrawable = (AnimationDrawable) loadingImage.getDrawable();
        tvMsg = (TextView) findViewById(R.id.tv_loading_msg);
    }


    @Override
    public void setMessage(CharSequence message) {
        super.setMessage(message);
        mMessage = message;
        showMsg();
    }

    private void showMsg() {
        if (!TextUtils.isEmpty(mMessage) && tvMsg != null)
            tvMsg.setText(mMessage);
    }

    @Override
    public void show() {
        super.show();
        startAnimation();
    }

    @Override
    public void dismiss() {
        super.dismiss();
        stopAnimation();
    }

    private void startAnimation() {
        animationDrawable.start();
        showMsg();
    }

    private void stopAnimation() {
        animationDrawable.stop();
    }
}
