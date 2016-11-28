package com.rjil.signatureauthentication;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


public class CustomAlertDialog extends DialogFragment implements
        OnClickListener {
    public static final String MESSAGE = "message";
    public static final String TITLE = "title";
    public static final String IS_ALERT = "is_alert";
    public static final String IS_RESULT_IMAGE = "is_image";
    private CharSequence mMsg;
    private CharSequence mTitle;
    private CharSequence mNegativeBtnTxt;
    private CharSequence mPositiveBtnTxt;
    private boolean isAlert = true;
    private ImageView ivIcon;
    private ImageView mImageView;
    private View mView;
    private Bitmap mResultBitmap;

    public enum Type {
        BUTTON_POSITIVE,
        BUTTON_NEGATIVE
    }

    private TextView mDialogMsg;
    private TextView mDialogTitle;
    private Button mBtnPositive;
    private Button mBtnNegative;
    private OnCustomDialogClickListener btnNegativeListener;
    private OnCustomDialogClickListener btnPositiveListener;

    public CustomAlertDialog() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.Translucent_ProgressDialog);

        Bundle arguments = getArguments();
        if (arguments != null) {
            mMsg = arguments.getString(MESSAGE);
            mTitle = arguments.getString(TITLE);
            isAlert = arguments.getBoolean(IS_ALERT);
            mResultBitmap = arguments.getParcelable(IS_RESULT_IMAGE);
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        return mView = inflater.inflate(R.layout.custom_alert_dialog_view, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        initView();
        initMembers();
        initData();
        initListener();
        initOthers();
    }

    private void initOthers() {
        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isAlert)
                    setIcon(R.drawable.alert);
                else
                    setIcon(R.drawable.success);
            }
        }, 400);

    }

    private void initListener() {
        setPositiveButton(mPositiveBtnTxt, btnPositiveListener);
        setNegativeButton(mNegativeBtnTxt, btnNegativeListener);
    }

    private void initData() {

    }

    private void initMembers() {
        setWidth(80);
        setTitle(mTitle);
        setMessage(mMsg);
        setImage(mResultBitmap);
    }

    private void setImage(Bitmap mResultBitmap) {
        mImageView.setImageBitmap(mResultBitmap);
    }

    protected void setWidth(int widthPercent) {
        int deviceWidth = deviceWidth();
        int deviceHeight = deviceHeight();
        if (deviceWidth > deviceHeight) {
            deviceWidth = deviceHeight;
        }

        int dialogWidth = (deviceWidth * widthPercent) / 100;
        getDialog().getWindow().setLayout(dialogWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public CustomAlertDialog setMessage(CharSequence message) {
        mMsg = message;
        if (mDialogMsg != null) {
            mDialogMsg.setText(mMsg);
        }
        return this;
    }

    private void setIcon(int icon) {
        Animation anim = AnimationUtils.loadAnimation(getActivity(), R.anim.scale_open);
        anim.setInterpolator(getActivity(), android.R.anim.bounce_interpolator);
        ivIcon.setImageResource(icon);
        ivIcon.setVisibility(View.VISIBLE);
        ivIcon.startAnimation(anim);
    }

    public CustomAlertDialog setPositiveButton(CharSequence text,
                                               OnCustomDialogClickListener listener) {

        btnPositiveListener = listener;
        mPositiveBtnTxt = text;
        if (mBtnPositive != null) {
            mBtnPositive.setOnClickListener(CustomAlertDialog.this);
            if (!TextUtils.isEmpty(mPositiveBtnTxt))
                mBtnPositive.setText(text);
        }
        return this;
    }

    public CustomAlertDialog setNegativeButton(CharSequence text,
                                               OnCustomDialogClickListener listener) {
        btnNegativeListener = listener;
        mNegativeBtnTxt = text;
        if (mBtnNegative != null && btnNegativeListener != null) {
            mBtnNegative.setVisibility(View.VISIBLE);
            mBtnNegative.setText(mNegativeBtnTxt);
            mBtnNegative.setOnClickListener(CustomAlertDialog.this);
        }
        return this;
    }

    public CustomAlertDialog setTitle(CharSequence title) {
        mTitle = title;
        if (mDialogTitle != null) {
            mDialogTitle.setText(mTitle);
        }
        return this;
    }


    private void initView() {
        mDialogMsg = (TextView) mView.findViewById(R.id.tv_alert_msg);
        mImageView = (ImageView) mView.findViewById(R.id.resultImage);
        mDialogTitle = (TextView) mView
                .findViewById(R.id.tv_alert_header);
        mBtnPositive = (Button) mView.findViewById(R.id.btn_positive);
        mBtnNegative = (Button) mView.findViewById(R.id.btn_negative);
        ivIcon = (ImageView) mView.findViewById(R.id.iv_header);


    }

    @Override
    public void setCancelable(boolean cancelable) {
        super.setCancelable(cancelable);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_positive) {
            if (btnPositiveListener != null) {

                btnPositiveListener.onCustomDialogClick(Type.BUTTON_POSITIVE);

            }
            dismiss();
        } else {
            if (btnNegativeListener != null) {
                btnNegativeListener.onCustomDialogClick(Type.BUTTON_NEGATIVE);
            }
            dismiss();
        }
    }

    public int deviceWidth() {
        return getDisplayMetrics().widthPixels;
    }

    public int deviceHeight() {
        return getDisplayMetrics().heightPixels;
    }


    private DisplayMetrics getDisplayMetrics() {
        WindowManager wm = (WindowManager) getActivity()
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        return metrics;
    }
}
