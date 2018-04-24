package com.jude.rollviewpagerdome.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jude.rollviewpagerdome.R;
import com.jude.rollviewpagerdome.utils.SpManager;
import com.jude.rollviewpagerdome.utils.SystemUtil;


public class ClearCacheDialog extends Dialog implements View.OnClickListener {
    private Button mPositiveTextView;
    private Button mNegativeView;

    public ClearCacheDialog(Context context) {
        super(context, R.style.base_dialog);
    }

    public ClearCacheDialog(Context context, int themeResId) {
        super(context, R.style.base_dialog);
    }

    protected ClearCacheDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, R.style.base_dialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clear_cache_dialog);
        mPositiveTextView = (Button) findViewById(R.id.tv_positive);
        mNegativeView = (Button) findViewById(R.id.tv_negative);
        mNegativeView.setOnClickListener(this);
        mPositiveTextView.setOnClickListener(this);
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void hide() {
        super.hide();
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_positive:
                SpManager.getInstance(getContext()).setUpdateTs("");
                Toast.makeText(getContext(), "菜单缓存已清除", Toast.LENGTH_LONG).show();
                dismiss();
                break;
            case R.id.tv_negative:
                dismiss();
                break;
        }
    }
}
