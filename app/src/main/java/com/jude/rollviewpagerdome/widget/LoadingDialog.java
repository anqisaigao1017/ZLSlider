package com.jude.rollviewpagerdome.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.jude.rollviewpagerdome.R;


public class LoadingDialog extends Dialog {
    
    private ImageView mImageView;
    
    public LoadingDialog(Context context) {
        super(context, R.style.base_dialog);
    }

    public LoadingDialog(Context context, int themeResId) {
        super(context, R.style.base_dialog);
    }

    protected LoadingDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, R.style.base_dialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_progress_dialog);
        mImageView = (ImageView) findViewById(R.id.custom_progress_dialog_img);
        mImageView.setImageResource(R.mipmap.spinner);
    }

    @Override
    public void show() {
        super.show();
        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(getContext(), R.anim
                .progress_animator);
        mImageView.startAnimation(hyperspaceJumpAnimation);
    }

    @Override
    public void hide() {
        super.hide();
        if (mImageView != null) {
            mImageView.clearAnimation();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.gravity = Gravity.CENTER;
        getWindow().setAttributes(layoutParams);
    }
    
}
